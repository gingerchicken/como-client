#!/bin/sh

# Remove the generated directories
rm -r bin build run .gradle
echo Removed generated directories, generating sources...

# Generate the sources
./gradlew genSources
echo Now you might want to generate the required configs for your given IDE, do this with gradlew (e.g., ./gradlew vscode)