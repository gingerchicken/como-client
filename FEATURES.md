# List of Features
## AntiInvisible (`.antiinvisible`)
[(Source Code)](src/main/java/net/como/client/modules/AntiInvisible.java) Makes all invisible entities visible.

## AntiItemDrop (`.antiitemdrop`)
[(Source Code)](src/main/java/net/como/client/modules/AntiItemDrop.java) Hide all dropped items so then your friends cannot kill your client repeatedly.

## ArmourDisplay (`.armourdisplay`)
[(Source Code)](src/main/java/net/como/client/modules/ArmourDisplay.java) Renders armour above the hot bar.
### Default Settings
 - RenderEmpty: `True`

## AutoReconnect (`.autoreconnect`)
[(Source Code)](src/main/java/net/como/client/modules/AutoReconnect.java) Automatically reconnects to a server after a given time.
### Default Settings
 - Delay: `5`
 - Manual: `True`

## AutoRespawn (`.autorespawn`)
[(Source Code)](src/main/java/net/como/client/modules/AutoRespawn.java) Automatically respawns the player.

## AutoShear (`.autoshear`)
[(Source Code)](src/main/java/net/como/client/modules/AutoShear.java) Shear sheep with a specific colour of wool automatically.
### Default Settings
 - DesiredColours: `{ ... }`
 - MaxDistance: `3.0`

## AutoTotem (`.autototem`)
[(Source Code)](src/main/java/net/como/client/modules/AutoTotem.java) Automatically places a totem into your off hand

## BetterNameTags (`.betternametags`)
[(Source Code)](src/main/java/net/como/client/modules/BetterNameTags.java) Renders a different kind of name-tag above nearby players.
### Default Settings
 - OutlineAlpha: `125`
 - Scale: `0.5`

## Binds (`.binds`)
[(Source Code)](src/main/java/net/como/client/modules/Binds.java) Allows you to bind client commands to keys.
### Default Settings
 - HideCommandOutput: `True`

## Blink (`.blink`)
[(Source Code)](src/main/java/net/como/client/modules/Blink.java) Delay your packets being sent.

## BlockESP (`.blockesp`)
[(Source Code)](src/main/java/net/como/client/modules/BlockESP.java) Makes specific blocks visible through walls.
### Default Settings
 - Blocks: `{ ... }`

## CamFlight (`.camflight`)
[(Source Code)](src/main/java/net/como/client/modules/CamFlight.java) Fly quickly where ever your camera is looking.
### Default Settings
 - Speed: `5.0`

## ChatIgnore (`.chatignore`)
[(Source Code)](src/main/java/net/como/client/modules/ChatIgnore.java) A client-side ignore command
### Default Settings
 - Phrases: `{ ... }`

## Criticals (`.criticals`)
[(Source Code)](src/main/java/net/como/client/modules/Criticals.java) Makes every hit a critical hit.

## CrystalAura (`.crystalaura`)
[(Source Code)](src/main/java/net/como/client/modules/CrystalAura.java) Automatically places/destroys nearby crystals.
### Default Settings
 - AllowLow: `True`
 - LineOfSight: `True`
 - MaxCrystals: `4`
 - MaxDistance: `6.0`
 - MaxHeightDiff: `1.0`
 - Mob: `True`
 - PlaceCrystal: `True`
 - Player: `True`
 - RenderTargetBlock: `True`
 - SelectCrystal: `True`

## DiscordRichPres (`.discordrichpres`)
[(Source Code)](src/main/java/net/como/client/modules/DiscordRichPres.java) Displays which client you are using in discord rich presence.
### Default Settings
 - ShowServer: `True`

## ElytraFlight (`.elytraflight`)
[(Source Code)](src/main/java/net/como/client/modules/ElytraFlight.java) Fly with the elytra but without needing fireworks etc.
### Default Settings
 - Acceleration: `1.1`
 - LegitMode: `True`
 - MaxSpeed: `10.0`

## EntityESP (`.entityesp`)
[(Source Code)](src/main/java/net/como/client/modules/EntityESP.java) Know where entities are more easily.
### Default Settings
 - BlendBoxes: `True`
 - BoxPadding: `0.0`
 - DrawMode: `MODE_GLOW`
 - GlowColour: `True`

## EntitySpeed (`.entityspeed`)
[(Source Code)](src/main/java/net/como/client/modules/EntitySpeed.java) Allows you to set a mounted entity's speed.
### Default Settings
 - Flight: `True`
 - Speed: `5.0`

## FastBreak (`.fastbreak`)
[(Source Code)](src/main/java/net/como/client/modules/FastBreak.java) Allows you to break blocks a bit quicker.
### Default Settings
 - BreakDelay: `0.0`
 - Potion: `True`
 - PotionAmplifier: `3`

## Flight (`.flight`)
[(Source Code)](src/main/java/net/como/client/modules/Flight.java) Basic flight (a bit terrible tbh).

## FreeCam (`.freecam`)
[(Source Code)](src/main/java/net/como/client/modules/FreeCam.java) Allows you to fly around the world (but client-side)
### Default Settings
 - Speed: `1.0`

## FullBright (`.fullbright`)
[(Source Code)](src/main/java/net/como/client/modules/FullBright.java) Allows you to see anywhere as if it was day.
### Default Settings
 - PotionEffect: `True`

## HideTitleMessage (`.hidetitlemessage`)
[(Source Code)](src/main/java/net/como/client/modules/HideTitleMessage.java) Hides the thank you message on the title screen.

## HomeGodMode (`.homegodmode`)
[(Source Code)](src/main/java/net/como/client/modules/HomeGodMode.java) Exploits the /sethome and /home feature on servers
### Default Settings
 - HomeName: `"Death"`
 - RespawnDelay: `250.0`

## ItemRenderTweaks (`.itemrendertweaks`)
[(Source Code)](src/main/java/net/como/client/modules/ItemRenderTweaks.java) Allows you to change how held items are rendered.
### Default Settings
 - LOffsetX: `0.0`
 - LOffsetY: `0.0`
 - LOffsetZ: `0.0`
 - LScaleX: `1.0`
 - LScaleY: `1.0`
 - LScaleZ: `1.0`
 - LeftHand: `True`
 - ROffsetX: `0.0`
 - ROffsetY: `0.0`
 - ROffsetZ: `0.0`
 - RScaleX: `1.0`
 - RScaleY: `1.0`
 - RScaleZ: `1.0`
 - RightHand: `True`

## KillAura (`.killaura`)
[(Source Code)](src/main/java/net/como/client/modules/KillAura.java) Automatically attacks specified targets.
### Default Settings
 - AttackFriends: `True`
 - Delay: `0.0`
 - MaxDistance: `7.0`
 - MaxFOV: `25.0`
 - SilentAim: `True`
 - TargetClosestAngle: `True`
 - TargetTracers: `True`
 - TracerLifeSpan: `0.25`

## ModList (`.modlist`)
[(Source Code)](src/main/java/net/como/client/modules/ModList.java) Displays all of your enabled mods
### Default Settings
 - ColouringMode: `"default"`
 - Positioning: `1`
 - RGBIntensity: `5`
 - Scale: `1.0`

## NoBoss (`.noboss`)
[(Source Code)](src/main/java/net/como/client/modules/NoBoss.java) Hide annoying boss bars and their effects.

## NoBreak (`.nobreak`)
[(Source Code)](src/main/java/net/como/client/modules/NoBreak.java) Prevent your pickaxes from accidentally breaking.
### Default Settings
 - MinDurability: `1`

## NoEnchantmentBook (`.noenchantmentbook`)
[(Source Code)](src/main/java/net/como/client/modules/NoEnchantmentBook.java) Hide the enchantment book on the enchantment table.

## NoFall (`.nofall`)
[(Source Code)](src/main/java/net/como/client/modules/NoFall.java) Take less fall damage.

## NoFireCam (`.nofirecam`)
[(Source Code)](src/main/java/net/como/client/modules/NoFireCam.java) Disables the annoying fire overlay to allow you to see a bit better.

## NoHurtCam (`.nohurtcam`)
[(Source Code)](src/main/java/net/como/client/modules/NoHurtCam.java) Disables the screen rotation when getting damaged.

## NoPortal (`.noportal`)
[(Source Code)](src/main/java/net/como/client/modules/NoPortal.java) Allows portal effects to be toggled.
### Default Settings
 - AllowTyping: `True`
 - NoNausea: `True`
 - NoOverlay: `True`

## NoRespondAlert (`.norespondalert`)
[(Source Code)](src/main/java/net/como/client/modules/NoRespondAlert.java) Displays an alert when the server has stopped sending data.
### Default Settings
 - DisplayHeight: `150`
 - WarningTime: `1.0`

## NoSubmerge (`.nosubmerge`)
[(Source Code)](src/main/java/net/como/client/modules/NoSubmerge.java) Allows submerge overlays to be toggled.
### Default Settings
 - Lava: `True`
 - PowderSnow: `True`
 - Water: `True`

## NoWeather (`.noweather`)
[(Source Code)](src/main/java/net/como/client/modules/NoWeather.java) Hides the rain.

## ShulkerDupe (`.shulkerdupe`)
[(Source Code)](src/main/java/net/como/client/modules/ShulkerDupe.java) Allows the user to duplicate shulker boxes (Vanilla Only.)
### Default Settings
 - DupeAll: `True`
 - TargetSlot: `0`

## ShulkerPeak (`.shulkerpeak`)
[(Source Code)](src/main/java/net/como/client/modules/ShulkerPeak.java) Displays the contents of shulkers without opening them.
### Default Settings
 - HUDOverlay: `True`

## SpeedHack (`.speedhack`)
[(Source Code)](src/main/java/net/como/client/modules/SpeedHack.java) Go quicker than normal.
### Default Settings
 - Acceleration: `2.2`
 - MaxSpeed: `2.0`

## SuperJump (`.superjump`)
[(Source Code)](src/main/java/net/como/client/modules/SuperJump.java) Jump higher than you should.
### Default Settings
 - UpwardSpeed: `2.0`

## TapeMeasure (`.tapemeasure`)
[(Source Code)](src/main/java/net/como/client/modules/TapeMeasure.java) Measure the distance between two points.
### Default Settings
 - DisableRenderCap: `True`
 - PyDistance: `True`

## Timer (`.timer`)
[(Source Code)](src/main/java/net/como/client/modules/Timer.java) Change the client-side tick rate.
### Default Settings
 - Speed: `1.0`

## TotemHide (`.totemhide`)
[(Source Code)](src/main/java/net/como/client/modules/TotemHide.java) Hide the totem item.

## TotemPopCount (`.totempopcount`)
[(Source Code)](src/main/java/net/como/client/modules/TotemPopCount.java) This counts the total number of totems used by a player before death.
### Default Settings
 - CountDuration: `60.0`
 - DeathMessage: `True`

## Tracers (`.tracers`)
[(Source Code)](src/main/java/net/como/client/modules/Tracers.java) Draws tracers to specified targets.
### Default Settings
 - Block: `True`
 - Blocks: `{ ... }`
 - Invisible: `True`
 - Item: `True`
 - Mob: `True`
 - OtherEntities: `True`
 - Player: `True`
 - Transparency: `1.0`

## UnfocusCPU (`.unfocuscpu`)
[(Source Code)](src/main/java/net/como/client/modules/UnfocusCPU.java) Decreases game performance while the window is not focused.
### Default Settings
 - MaxFPS: `15`

## Watermark (`.watermark`)
[(Source Code)](src/main/java/net/como/client/modules/Watermark.java) Renders the Como Client watermark on the screen.
### Default Settings
 - Scale: `1.0`

## Waypoints (`.waypoints`)
[(Source Code)](src/main/java/net/como/client/modules/Waypoints.java) Renders where waypoints are in the world.

## XCarry (`.xcarry`)
[(Source Code)](src/main/java/net/como/client/modules/XCarry.java) Allows odd behavior such as storing items in the crafting table slot.

## XRay (`.xray`)
[(Source Code)](src/main/java/net/como/client/modules/XRay.java) See blocks through the floor.
### Default Settings
 - AutoFullbright: `True`
 - BlockSearch: `{ ... }`
 - DesiredBlocks: `{ ... }`
 - NonSpecificSearch: `True`

