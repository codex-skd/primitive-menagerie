// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.item;

import java.util.function.Supplier;

import com.skd.mobweavecore.common.capabilities.ITameableEntity;
import com.skd.primitivemenagerie.core.PMEntities;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Base thrown "primitive egg" projectile, ported from {@code EntityPrimitiveThrowable}. On impact it
 * has a chance to spawn a configured mob and drops a matching egg particle effect.
 *
 * <p>Simplification: the original selected the spawned entity by its Java class through the old Forge
 * entity registry; here the spawned {@link EntityType} is supplied directly, which is the 1.21 way.</p>
 *
 * @author Daveyx0
 */
public class PrimitiveThrowable extends ThrowableItemProjectile {

    private Supplier<EntityType<? extends Mob>> spawnEntityType = () -> EntityType.CHICKEN;
    private int spawnChance;
    private LivingEntity thrower;
    private Item defaultItem = Items.EGG;

    public PrimitiveThrowable(EntityType<? extends PrimitiveThrowable> type, Level level) {
        super(type, level);
    }

    public PrimitiveThrowable(Level level, LivingEntity thrower, Supplier<EntityType<? extends Mob>> entity, int chance) {
        super(PMEntities.PRIMITIVE_EGG.get(), thrower, level);
        this.spawnEntityType = entity;
        this.spawnChance = chance;
        this.thrower = thrower;
    }

    public PrimitiveThrowable(Level level, double x, double y, double z, Supplier<EntityType<? extends Mob>> entity, int chance) {
        super(PMEntities.PRIMITIVE_EGG.get(), x, y, z, level);
        this.spawnEntityType = entity;
        this.spawnChance = chance;
    }

    public void setSpawnEntityType(Supplier<EntityType<? extends Mob>> entity) {
        this.spawnEntityType = entity;
    }

    public void setSpawnChance(int chance) {
        this.spawnChance = chance;
    }

    /** Sets the item rendered by the projectile, so each thrown egg keeps its own texture. */
    public void setDefaultItem(Item item) {
        this.defaultItem = item;
    }

    @Override
    protected Item getDefaultItem() {
        return this.defaultItem;
    }

    /** Kept for the flying-item renderer, mirroring {@code getItemFromEntity()}. */
    public ItemStack getItemFromEntity() {
        return new ItemStack(this.defaultItem);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            if (this.spawnChance > 0 && this.random.nextInt(this.spawnChance) == 0) {
                Mob entity = this.spawnEntityType.get().create(this.level());

                if (entity != null) {
                    // The original spawned animals and creepers as babies (setGrowingAge(-24000)).
                    if (entity instanceof net.minecraft.world.entity.AgeableMob ageable) {
                        ageable.setBaby(true);
                    } else if (entity instanceof com.skd.primitivemenagerie.entity.monster.PrimitiveCreeper creeper) {
                        creeper.setGrowingAge(-24000);
                    }

                    entity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);

                    if (entity instanceof ITameableEntity tameable && !tameable.isTamed() && this.thrower != null) {
                        tameable.setTamed(true);
                        tameable.setOwner(this.thrower.getUUID());
                    }

                    this.level().addFreshEntity(entity);
                }
            }

            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                        this.getX(), this.getY(), this.getZ(),
                        (this.random.nextDouble() - 0.5D) * 0.08D,
                        (this.random.nextDouble() - 0.5D) * 0.08D,
                        (this.random.nextDouble() - 0.5D) * 0.08D);
            }
        }
    }
}
