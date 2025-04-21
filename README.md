# Hub Combat
### Simple but sexy plugin for enhance your lobby adding weapons! 

## Why hub combat
Easy to use, free, optimized and feature rich!

It's the first hub combat plugin that includes placeholders!
Also, you can suggest any idea to us at discord or github, which we will think about and probably add.

## Features
- Fully customizable name and lore (MiniMessage based), with PlaceholderAPI support (add placeholders to name or lore!).
- Placeholders: time left, kills, deaths and hits.
- Built in performance

## Commands, permissions and placeholders
- `/hubcombat reload` - reload config and players status. Requieres `hubcombat.admin` permission.
- Placeholders: `%hubcombat_kills%`,  `%hubcombat_deaths%`,  `%hubcombat_hits%`, `%hubcombat_time_left%`.

## Usage and installation
Simply download the plugin .jar, upload to your plugins folder and restart the server.
By default h2 storage is enabled, you don't need to setup that database, but you can migrate to MySQL if multi-lobby is intended!
Messages can be modified at lang.yml file.

On join the weapon will be given to players. They must wait the time specified in config with the weapon equiped in main hand in order to start pvp.
PvP is blocked by the plugin if players/entities are not holding the weapon.

## Roadmap
- Add multiple weapons based on permissions.
- Add (multiple) armor(s) based on permissions too.
- More placeholders if suggested.
