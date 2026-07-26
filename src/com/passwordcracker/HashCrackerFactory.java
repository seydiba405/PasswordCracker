package com.passwordcracker;

/**
 * Fabrique simple (Simple Factory).
 *
 * Elle centralise la creation des strategies de cassage. Le reste de
 * l'application ne connait que l'interface {@link HashCracker} et n'instancie
 * jamais directement les classes concretes.
 */
public class HashCrackerFactory {

    /** Classe utilitaire : pas d'instanciation. */
    private HashCrackerFactory() {
    }

    /**
     * Cree la strategie de cassage correspondant a la methode demandee.
     *
     * @param method "DICO" pour le dictionnaire, "BRUTE" pour la force brute
     * @return une instance de {@link HashCracker}
     * @throws IllegalArgumentException si la methode est inconnue
     */
    public static HashCracker create(String method) {
        if (method == null) {
            throw new IllegalArgumentException("La methode ne peut pas etre nulle");
        }
        switch (method.trim().toUpperCase()) {
            case "DICO":
                return new DictionaryHashCracker();
            case "BRUTE":
                return new BruteForceHashCracker();
            default:
                throw new IllegalArgumentException(
                        "Methode inconnue : '" + method + "' (attendu : BRUTE ou DICO)");
        }
    }
}
