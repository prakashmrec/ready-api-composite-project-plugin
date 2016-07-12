# ready-api-composite-project-plugin
A Ready! API plugin which adds a composite project action to save and recreate files with names present in GUI

File names are created when a composite project is saved for first time in Ready! API. File names are never changed afterwards even if the names are changed in GUI for various model items (Tes Suites, Test Cases, Interfaces/operations etc.).

So, you need to look at the xml files to figure out what model item is saved into what file. 

This plugin adds a possibility to recraete the files with new names (as defined in GUI) for composite project. It add an action 'Save and recreate files', which is enabled only for composite projects.
This action deletes existing files and saves a fresh copy of the composite project with new file names.

# Installation
Build the latest version using 'mvn clean install'
Either install it using **Plugins** toolbar button in Ready! API or copy the jar file in **$user.home/.soapui/plugins** and restart Ready! API.
