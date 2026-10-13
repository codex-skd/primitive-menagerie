// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.item;

import com.skd.primitivemenagerie.core.PMEntities;
import com.skd.primitivemenagerie.core.PMItems;
import com.skd.primitivemenagerie.entity.monster.BabySpider;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Thrown spider egg, ported from {@code EntitySpiderEgg}. On impact it drops the eggshell and has a
 * chance to spawn a baby spider tamed to the thrower.
 *
 * @author Daveyx0
 */
public class SpiderEgg extends ThrowableItemProjectile {

    private int spawnChance = 8;
    private Item defaultItem = PMItems.SPIDER_EGG.get();

    public SpiderEgg(EntityType<? extends SpiderEgg> type, Level level) {
        super(type, level);
    }

    public SpiderEgg(Level level, LivingEntity thrower) {
        super(PMEntities.SPIDER_EGG.get(), thrower, level);
    }

    public SpiderEgg(Level level, double x, double y, double z) {
        super(PMEntities.SPIDER_EGG.get(), x, y, z, level);
    }

    public void setSpawnChance(int spawnChance) {
        this.spawnChance = spawnChance;
    }

    /** Sets the item rendered by the projectile. */
    public void setDefaultItem(Item item) {
        this.defaultItem = item;
    }

    @Override
    protected Item getDefaultItem() {
        return this.defaultItem;
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

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            this.spawnAtLocation(PMItems.SPIDER_EGGSHELL.get());

            if (this.random.nextInt(Math.max(1, this.spawnChance)) == 0) {
                BabySpider baby = PMEntities.BABY_SPIDER.get().create(this.level());

                if (baby != null) {
                    baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);

                    if (this.getOwner() instanceof LivingEntity owner) {
                        baby.setTamed(true);
                        baby.setOwner(owner.getUUID());
                    }

                    this.level().addFreshEntity(baby);
                }
            }

            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}
