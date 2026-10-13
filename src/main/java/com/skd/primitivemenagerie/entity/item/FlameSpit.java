// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Flame spit, ported from {@code EntityFlameSpit}. A small fireball with a larger flame trail that
 * expires after 30 ticks.
 *
 * @author Daveyx0
 */
public class FlameSpit extends SmallFireball {

    public FlameSpit(EntityType<? extends SmallFireball> type, Level level) {
        super(type, level);
    }

    public FlameSpit(Level level, LivingEntity shooter, Vec3 direction) {
        super(com.skd.primitivemenagerie.core.PMEntities.FLAME_SPIT.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.shoot(direction.x, direction.y, direction.z, 1.5F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            for (int i = 0; i < 10; i++) {
                this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.tickCount > 30) {
            this.discard();
        }
    }
}
