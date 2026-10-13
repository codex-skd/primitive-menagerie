// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.world.gen;

import com.mojang.serialization.Codec;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Mimic chest world generation, ported from {@code WorldGenMimic}. The original scanned a cave floor
 * between y 5 and y 40 and placed a chest flagged with the {@code Mimic} tile-entity tag; the port
 * places a chest carrying the {@code mimic_unopened} marker loot table, which the interaction handler
 * recognises when the chest is first opened.
 *
 * <p>The original 1.12 chunk-based rarity roll is kept inside {@link #place}: it reads
 * {@link PMConfig#getEnableMimic()}, {@link PMConfig#getMimicGeneratesInCaves()} and
 * {@link PMConfig#getMimicRarity()} at generation time.</p>
 *
 * @author Daveyx0
 */
public class MimicChestFeature extends Feature<NoneFeatureConfiguration> {

    public MimicChestFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!PMConfig.getEnableMimic() || !PMConfig.getMimicGeneratesInCaves()) {
            return false;
        }

        int chance = PMConfig.getMimicRarity();

        if (chance <= 0) {
            return false;
        }

        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        if (random.nextInt(chance) != 0) {
            return false;
        }

        BlockPos pos = findCaveFloor(level, context.origin());

        if (pos == null) {
            return false;
        }

        level.setBlock(pos, Blocks.CHEST.defaultBlockState(), 2);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof RandomizableContainerBlockEntity chest) {
            chest.setLootTable(PMLootTables.key(PMLootTables.MIMIC_UNOPENED), random.nextLong());
        }

        return true;
    }

    /**
     * Ported {@code getAboveSolidOrLiquidBlock}: scans y from 5 to 40 for the first air block with a
     * solid, non-leaf block below it.
     */
    private BlockPos findCaveFloor(WorldGenLevel level, BlockPos pos) {
        for (int y = 5; y < 40; y++) {
            BlockPos candidate = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState below = level.getBlockState(candidate.below());

            if (level.isEmptyBlock(candidate) && below.blocksMotion() && !below.is(BlockTags.LEAVES)) {
                return candidate;
            }
        }

        return null;
    }
}
