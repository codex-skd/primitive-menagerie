// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.passive;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMobPassive;
import com.skd.mobweavecore.util.ColorUtil;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Chameleon, ported from {@code EntityChameleon}. A passive animal that slowly shifts its skin color
 * to match the block it stands on. The color logic runs on the client only, exactly like the
 * original.
 *
 * @author Daveyx0
 */
public class Chameleon extends Animal implements IMultiMobPassive {

    private float r;
    private float g;
    private float b;
    private float newR;
    private float newG;
    private float newB;
    private final int colorSpeed = 4;

    @Nullable
    private BlockState currentState;
    private int currentMultiplier;

    public Chameleon(EntityType<? extends Chameleon> type, Level level) {
        super(type, level);
        this.setSkinRGB(new int[] {0, 125, 25});
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void registerGoals() {
        int prio = 0;
        this.goalSelector.addGoal(++prio, new FloatGoal(this));
        this.goalSelector.addGoal(++prio, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(++prio, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(++prio, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(++prio, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(++prio, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(++prio, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.FERMENTED_SPIDER_EYE);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return new Chameleon(com.skd.primitivemenagerie.core.PMEntities.CHAMELEON.get(), level);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_CHAMELEON);
    }

    @Override
    public void tick() {
        if (this.isInWater() && !this.horizontalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.02D, this.getDeltaMovement().z);
        }

        if (this.level().isClientSide) {
            this.changeColor();
        }

        if (this.r != this.newR || this.g != this.newG || this.b != this.newB) {
            for (int i = 0; i < this.colorSpeed; i++) {
                if (this.r > this.newR) {
                    this.r--;
                } else if (this.r < this.newR) {
                    this.r++;
                }

                if (this.g > this.newG) {
                    this.g--;
                } else if (this.g < this.newG) {
                    this.g++;
                }

                if (this.b > this.newB) {
                    this.b--;
                } else if (this.b < this.newB) {
                    this.b++;
                }
            }
        }

        super.tick();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    public float[] getSkinRGB() {
        return new float[] {this.r, this.g, this.b};
    }

    public void setSkinRGB(int[] rgb) {
        this.r = rgb[0];
        this.g = rgb[1];
        this.b = rgb[2];
    }

    public void setNewSkinRGB(int[] rgb) {
        this.newR = rgb[0];
        this.newG = rgb[1];
        this.newB = rgb[2];
    }

    /** Client-side color matching against the block the chameleon is standing on. */
    private void changeColor() {
        int i = net.minecraft.util.Mth.floor(this.getX());
        int j = net.minecraft.util.Mth.floor(this.getBoundingBox().minY);
        int k = net.minecraft.util.Mth.floor(this.getZ());

        BlockPos pos = new BlockPos(i, j, k);

        if (this.level().getBlockState(pos).isAir()) {
            pos = new BlockPos(i, j - 1, k);
        }

        BlockState state = this.level().getBlockState(pos);

        if (state.isAir()) {
            return;
        }

        int colorMultiplier = net.minecraft.client.Minecraft.getInstance().getBlockColors()
                .getColor(state, this.level(), pos, 0);

        if (this.currentState != state || this.currentMultiplier != colorMultiplier) {
            this.currentState = state;
            this.currentMultiplier = colorMultiplier;

            int[] newColor = ColorUtil.getBlockStateColor(state, pos, this.level(), true);

            if (newColor != null) {
                if (ColorUtil.isColorInvalid(newColor)) {
                    newColor = new int[] {63, 118, 42, 255};
                }

                this.setNewSkinRGB(newColor);
            }
        }
    }

}
