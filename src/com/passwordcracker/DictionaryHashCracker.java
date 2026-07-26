package com.passwordcracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Strategie de cassage par dictionnaire.
 *
 * Elle charge une liste de mots depuis un fichier texte (un mot par ligne)
 * puis teste chaque mot. La classe de base se charge de calculer le hash
 * et de comparer.
 */
public class DictionaryHashCracker extends AbstractHashCracker {

    /** Dictionnaire embarque, utilise si aucun fichier externe n'est fourni. */
    private static final String DEFAULT_DICTIONARY = "dictionary.txt";

    private final List<String> words;

    /** Construit une strategie utilisant le dictionnaire par defaut. */
    public DictionaryHashCracker() {
        this(DEFAULT_DICTIONARY);
    }

    /**
     * Construit une strategie utilisant le fichier de dictionnaire indique.
     *
     * @param dictionaryPath chemin du fichier (un mot par ligne)
     */
    public DictionaryHashCracker(String dictionaryPath) {
        this.words = loadWords(dictionaryPath);
    }

    @Override
    protected Iterator<String> candidates() {
        return words.iterator();
    }

    /** @return le nombre de mots charges dans le dictionnaire. */
    public int size() {
        return words.size();
    }

    /**
     * Charge les mots du dictionnaire. On essaie d'abord un fichier sur le
     * disque ; a defaut, une ressource embarquee dans le classpath ; a defaut,
     * une petite liste par defaut codee en dur.
     */
    private static List<String> loadWords(String dictionaryPath) {
        // 1. Fichier present sur le disque
        Path path = Paths.get(dictionaryPath);
        if (Files.isReadable(path)) {
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                return clean(lines);
            } catch (IOException e) {
                System.err.println("Avertissement : lecture du dictionnaire impossible ("
                        + e.getMessage() + ")");
            }
        }

        // 2. Ressource embarquee dans le JAR / classpath
        try (InputStream in = DictionaryHashCracker.class
                .getClassLoader().getResourceAsStream(dictionaryPath)) {
            if (in != null) {
                List<String> lines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }
                return clean(lines);
            }
        } catch (IOException e) {
            System.err.println("Avertissement : ressource dictionnaire illisible ("
                    + e.getMessage() + ")");
        }

        // 3. Dictionnaire minimal de secours
        System.err.println("Avertissement : dictionnaire '" + dictionaryPath
                + "' introuvable, utilisation d'une liste par defaut.");
        return clean(java.util.Arrays.asList(
                "bonjour", "secret", "admin", "password", "azerty", "test"));
    }

    /** Retire les lignes vides et les espaces superflus. */
    private static List<String> clean(List<String> lines) {
        List<String> result = new ArrayList<>(lines.size());
        for (String line : lines) {
            String w = line.trim();
            if (!w.isEmpty()) {
                result.add(w);
            }
        }
        return result;
    }
}
