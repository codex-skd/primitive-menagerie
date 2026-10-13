# Port notes — Primitive Mobs 1.2.3 → Primitive Menagerie (NeoForge 1.21.1)

Notes about conversions that could not be done fully faithfully. Entries marked `TODO(verify)`
need a functional check once the corresponding behaviour is implemented in a later phase.

## Phase P1 (framework)

### Loot tables (1.12 → 1.21 format)

All JSON files under `data/primitive_menagerie/loot_table/` were mechanically converted:

- `entries[].type` `item` → `minecraft:item`.
- `rolls`, `functions.count` ranges → `{"type": "minecraft:uniform", "min": .., "max": ..}`;
  single values stay as plain numbers.
- `function: looting_enchant` → `minecraft:enchanted_count_increase` with
  `"enchantment": "minecraft:looting"`.
- `condition: killed_by_player` → `minecraft:killed_by_player`.
- `condition: entity_properties` (`properties.on_fire`) →
  `minecraft:entity_properties` with `predicate.flags.is_on_fire`.
- Renamed items: `minecraft:waterlily` → `minecraft:lily_pad`,
  `minecraft:firework_charge` → `minecraft:firework_star`,
  `minecraft:record_wait` → `minecraft:music_disc_wait`,
  `primitivemobs:*` → `primitive_menagerie:*`.

The loot tables reference seven mod items that are not implemented yet. To keep the loot data
parsing cleanly (a missing item id makes the whole table fail to load in 1.21), they are registered
in P1 as plain placeholder `Item`s in `PMItems`: `camouflage_dye`, `dodo`, `mimic_orb`,
`spider_egg`, `mysteryegg1/2/3`.
`TODO(verify)` / `TODO(P6)` — Replace these placeholders with the real item classes of the original
`PrimitiveMobsItems` (dye, food, eggs, orb, mace, sap, armour) in P6. If any of these item ids
change during P6, the loot tables must be updated to match.

- `TODO(verify)` — The old `set_data` function (`minecraft:dye` with data 4/5) was folded into the
  item: data 4 → `minecraft:lapis_lazuli`, data 5 → `minecraft:purple_dye`. The `set_data` function
  no longer exists in 1.21.
- `TODO(verify)` — Pools used negative `count` minimums (e.g. `{min: -1, max: 1}` or the egg pools
  `{min: -49, max: 1}`) as a 1.12 trick to give a chance of no drop. In 1.21 a non-positive count
  makes the stack empty, so the intent (a chance of no drop) still holds, but the exact drop
  probabilities may differ slightly. Re-check against the original behaviour.
- `TODO(verify)` — `entities/special/*.json` (filch lizard steal/spawn, haunted tool, treasure
  slime) are used from code as weighted item pools, not as entity/chest loot tables. They were left
  without a top-level `"type"`, so they use the permissive default parameter set. If the calling
  code uses a stricter `LootContextParamSet`, add the matching `"type"` field.
- `TODO(verify)` — `empty.json` was copied as `{}` (same as the original 1.12 file); a loot table
  with no `pools` is valid in 1.21 (pools default to an empty list).

### Resources

- `sounds.json` namespace rewritten `primitivemobs:` → `primitive_menagerie:`; the `.ogg` files are
  unchanged. The file still contains the `entity.grovesprite.friend`, `dig.squish` and
  `step.squish` entries of the original that have no corresponding `SoundEvent` constant in
  `PMSoundEvents` (they were not registered by the original either).

## Phase P2 (entity models)

The 21 original `ModelBase`/`ModelRenderer` classes were ported to `HierarchicalModel` (an
`EntityModel` subclass) with static `createBodyLayer()` methods in
`com.skd.primitivemenagerie.client.models`. Part names and class names match the originals. Layer
locations live in `PMModelLayers` and are registered client-only from `PMClientSetup` (wired from the
mod constructor behind `FMLEnvironment.dist == Dist.CLIENT`). Renderers and render layers are **not**
part of P2.

Models that had a constructor argument in the original (slimes, flame spewer, void eye, sheepman,
grove sprite, lost miner) were collapsed into a single layer holding every box of every variant; the
future renderers must toggle visibility, exactly like the vanilla `SlimeRenderer`. The list of
`TODO(entity-phase)` / `TODO(verify)` items below covers every place where the original read
entity-specific state that does not exist yet (a safe default is used so the model still renders):

- `ModelPrimitiveCreeper` — the original render method scaled the head/body for children; in 1.21
  that is renderer-side. `TODO(entity-phase)`: apply the baby scale in the renderer.
  **P3**: wired. The model now targets `PrimitiveCreeper`; the renderer (`RenderPrimitiveCreeper`)
  scales babies uniformly by `0.5` (the original scaled head and body separately — a simplification
  that only matters for the thrown-egg spawns added in P6).
- `ModelBrainSlime` / `ModelTreasureSlime` — the original chose the outer gel shell vs. the inner
  body with eyes by constructor argument. `TODO(entity-phase)`: the renderer (and the gel overlay
  layer) must hide `cube` or `inner_cube`/`right_eye`/`left_eye`/`mouth` accordingly.
- `ModelVoidEye` — original `ModelVoidEye(false)` hid `body1`/`body2` for the "seen" overlay.
  `TODO(entity-phase)`: the renderer must hide those parts when only the eye is drawn.
- `ModelFlameSpewer` — original read `EntityFlameSpewer.isInDanger()` and scaled the body by the
  entity attack signal. `TODO(entity-phase)`: restore the danger tentacle animation and the body
  scaling. The model uses the regular (non-lava) texture offsets and `64x64`; the lava skin is a
  separate render layer with different UVs and is not covered by this layer.
- `ModelHarpy` — original read `EntityHarpy.isFlying()`. `TODO(entity-phase)`: restore the flying
  pose (adds `2π/9` to both legs) and the sitting state.
  **P3**: wired. The model now targets `Harpy` and reads `isFlying()`; the sitting state is not used
  because the ported entity has no sit state.
- `ModelYeti` — `EntityYeti.getAttackTimer()` is disabled in the original source, so `i` is always
  zero and the idle branch is used. The original used the render scale factor (`0.0625F`) in the arm
  maths; that constant is kept. `TODO(entity-phase)`: restore the attack timer branch if re-enabled.
- `ModelGoblin` — the original extended the vanilla biped model; the inherited head yaw/pitch and
  arm/leg walk are reproduced by hand. The default biped headwear overlay is kept as a part.
  **P3**: the model now targets `Goblin` so it can be used by the goblin renderer.
- `ModelTrollager` — original read `EntityTrollager.getAnimationState()`, `getAnimVar()` and
  `isStone()`. `TODO(entity-phase)`: restore the throwing/crushing/melee state machine; it currently
  runs the idle branch with the entity state defaulted.
- `ModelSheepman` — original built a wool variant and a sheared variant with a constructor argument
  and used the render scale factor for the head pitch (`Head.rotateAngleY`/`X` were swapped in the
  original; kept verbatim). `TODO(entity-phase)`: the renderer and the wool overlay layer must
  toggle the `*Wool`/`FoldedArms` parts and only show the arms when the entity is sheared.
- `ModelGroveSprite` — original built a stump variant or a leaves variant with a constructor
  argument, and read `EntityGroveSprite.isBegging()`. `TODO(entity-phase)`: toggle the
  headwear/stem vs. `leaf0..2` parts per layer and read `isBegging()`. The leaves' render-time pivot
  rotation done by the original with GL calls cannot be represented in a static layer and is left to
  the renderer.
- `ModelLostMiner` — original read `EntityLostMiner.isSaved()` to switch between the folded villager
  arms and the custom mining arms. `TODO(entity-phase)`: restore that switch.
- `ModelChameleon` — the original defined `tongue1`/`tongue2` but never rendered them; they are
  present in the layer and hidden in `setupAnim`. The child scaling is renderer-side.
  `TODO(entity-phase)`: apply the baby scale in the renderer.
- `ModelDodo` — the child scaling (and the separate neck/beak transform for children) is
  renderer-side. `TODO(entity-phase)`: apply it in the renderer.
- `ModelFilchLizard` / `ModelLilyLurker` — the "holding an item" state is read through the generic
  `LivingEntity.getMainHandItem()` (no entity class needed).
- `ModelBlazingJuggernaut` — the original `func_78104_a()` (returns 8) was only used by the
  renderer for the stick count/scale; not needed by the model.

## Phase P3 (simple combat creatures)

Ported: creepers (base `PrimitiveCreeper`, `FestiveCreeper`, `RocketCreeper`, `SupportCreeper`),
spiders (base `PrimitiveSpider`, `BabySpider`, `MotherSpider`), `SkeletonWarrior`, `Goblin`,
`Harpy`, the `SpiderEgg` projectile, plus the renderers/layers
(`RenderPrimitiveCreeper` + `LayerPrimitiveCreeperCharge`, `RenderBabySpider` + `LayerBabySpiderEyes`,
`RenderMotherSpider` + `LayerPrimitiveSpiderEyes`, `RenderSkeletonWarrior`, `RenderGoblin`,
`RenderHarpy`) and the `en_us.json` names. Entity types are declared in `core/PMEntities` and
registered through the `PMRegistries` helpers; attributes are supplied lazily (as a
`Supplier<AttributeSupplier.Builder>`) because evaluating a NeoForge attribute builder during registry
filling fails on unbound attributes such as `neoforge:swim_speed`.

Notes and simplifications:

- **Spawn placements** — none of these creatures overrode the 1.12 spawn placement logic
  (`getCanSpawnHere`/`isValidLightLevel`), so no `RegisterSpawnPlacementsEvent` entries were added.
  The actual biome spawn entries are registered in P6.
- **Config enable flags** — the entity types are always registered so existing worlds do not break;
  the per-mob enable flags only gate the spawn entries registered in P6.
- `isCreatureType(MONSTER) == false` (the original excluded these mobs from the monster spawn cap)
  cannot be expressed per instance in 1.21; the entities keep the `MONSTER` spawn category.
- **Tameable base** — `PrimitiveSpider` implements the Mobweave Core `ITameableEntity` interface by
  delegating to the `mobweave_core:tameable` attachment, so the ported owner goals
  (`CustomFollowOwnerGoal`, `CustomOwnerHurtByTargetGoal`, `CustomOwnerHurtTargetGoal`) work. The
  original `EntityPrimitiveTameableMob` sit flag is replaced by follow state `0 = sitting`.
- **FestiveCreeper** — the TNT throw goal is ported but does not spawn anything yet:
  `TODO(P4)` spawn the ported `EntityPrimitiveTNTPrimed`. The creeper cannot explode (its swell is
  permanently disabled), matching the original.
- **RocketCreeper** — the original shadowed the vanilla fuse with its own `timeSinceIgnited`; here the
  vanilla swell state is reused (`setSwellDir(-1)` to cancel). `mobGriefing` is respected through
  `ExplosionInteraction.MOB`.
- **SupportCreeper** — `TODO(verify)`: powering nearby vanilla creepers (no public API in 1.21) and
  flagging a nearby Trollager (not ported yet) are omitted; the effect buffing is ported.
- **MotherSpider** — the original shared its navigation path with the riding baby; the ported version
  only syncs the movement speed. The original also discarded itself when the spider family was
  disabled, which is unnecessary because the spawn entries are gated in P6.
- **BabySpider** — `isMovementBlocked` (sitting) is not ported; the custom `travel` steering is
  replaced by the vanilla `getControllingPassenger`/`getRiddenInput`/`getRiddenSpeed` hooks. The eye
  colour is stored but `LayerBabySpiderEyes` renders the emissive texture untinted (the original
  tinted it per dye colour) — `TODO(verify)`.
- **SkeletonWarrior** — `reassessWeaponGoal` is a no-op because the ported
  `EntityAISwitchBetweenRangedAndMelee` and `EntityAISwitchWeapons` goals handle the weapon switching.
  The original back-item render layer (`LayerHeldItemCustom`) is not ported yet (`TODO`); `getBackItem`
  is kept for it.
- **Harpy** — the bespoke `EntityHarpyFlyHelper` is replaced by the `FlyingMoveControl` inherited from
  `MMFlyingMob`; the original's entity-collision override is not ported.
- **SpiderEgg** — `TODO(P6)`: drop the ported spider eggshell item (not registered yet) and let the
  throwing item set the spawn chance (default `8` is used meanwhile).
- **Loot tables** — festive/rocket/support creeper, mother spider, goblin and harpy use the ported
  `PMLootTables` ids through `getDefaultLootTable()`; skeleton warrior and the spiders keep the
  vanilla skeleton/spider loot, as in the original.

## Not ported
- The Forge config GUI (`PrimitiveMobsFactoryGui`, `PrimitiveMobsGuiConfig`) is not ported; the
  config screen is generated by NeoForge from `PMConfig`'s `ModConfigSpec`.
