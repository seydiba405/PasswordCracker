package com.passwordcracker;

/**
 * Application en ligne de commande.
 *
 * Usage :
 *   passwordCracker -m BRUTE -h e7247759c1633c0f9f1485f3690294a9
 *   passwordCracker -m DICO  -h e7247759c1633c0f9f1485f3690294a9
 *
 * L'application ne connait que la fabrique et l'interface HashCracker :
 * elle n'instancie aucune classe concrete directement.
 */
public class PasswordCracker {

    public static void main(String[] args) {
        String method = null;
        String hash = null;

        // Analyse simple des arguments -m <methode> et -h <hash>
        for (int i = 0; i < args.length - 1; i++) {
            if ("-m".equals(args[i])) {
                method = args[i + 1];
            } else if ("-h".equals(args[i])) {
                hash = args[i + 1];
            }
        }

        if (method == null || hash == null) {
            printUsage();
            System.exit(1);
        }

        final HashCracker cracker;
        try {
            // Creation centralisee via la fabrique simple.
            cracker = HashCrackerFactory.create(method);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : " + e.getMessage());
            printUsage();
            System.exit(1);
            return; // inutile (System.exit), mais rassure le compilateur
        }

        System.out.println("Methode : " + method.toUpperCase());
        System.out.println("Hash    : " + hash);
        System.out.println("Recherche en cours...");

        long start = System.nanoTime();
        String result = cracker.crack(hash);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        if (result != null) {
            System.out.println("Password found: " + result);
        } else {
            System.out.println("Password not found");
        }

        // Informations complementaires
        if (cracker instanceof AbstractHashCracker) {
            long attempts = ((AbstractHashCracker) cracker).getAttempts();
            System.out.println("Tentatives : " + attempts);
        }
        System.out.println("Temps d'execution : " + elapsedMs + " ms");
    }

    private static void printUsage() {
        System.err.println();
        System.err.println("Usage : passwordCracker -m <BRUTE|DICO> -h <hashMD5>");
        System.err.println("Exemple :");
        System.err.println("  passwordCracker -m BRUTE -h e7247759c1633c0f9f1485f3690294a9");
        System.err.println("  passwordCracker -m DICO  -h e7247759c1633c0f9f1485f3690294a9");
    }
}
