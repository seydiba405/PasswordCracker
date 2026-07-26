package com.passwordcracker;

/**
 * Interface commune a toutes les strategies de cassage de mot de passe.
 *
 * Chaque strategie recoit une empreinte (hash MD5) et tente de retrouver
 * le mot de passe original correspondant.
 */
public interface HashCracker {

    /**
     * Tente de retrouver le mot de passe correspondant au hash fourni.
     *
     * @param hash empreinte MD5 (chaine hexadecimale de 32 caracteres)
     * @return le mot de passe trouve, ou {@code null} si aucun resultat
     */
    String crack(String hash);
}
