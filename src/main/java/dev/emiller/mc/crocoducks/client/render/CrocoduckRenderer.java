package dev.emiller.mc.crocoducks.client.render;

import dev.emiller.mc.crocoducks.Crocoducks;
import dev.emiller.mc.crocoducks.client.model.CrocoduckModel;
import dev.emiller.mc.crocoducks.entity.CrocoduckEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CrocoduckRenderer extends MobRenderer<CrocoduckEntity, CrocoduckModel> {
    private static final ResourceLocation CROCODUCK_LOCATION = ResourceLocation.fromNamespaceAndPath(
            Crocoducks.MODID,
            "textures/entities/crocoduck.png"
    );

    private static final ResourceLocation CROCODUCK_TAME_LOCATION = ResourceLocation.fromNamespaceAndPath(
            Crocoducks.MODID,
            "textures/entities/crocoduck_tame.png"
    );

    public CrocoduckRenderer(EntityRendererProvider.Context p_173960_) {
        super(p_173960_, new CrocoduckModel(p_173960_.bakeLayer(CrocoduckModelLayers.CROCODUCK)), 0.25F);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull CrocoduckEntity player) {
        if (player.isTame()) {
            return CROCODUCK_TAME_LOCATION;
        }

        return CROCODUCK_LOCATION;
    }

   protected float getBob(CrocoduckEntity p_114000_, float p_114001_) {
      float f = Mth.lerp(p_114001_, p_114000_.oFlap, p_114000_.flap);
      float f1 = Mth.lerp(p_114001_, p_114000_.oFlapSpeed, p_114000_.flapSpeed);
      return (Mth.sin(f) + 1.0F) * f1;
   }
}
