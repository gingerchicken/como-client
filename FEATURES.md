# List of Features
## AntiInvisible (`.antiinvisible`)
[(Source Code)](src/main/java/net/como/client/modules/render/AntiInvisible.java) Makes all invisible entities visible.

## AntiItemDrop (`.antiitemdrop`)
[(Source Code)](src/main/java/net/como/client/modules/render/AntiItemDrop.java) Hide all dropped items so then your friends cannot kill your client repeatedly.

## AntiKick (`.antikick`)
[(Source Code)](src/main/java/net/como/client/modules/packet/AntiKick.java) Blocks packets that cause you to disconnect from the server

## AntiResourcePack (`.antiresourcepack`)
[(Source Code)](src/main/java/net/como/client/modules/packet/AntiResourcePack.java) Deny all resource packs but say to the server that they got downloaded.

## ArmourDisplay (`.armourdisplay`)
[(Source Code)](src/main/java/net/como/client/modules/hud/ArmourDisplay.java) Renders armour above the hot bar.
### Default Settings
 - RenderEmpty: `True`

## AutoReconnect (`.autoreconnect`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/AutoReconnect.java) Automatically reconnects to a server after a given time.
### Default Settings
 - Delay: `5`
 - InGameButton: `True`
 - Manual: `True`

## AutoRespawn (`.autorespawn`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/AutoRespawn.java) Automatically respawns the player.

## AutoShear (`.autoshear`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/AutoShear.java) Shear sheep with a specific colour of wool automatically.
### Default Settings
 - DesiredColours: `{ ... }`
 - MaxDistance: `3.0`

## AutoSprint (`.autosprint`)
[(Source Code)](src/main/java/net/como/client/modules/movement/AutoSprint.java) Makes you sprint whenever you move.

## AutoTotem (`.autototem`)
[(Source Code)](src/main/java/net/como/client/modules/combat/AutoTotem.java) Automatically places a totem into your off hand

## AutoWalk (`.autowalk`)
[(Source Code)](src/main/java/net/como/client/modules/movement/AutoWalk.java) A simple module that just walks forward without you having to press anything.

## BetterNameTags (`.betternametags`)
[(Source Code)](src/main/java/net/como/client/modules/render/BetterNameTags.java) Renders a different kind of name-tag above nearby players.
### Default Settings
 - OutlineAlpha: `125`
 - Scale: `0.5`

## Binds (`.binds`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/Binds.java) Allows you to bind client commands to keys.
### Default Settings
 - GUIKey: `True`
 - HideCommandOutput: `True`

## Blink (`.blink`)
[(Source Code)](src/main/java/net/como/client/modules/movement/Blink.java) Delay your packets being sent.

## BlockESP (`.blockesp`)
[(Source Code)](src/main/java/net/como/client/modules/render/BlockESP.java) Makes specific blocks visible through walls.
### Default Settings
 - Blocks: `{ ... }`

## CamFlight (`.camflight`)
[(Source Code)](src/main/java/net/como/client/modules/movement/CamFlight.java) Fly quickly where ever your camera is looking.
### Default Settings
 - Speed: `5.0`

## ChatIgnore (`.chatignore`)
[(Source Code)](src/main/java/net/como/client/modules/chat/ChatIgnore.java) A client-side ignore command
### Default Settings
 - Phrases: `{ ... }`

## ChatSpam (`.chatspam`)
[(Source Code)](src/main/java/net/como/client/modules/chat/ChatSpam.java) Spams the chat with different messages
### Default Settings
 - Delay: `0.5`
 - HashPrefix: `True`
 - Messages: `{ ... }`
 - RandPrefix: `True`
 - RandomCase: `True`

## ClickGUI (`.clickgui`)
[(Source Code)](src/main/java/net/como/client/modules/hud/ClickGUI.java) A way of toggling your settings with a GUI (Currently WIP)
### Default Settings
 - BlockWidth: `90`
 - Bouncy: `True`
 - BouncySpeed: `1.0`
 - HorizontalSpacing: `15`
 - Scale: `1.0`
 - TotalBouncies: `1`
 - VerticalSpacing: `5`

## CommandAutoFill (`.commandautofill`)
[(Source Code)](src/main/java/net/como/client/modules/chat/CommandAutoFill.java) Allows you to have command auto-fill in chat.

## CraftingDupe (`.craftingdupe`)
[(Source Code)](src/main/java/net/como/client/modules/dupes/CraftingDupe.java) An old drop dupe for versions 1.17 and earlier
### Default Settings
 - ShowTip: `True`

## CreativeMagic (`.creativemagic`)
[(Source Code)](src/main/java/net/como/client/modules/exploits/CreativeMagic.java) Adds a creative item group with custom NBT items.

## Criticals (`.criticals`)
[(Source Code)](src/main/java/net/como/client/modules/combat/Criticals.java) Makes every hit a critical hit.

## CrystalAura (`.crystalaura`)
[(Source Code)](src/main/java/net/como/client/modules/combat/CrystalAura.java) Automatically places/destroys nearby crystals.
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
[(Source Code)](src/main/java/net/como/client/modules/utilities/DiscordRichPres.java) Displays which client you are using in discord rich presence.
### Default Settings
 - ShowServer: `True`

## ElytraFlight (`.elytraflight`)
[(Source Code)](src/main/java/net/como/client/modules/movement/ElytraFlight.java) Fly with the elytra but without needing fireworks etc.
### Default Settings
 - Acceleration: `1.1`
 - LegitMode: `True`
 - MaxSpeed: `10.0`

## EntityESP (`.entityesp`)
[(Source Code)](src/main/java/net/como/client/modules/render/EntityESP.java) Know where entities are more easily.
### Default Settings
 - BlendBoxes: `True`
 - BoxPadding: `0.0`
 - DrawMode: `MODE_GLOW`
 - GlowColour: `True`

## EntitySpeed (`.entityspeed`)
[(Source Code)](src/main/java/net/como/client/modules/movement/EntitySpeed.java) Allows you to set a mounted entity's speed, you can also control entities without saddles.
### Default Settings
 - Flight: `True`
 - ForceAngles: `True`
 - Speed: `5.0`

## EntitySpin (`.entityspin`)
[(Source Code)](src/main/java/net/como/client/modules/movement/EntitySpin.java) Rotates the ridden entity at varying speed.
### Default Settings
 - Pitch: `True`
 - Speed: `5.0`
 - Yaw: `True`

## FakeClient (`.fakeclient`)
[(Source Code)](src/main/java/net/como/client/modules/packet/FakeClient.java) Makes the client appear as vanilla to any servers
### Default Settings
 - Client: `"vanilla"`

## FastBreak (`.fastbreak`)
[(Source Code)](src/main/java/net/como/client/modules/packet/FastBreak.java) Allows you to break blocks a bit quicker.
### Default Settings
 - BreakDelay: `0.0`
 - BreakMultiplier: `2`
 - MultiplierOnly: `True`
 - Potion: `True`
 - PotionAmplifier: `3`

## FastUse (`.fastuse`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/FastUse.java) Allows you to use items at light speed
### Default Settings
 - TickDelay: `0`

## Flight (`.flight`)
[(Source Code)](src/main/java/net/como/client/modules/movement/Flight.java) Basic flight (a bit terrible tbh).

## FreeCam (`.freecam`)
[(Source Code)](src/main/java/net/como/client/modules/render/FreeCam.java) Allows you to fly around the world (but client-side)
### Default Settings
 - PosReset: `True`
 - Speed: `1.0`

## FullBright (`.fullbright`)
[(Source Code)](src/main/java/net/como/client/modules/render/FullBright.java) Allows you to see anywhere as if it was day.
### Default Settings
 - PotionEffect: `True`

## HClip (`.hclip`)
[(Source Code)](src/main/java/net/como/client/modules/packet/HClip.java) Teleports the player a set amount of blocks away
### Default Settings
 - AngleRelative: `True`
 - ChatMessage: `True`
 - X: `0.0`
 - Y: `0.0`
 - Z: `0.0`

## HideTitleMessage (`.hidetitlemessage`)
[(Source Code)](src/main/java/net/como/client/modules/render/HideTitleMessage.java) Hides the thank you message on the title screen.

## Hitmarker (`.hitmarker`)
[(Source Code)](src/main/java/net/como/client/modules/hud/Hitmarker.java) Show when you hit someone with a melee weapon
### Default Settings
 - AlphaStep: `255.0`
 - AnimClose: `True`
 - AnimOpen: `True`
 - HoldTicks: `0`
 - MaxDelay: `1.0`
 - Scale: `0.25`
 - Sound: `True`

## HomeGodMode (`.homegodmode`)
[(Source Code)](src/main/java/net/como/client/modules/exploits/HomeGodMode.java) Exploits the /sethome and /home feature on servers
### Default Settings
 - CommandDelay: `250.0`
 - DeleteHome: `True`
 - HomeName: `"Death"`
 - RespawnDelay: `250.0`

## InfChat (`.infchat`)
[(Source Code)](src/main/java/net/como/client/modules/chat/InfChat.java) Allows you to type as much as you want in the chat.

## InstaBowKill (`.instabowkill`)
[(Source Code)](src/main/java/net/como/client/modules/exploits/InstaBowKill.java) Kill anyone instantly with a bow
### Default Settings
 - Multiplier: `100`

## ItemRenderTweaks (`.itemrendertweaks`)
[(Source Code)](src/main/java/net/como/client/modules/render/ItemRenderTweaks.java) Allows you to change how held items are rendered.
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
[(Source Code)](src/main/java/net/como/client/modules/combat/KillAura.java) Automatically attacks specified targets.
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
[(Source Code)](src/main/java/net/como/client/modules/hud/ModList.java) Displays all of your enabled mods
### Default Settings
 - ColouringMode: `"default"`
 - Positioning: `1`
 - RGBIntensity: `5`
 - Scale: `1.0`

## NoBoss (`.noboss`)
[(Source Code)](src/main/java/net/como/client/modules/render/NoBoss.java) Hide annoying boss bars and their effects.

## NoBreak (`.nobreak`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/NoBreak.java) Prevent your pickaxes from accidentally breaking.
### Default Settings
 - MinDurability: `1`

## NoComCrash (`.nocomcrash`)
[(Source Code)](src/main/java/net/como/client/modules/exploits/NoComCrash.java) Hits lots of blocks in random locations causing chunks to be loaded.
### Default Settings
 - Amount: `1000`
 - Height: `255`

## NoEffect (`.noeffect`)
[(Source Code)](src/main/java/net/como/client/modules/render/NoEffect.java) Allows you to disable specific effects.
### Default Settings
 - Effects: `{ ... }`

## NoEnchantmentBook (`.noenchantmentbook`)
[(Source Code)](src/main/java/net/como/client/modules/render/NoEnchantmentBook.java) Hide the enchantment book on the enchantment table.

## NoFall (`.nofall`)
[(Source Code)](src/main/java/net/como/client/modules/packet/NoFall.java) Take less fall damage.

## NoFireCam (`.nofirecam`)
[(Source Code)](src/main/java/net/como/client/modules/render/NoFireCam.java) Disables the annoying fire overlay to allow you to see a bit better.

## NoHurtCam (`.nohurtcam`)
[(Source Code)](src/main/java/net/como/client/modules/render/NoHurtCam.java) Disables the screen rotation when getting damaged.

## NoPortal (`.noportal`)
[(Source Code)](src/main/java/net/como/client/modules/render/NoPortal.java) Allows portal effects to be toggled.
### Default Settings
 - AllowTyping: `True`
 - NoNausea: `True`
 - NoOverlay: `True`

## NoRespondAlert (`.norespondalert`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/NoRespondAlert.java) Displays an alert when the server has stopped sending data.
### Default Settings
 - DisplayHeight: `150`
 - ShowWhenClosed: `True`
 - WarningTime: `1.0`

## NoSubmerge (`.nosubmerge`)
[(Source Code)](src/main/java/net/como/client/modules/render/NoSubmerge.java) Allows submerge overlays to be toggled.
### Default Settings
 - Lava: `True`
 - PowderSnow: `True`
 - Water: `True`

## NoWeather (`.noweather`)
[(Source Code)](src/main/java/net/como/client/modules/render/NoWeather.java) Hides the rain.

## Nuker (`.nuker`)
[(Source Code)](src/main/java/net/como/client/modules/packet/Nuker.java) Breaks the blocks around you in a given radius
### Default Settings
 - Blocks: `new HashMap<>()`
 - ForceAngles: `True`
 - ForceBreak: `True`
 - Radius: `2`
 - RenderTargets: `True`
 - SeriesBreak: `True`
 - Silent: `True`
 - SpecificBlocks: `True`

## PacketFlight (`.packetflight`)
[(Source Code)](src/main/java/net/como/client/modules/packet/PacketFlight.java) Sets your position relative to your controls
### Default Settings
 - Elytra: `True`
 - Noclip: `True`
 - Step: `0.25`
 - ZeroVelocity: `True`

## PacketLimiter (`.packetlimiter`)
[(Source Code)](src/main/java/net/como/client/modules/packet/PacketLimiter.java) Delays packets to prevent getting kicked for 'too many packets'.
### Default Settings
 - IgnoreMovement: `True`
 - MaxPackets: `10`
 - SendDelay: `0.05`

## ShulkerDupe (`.shulkerdupe`)
[(Source Code)](src/main/java/net/como/client/modules/dupes/ShulkerDupe.java) Allows the user to duplicate shulker boxes (Vanilla Only.)
### Default Settings
 - DupeAll: `True`
 - TargetSlot: `0`

## ShulkerPeak (`.shulkerpeak`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/ShulkerPeak.java) Displays the contents of shulkers without opening them.
### Default Settings
 - HUDOverlay: `True`

## SignSearch (`.signsearch`)
[(Source Code)](src/main/java/net/como/client/modules/render/SignSearch.java) Allows you to search for signs and their text.
### Default Settings
 - Box: `True`
 - CaseSensitive: `True`
 - SearchText: `{ ... }`
 - Tracer: `True`

## SpeedHack (`.speedhack`)
[(Source Code)](src/main/java/net/como/client/modules/movement/SpeedHack.java) Go quicker than normal.
### Default Settings
 - Acceleration: `2.2`
 - MaxSpeed: `2.0`

## SuperJump (`.superjump`)
[(Source Code)](src/main/java/net/como/client/modules/movement/SuperJump.java) Jump higher than you should.
### Default Settings
 - UpwardSpeed: `2.0`

## TapeMeasure (`.tapemeasure`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/TapeMeasure.java) Measure the distance between two points.
### Default Settings
 - DisableRenderCap: `True`
 - PyDistance: `True`

## Timer (`.timer`)
[(Source Code)](src/main/java/net/como/client/modules/movement/Timer.java) Change the client-side tick rate.
### Default Settings
 - Speed: `1.0`

## TotemHide (`.totemhide`)
[(Source Code)](src/main/java/net/como/client/modules/render/TotemHide.java) Hide the totem item.

## TotemPopCount (`.totempopcount`)
[(Source Code)](src/main/java/net/como/client/modules/combat/TotemPopCount.java) This counts the total number of totems used by a player before death.
### Default Settings
 - CountDuration: `60.0`
 - DeathMessage: `True`

## Tracers (`.tracers`)
[(Source Code)](src/main/java/net/como/client/modules/render/Tracers.java) Draws tracers to specified targets.
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
[(Source Code)](src/main/java/net/como/client/modules/utilities/UnfocusCPU.java) Decreases game performance while the window is not focused.
### Default Settings
 - MaxFPS: `15`

## Watermark (`.watermark`)
[(Source Code)](src/main/java/net/como/client/modules/hud/Watermark.java) Renders the Como Client watermark on the screen.
### Default Settings
 - Scale: `1.0`

## Waypoints (`.waypoints`)
[(Source Code)](src/main/java/net/como/client/modules/utilities/Waypoints.java) Renders where waypoints are in the world.

## XCarry (`.xcarry`)
[(Source Code)](src/main/java/net/como/client/modules/packet/XCarry.java) Allows odd behavior such as storing items in the crafting table slot.

## XRay (`.xray`)
[(Source Code)](src/main/java/net/como/client/modules/render/XRay.java) See blocks through the floor.
### Default Settings
 - AutoFullbright: `True`
 - BlockSearch: `{ ... }`
 - DesiredBlocks: `{ ... }`
 - NonSpecificSearch: `True`

## XStorage (`.xstorage`)
[(Source Code)](src/main/java/net/como/client/modules/packet/XStorage.java) Cancels the close screen packet for everything other than inventory.

