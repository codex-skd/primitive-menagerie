# Graph Report - .  (2026-10-15)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 2354 nodes · 5257 edges · 142 communities (85 shown, 57 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 81 edges (avg confidence: 0.8)
- Token cost: 7,485 input · 2,968 output

## Graph Freshness
- Built from commit: `968784fc`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- VoidEye Model
- Treasure Slime Model
- Baby Spider Model
- Chest Block Events
- Lost Miner Model
- Goblin Model
- Mod Configuration
- PM Entities
- Filch Lizard Model
- Empty Model
- Creeper Model
- Lily Lurker Model
- Tameable Slime
- Harpy Model
- Mimic Model
- Dodo Model
- Mother Spider Render
- Enchanted Book Model
- Custom Packet Payload
- Chameleon Model
- Traveling Merchant Model
- Mod Registration
- Trollager Entity
- Camouflage Armor
- Ocelot Entity
- Spider Attack AI
- Blazing Juggernaut
- Grove Sprite
- Primitive TNT
- Sheepman Entity
- Grove Sprite Model
- Thrown Block
- Spider Chest Texture
- Yeti Entity
- Brain Slime AI
- Villager Professions
- Grove Sprite AI
- Lava Mob
- Camouflage Items
- Brain Slime Model
- Sheepman Model
- Entity Interactions
- Trollager Entity
- Flame Spewer Model
- Trollager Attacks
- Blazing Juggernaut Render
- Yeti Model
- Brain Slime Goals
- Blazing Juggernaut Attributes
- Camouflage Recipe
- Block Management
- Sheep Management
- Arrow Mechanics
- Vertex Rendering
- Recipe Management
- Flame Spewer AI
- Goblin Mace Item
- Grove Sprite Sap Item
- Skeleton Renderer
- Melee Attack AI
- Sapling Management
- Brain Slime Entity
- Flame Spew Attack AI
- Flying Item Renderer
- Trollager Model
- Grove Sprite Renderer
- TNT Primed Renderer
- Flame Spewer Eyes Layer
- Skeleton Warrior Entity
- Item Color Management
- Grove Sprite Stump Layer
- Thrown Block Renderer
- Data Component Registration
- Client Rendering Registration
- Lava Skin Layer
- Brain Slime Renderer
- Animated Mob Interface
- Spider Eggshell Item
- Sheepman Renderer
- Trollager Renderer
- Recipe Registration
- Entity Utility Methods
- Model Layer Registration
- Resource Location Management
- Gradle Wrapper Script
- Primitive Menagerie Mod
- Festive Creeper Texture
- Filch Lizard Texture
- Flame Spewer Texture
- Goblin Texture
- Grove Sprite Cinderleaf Texture
- Grove Sprite Grovebase Texture
- Grove Sprite Grove Cinder Texture
- Grove Sprite Grove Leaf Texture
- Grove Sprite Grove Stump Texture
- Icon
- Harpy Texture
- Spider Eyes Texture
- Blazing Juggernaut Texture
- Brain Slime Texture
- Chameleon Texture
- Creeper Chest Texture
- Enchanted Book Texture
- Lilylurker Texture
- Waterlily Texture
- Mimic Chest Texture
- Haunted Tool Texture
- Dodo Texture
- Rocket Creeper Texture
- Skeleton Warrior Texture
- Spider Egg Texture
- Baby Spider Texture
- Mother Spider Texture
- Spider Texture
- Support Creeper Texture
- Treasure Slime Texture
- Troll Texture
- Stoned Troll Texture
- Lost Miner Texture
- Sheepman Texture
- Traveling Merchant Texture
- Void Eye Beam Texture
- Voideye Beam Weak Texture
- Void Eye Texture
- Voideye Seen Texture
- Worm Texture
- Yeti Texture
- Camouflage Dye Texture
- Cooked Dodo Texture
- Dodo Texture
- Dodo Egg Texture
- Goblin Mace Texture
- Mimic Orb Texture
- Mystery Egg 1 Texture
- Mystery Egg 2 Texture
- Mystery Egg 3 Texture
- Spider Egg Texture
- Spider Egg Shell Texture
- Wonder Sap Texture

## God Nodes (most connected - your core abstractions)
1. `GroveSprite` - 73 edges
2. `BrainSlime` - 54 edges
3. `Trollager` - 53 edges
4. `PMConfig` - 47 edges
5. `FlameSpewer` - 46 edges
6. `Sheepman` - 45 edges
7. `VoidEye` - 43 edges
8. `TreasureSlime` - 39 edges
9. `BabySpider` - 38 edges
10. `PMEntities` - 36 edges

## Surprising Connections (you probably didn't know these)
- `ModelBlazingJuggernaut` --references--> `BlazingJuggernaut`  [EXTRACTED]
  src/main/java/com/skd/primitivemenagerie/client/models/ModelBlazingJuggernaut.java → src/main/java/com/skd/primitivemenagerie/entity/monster/BlazingJuggernaut.java
- `ModelBrainSlime` --references--> `BrainSlime`  [EXTRACTED]
  src/main/java/com/skd/primitivemenagerie/client/models/ModelBrainSlime.java → src/main/java/com/skd/primitivemenagerie/entity/monster/BrainSlime.java
- `RenderBrainSlime` --references--> `ModelBrainSlime`  [EXTRACTED]
  src/main/java/com/skd/primitivemenagerie/client/renderer/RenderBrainSlime.java → src/main/java/com/skd/primitivemenagerie/client/models/ModelBrainSlime.java
- `ModelFlameSpewer` --references--> `FlameSpewer`  [EXTRACTED]
  src/main/java/com/skd/primitivemenagerie/client/models/ModelFlameSpewer.java → src/main/java/com/skd/primitivemenagerie/entity/monster/FlameSpewer.java
- `LayerFlameSpewerEyes` --references--> `ModelFlameSpewer`  [EXTRACTED]
  src/main/java/com/skd/primitivemenagerie/client/renderer/layer/LayerFlameSpewerEyes.java → src/main/java/com/skd/primitivemenagerie/client/models/ModelFlameSpewer.java

## Import Cycles
- None detected.

## Communities (142 total, 57 thin omitted)

### Community 0 - "VoidEye Model"
Cohesion: 0.06
Nodes (36): Matrix4f, MMFlyingCreature, LayerDefinition, ModelPart, Override, ModelVoidEye, EntityModelSet, MultiBufferSource (+28 more)

### Community 1 - "Treasure Slime Model"
Cohesion: 0.05
Nodes (41): RemovalReason, LayerDefinition, ModelPart, Override, ModelTreasureSlime, MultiBufferSource, Override, PoseStack (+33 more)

### Community 2 - "Baby Spider Model"
Cohesion: 0.05
Nodes (35): EyesLayer, Items, RandomSource, Override, RenderLayerParent, RenderType, SpiderModel, LayerBabySpiderEyes (+27 more)

### Community 3 - "Chest Block Events"
Cohesion: 0.05
Nodes (34): ChestBlockEntity, Direction, EntityJoinLevelEvent, EntityMountEvent, LeftClickBlock, LivingChangeTargetEvent, LivingFallEvent, LivingIncomingDamageEvent (+26 more)

### Community 4 - "Lost Miner Model"
Cohesion: 0.06
Nodes (34): LayerDefinition, ModelPart, Override, ModelLostMiner, ItemStack, LivingEntity, MultiBufferSource, Override (+26 more)

### Community 5 - "Goblin Model"
Cohesion: 0.06
Nodes (32): ITameableEntity, Monster, LayerDefinition, ModelPart, Override, ModelGoblin, Context, Override (+24 more)

### Community 6 - "Mod Configuration"
Cohesion: 0.06
Nodes (21): BooleanValue, ConfigValue, Feature, FeaturePlaceContext, IntValue, ModConfigSpec, MWConfigSpawnEntry, NoneFeatureConfiguration (+13 more)

### Community 7 - "PM Entities"
Cohesion: 0.06
Nodes (38): InteractionResultHolder, SmallFireball, DeferredHolder, EntityType, PMEntities, FlameSpit, EntityType, Level (+30 more)

### Community 8 - "Filch Lizard Model"
Cohesion: 0.06
Nodes (33): AvoidEntityGoal, PathfinderMob, LayerDefinition, ModelPart, Override, ModelFilchLizard, Context, Override (+25 more)

### Community 9 - "Empty Model"
Cohesion: 0.06
Nodes (32): LayerDefinition, ModelPart, Override, ModelEmpty, MultiBufferSource, Override, PoseStack, RenderLayerParent (+24 more)

### Community 10 - "Creeper Model"
Cohesion: 0.08
Nodes (25): Creeper, EnergySwirlLayer, EntityModel, LayerDefinition, ModelPart, Override, ModelPrimitiveCreeper, EntityModelSet (+17 more)

### Community 11 - "Lily Lurker Model"
Cohesion: 0.07
Nodes (29): IMultiMobWater, MMSwimmingCreature, LayerDefinition, ModelPart, Override, ModelLilyLurker, Context, Override (+21 more)

### Community 12 - "Tameable Slime"
Cohesion: 0.08
Nodes (15): SimpleParticleType, Slime, EntityType, ItemStack, Level, LivingEntity, Nullable, Override (+7 more)

### Community 13 - "Harpy Model"
Cohesion: 0.07
Nodes (28): MMFlyingMob, LayerDefinition, ModelPart, Override, ModelHarpy, State, FLYING, SITTING (+20 more)

### Community 14 - "Mimic Model"
Cohesion: 0.08
Nodes (28): LayerDefinition, ModelPart, Override, ModelMimic, EntityModelSet, MultiBufferSource, Override, PoseStack (+20 more)

### Community 15 - "Dodo Model"
Cohesion: 0.07
Nodes (25): Chicken, LayerDefinition, ModelPart, Override, ModelDodo, Context, Override, PoseStack (+17 more)

### Community 16 - "Mother Spider Render"
Cohesion: 0.07
Nodes (27): Context, Override, ResourceLocation, SpiderModel, RenderMotherSpider, DeferredHolder, SoundEvent, PMSoundEvents (+19 more)

### Community 17 - "Enchanted Book Model"
Cohesion: 0.08
Nodes (26): LayerDefinition, ModelPart, Override, ModelEnchantedBook, Context, Override, PoseStack, ResourceLocation (+18 more)

### Community 18 - "Custom Packet Payload"
Cohesion: 0.07
Nodes (28): CustomPacketPayload, Key, RegisterPayloadHandlersEvent, PMClientEvents, Entity, IEventBus, Player, PMNetwork (+20 more)

### Community 19 - "Chameleon Model"
Cohesion: 0.08
Nodes (25): Animal, HierarchicalModel, IMultiMobPassive, LayerDefinition, ModelPart, Override, ModelChameleon, Context (+17 more)

### Community 20 - "Traveling Merchant Model"
Cohesion: 0.08
Nodes (26): LayerDefinition, ModelPart, Override, ModelTravelingMerchant, Context, Override, PoseStack, ResourceLocation (+18 more)

### Community 21 - "Mod Registration"
Cohesion: 0.14
Nodes (17): EntityAttributeCreationEvent, EntityFactory, Logger, MobCategory, Mod, ModContainer, SpawnEggItem, Builder (+9 more)

### Community 22 - "Trollager Entity"
Cohesion: 0.16
Nodes (7): BlockPos, BlockState, Builder, CompoundTag, Override, SoundEvent, Trollager

### Community 23 - "Camouflage Armor"
Cohesion: 0.12
Nodes (15): ArmorItem, EquipmentSlot, Holder, Post, PMCamouflageHandler, Item, ItemCamouflageArmor, ArmorMaterial (+7 more)

### Community 24 - "Ocelot Entity"
Cohesion: 0.13
Nodes (14): Ocelot, EntityAIBuffMob, AvoidEntityGoal, Builder, Creeper, EntityType, Level, LivingEntity (+6 more)

### Community 25 - "Spider Attack AI"
Cohesion: 0.11
Nodes (12): AISpiderAttack, AISpiderTarget, BlockPos, BlockState, Builder, DamageSource, EntityDataAccessor, LivingEntity (+4 more)

### Community 26 - "Blazing Juggernaut"
Cohesion: 0.14
Nodes (10): AIChargeAttack, BlazingJuggernaut, Builder, DamageSource, EntityType, Level, LootTable, Override (+2 more)

### Community 27 - "Grove Sprite"
Cohesion: 0.18
Nodes (3): GroveSprite, BlockState, CompoundTag

### Community 28 - "Primitive TNT"
Cohesion: 0.16
Nodes (10): Entity, Builder, CompoundTag, EntityDataAccessor, EntityType, Level, LivingEntity, Nullable (+2 more)

### Community 29 - "Sheepman Entity"
Cohesion: 0.12
Nodes (15): IShearable, LightningBolt, Component, DamageSource, DifficultyInstance, EntityDataAccessor, LootTable, MobSpawnType (+7 more)

### Community 30 - "Grove Sprite Model"
Cohesion: 0.15
Nodes (11): LayerDefinition, ModelPart, Override, ModelGroveSprite, EntityModelSet, MultiBufferSource, Override, PoseStack (+3 more)

### Community 31 - "Thrown Block"
Cohesion: 0.19
Nodes (10): BlockPos, Builder, CompoundTag, EntityDataAccessor, EntityType, Level, LivingEntity, Nullable (+2 more)

### Community 32 - "Spider Chest Texture"
Cohesion: 0.14
Nodes (15): Spider Chest Texture, MobEffectInstance, PathNavigation, DifficultyInstance, EntityDimensions, EntityType, Level, LootTable (+7 more)

### Community 33 - "Yeti Entity"
Cohesion: 0.17
Nodes (11): IMultiMob, NearestAttackableTargetGoal, BlockPos, BlockState, Builder, DamageSource, EntityType, Level (+3 more)

### Community 34 - "Brain Slime AI"
Cohesion: 0.13
Nodes (12): MoveControl, BrainSlimeMoveControl, DamageSource, DifficultyInstance, EntityDataAccessor, EntityDimensions, EntityType, Level (+4 more)

### Community 35 - "Villager Professions"
Cohesion: 0.29
Nodes (8): ItemListing, DeferredHolder, DeferredRegister, IEventBus, ItemStack, PMVillagerProfessions, VillagerProfession, VillagerTradesEvent

### Community 36 - "Grove Sprite AI"
Cohesion: 0.16
Nodes (10): DifficultyInstance, EntityDataAccessor, InteractionHand, InteractionResult, ItemStack, MobSpawnType, Nullable, Player (+2 more)

### Community 37 - "Lava Mob"
Cohesion: 0.14
Nodes (13): IMultiMobLava, ItemEntity, MoveToBlockGoal, AIGoToLava, BlockPos, EntityDataAccessor, EntityType, ItemStack (+5 more)

### Community 38 - "Camouflage Items"
Cohesion: 0.15
Nodes (10): Item, ItemCamouflageDye, ItemMimicOrb, Component, ItemStack, Override, TooltipContext, TooltipFlag (+2 more)

### Community 39 - "Brain Slime Model"
Cohesion: 0.19
Nodes (11): LayerDefinition, ModelPart, Override, ModelBrainSlime, EntityModelSet, MultiBufferSource, Override, PoseStack (+3 more)

### Community 40 - "Sheepman Model"
Cohesion: 0.18
Nodes (11): LayerDefinition, ModelPart, Override, ModelSheepman, EntityModelSet, MultiBufferSource, Override, PoseStack (+3 more)

### Community 41 - "Entity Interactions"
Cohesion: 0.20
Nodes (3): CompoundTag, LivingEntity, Nullable

### Community 42 - "Trollager Entity"
Cohesion: 0.16
Nodes (13): DamageSource, DifficultyInstance, EntityDataAccessor, EntityType, ItemStack, Level, LootTable, MobSpawnType (+5 more)

### Community 43 - "Flame Spewer Model"
Cohesion: 0.19
Nodes (8): LayerDefinition, ModelPart, Override, ModelFlameSpewer, Context, Override, ResourceLocation, RenderFlameSpewer

### Community 44 - "Trollager Attacks"
Cohesion: 0.20
Nodes (4): Goal, Override, TrollagerAttacksGoal, LivingEntity

### Community 45 - "Blazing Juggernaut Render"
Cohesion: 0.19
Nodes (9): MobRenderer, LayerDefinition, ModelPart, Override, ModelBlazingJuggernaut, Context, Override, ResourceLocation (+1 more)

### Community 46 - "Yeti Model"
Cohesion: 0.19
Nodes (8): LayerDefinition, ModelPart, Override, ModelYeti, Context, Override, ResourceLocation, RenderYeti

### Community 47 - "Brain Slime Goals"
Cohesion: 0.18
Nodes (5): BrainSlimeAttackGoal, BrainSlimeFaceRandomGoal, BrainSlimeFloatGoal, BrainSlimeHopGoal, Override

### Community 48 - "Blazing Juggernaut Attributes"
Cohesion: 0.17
Nodes (6): Builder, DamageSource, Entity, Override, ServerLevel, SoundEvent

### Community 49 - "Camouflage Recipe"
Cohesion: 0.21
Nodes (9): CustomRecipe, CamouflageToggleRecipe, CraftingBookCategory, CraftingInput, ItemStack, Level, Override, Provider (+1 more)

### Community 50 - "Block Management"
Cohesion: 0.17
Nodes (8): BlockPos, BlockState, EntityType, InteractionHand, InteractionResult, ItemStack, Level, Player

### Community 51 - "Sheep Management"
Cohesion: 0.23
Nodes (4): Builder, CompoundTag, DyeColor, Sheepman

### Community 52 - "Arrow Mechanics"
Cohesion: 0.23
Nodes (5): AbstractArrow, EntityAISwitchWeapons, ItemStack, LivingEntity, Override

### Community 53 - "Vertex Rendering"
Cohesion: 0.31
Nodes (4): Override, VertexConsumer, PMTint, Tinted

### Community 54 - "Recipe Management"
Cohesion: 0.24
Nodes (8): CraftingBookCategory, CraftingInput, ItemStack, Level, Override, Provider, RecipeSerializer, WonderSapRecipe

### Community 55 - "Flame Spewer AI"
Cohesion: 0.24
Nodes (3): FlameSpewer, Builder, CompoundTag

### Community 56 - "Goblin Mace Item"
Cohesion: 0.25
Nodes (9): ItemGoblinMace, Component, ItemStack, LivingEntity, Override, TooltipContext, TooltipFlag, SwordItem (+1 more)

### Community 57 - "Grove Sprite Sap Item"
Cohesion: 0.23
Nodes (7): ItemGroveSpriteSap, BlockState, Component, ItemStack, Override, TooltipContext, TooltipFlag

### Community 58 - "Skeleton Renderer"
Cohesion: 0.27
Nodes (8): HumanoidMobRenderer, SkeletonModel, Context, Override, ResourceLocation, RenderSkeletonWarrior, Builder, SkeletonWarrior

### Community 59 - "Melee Attack AI"
Cohesion: 0.26
Nodes (3): MeleeAttackGoal, EntityAISwitchBetweenRangedAndMelee, Override

### Community 60 - "Sapling Management"
Cohesion: 0.29
Nodes (5): AIManageSaplings, BlockPos, EntityType, Level, LevelReader

### Community 61 - "Brain Slime Entity"
Cohesion: 0.26
Nodes (3): BrainSlime, Builder, Entity

### Community 62 - "Flame Spew Attack AI"
Cohesion: 0.22
Nodes (4): AIFlameSpewAttack, DamageSource, Override, SoundEvent

### Community 63 - "Flying Item Renderer"
Cohesion: 0.26
Nodes (8): EntityRenderer, ItemRenderer, Context, MultiBufferSource, Override, PoseStack, ResourceLocation, RenderFlyingItem

### Community 64 - "Trollager Model"
Cohesion: 0.27
Nodes (4): LayerDefinition, ModelPart, Override, ModelTrollager

### Community 65 - "Grove Sprite Renderer"
Cohesion: 0.27
Nodes (6): Context, MultiBufferSource, Override, PoseStack, ResourceLocation, RenderGroveSprite

### Community 66 - "TNT Primed Renderer"
Cohesion: 0.24
Nodes (7): BlockRenderDispatcher, Context, MultiBufferSource, Override, PoseStack, ResourceLocation, RenderPrimitiveTNTPrimed

### Community 67 - "Flame Spewer Eyes Layer"
Cohesion: 0.29
Nodes (8): RenderLayer, EntityModelSet, MultiBufferSource, Override, PoseStack, RenderLayerParent, ResourceLocation, LayerFlameSpewerEyes

### Community 68 - "Skeleton Warrior Entity"
Cohesion: 0.29
Nodes (8): Skeleton, DifficultyInstance, EntityType, Level, MobSpawnType, Nullable, ServerLevelAccessor, SpawnGroupData

### Community 69 - "Item Color Management"
Cohesion: 0.27
Nodes (7): PMItemColors, ArmorMaterial, DeferredHolder, DeferredItem, DeferredRegister, Item, PMItems

### Community 70 - "Grove Sprite Stump Layer"
Cohesion: 0.27
Nodes (7): EntityModelSet, MultiBufferSource, Override, PoseStack, RenderLayerParent, ResourceLocation, LayerGroveSpriteStump

### Community 71 - "Thrown Block Renderer"
Cohesion: 0.27
Nodes (7): BlockRenderDispatcher, Context, MultiBufferSource, Override, PoseStack, ResourceLocation, RenderThrownBlock

### Community 72 - "Data Component Registration"
Cohesion: 0.33
Nodes (7): DataComponentType, Codec, DeferredHolder, DeferredRegister, RegistryFriendlyByteBuf, StreamCodec, PMDataComponents

### Community 73 - "Client Rendering Registration"
Cohesion: 0.22
Nodes (4): RegisterLayerDefinitions, RegisterRenderers, IEventBus, PMClientSetup

### Community 74 - "Lava Skin Layer"
Cohesion: 0.31
Nodes (7): EntityModelSet, MultiBufferSource, Override, PoseStack, RenderLayerParent, ResourceLocation, LayerLavaSkin

### Community 75 - "Brain Slime Renderer"
Cohesion: 0.31
Nodes (5): Context, Override, PoseStack, ResourceLocation, RenderBrainSlime

### Community 77 - "Spider Eggshell Item"
Cohesion: 0.31
Nodes (7): ItemSpiderEggshell, InteractionHand, InteractionResult, ItemStack, LivingEntity, Override, Player

### Community 78 - "Sheepman Renderer"
Cohesion: 0.33
Nodes (5): Context, Override, PoseStack, ResourceLocation, RenderSheepman

### Community 79 - "Trollager Renderer"
Cohesion: 0.33
Nodes (5): Context, Override, PoseStack, ResourceLocation, RenderTrollager

### Community 81 - "Recipe Registration"
Cohesion: 0.43
Nodes (4): DeferredHolder, DeferredRegister, RecipeSerializer, PMRecipes

### Community 82 - "Entity Utility Methods"
Cohesion: 0.40
Nodes (3): Entity, EntityDimensions, Vec3

### Community 85 - "Gradle Wrapper Script"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **57 isolated node(s):** `FLYING`, `STANDING`, `SITTING`, `Icon`, `Spider Eyes Texture` (+52 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **57 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PMEntities` connect `PM Entities` to `VoidEye Model`, `Treasure Slime Model`, `Baby Spider Model`, `Chest Block Events`, `Lost Miner Model`, `Goblin Model`, `Filch Lizard Model`, `Empty Model`, `Lily Lurker Model`, `Harpy Model`, `Mimic Model`, `Dodo Model`, `Mother Spider Render`, `Enchanted Book Model`, `Chameleon Model`, `Traveling Merchant Model`, `Mod Registration`, `Trollager Entity`, `Ocelot Entity`, `Blazing Juggernaut`, `Grove Sprite`, `Primitive TNT`, `Thrown Block`, `Yeti Entity`, `Sheep Management`, `Flame Spewer AI`, `Skeleton Renderer`, `Brain Slime Entity`?**
  _High betweenness centrality (0.144) - this node is a cross-community bridge._
- **Why does `BrainSlime` connect `Brain Slime Entity` to `Yeti Entity`, `Brain Slime AI`, `PM Entities`, `Brain Slime Model`, `Entity Interactions`, `Brain Slime Renderer`, `Tameable Slime`, `Brain Slime Goals`?**
  _High betweenness centrality (0.077) - this node is a cross-community bridge._
- **Why does `TreasureSlime` connect `Treasure Slime Model` to `Tameable Slime`, `PM Entities`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **What connects `FLYING`, `STANDING`, `SITTING` to the rest of the system?**
  _57 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `VoidEye Model` be split into smaller, more focused modules?**
  _Cohesion score 0.055501460564751706 - nodes in this community are weakly interconnected._
- **Should `Treasure Slime Model` be split into smaller, more focused modules?**
  _Cohesion score 0.05365686944634313 - nodes in this community are weakly interconnected._
- **Should `Baby Spider Model` be split into smaller, more focused modules?**
  _Cohesion score 0.0547945205479452 - nodes in this community are weakly interconnected._