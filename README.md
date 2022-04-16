# One Chunk Survival

Survive on one chunk, expand the area by unlocking advancements.

Key features:
- Managed world border growing.
- Ad-hoc creation of a new session (see commands).
- Only unique advancement progress the border

Requirements:
- Spigot Server (1.18.2)

## Installation

1. Download the .jar file from 
2. Put in it your "plugins" folder of your Spigot Server.
3. Boot up your server and use the commands.

## Commands

```
Command: /ocs-start
Description: Creates a new one chunk survival session, will teleport all players, set world border.
Permission node: ocs.start
```

```
Command: /ocs-stop
Description: Clears the world border and resets the current session.
Permission node: ocs.stop
```

## Clearing Player Achievements

In order for this to work, players need to complete advancements.

Any advancements achieved before the start of a game will be **ignored**. 

One option is to delete advancement data, this is located in your world directory ('world/advancement' folder)

## Config
Default config, configure to suit your needs!

```yml
border:
  start: 16   # Size of the border at start (in blocks, where 16 blocks = one chunk)
  grow: 8     # How much it grows each advancement (in blocks, where 16 blocks = one chunk)
  speed: 3    # How many seconds it takes to grow the border.

# Who can start and stop new sessions (requires OP to work)!
gamemasters:
  - Notch

# Which categories do we count advancements for? (Best left on default)
advancement_namespaces:
  - minecraft:story
  - minecraft:adventure
  - minecraft:end
  - minecraft:husbandry
  - minecraft:nether
```

## Planned / Known limitations
- World border sync in Nether and the End.
- Game state persistence after shutdown/crash.
- Growing and shrinking of world border via commands.
