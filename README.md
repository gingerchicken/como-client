# Como Client
<p align="center">
    <img src="src/main/resources/assets/como-client/textures/misc/watermark.png" width="500px"/>
    <br>
    <img src="https://github.com/gingerchicken/como-client/actions/workflows/build.yml/badge.svg" alt="Gradlew Build"/>
    <img src="https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat" alt="Contributions Welcome">
</p>

**A Minecraft "hacked client"** in the form of a Fabric mod developed with the aim of giving its users an advantage in most (if not all) situations (*but since I made it it probably won't to be honest...*), while still aiming to be as simplistic as possible.

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
Install OpenJDK 16, there is probably already have a billion tutorials online how to do this but for me, while it tailored towards setting up a server, [this helpful gist from one of the spigot devs](https://gist.github.com/Proximyst/67615353e2575a71faaff3f7ae9cc2b4) worked best for me.

#### Step 3
Finally, building the project, to do this we can run the following command:
```bash
./gradlew build
```

#### Step 4
To retrieve the built files, go to the `/build/libs` directory. Now you can place the como-client-*version*.jar file in your mods folder.

### Other Requirements
Once you have built the jar or downloaded a pre-built jar for the mod, you will need one more library to accompany it: the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api). Place this in the your mods folder **along side** the Como Client jar, and it should work. If it doesn't work make sure to [create an issue](https://github.com/gingerchicken/como-client/issues).

## Features
Most of the features you will get in this client aren't too different to any other client, however here is the list of features.

*Automatic list of features coming soon! Sorry for the wait!*

## Thanks
### The Wurst Project
While developing this I was aided by the source code of the [Wurst Project](https://github.com/Wurst-Imperium/Wurst7), since this was my first ever mod for Minecraft that I had made, I was quite unfamiliar with the way Minecraft and its APIs work etc. therefore, by looking some parts of Wurst's source code, I could quickly understand the API and start making this mod.
