# List of Features
## AntiInvisible (*`.antiinvisible`*)
[(Source Code)](src/main/java/net/como/client/cheats/AntiInvisible.java) Makes all invisible entities visible.

## AntiItemDrop (*`.antiitemdrop`*)
[(Source Code)](src/main/java/net/como/client/cheats/AntiItemDrop.java) Hide all dropped items so then your friends cannot kill your client repeatedly.

## ArmourDisplay (*`.armourdisplay`*)
[(Source Code)](src/main/java/net/como/client/cheats/ArmourDisplay.java) Renders armour above the hot bar.
### Default Settings
 - RenderEmpty: `True`

## AutoReconnect (*`.autoreconnect`*)
[(Source Code)](src/main/java/net/como/client/cheats/AutoReconnect.java) Automatically reconnects to a server after a given time
### Default Settings
 - Delay: `5`
 - Manual: `True`

## AutoRespawn (*`.autorespawn`*)
[(Source Code)](src/main/java/net/como/client/cheats/AutoRespawn.java) Automatically respawns the player.

## AutoShear (*`.autoshear`*)
[(Source Code)](src/main/java/net/como/client/cheats/AutoShear.java) Shear sheep with a specific colour of wool automatically.
### Default Settings
 - MaxDistance: `3.0`
 - DesiredColours: `{ ... }`

## AutoTotem (*`.autototem`*)
[(Source Code)](src/main/java/net/como/client/cheats/AutoTotem.java) Automatically places a totem into your off hand

## BetterNameTags (*`.betternametags`*)
[(Source Code)](src/main/java/net/como/client/cheats/BetterNameTags.java) Renders a different kind of name-tag above nearby players.
### Default Settings
 - Scale: `0.5`
 - OutlineAlpha: `125`

## Blink (*`.blink`*)
[(Source Code)](src/main/java/net/como/client/cheats/Blink.java) Delay your packets being sent.

## BlockESP (*`.blockesp`*)
[(Source Code)](src/main/java/net/como/client/cheats/BlockESP.java) Makes specific blocks visible through walls.
### Default Settings
 - Blocks: `{ ... }`

## CamFlight (*`.camflight`*)
[(Source Code)](src/main/java/net/como/client/cheats/CamFlight.java) Fly quickly where ever your camera is looking.
### Default Settings
 - Speed: `5.0`

## ChatIgnore (*`.chatignore`*)
[(Source Code)](src/main/java/net/como/client/cheats/ChatIgnore.java) A client-side ignore command
### Default Settings
 - Phrases: `{ ... }`

## Criticals (*`.criticals`*)
[(Source Code)](src/main/java/net/como/client/cheats/Criticals.java) Makes every hit a critical hit.

## CrystalAura (*`.crystalaura`*)
[(Source Code)](src/main/java/net/como/client/cheats/CrystalAura.java) Automatically places/destroys nearby crystals.
### Default Settings
 - MaxDistance: `6.0`
 - LineOfSight: `True`
 - MaxHeightDiff: `1.0`
 - MaxCrystals: `4`
 - RenderTargetBlock: `True`
 - AllowLow: `True`
 - SelectCrystal: `True`
 - PlaceCrystal: `True`
 - Player: `True`
 - Mob: `True`

## ElytraFlight (*`.elytraflight`*)
[(Source Code)](src/main/java/net/como/client/cheats/ElytraFlight.java) Fly with the elytra but without needing fireworks etc.
### Default Settings
 - MaxSpeed: `10.0`
 - Acceleration: `1.1`
 - LegitMode: `True`

## EntityESP (*`.entityesp`*)
[(Source Code)](src/main/java/net/como/client/cheats/EntityESP.java) Know where entities are more easily.
### Default Settings
 - BoxPadding: `0.0`
 - BlendBoxes: `True`
 - GlowColour: `True`
 - DrawMode: `MODE_GLOW`

## Flight (*`.flight`*)
[(Source Code)](src/main/java/net/como/client/cheats/Flight.java) Basic flight (a bit terrible tbh).

## FreeCam (*`.freecam`*)
[(Source Code)](src/main/java/net/como/client/cheats/FreeCam.java) Allows you to fly around the world (but client-side)
### Default Settings
 - Speed: `1.0`

## FullBright (*`.fullbright`*)
[(Source Code)](src/main/java/net/como/client/cheats/FullBright.java) Allows you to see anywhere as if it was day.

## HomeGodMode (*`.homegodmode`*)
[(Source Code)](src/main/java/net/como/client/cheats/HomeGodMode.java) Exploits the /sethome and /home feature on servers
### Default Settings
 - HomeName: `"Death"`
 - RespawnDelay: `250.0`

## ItemRenderTweaks (*`.itemrendertweaks`*)
[(Source Code)](src/main/java/net/como/client/cheats/ItemRenderTweaks.java) Allows you to change how held items are rendered.
### Default Settings
 - RightHand: `True`
 - ROffsetX: `0.0`
 - ROffsetY: `0.0`
 - ROffsetZ: `0.0`
 - RScaleX: `1.0`
 - RScaleY: `1.0`
 - RScaleZ: `1.0`
 - LeftHand: `True`
 - LOffsetX: `0.0`
 - LOffsetY: `0.0`
 - LOffsetZ: `0.0`
 - LScaleX: `1.0`
 - LScaleY: `1.0`
 - LScaleZ: `1.0`

## KillAura (*`.killaura`*)
[(Source Code)](src/main/java/net/como/client/cheats/KillAura.java) Automatically attacks specified targets.
### Default Settings
 - MaxDistance: `7.0`
 - Delay: `0.0`
 - SilentAim: `True`
 - AttackFriends: `True`
 - TargetClosestAngle: `True`
 - MaxFOV: `25.0`
 - TargetTracers: `True`
 - TracerLifeSpan: `0.25`

## ModList (*`.modlist`*)
[(Source Code)](src/main/java/net/como/client/cheats/ModList.java) Displays all of your enabled mods
### Default Settings
 - ColouringMode: `"default"`
 - Scale: `1.0`
 - RGBIntensity: `5`
 - Positioning: `1`

## NoBoss (*`.noboss`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoBoss.java) Hide annoying boss bars and their effects.

## NoBreak (*`.nobreak`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoBreak.java) Prevent your pickaxes from accidentally breaking.
### Default Settings
 - MinDurability: `1`

## NoEnchantmentBook (*`.noenchantmentbook`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoEnchantmentBook.java) Hide the enchantment book on the enchantment table.

## NoFall (*`.nofall`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoFall.java) Take less fall damage.

## NoFireCam (*`.nofirecam`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoFireCam.java) Disables the annoying fire overlay to allow you to see a bit better.

## NoHurtCam (*`.nohurtcam`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoHurtCam.java) Disables the screen rotation when getting damaged.

## NoPortal (*`.noportal`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoPortal.java) Allows portal effects to be toggled.
### Default Settings
 - NoOverlay: `True`
 - NoNausea: `True`
 - AllowTyping: `True`

## NoRespondAlert (*`.norespondalert`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoRespondAlert.java) Displays an alert when the server has stopped sending data.
### Default Settings
 - WarningTime: `1.0`
 - DisplayHeight: `150`

## NoSubmerge (*`.nosubmerge`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoSubmerge.java) Allows submerge overlays to be toggled.
### Default Settings
 - Lava: `True`
 - Water: `True`
 - PowderSnow: `True`

## NoWeather (*`.noweather`*)
[(Source Code)](src/main/java/net/como/client/cheats/NoWeather.java) Hides the rain.

## ShulkerPeak (*`.shulkerpeak`*)
[(Source Code)](src/main/java/net/como/client/cheats/ShulkerPeak.java) Displays the contents of shulkers without opening them.
### Default Settings
 - HUDOverlay: `True`

## SpeedHack (*`.speedhack`*)
[(Source Code)](src/main/java/net/como/client/cheats/SpeedHack.java) Go quicker than normal.
### Default Settings
 - Acceleration: `2.2`
 - MaxSpeed: `2.0`

## SuperJump (*`.superjump`*)
[(Source Code)](src/main/java/net/como/client/cheats/SuperJump.java) Jump higher than you should.
### Default Settings
 - UpwardSpeed: `2.0`

## TapeMeasure (*`.tapemeasure`*)
[(Source Code)](src/main/java/net/como/client/cheats/TapeMeasure.java) Measure the distance between two points.
### Default Settings
 - PyDistance: `True`
 - DisableRenderCap: `True`

## Timer (*`.timer`*)
[(Source Code)](src/main/java/net/como/client/cheats/Timer.java) Change the client-side tick rate.
### Default Settings
 - Speed: `1.0`

## TotemHide (*`.totemhide`*)
[(Source Code)](src/main/java/net/como/client/cheats/TotemHide.java) Hide the totem item.

## Tracers (*`.tracers`*)
[(Source Code)](src/main/java/net/como/client/cheats/Tracers.java) Draws tracers to specified targets.
### Default Settings
 - Player: `True`
 - Mob: `True`
 - Item: `True`
 - OtherEntities: `True`
 - Invisible: `True`
 - Block: `True`
 - Blocks: `{ ... }`
 - Transparency: `1.0`

## Watermark (*`.watermark`*)
[(Source Code)](src/main/java/net/como/client/cheats/Watermark.java) Renders the Como Client watermark on the screen.
### Default Settings
 - Scale: `1.0`

## Waypoints (*`.waypoints`*)
[(Source Code)](src/main/java/net/como/client/cheats/Waypoints.java) Renders where waypoints are in the world.

## XRay (*`.xray`*)
[(Source Code)](src/main/java/net/como/client/cheats/XRay.java) See blocks through the floor.
### Default Settings
 - AutoFullbright: `True`
 - DesiredBlocks: `{ ... }`
 - NonSpecificSearch: `True`
 - BlockSearch: `{ ... }`

