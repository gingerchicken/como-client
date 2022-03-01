# Como Client
<div align="center">
    <img src="src/main/resources/assets/como-client/textures/misc/watermark.png" width="500px"/>
    <br>
    <img src="https://github.com/gingerchicken/como-client/actions/workflows/build.yml/badge.svg" alt="Gradlew Build"/>
    <a href="https://github.com/gingerchicken/como-client/issues/"><img src="https://img.shields.io/github/issues/gingerchicken/como-client.svg" alt="issues"></a>
    <img src="https://img.shields.io/tokei/lines/github/gingerchicken/como-client.svg" alt="lines">
    <a href="https://minecraft.net/"><img src="https://img.shields.io/badge/MC-1.18.2-brightgreen.svg" alt="Minecraft"/></a>
    <img src="https://img.shields.io/badge/license-GPL--3.0-green.svg" alt="GNU-3.0">
    <img src="https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat" alt="Contributions Welcome">
    <br>
    <img src="https://forthebadge.com/images/badges/built-with-love.svg" alt="Built With Love">
    <img src="https://forthebadge.com/images/badges/built-by-codebabes.svg" alt="Built By Code Babes">
</div>

A Minecraft utility mod developed with the aim of giving its users an advantage in most (if not all) situations (*but since I made it it probably won't to be honest...*), while still aiming to be as simplistic as possible.

## Installation
Como Client is just a Fabric mod, so it should just install like any other Fabric mod. If you are unsure how to install mods with Fabric - (*don't worry it is pretty straight forward*) - there are quite a few guides elsewhere on how to do this.

### Building the project
You can use some of the pre-built libraries or you can build it yourself using the following steps.

#### Step 1
Clone the repository, here's how to do in using the git command line:
```bash
git clone https://github.com/gingerchicken/como-client
```

#### Step 2
Install OpenJDK 17, there is probably already half a billion tutorials online how to do this, however here are *some* ways of doing it:

##### Ubuntu/Debian (sid/unstable)
`
sudo apt install openjdk-17-jdk
`

##### Manjaro/Arch Linux
`
sudo pacman -Sy jdk-openjdk
`

#### Step 3
Finally, building the project, to do this we can run the following command:
```bash
./gradlew build
```

#### Step 4
To retrieve the built files, go to the `/build/libs` directory. Now you can place the como-client-*version*.jar file in your mods folder.

### Other Requirements
Once you have built the jar or downloaded a pre-built jar for the mod, you will need one more library to accompany it: the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api). Place this in the your mods folder **along side** the Como Client jar, and it should work. If it doesn't work make sure to [create an issue](https://github.com/gingerchicken/como-client/issues).

## Contributions
I am yet to setup a code of conduct for this project, however this will be done at some point. Code of conduct asside... the enviroment setup is simple since it is no different from most other Fabric mods, instructions on which can be found on the [Fabric Wiki](https://fabricmc.net/wiki/tutorial:setup).

## Features
Most of the features you will get in this client aren't too different to any other client, however, if you're *that* interested you may wish to [read the features list](/FEATURES.md).

## Thanks
### The Wurst Project
While developing this I was aided by the source code of the [Wurst Project](https://github.com/Wurst-Imperium/Wurst7), since this is my first ever mod for Minecraft, I was quite unfamiliar with the way Minecraft and its APIs work etc. therefore, by looking some parts of Wurst's source code, I could quickly understand the API and start making this mod.

## Reviews
### Happy Customer #1
Here is a very satisfied and happy customer !!!
<p align="center">
    <img src="src/main/resources/assets/como-client/review.png"/>
</p>
