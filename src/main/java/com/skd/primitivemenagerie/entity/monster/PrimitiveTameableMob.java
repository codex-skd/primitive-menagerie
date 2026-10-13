// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.UUID;

import javax.annotation.Nullable;

import com.skd.mobweavecore.common.capabilities.CapabilityTameableEntity;
import com.skd.mobweavecore.common.capabilities.ITameableEntity;
import com.skd.mobweavecore.common.capabilities.TameableEntityHandler;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base tameable monster, ported from {@code EntityPrimitiveTameableMob}. The original stored the
 * tamed/owner/sitting state in its own data parameters; here that state is delegated to the Mobweave
 * Core tameable attachment (see {@link ITameableEntity}) so the ported owner goals work.
 *
 * <p>The original {@code getTeam()}/{@code isOnSameTeam(...)} overrides have no equivalent in 1.21 and
 * are dropped. The original sit flag is represented by follow state {@code 0 = sitting}.</p>
 *
 * @author Daveyx0
 */
public abstract class PrimitiveTameableMob extends Monster implements ITameableEntity {

    public PrimitiveTameableMob(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    // ---------------------------------------------------------------------------------------------
    // Tameable API (backed by the Mobweave Core attachment)
    // ---------------------------------------------------------------------------------------------

    protected TameableEntityHandler tameable() {
        return CapabilityTameableEntity.getCapability(this);
    }

    @Override
    public boolean isTamed() {
        return this.tameable().isTamed();
    }

    @Override
    public void setTamed(boolean set) {
        this.tameable().setTamed(set);
    }

    @Nullable
    @Override
    public LivingEntity getOwner(LivingEntity entity) {
        return this.tameable().getOwner(entity);
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.tameable().getOwner(this);
    }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return this.tameable().getOwnerId();
    }

    @Override
    public void setOwner(UUID id) {
        this.tameable().setOwner(id);
    }

    @Override
    public int getFollowState() {
        return this.tameable().getFollowState();
    }

    @Override
    public void setFollowState(int set) {
        this.tameable().setFollowState(set);
    }

    public boolean isOwner(LivingEntity entity) {
        return entity == this.getOwner();
    }

    public void setTamedBy(Entity entity) {
        this.setTamed(true);
        this.setOwner(entity.getUUID());
    }

    public void setTamedByPlayer(Player player) {
        this.setTamed(true);
        this.setOwner(player.getUUID());
    }

    /** Follow state {@code 0} is the ported equivalent of the original sitting flag. */
    public boolean isSitting() {
        return this.getFollowState() == 0;
    }

    public void setSitting(boolean sitting) {
        this.setFollowState(sitting ? 0 : 1);
    }

    @Override
    public boolean canBeLeashed() {
        return true;
    }

    /** Plays the taming effect: hearts when tamed, smoke otherwise. */
    protected void playTameEffect(boolean play) {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(play ? ParticleTypes.HEART : ParticleTypes.SMOKE,
                    this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
                    this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getBbHeight()),
                    this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
                    d0, d1, d2);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            this.playTameEffect(true);
        } else if (id == 6) {
            this.playTameEffect(false);
        } else {
            super.handleEntityEvent(id);
        }
    }
}
