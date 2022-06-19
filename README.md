# DoubleLifeReloaded

This is my implementation of the Double Life gamemode. This plugin requires Spigot or its forks (e.g. Paper). Please report bugs if there are any.

## Permissions

- `doublelife.admin`: Ability to use commands
- `doublelife.player`: Any player with this permission will be able to participate in the game

## Config

- `shuffleStartDelay`: Specify the delay (in seconds) in between when the admin first executes the `/doublelifestart` command and when the server starts choosing the player's soulmate.

## Commands

- `/doublelifestart`: Start Double Life. Player shuffling will start after a specified number of seconds.
- `/doublelifestop`: Stop Double Life. Executing this command is not recommended unless all players are on the server currently. To reset the game it is recommended to use a new map instead.
- `/lifeset`: Set the life of a player. If this player is soulbound to another player, the other player's life will also be affected.
- `/soulbind`: Soulbind two players who are not yet soulbound to another player.