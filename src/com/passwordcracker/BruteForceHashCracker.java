package com.passwordcracker;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Strategie de cassage par force brute.
 *
 * Elle genere de facon exhaustive et paresseuse (lazy) toutes les
 * combinaisons possibles de l'alphabet, de la longueur 1 a la longueur
 * maximale, dans l'ordre :  a, b, ..., z, aa, ab, ..., zzzz.
 */
public class BruteForceHashCracker extends AbstractHashCracker {

    /** Alphabet utilise pour la generation des combinaisons. */
    private static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    /** Longueur maximale des mots de passe testes. */
    private static final int DEFAULT_MAX_LENGTH = 4;

    private final char[] alphabet;
    private final int maxLength;

    /** Construit une strategie avec l'alphabet a-z et une longueur max de 4. */
    public BruteForceHashCracker() {
        this(DEFAULT_ALPHABET, DEFAULT_MAX_LENGTH);
    }

    /**
     * @param alphabet  ensemble des caracteres autorises
     * @param maxLength longueur maximale des combinaisons generees
     */
    public BruteForceHashCracker(String alphabet, int maxLength) {
        if (alphabet == null || alphabet.isEmpty()) {
            throw new IllegalArgumentException("L'alphabet ne peut pas etre vide");
        }
        if (maxLength < 1) {
            throw new IllegalArgumentException("La longueur maximale doit etre >= 1");
        }
        this.alphabet = alphabet.toCharArray();
        this.maxLength = maxLength;
    }

    @Override
    protected Iterator<String> candidates() {
        return new CombinationIterator();
    }

    /**
     * Iterateur "compteur" (odometer) qui enumere toutes les combinaisons
     * sans jamais toutes les stocker en memoire.
     */
    private final class CombinationIterator implements Iterator<String> {

        private int length = 1;          // longueur courante des mots generes
        private int[] indices = new int[1]; // position de chaque caractere dans l'alphabet
        private boolean finished = false;

        @Override
        public boolean hasNext() {
            return !finished;
        }

        @Override
        public String next() {
            if (finished) {
                throw new NoSuchElementException();
            }
            String current = build();
            advance();
            return current;
        }

        /** Construit la chaine correspondant a l'etat courant du compteur. */
        private String build() {
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(alphabet[indices[i]]);
            }
            return sb.toString();
        }

        /** Incremente le compteur, en passant a la longueur suivante si besoin. */
        private void advance() {
            int pos = length - 1;
            while (pos >= 0) {
                indices[pos]++;
                if (indices[pos] < alphabet.length) {
                    return; // retenue absorbee, combinaison suivante prete
                }
                indices[pos] = 0;
                pos--;
            }
            // Toutes les combinaisons de cette longueur ont ete epuisees.
            length++;
            if (length > maxLength) {
                finished = true;
            } else {
                indices = new int[length]; // reinitialise a "aaa...a"
            }
        }
    }
}
