<span style="color:#e03e2d"><em>Mod WIP! Bugs and big changes may happen</em></span>

# Current Features

### Scrolls

* Scrolls are a package of enchantments, having 2-3 enchants .
* Combine equipment with a scroll and lapis lazuli in a smithing table to upgrade an enchantment's level by one.
* Each Equipment has Tool Level that can be seen when placed in a smithing table. When it reaches max, you can no longer
  enchant it using scrolls.
* By default, enchantments are normalized to be 1 or 3 levels.
* Scrolls can be blessings (positive enchantments that use up Tool Levels), curses (negative enchantments that free up
  Tool Levels), and deals (both).
* When getting enchanted books and tools in the wild - They will be reduced to their scrolls.

### Level Ups

* Tools can level up when being used.
* Leveling up will allow them to accept more scrolls.

### XP Rework

* Now each level is 16 points.
* There are 3 new gamerules to improve XP handling:
  * `xpPerLevel` defines the number of experience points per level (default: 16)
  * `percentageXPLost` defines the percentage of XP lost (unrecoverable) on death (default: 0)
  * `percentageXPDropped` defines the percentage of XP dropped on the ground on death (default: 100)
* Anvils can be mend using iron ingot (or #hopeful:anvil\_mends)
* Anvils will fully repair an item using only one repair material (Thanks Forgery for the code)

### Enchanting Table Overhaul

The enchanting table menu is _gone_ Now, clicking on it with a piece of paper has a chance of transforming into a
scroll.

Default chance is quite low though, but you can increase it with catalysts! 

These are blocks that have to be in a 5x5x5 (configurable using `enchantingTableRange` gamerule)
area around the table, and can give different effects.

Currently, we have:
* Lookshelves: increase chance of producing a scroll, but increase xp requirement.
* Lapis Block: decreases xp level requirement, but has a chance of evaporating.
* The following increase chance of a scroll, but increase xp requirement:
  * Magma: flame scroll.
  * Prismarine: fins and gills scrolls
  * Soul blocks & soul fire blocks: undead scroll
  * Vault: wind scroll (has a chance of spawning a breeze)
  * Carved pumpkins: binding scroll
  * Enchanting Table: Endless scroll
  * Lightning Rod: channeling scroll
  * Pointed Dripstone: extermination scroll.
  * Sculk blocks: echoes scroll.

# Dev Stuff

## Scroll

Scrolls will be placed in `datapack_name/hopeful/scroll/scroll_name.json` and its format will look like this:

```json
{
  "title":{
    "translate": "scroll.hopeful.acceleration"
  },
  "type": "blessing",
  "max_level": 3,
  "score_per_level": 1,
  "required_xp_levels": 5,
  "enchantments": "#hopeful:hopeful/acceleration"
}
```

It is highly recommended to use tags, because they allow for easier compatibility, but an array of enchantments would
also work.

The tags are located in `datapack_name/tags/enchantment/hopeful`

## Equipment

Tool Level stats can be modified in `datapack_name/data_maps/item/equipment.json` in
this format:
```json5
{
  "values": {
    "minecraft:chainmail_chestplate": {
      "starting_level": 5,
      // these are the differences between each level up
      "levelups": [10,50,100,0,200,0] 
    },
    "minecraft:crossbow": {
      "starting_level": 3,
      "levelups": [50, 100, 150]
    },
  }
}

```

## Catalysts

These catalysts are data-driven, and we have 5 effects, with plenty more to come.

The catalysts are of the form: `datapack_name/hopeful/catalyst/unused_name.json`

And look like so: (made up catalyst)

```json
{
  "blocks": "minecraft:bedrock",
  "limit": 2,
  "effects": [
    {
      "type": "hopeful:scroll_weight",
      "scrolls": "hopeful:acceleration",
      "increase_by": 2
    },
    {
      "type": "hopeful:xp_levels_requirement",
      "increase_by": 10000
    },
    {
      "type": "hopeful:success_chance",
      "increase_by": 0.5
    }

  ]
}
```