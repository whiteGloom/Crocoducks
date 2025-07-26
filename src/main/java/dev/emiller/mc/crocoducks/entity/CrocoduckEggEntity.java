package dev.emiller.mc.crocoducks.entity;

import dev.emiller.mc.crocoducks.registries.CrocoducksEntities;
import dev.emiller.mc.crocoducks.registries.CrocoducksItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class CrocoduckEggEntity extends ThrowableItemProjectile {
    public CrocoduckEggEntity(EntityType<? extends CrocoduckEggEntity> type, Level level) {
        super(type, level);
    }

    public CrocoduckEggEntity(Level p_37481_, LivingEntity p_37482_) {
        super(EntityType.EGG, p_37482_, p_37481_);
    }

    public CrocoduckEggEntity(Level p_37476_, double p_37477_, double p_37478_, double p_37479_) {
        super(EntityType.EGG, p_37477_, p_37478_, p_37479_, p_37476_);
    }

    public void handleEntityEvent(byte p_37484_) {
        if (p_37484_ == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D
                );
            }
        }

    }

    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        hitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    CrocoduckEntity crocoduck = CrocoducksEntities.CROCODUCK.get().create(this.level());
                    if (crocoduck != null) {
                        crocoduck.setAge(-24000);
                        crocoduck.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        this.level().addFreshEntity(crocoduck);
                    }
                }
            }

            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }

    }

    protected @NotNull Item getDefaultItem() {
        return CrocoducksItems.CROCODUCK_EGG.get();
    }
}
