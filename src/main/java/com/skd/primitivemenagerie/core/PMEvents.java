// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import java.util.ArrayList;
import java.util.List;

import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.entity.monster.Goblin;
import com.skd.primitivemenagerie.entity.monster.Harpy;
import com.skd.primitivemenagerie.entity.monster.HauntedTool;
import com.skd.primitivemenagerie.entity.monster.Mimic;
import com.skd.primitivemenagerie.entity.monster.RocketCreeper;
import com.skd.primitivemenagerie.entity.monster.SkeletonWarrior;
import com.skd.primitivemenagerie.entity.passive.Sheepman;
import com.skd.primitivemenagerie.item.ItemCamouflageArmor;
import com.skd.primitivemenagerie.item.ItemGoblinMace;
import com.skd.primitivemenagerie.item.ItemMimicOrb;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Game events of the mod, replacing the original {@code PrimitiveMobsEvents}. Every handler that the
 * ported entities and items need is ported here.
 *
 * <p>Simplifications: the 1.12 {@code EntityAIAvoidEntity} tasks added to villagers are not ported
 * (1.21 villagers are brain driven); the sheepman sound swap is handled by the entity's own sound
 * overrides; the smoke/cloud particle messages are replaced by the vanilla smoke/cloud particles.</p>
 *
 * @author Daveyx0
 */
public final class PMEvents {

    private static final int FULL_CAMOUFLAGE_DISTANCE = 6;

    private PMEvents() {}

    /** Registers the handlers on the game event bus. */
    public static void register() {
        NeoForge.EVENT_BUS.addListener(PMEvents::onEntityJoinLevel);
        NeoForge.EVENT_BUS.addListener(PMEvents::onLeftClickBlock);
        NeoForge.EVENT_BUS.addListener(PMEvents::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(PMEvents::onLivingIncomingDamage);
        NeoForge.EVENT_BUS.addListener(PMEvents::onLivingChangeTarget);
        NeoForge.EVENT_BUS.addListener(PMEvents::onLivingFall);
        NeoForge.EVENT_BUS.addListener(PMEvents::onEntityMount);
        NeoForge.EVENT_BUS.addListener(PMEvents::onPlayerLoggedOut);
    }

    // ---------------------------------------------------------------------------------------------
    // Entity join: disabled mobs and villager profession cleanup
    // ---------------------------------------------------------------------------------------------
    private static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        removeWhenDisabled(entity);

        if (entity instanceof Villager villager && !(villager instanceof Sheepman)
                && !(villager instanceof com.skd.primitivemenagerie.entity.passive.LostMiner)
                && !(villager instanceof com.skd.primitivemenagerie.entity.passive.TravelingMerchant)) {
            var profession = villager.getVillagerData().getProfession();

            if (profession == PMVillagerProfessions.MINER.get()
                    || profession == PMVillagerProfessions.MERCHANT.get()
                    || profession == PMVillagerProfessions.FAKE_MERCHANT.get()
                    || profession == PMVillagerProfessions.SHEEPMAN_SCAVENGER.get()
                    || profession == PMVillagerProfessions.SHEEPMAN_ALCHEMIST.get()
                    || profession == PMVillagerProfessions.SHEEPMAN_THIEF.get()) {
                // Replace the profession with a plain one, matching the original replaceVillager().
                villager.setVillagerData(villager.getVillagerData()
                        .setProfession(net.minecraft.world.entity.npc.VillagerProfession.NONE)
                        .setLevel(1));
            }
        }
    }

    /** Discards a ported entity whose mob enable flag is off, mirroring {@code removeWhenDisabled}. */
    private static void removeWhenDisabled(Entity entity) {
        if (!(entity instanceof Mob) || entity.level().isClientSide) {
            return;
        }

        EntityType<?> type = entity.getType();
        boolean disabled = (type == PMEntities.CHAMELEON.get() && !PMConfig.getEnableChameleon())
                || (type == PMEntities.TREASURE_SLIME.get() && !PMConfig.getEnableTreasureSlime())
                || (type == PMEntities.HAUNTED_TOOL.get() && !PMConfig.getEnableHauntedTool())
                || (type == PMEntities.GROVE_SPRITE.get() && !PMConfig.getEnableGroveSprite())
                || (type == PMEntities.BEWITCHED_TOME.get() && !PMConfig.getEnableEnchantedBook())
                || (type == PMEntities.FILCH_LIZARD.get() && !PMConfig.getEnableFilchLizard())
                || (type == PMEntities.BRAIN_SLIME.get() && !PMConfig.getEnableBrainSlime())
                || (type == PMEntities.ROCKET_CREEPER.get() && !PMConfig.getEnableRocketCreeper())
                || (type == PMEntities.FESTIVE_CREEPER.get() && !PMConfig.getEnableFestiveCreeper())
                || (type == PMEntities.SUPPORT_CREEPER.get() && !PMConfig.getEnableSupportCreeper())
                || (type == PMEntities.SKELETON_WARRIOR.get() && !PMConfig.getEnableSkeletonWarrior())
                || (type == PMEntities.BLAZING_JUGGERNAUT.get() && !PMConfig.getEnableBlazingJuggernaut())
                || (type == PMEntities.LILY_LURKER.get() && !PMConfig.getEnableLilyLurker())
                || (type == PMEntities.MOTHER_SPIDER.get() && !PMConfig.getEnableSpiderFamily())
                || (type == PMEntities.BABY_SPIDER.get() && !PMConfig.getEnableSpiderFamily())
                || (type == PMEntities.TROLLAGER.get() && !PMConfig.getEnableTrollager())
                || (type == PMEntities.LOST_MINER.get() && !PMConfig.getEnableLostMiner())
                || (type == PMEntities.TRAVELING_MERCHANT.get() && !PMConfig.getEnableMerchant())
                || (type == PMEntities.DODO.get() && !PMConfig.getEnableDodo())
                || (type == PMEntities.MIMIC.get() && !PMConfig.getEnableMimic())
                || (type == PMEntities.SHEEPMAN.get() && !PMConfig.getEnableSheepman())
                || (type == PMEntities.GOBLIN.get() && !PMConfig.getEnableGoblin())
                || (type == PMEntities.HARPY.get() && !PMConfig.getEnableHarpy())
                || (type == PMEntities.FLAME_SPEWER.get() && !PMConfig.getEnableFlameSpewer())
                || (type == PMEntities.VOID_EYE.get() && !PMConfig.getEnableVoidWatcher());

        if (disabled) {
            entity.discard();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Mimic orb and mimic chest interaction
    // ---------------------------------------------------------------------------------------------
    private static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        if (level.isClientSide || !player.isCreative()) {
            return;
        }

        ChestBlockEntity chest = getSingleChest(level, event.getPos());

        if (chest == null || !chest.isEmpty() || chest.getLootTable() != null) {
            return;
        }

        if (player.getMainHandItem().getItem() instanceof ItemMimicOrb) {
            chest.setLootTable(PMLootTables.key(PMLootTables.MIMIC_UNOPENED), level.getRandom().nextLong());
            ((ServerLevel) level).sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE,
                    event.getPos().getX() + 0.5D, event.getPos().getY() + 0.5D, event.getPos().getZ() + 0.5D, 10, 0.1D, 0.1D, 0.1D, 0.01D);
            event.setCanceled(true);
        }
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        if (level.isClientSide) {
            return;
        }

        ChestBlockEntity chest = getSingleChest(level, event.getPos());

        if (chest == null) {
            return;
        }

        var lootTable = chest.getLootTable();

        if (lootTable != null && (lootTable.location().equals(PMLootTables.MIMIC_UNOPENED)
                || lootTable.location().equals(PMLootTables.MIMIC_GUARANTEED))) {
            resolveMimicChest(player, level, event.getPos(), chest,
                    lootTable.location().equals(PMLootTables.MIMIC_GUARANTEED));
            event.setCanceled(true);
            return;
        }

        // Mark an empty chest with the mimic orb.
        if (lootTable == null && chest.isEmpty() && player.getMainHandItem().getItem() instanceof ItemMimicOrb) {
            chest.setLootTable(PMLootTables.key(PMLootTables.MIMIC_GUARANTEED), level.getRandom().nextLong());

            if (!player.getAbilities().instabuild) {
                player.getMainHandItem().shrink(1);
            }

            ((ServerLevel) level).sendParticles(net.minecraft.core.particles.ParticleTypes.CLOUD,
                    event.getPos().getX() + 0.5D, event.getPos().getY() + 0.5D, event.getPos().getZ() + 0.5D, 10, 0.1D, 0.1D, 0.1D, 0.0D);
            event.setCanceled(true);
        }
    }

    /** Resolves a marker chest: spawn a mimic, spawn a trap or turn it into a treasure chest. */
    private static void resolveMimicChest(Player player, Level level, BlockPos pos, ChestBlockEntity chest, boolean guaranteed) {
        int chance = level.getRandom().nextInt(3);
        boolean spawnMimic = guaranteed || chance == 0;
        boolean trap = chance == 1;

        if (spawnMimic) {
            Mimic mimic = PMEntities.MIMIC.get().create(level);

            if (mimic != null) {
                mimic.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 180.0F, 0.0F);
                mimic.setChest(level.getBlockState(pos));
                level.addFreshEntity(mimic);

                if (!guaranteed && level.getRandom().nextInt(10) == 0) {
                    mimic.setToExplode();
                }
            }

            level.removeBlock(pos, false);
            ((ServerLevel) level).sendParticles(net.minecraft.core.particles.ParticleTypes.CLOUD,
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 10, 0.1D, 0.1D, 0.1D, 0.0D);
        } else if (trap) {
            int option = level.getRandom().nextInt(4);

            if (option == 0) {
                SkeletonWarrior skeleton = PMEntities.SKELETON_WARRIOR.get().create(level);

                if (skeleton != null) {
                    skeleton.moveTo(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 180.0F, 0.0F);
                    level.addFreshEntity(skeleton);
                }
            } else if (option == 1) {
                for (int i = 0; i < 3; i++) {
                    var bat = EntityType.BAT.create(level);

                    if (bat != null) {
                        bat.moveTo(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 180.0F, 0.0F);
                        level.addFreshEntity(bat);
                    }
                }
            } else {
                HauntedTool tool = PMEntities.HAUNTED_TOOL.get().create(level);

                if (tool != null) {
                    tool.moveTo(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 180.0F, 0.0F);
                    level.addFreshEntity(tool);
                }
            }

            chest.setLootTable(PMLootTables.key(PMLootTables.MIMIC_TRAP), level.getRandom().nextLong());
            ((ServerLevel) level).sendParticles(net.minecraft.core.particles.ParticleTypes.CLOUD,
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 10, 0.1D, 0.1D, 0.1D, 0.0D);
        } else {
            chest.setLootTable(PMLootTables.key(PMLootTables.MIMIC_TREASURE), level.getRandom().nextLong());
        }
    }

    /** @return the chest at the position, or null if it is missing or part of a double chest */
    private static ChestBlockEntity getSingleChest(Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof ChestBlockEntity chest)) {
            return null;
        }

        if (!(level.getBlockState(pos).getBlock() instanceof ChestBlock)) {
            return null;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(direction)).getBlock() instanceof ChestBlock) {
                return null;
            }
        }

        return chest;
    }

    // ---------------------------------------------------------------------------------------------
    // Goblin mace armor damage
    // ---------------------------------------------------------------------------------------------
    private static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        if (!(attacker.getMainHandItem().getItem() instanceof ItemGoblinMace)) {
            return;
        }

        LivingEntity victim = event.getEntity();
        List<ItemStack> damageableArmor = new ArrayList<>();

        for (ItemStack piece : victim.getArmorSlots()) {
            if (!piece.isEmpty() && piece.isDamageableItem()) {
                damageableArmor.add(piece);
            }
        }

        if (!damageableArmor.isEmpty()) {
            ItemStack target = damageableArmor.get(victim.getRandom().nextInt(damageableArmor.size()));
            target.hurtAndBreak((int) (target.getMaxDamage() * ItemGoblinMace.damagePercentage), victim, EquipmentSlot.CHEST);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Targeting: sheepman immunity and full camouflage armor
    // ---------------------------------------------------------------------------------------------
    private static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();

        if (event.getNewAboutToBeSetTarget() instanceof Sheepman && isSheepmanHater(entity)) {
            event.setNewAboutToBeSetTarget(null);
            return;
        }

        if (event.getNewAboutToBeSetTarget() instanceof Player player && entity instanceof Mob
                && hasFullCamouflageArmor(player) && entity.distanceToSqr(player) > FULL_CAMOUFLAGE_DISTANCE * FULL_CAMOUFLAGE_DISTANCE) {
            event.setNewAboutToBeSetTarget(null);
        }
    }

    private static boolean isSheepmanHater(LivingEntity entity) {
        return entity instanceof Zombie || entity instanceof AbstractIllager || entity instanceof Goblin;
    }

    /** @return whether the player wears a full set of camouflage armor */
    public static boolean hasFullCamouflageArmor(Player player) {
        int pieces = 0;

        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.getItem() instanceof ItemCamouflageArmor) {
                pieces++;
            }
        }

        return pieces == 4;
    }

    // ---------------------------------------------------------------------------------------------
    // Rocket creeper fall immunity
    // ---------------------------------------------------------------------------------------------
    private static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof RocketCreeper) {
            event.setCanceled(true);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Harpy dismount prevention
    // ---------------------------------------------------------------------------------------------
    private static void onEntityMount(EntityMountEvent event) {
        if (event.isDismounting() && event.getEntityBeingMounted() instanceof Harpy
                && event.getEntityMounting() != null && event.getEntityMounting().isShiftKeyDown()
                && event.getEntityMounting() instanceof Player) {
            event.setCanceled(true);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Player logout: drop passengers
    // ---------------------------------------------------------------------------------------------
    private static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();

        if (player.isPassenger()) {
            player.removeVehicle();
        }

        player.ejectPassengers();
    }
}
