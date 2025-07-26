package dev.emiller.mc.crocoducks.client.render;

import dev.emiller.mc.crocoducks.Crocoducks;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class CrocoduckModelLayers {
    public static final ModelLayerLocation CROCODUCK = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(
                    Crocoducks.MODID,
                    "crocoduck"
            ), "main"
    );
}
