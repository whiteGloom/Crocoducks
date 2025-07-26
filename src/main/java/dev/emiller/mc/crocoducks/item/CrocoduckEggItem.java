package dev.emiller.mc.crocoducks.item;

import dev.emiller.mc.crocoducks.entity.CrocoduckEggEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CrocoduckEggItem extends Item implements ProjectileItem {
    public CrocoduckEggItem(Item.Properties properties) {
        super(properties);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(
            Level p_41128_,
            Player p_41129_,
            @NotNull InteractionHand hand
    ) {
        ItemStack itemstack = p_41129_.getItemInHand(hand);
        p_41128_.playSound(
                null,
                p_41129_.getX(),
                p_41129_.getY(),
                p_41129_.getZ(),
                SoundEvents.EGG_THROW,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (p_41128_.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!p_41128_.isClientSide) {
            CrocoduckEggEntity thrownEgg = new CrocoduckEggEntity(p_41128_, p_41129_);
            thrownEgg.setItem(itemstack);
            thrownEgg.shootFromRotation(p_41129_, p_41129_.getXRot(), p_41129_.getYRot(), 0.0F, 1.5F, 1.0F);
            p_41128_.addFreshEntity(thrownEgg);
        }

        p_41129_.awardStat(Stats.ITEM_USED.get(this));
        if (!p_41129_.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, p_41128_.isClientSide());
    }

    @Override
    public @NotNull Projectile asProjectile(@NotNull Level level, Position position, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        CrocoduckEggEntity egg = new CrocoduckEggEntity(level, position.x(), position.y(), position.z());
        egg.setItem(itemStack);
        return egg;
    }
}
