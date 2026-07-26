package com.passwordcracker;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

/**
 * Classe de base commune aux strategies concretes.
 *
 * Elle factorise (pour eviter toute duplication de code) :
 *   - le calcul du hash MD5 ;
 *   - la boucle de cassage (patron Template Method) ;
 *   - le comptage des tentatives.
 *
 * Chaque strategie concrete n'a plus qu'a fournir la source des mots
 * candidats a tester, via la methode {@link #candidates()}.
 */
public abstract class AbstractHashCracker implements HashCracker {

    /** Nombre de mots de passe testes lors du dernier appel a crack(). */
    private long attempts;

    /**
     * Fournit les mots candidats a tester (dictionnaire, combinaisons, ...).
     * La generation peut etre paresseuse (lazy) pour economiser la memoire.
     *
     * @return un iterateur sur les mots candidats
     */
    protected abstract Iterator<String> candidates();

    /**
     * Algorithme de cassage commun (Template Method) : on parcourt les
     * candidats, on calcule leur hash MD5 et on compare au hash recherche.
     */
    @Override
    public String crack(String hash) {
        if (hash == null) {
            return null;
        }
        final String target = hash.trim().toLowerCase();
        attempts = 0;

        Iterator<String> it = candidates();
        while (it.hasNext()) {
            String candidate = it.next();
            attempts++;
            if (md5(candidate).equals(target)) {
                return candidate;
            }
        }
        return null;
    }

    /** @return le nombre de tentatives du dernier cassage. */
    public long getAttempts() {
        return attempts;
    }

    /**
     * Calcule l'empreinte MD5 d'une chaine et la retourne en hexadecimal
     * minuscule (32 caracteres).
     */
    protected String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(Character.forDigit((b >> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // MD5 fait partie du JDK standard : cette exception ne devrait jamais survenir.
            throw new IllegalStateException("Algorithme MD5 indisponible", e);
        }
    }
}
