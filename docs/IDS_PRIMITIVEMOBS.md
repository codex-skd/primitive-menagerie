# ID mapping — Primitive Mobs (1.12.2) → Primitive Menagerie (NeoForge 1.21.1)

This table maps every old registry name (`primitivemobs:<name>`, from
`temp/original/.../core/PrimitiveMobsEntityRegistry.java` and `PrimitiveMobsItems.java`) to the new
registry name (`primitive_menagerie:<name>`, from `PMEntities`/`PMItems`). It feeds the Warhost
structure converter.

The namespace changes from `primitivemobs` to `primitive_menagerie`; the path is kept identical for
every entry, so the converter can usually rewrite the namespace only.

## Creatures (entity types)

| Old id (1.12) | New id (1.21) | Notes |
|---|---|---|
| `primitivemobs:chameleon` | `primitive_menagerie:chameleon` | |
| `primitivemobs:treasure_slime` | `primitive_menagerie:treasure_slime` | |
| `primitivemobs:haunted_tool` | `primitive_menagerie:haunted_tool` | |
| `primitivemobs:grovesprite` | `primitive_menagerie:grovesprite` | |
| `primitivemobs:bewitched_tome` | `primitive_menagerie:bewitched_tome` | original class `EntityEnchantedBook` |
| `primitivemobs:filch_lizard` | `primitive_menagerie:filch_lizard` | |
| `primitivemobs:brain_slime` | `primitive_menagerie:brain_slime` | |
| `primitivemobs:rocket_creeper` | `primitive_menagerie:rocket_creeper` | |
| `primitivemobs:festive_creeper` | `primitive_menagerie:festive_creeper` | |
| `primitivemobs:support_creeper` | `primitive_menagerie:support_creeper` | |
| `primitivemobs:skeleton_warrior` | `primitive_menagerie:skeleton_warrior` | |
| `primitivemobs:blazing_juggernaut` | `primitive_menagerie:blazing_juggernaut` | |
| `primitivemobs:lily_lurker` | `primitive_menagerie:lily_lurker` | |
| `primitivemobs:mother_spider` | `primitive_menagerie:mother_spider` | |
| `primitivemobs:baby_spider` | `primitive_menagerie:baby_spider` | |
| `primitivemobs:trollager` | `primitive_menagerie:trollager` | |
| `primitivemobs:lost_miner` | `primitive_menagerie:lost_miner` | |
| `primitivemobs:traveling_merchant` | `primitive_menagerie:traveling_merchant` | |
| `primitivemobs:dodo` | `primitive_menagerie:dodo` | |
| `primitivemobs:mimic` | `primitive_menagerie:mimic` | |
| `primitivemobs:sheepman` | `primitive_menagerie:sheepman` | |
| `primitivemobs:goblin` | `primitive_menagerie:goblin` | |
| `primitivemobs:harpy` | `primitive_menagerie:harpy` | |
| `primitivemobs:flame_spewer` | `primitive_menagerie:flame_spewer` | |
| `primitivemobs:void_eye` | `primitive_menagerie:void_eye` | |
| — | `primitive_menagerie:yeti` | new: the original shipped the class but never registered it |

## Projectiles / non-mob entities

| Old id (1.12) | New id (1.21) | Notes |
|---|---|---|
| `primitivemobs:primitive_tnt_primed` | `primitive_menagerie:primitive_tnt_primed` | |
| `primitivemobs:flame_spit` | `primitive_menagerie:flame_spit` | |
| `primitivemobs:thrown_block` | `primitive_menagerie:thrown_block` | |
| `primitivemobs:primitive_egg` | `primitive_menagerie:primitive_egg` | base thrown egg |
| `primitivemobs:spider_egg` | `primitive_menagerie:spider_egg` | |

## Items

| Old id (1.12) | New id (1.21) | Notes |
|---|---|---|
| `primitivemobs:camouflage_dye` | `primitive_menagerie:camouflage_dye` | |
| `primitivemobs:camouflage_helmet` | `primitive_menagerie:camouflage_helmet` | |
| `primitivemobs:camouflage_chestplate` | `primitive_menagerie:camouflage_chestplate` | |
| `primitivemobs:camouflage_leggings` | `primitive_menagerie:camouflage_leggings` | |
| `primitivemobs:camouflage_boots` | `primitive_menagerie:camouflage_boots` | |
| `primitivemobs:dodo` | `primitive_menagerie:dodo` | raw dodo meat |
| `primitivemobs:cooked_dodo` | `primitive_menagerie:cooked_dodo` | |
| `primitivemobs:dodo_egg` | `primitive_menagerie:dodo_egg` | thrown egg |
| `primitivemobs:spider_egg` | `primitive_menagerie:spider_egg` | thrown egg |
| `primitivemobs:mysteryegg1` | `primitive_menagerie:mysteryegg1` | festive creeper egg |
| `primitivemobs:mysteryegg2` | `primitive_menagerie:mysteryegg2` | support creeper egg |
| `primitivemobs:mysteryegg3` | `primitive_menagerie:mysteryegg3` | rocket creeper egg |
| `primitivemobs:mimic_orb` | `primitive_menagerie:mimic_orb` | |
| `primitivemobs:goblin_mace` | `primitive_menagerie:goblin_mace` | |
| `primitivemobs:wonder_sap` | `primitive_menagerie:wonder_sap` | |
| `primitivemobs:spider_eggshell` | `primitive_menagerie:spider_eggshell` | |

## Spawn eggs

Every creature gets a `primitive_menagerie:<name>_spawn_egg` item (the original also registered
spawn eggs for each creature). Non-mob entities have no spawn egg.

## Not ported

- DynamicTrees and JER integrations.
- The unused original item ids that only exist as commented-out code (`mysteryegg4`, the
  `SkullMinionOrb`/`WizardTome`/`TarBall`/`PinkSlimeBall`/`WebBall`/`SmallRock` test items, the
  spawner block items).
