# BuildFFA ⚔️🛡️ (Optimized for Minecraft 1.21.4)

A fast-paced and engaging BuildFFA/KnockbackFFA gamemode plugin, now optimized for Minecraft 1.21.4! Based on the original [BuildFFA](https://github.com/AdiihYT/BuildFFA) project, this version brings enhanced performance and compatibility for the latest Minecraft experience.

## Key Features ✨

- **Disappearing Blocks:** Dynamic block regeneration system with visual feedback (green, yellow, orange, red states). 🧱➡️💨
- **Customizable Kits:** Equip players with default kits and allow them to save their preferred loadouts. 🎒
- **Automated Map Cycle:** Keep the gameplay fresh with a rotating map system. 🔄 Integrates seamlessly with [ASWM](https://github.com/Paul19988/Advanced-Slime-World-Manager) for world management. 🗺️
- **Configurable Game Rules:** Tailor the gameplay experience with per-map game rules (e.g., autorespawn, no fall damage). 📜
- **World Setup:** Define game maps with custom display names, spawn points, and void kill heights. 📍
- **Killstreak Tracking:** Reward skilled players with a killstreak counter. 💀
- **PlaceholderAPI Support:** Enhance your server with PlaceholderAPI integration for dynamic information display. 💬
- **Heal-on-Kill:** Encourage aggressive play with health regeneration on successful kills. ❤️‍🩹
- **Instant Void Kill:** Eliminate players who fall into the void at a specified Y-coordinate. 🕳️
- **Kill Commands:** Execute custom commands upon a player's death for unique effects. 🔪

**Special Items:**

- **Trampoline:** Launch players high into the air with a right-click. 🦘
- **More to come!** Stay tuned for new and exciting items. ⏳

## Planned Features 📝

- **Bonus Implementation:** Introduce gameplay-altering bonuses (ENDER_PEARL_SAVER, STRENGTH, POISON_HIT, SPEED). ➕

## Admin Commands 💻

Base command: **/buildffa** or **/bffa**

**Subcommands:**

- `/bffa give (player) (item) (amount)`: Grant players special items. 🎁
- `/bffa reload`: Reload the plugin's configuration files. 🔄
- `/bffa setmap (map)`: Force the game to switch to a specific map. 🗺️
- `/bffa skipmap`: Immediately advance to the next map in the cycle. ⏭️
- `/bffa activatebonus (bonus enum) (activator player)`: Activate a bonus for a limited time. *(Not yet implemented)* ➕
- `/bffa resetbonuses`: Clear all active bonuses. *(Not yet implemented)* 🔄

## Setup Commands ⚙️

Base command: **/buildffa setup** or **/bffa setup**

**Subcommands:**

- `/bffa setup create (world)`: Create a new BuildFFA map from an existing world. ➕
- `/bffa setup setdisplayname (map) (displayname)`: Set the display name for a map. ✏️
- `/bffa setup setspawn (map)`: Set the spawn point for a map. 📍
- `/bffa setup setvoidkillheight (map) (y)`: Configure the void kill height for a map. 🕳️