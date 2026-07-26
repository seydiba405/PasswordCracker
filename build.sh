#!/usr/bin/env bash
# Compile le projet dans le dossier bin/
set -e
mkdir -p bin
# --release 8 : compatible avec le runtime Java 8 present sur la machine
javac --release 8 -d bin src/com/passwordcracker/*.java
echo "Compilation terminee. Lancer avec :"
echo "  java -cp bin com.passwordcracker.PasswordCracker -m DICO -h <hash>"
