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
  **P4a**: wired. Both models now target their entity type; `RenderBrainSlime`/`RenderTreasureSlime`
  hide `cube` and the gel layers (`LayerBrainSlimeGel`/`LayerTreasureSlimeGel`) hide the inner parts.
  `ModelEmpty` and `ModelEnchantedBook` were also retargeted to `HauntedTool`/`EnchantedBook` so they
  can be used by their `MobRenderer`s.
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

## Phase P4a (slimes, tameable base, haunted/bewitched creatures, throwables)

Ported: the tameable base `PrimitiveTameableMob`, the tameable slime base `TameableSlime`, the
`TreasureSlime`, `BrainSlime`, `HauntedTool`, `EnchantedBook` (`bewitched_tome`), the item entities
`PrimitiveThrowable` (`primitive_egg`), `ThrownBlock` (`thrown_block`), `FlameSpit` (`flame_spit`) and
`PrimitiveTNTPrimed` (`primitive_tnt_primed`), plus the renderers/layers (`RenderBrainSlime` +
`LayerBrainSlimeGel`, `RenderTreasureSlime` + `LayerTreasureSlimeGel` + `LayerHeldItemSlime`,
`RenderHauntedTool` + `LayerHeldItemHaunted`, `RenderEnchantedBook`, `RenderFlyingItem`,
`RenderThrownBlock`, `RenderPrimitiveTNTPrimed`, `LayerHeldItemCustom`) and the `en_us.json` names.
The Festive Creeper now really throws `PrimitiveTNTPrimed` (the P3 `TODO(P4)` is closed).

Notes and simplifications:

- **Tameable base** — `PrimitiveSpider` now extends `PrimitiveTameableMob` (the tamed/owner/follow
  state live in the Mobweave Core `mobweave_core:tameable` attachment, follow state `0 = sitting`).
  The original `getTeam()`/`isOnSameTeam()` overrides have no 1.21 equivalent and are dropped. The
  original `EntityPrimitiveTameableMob.canBeLeashedTo` is replaced by `canBeLeashed()`.
- **TameableSlime** — the original's bespoke move helper and nested goals are ported to a custom
  `MoveControl` plus goal classes; the sit state stops movement and keeps the look control on the
  target. The original `EntityAIFindEntityNearestPlayer` target is replaced by
  `NearestAttackableTargetGoal`.
- **TreasureSlime** — `TODO(verify)`: the coloured slime landing particles of the original are
  replaced by the vanilla slime particles; the colour tint is applied to the gel layer through vertex
  colours instead of the removed `GlStateManager.color`, and the rainbow camouflage-dye cycle and the
  enchantment glint overlay are not ported. The child-splitting on death is re-implemented with a
  custom `remove` override that preserves the owner/held item (the inherited `Slime` split is
  suppressed by dropping the size to 1 first). `getCanSpawnHere` (dark light level) is not ported
  because spawn placements are registered in P6.
- **BrainSlime** — the custom coloured slime particles and the particle network message are dropped
  (vanilla slime particles are used); the sand-only spawn check is not ported (P6). The creature
  attribute check for undead targets uses the `minecraft:undead` entity type tag (the 1.12
  `EnumCreatureAttribute` no longer exists). `Slime.setSize` clamps the size to a minimum of 1, so the
  original's size-0 spawn variant is not reachable.
- **HauntedTool / EnchantedBook** — the original `EntityAIMoveTowardsRestriction` goal is omitted.
  The Haunted Tool durability drop uses `hitByPlayer ? 1 : 0` as the looting modifier because the
  1.21 `dropCustomDeathLoot` hook does not expose the looting level (`TODO(verify)`); the
  `getCanSpawnHere` below-Y-40 check of the Enchanted Book is not ported (P6). The 1.12
  `EntityMob.getCreatureAttribute` (undead) has no 1.21 override.
- **PrimitiveThrowable** — the original selected the spawned entity by Java class through the old
  Forge registry; here the spawned `EntityType` is supplied directly. Taming of the spawned mob uses
  the Mobweave Core `ITameableEntity` interface (the original set up the capability explicitly).
- **ThrownBlock / PrimitiveTNTPrimed / FlameSpit** — `ThrownBlock` is a plain `Entity` that damages
  nearby entities on landing; `PrimitiveTNTPrimed` mirrors `PrimedTnt` with a configurable strength
  and the `Festive Creeper tnt harms terrain` config flag; `FlameSpit` extends `SmallFireball` and
  expires after 30 ticks. `FlameSpit` is rendered with the vanilla `ThrownItemRenderer` (the original
  `RenderFlyingItem` returned early for it).
- **RenderPrimitiveTNTPrimed** — the flickering white overlay of the original is not ported
  (`TODO(verify)`); the fuse scaling and the small-charge scaling are.
- **LayerHeldItemCustom** — only the Skeleton Warrior back-item branch is ported (the P3 TODO for the
  back item is closed); the Grove Sprite, Filch Lizard, Lily Lurker, Trollager and Lost Miner branches
  belong to later phases (`TODO`).
- **LayerHeldItemSlime / LayerHeldItemHaunted** — the 1.12 "full 3D item" branch no longer exists;
  items are split into the block and non-block cases only.
- **Loot tables** — `TreasureSlime`, `HauntedTool` and `EnchantedBook` return `null` from
  `getDefaultLootTable()` and drop their held/generated item through `dropCustomDeathLoot`, matching
  the original `getLootTable() == null` + `dropFewItems`.

## Not ported
- The Forge config GUI (`PrimitiveMobsFactoryGui`, `PrimitiveMobsGuiConfig`) is not ported; the
  config screen is generated by NeoForge from `PMConfig`'s `ModConfigSpec`.

## Phase P5 (passive creatures, villagers and network)

Ported: the passive creatures `Chameleon`, `Dodo`, `FilchLizard`, `GroveSprite`, `LostMiner`,
`Sheepman`, `TravelingMerchant` (`com.skd.primitivemenagerie.entity.passive`), the
`GroveSpriteTemptGoal` (`EntityAIGroveSpriteTempt`), the `PMVillagerProfessions` registry with the
original trade lists, the `PMNetwork` class with the four original messages ported to
`CustomPacketPayload` (`PrimitiveColorPayload`, `PrimitiveColorSapPayload`, `PrimitiveJumpingPayload`,
`TeleportEyePayload`), the renderers `RenderChameleon`, `RenderDodo`, `RenderFilchLizard`,
`RenderGroveSprite`, `RenderLostMiner`, `RenderSheepman`, `RenderTravelingMerchant`, the layers
`LayerGroveSpriteLeaves`, `LayerGroveSpriteStump`, `LayerSheepmanWool`, the `PMTint` vertex-tint
helper and the held-item branches for Filch Lizard / Grove Sprite / Lost Miner in
`LayerHeldItemCustom`. Entity types/eggs are in `PMEntities`, renderers/layers in `PMClientSetup`,
the textures were copied to `assets/primitive_menagerie/textures/entity/...` and the names were added
to `en_us.json`.

The P2 model TODOs are now wired: the seven models were retargeted to their entity types;
`ModelGroveSprite` reads `isBegging()` and exposes a stump/leaves variant switch; `ModelLostMiner`
reads `isSaved()`; `ModelSheepman` exposes a skin/wool variant switch and only shows the arms when
sheared; `ModelChameleon`/`ModelDodo` child scaling is in the renderers.

Notes and simplifications:

- **Chameleon** — the original blocked itself from the `CREATURE` spawn cap (`isCreatureType`);
  in 1.21 the entity keeps the `CREATURE` category. The skin color is applied by the renderer as a
  per-vertex tint (`PMTint`) instead of `GlStateManager.color`; the client color matching uses the
  vanilla block colors and the Mobweave Core `ColorUtil` (`TODO(verify)` for the exact tints).
- **Dodo** — extends the vanilla `Chicken`, matching the original; the breeding-food check follows
  the original held-item logic. The dodo egg drop uses the P1 placeholder item (`TODO(P6)`: the
  throwing dodo-egg item). `getCanSpawnHere` (mycelium) is P6.
- **FilchLizard** — the original changed its collision height while holding an item; in 1.21 the
  entity keeps a fixed size (`TODO(verify)`). The steal/spawn loot uses the Mobweave Core
  `EntityUtil` helpers. `getCanSpawnHere` (sand) is P6.
- **GroveSprite** — the DynamicTrees integration is not ported, so only the Mobweave Core tree search
  is used. The wonder-sap color is synchronised through `PrimitiveColorSapPayload`, but the sap item
  is still a P1 placeholder, so the color is only stored on the entity (`TODO(P6)`: apply it to the
  real `ItemGroveSpriteSap`). The leaves' render-time pivot rotation of the original is replaced by a
  static per-leaf spin in the model. The held sapling item is derived from the found leaves block name
  (`*_leaves` → `*_sapling`).
- **LostMiner / Sheepman / TravelingMerchant** — these extend the 1.21 `Villager`, as the originals
  extended `EntityVillager`. Their profession/trades are assigned through `VillagerData` and the
  global `VillagerTrades` map (see below). The 1.21 villager is brain-driven, so the original custom
  goal tasks do not apply; the Lost Miner follow/save logic is ported through `aiStep`/`mobInteract`
  and the Sheepman tempt goal is added to the goal selector (ticked alongside the brain). The
  `TRAVELER_VISIT` config (removing the move-indoors tasks) has no brain equivalent (`TODO(verify)`).
  `getCanSpawnHere`/light checks are P6.
- **Villager professions** — six professions are registered (`miner`, `merchant`, `fakemerchant`,
  `sheepman_scavenger`, `sheepman_alchemist`, `sheepman_thief`). The original Forge 1.12 professions
  had **no point of interest**, so `PMVillagerProfessions` registers with `PoiType.NONE` predicates:
  vanilla villagers never claim them and the ported entities assign the profession directly. No
  `PoiType` is therefore registered. The trades are added through the NeoForge `VillagerTradesEvent`;
  the original ranged prices (`PriceInfo`) became a single representative count per listing, and the
  Traveling Merchant's randomised stock is replaced by the standard villager trade map
  (`TODO(verify)`). `FAKE_MERCHANT` is registered for the Mimic disguise of later phases.
- **Network** — `PMNetwork` registers the four payloads on the mod bus. `PrimitiveColorPayload`
  (camouflage armor, P6) is a no-op until the real item exists (`TODO(P6)`); `PrimitiveColorSapPayload`
  stores the sap color on the Grove Sprite; `PrimitiveJumpingPayload` sets the jump flag on a ridden
  Baby Spider (client key-input wiring is not ported, `TODO(verify)`); `TeleportEyePayload` sets the
  Void Eye teleport flag. The Void Eye keeps its server-side teleport roll; the client message is
  ported but not required by that path.
- **Textures** — only the textures referenced by the ported code were copied (chameleon, dodo,
  filch lizard, the five grove-sprite textures and the three villager textures). Files such as
  `beaver/`, `squirrel/`, `rareanimals/sheeper*` and `villager/wizard*` are for content that is not
  ported in P5.

## Phase P4b (complex hostile creatures)

Ported: `BlazingJuggernaut`, `LilyLurker`, `Mimic`, `Yeti`, `VoidEye`, `FlameSpewer`, `Trollager`,
the `IAnimatedMob` interface and the `TrollagerAttacksGoal` (`EntityAITrollagerAttacks`), the
renderers/layers (`RenderBlazingJuggernaut`, `RenderLilyLurker`, `RenderMimic` + `LayerMimicMouth`,
`RenderYeti`, `RenderVoidEye` + `LayerVoidEyeSeen`, `RenderFlameSpewer` + `LayerLavaSkin` +
`LayerFlameSpewerEyes`, `RenderTrollager`), the entity types/eggs in `PMEntities` and the
`en_us.json` names. The P2 model TODOs for these creatures are now wired (`ModelTrollager` reads the
animation state/`animVar`/`isStone`, `ModelFlameSpewer` reads `isInDanger`/attack signal and scales
the body/mouth, `ModelVoidEye` is retargeted; all seven models now target their entity type) and the
`LayerHeldItemCustom` Lily Lurker and Trollager branches are added.

Notes and simplifications:

- **Common** — `isCreatureType(MONSTER) == false` cannot be expressed per instance in 1.21 (as in
  earlier phases); the entities keep the `MONSTER` spawn category. The `getCanSpawnHere`/light-level
  overrides of the originals are P6 spawn-placement concerns.
- **BlazingJuggernaut** — `canTriggerWalking` (crop trampling) and the custom render-brightness
  overrides have no direct 1.21 equivalent and are not ported. The charge attack is a `Goal` using
  the move control in place of the 1.12 move helper.
- **LilyLurker** — the collision-box overrides that change how a camouflaged lurker can be pushed are
  not expressible in 1.21 and are omitted. `canBreatheUnderwater` is now a final vanilla check driven
  by the `minecraft:can_breathe_under_water` entity-type tag, so the mob is added to that tag via
  `data/minecraft/tags/entity_type/can_breathe_under_water.json`. The camouflage size/eye height uses
  `getDefaultDimensions`.
- **Mimic** — the original smoke-particle network message is replaced by vanilla smoke particles.
- **Yeti** — the original mod never registers this mob (only the class, model and renderer exist) and
  most of its behaviour (feeding, hunger, owner following) is commented out in the 1.12.2 source, so
  only the active setup is ported. The spawn-egg colours are new (the original has none).
- **VoidEye** — the original sent a network message from the client to request the teleport; here the
  teleport roll is done on the hurt call on the server. The gaze beam of the original is an additive,
  rotating multi-segment beam; the port draws a simpler two-plane translucent beam
  (`TODO(verify)`), and the custom `shouldRender` culling override is not ported. The dimension
  (dragon) and light-level spawn checks are P6.
- **FlameSpewer** — the empty `IRangedAttackMob` methods of the original are not ported (nothing to
  port in 1.21). The lava-skin layer of the original built a dynamic texture from the lava sprite and
  scrolled its UVs; the port binds the vanilla `lava_flow` texture directly and omits the UV scroll
  (`TODO(verify)`). The bullet uses the ported `FlameSpit`.
- **Trollager** — the boat-collision, collision-box and `isNotColliding` overrides are not
  expressible in 1.21 and are omitted; the explosion particle network message is replaced by the
  vanilla explosion effect. The `dropCustomDeathLoot` hook does not expose the looting level, so the
  looting modifier on the obsidian/emerald/golden-apple drops is not applied (`TODO(verify)`).
- **LayerLavaSkin / LayerFlameSpewerEyes** — both fade with `(10 - attackTime) / 10` like the
  original, applied through vertex tint alpha/translucency instead of `GlStateManager.color`; the
  lava model uses a dedicated `ModelFlameSpewer::createLavaLayer` layer definition with the original
  `32x512` lava UVs.
- **SupportCreeper** — the P3 `TODO(verify)` is closed: a nearby Trollager is now flagged
  `isBeingSupported` and vanilla creepers are buffed. Powering a vanilla creeper still has no public
  API in 1.21, so the private `DATA_IS_POWERED` synched data is set reflectively
  (`TODO(verify)`); prefer a public API if one is added.
- **WorldGenMimic** — `TODO(P6)`: the original cave chest generation cannot be ported faithfully yet
  because it relies on a `TileEntityChest` data flag plus the chest-open transform event, both of
  which belong to P6 (world generation and events). No feature/placed-feature is registered here.
  **P6**: closed. `MimicChestFeature` now places the cave chests and `PMEvents` handles the chest
  interaction (see the P6 section below).

## Not ported
- The Forge config GUI (`PrimitiveMobsFactoryGui`, `PrimitiveMobsGuiConfig`) is not ported; the
  config screen is generated by NeoForge from `PMConfig`'s `ModConfigSpec`.

## Phase P6 (items, recipes, events, spawns and world generation)

Ported: the seven item classes (`ItemPrimitive`, `ItemPrimitiveFood`, `ItemPrimitiveEgg`,
`ItemGoblinMace`, `ItemGroveSpriteSap`, `ItemCamouflageArmor`, `ItemCamouflageDye`) plus the
`ItemMimicOrb` and `ItemSpiderEggshell` helpers in `com.skd.primitivemenagerie.item`, the real
`PMItems` registration (all original ids kept), the `camouflage` `ArmorMaterial` and the item data
components (`PMDataComponents`), the item models/textures/lang, the 9 recipes (7 shapeless + the two
`CustomRecipe`s `CamouflageToggleRecipe`/`WonderSapRecipe` with `SimpleCraftingRecipeSerializer`
registered through `PMRecipes`), the spawn entries (`PMSpawnRegistry`), the tameable entries
(`PMTameableEntries`), the game events (`PMEvents` + the client-only `PMCamouflageHandler`/
`PMClientEvents`), the mimic chest world generation (`MimicChestFeature` + configured/placed feature
+ `neoforge:add_features` biome modifier) and the client item colors (`PMItemColors`). The P1
placeholder items are gone; every `TODO(P6)` in the source is closed.

Notes and simplifications:

- **Items** — the camouflage armor color, the "camouflage disabled" flag and the sampled block are
  stored in the `camouflage_color`/`camouflage_disabled`/`camouflage_block` data components (the 1.12
  raw NBT is gone); the color is mirrored into the vanilla `DYED_COLOR` component so the dyeable
  leather armor layer tints the worn armor. The wonder sap stores its log id and tint in the
  `sap_log`/`sap_color` components. The goblin mace's 1.12 `ToolMaterial` is a `Tier`; the goblin
  equips it in `finalizeSpawn`.
- **Camouflage armor tinting** — the original interpolated the color over several ticks and recomputed
  it in `onArmorTick`/`LivingUpdateEvent`; here the client-only `PMCamouflageHandler` samples the
  block the entity stands on every 5 ticks, tints the pieces through `PMItemColors` and synchronises
  the color with the server via the ported `PrimitiveColorPayload` (its P5 `TODO(P6)` no-op handler
  now applies the color). The interpolation is dropped.
- **Dodo egg / mystery eggs** — the thrown eggs now spawn their creature as a baby (`AgeableMob` /
  `PrimitiveCreeper.setGrowingAge(-24000)`), matching the original. The spider egg drops the
  `spider_eggshell` item on impact (P3 `TODO(P6)` closed).
- **Recipes** — the two special recipes became 1.21 `CustomRecipe`s; the 1.12 `_factories.json` and
  the `ShapelessOreRecipe` subclasses are gone. The four toggle recipes are generic (each matches a
  single camouflage armor piece and toggles it), so any of them works for any piece. The wonder sap
  recipe keeps the DynamicTrees branch out (as everywhere else).
- **Spawns** — `PMSpawnRegistry` registers every original entry through the Mobweave Core
  `MWSpawnAPI` on `ServerAboutToStartEvent` (before Mobweave builds its list on `ServerStartedEvent`),
  gated by the per-mob `PMConfig` flags. Biome types map to biome tags: `FOREST`/`JUNGLE`/`BEACH` use
  the vanilla `minecraft:is_*` tags, while `SANDY`/`SWAMP` use the NeoForge common `c:is_sandy`/
  `c:is_swamp` tags because vanilla has no matching tags. Structures use full ids
  (`minecraft:stronghold`, `minecraft:mineshaft`), `minecraft:grass` became `minecraft:grass_block`
  and the legacy integer dimension ids (0/-1) are kept (Mobweave maps them to overworld/nether).
  Mobweave Core already provides the lava/water placements and the position checks, so no
  `RegisterSpawnPlacementsEvent` entry was needed; no `TODO(mobweave)` remains.
- **Tameables** — `PMTameableEntries` feeds the original entries into the Mobweave Core
  `MMTameableEntries` map (melon slice tames, spider eye/fermented spider eye/rotten flesh/gunpowder
  heal). Mobweave does not call `registerTameables()` itself, so the mod calls it from the
  constructor.
- **Events** — the mimic orb/chest handling replaces the 1.12 chest tile-entity `Mimic` flag with
  marker loot tables (`mimic_unopened`/`mimic_guaranteed`); opening such a chest either spawns a
  mimic, spawns a trap (skeleton warrior/bats/haunted tool + `mimic_trap` loot) or becomes a
  `mimic_treasure` chest. The smoke/cloud particle network messages became vanilla particles. The
  goblin mace armor damage, the sheepman immunity, the full-camouflage target clearing, the rocket
  creeper fall immunity and the harpy dismount prevention are ported.
- **Events not ported** — the 1.12 `EntityAIAvoidEntity` tasks added to villagers are dropped (1.21
  villagers are brain driven); the sheepman villager-sound swap is unnecessary because the ported
  `Sheepman` already overrides its sounds; `EntityUtil.removeWhenDisabled` is re-implemented locally
  against the `PMConfig` flags. The `PlayerLoggedOutEvent` passenger drop is simplified to
  `removeVehicle`/`ejectPassengers` (no explicit `SPacketSetPassengers`).
- **World generation** — `MimicChestFeature` replaces `WorldGenMimic`: it keeps the original y 5..40
  cave-floor scan and the config-driven rarity roll (`getMimicRarity`, `getMimicGeneratesInCaves`,
  `getEnableMimic`), and places a chest carrying the `mimic_unopened` marker instead of the 1.12 tile
  flag. It is registered as a configured + placed feature and added to `#minecraft:is_overworld` via
  a `neoforge:add_features` biome modifier in the `underground_decoration` step. `PrimitiveMobsMapGen`
  (the 1.12 `IWorldGenerator`) is replaced by this data-driven mechanism.
- **Client** — `PMItemColors` tints the camouflage armor (layer 0) and the wonder sap from their
  components; `PMClientEvents` ports the jump key input (sends `PrimitiveJumpingPayload` while riding
  a Baby Spider), closing the P5 `TODO(verify)`.

## Not ported
- The Forge config GUI (`PrimitiveMobsFactoryGui`, `PrimitiveMobsGuiConfig`) is not ported; the
  config screen is generated by NeoForge from `PMConfig`'s `ModConfigSpec`.
- The DynamicTrees and JER integrations (as in every earlier phase).
