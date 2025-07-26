package dev.emiller.mc.crocoducks.registries;

import dev.emiller.mc.crocoducks.client.render.CrocoduckRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class CrocoduckEntityRenderers {
    public static void setupEntityRenderers() {
        EntityRenderers.register(CrocoducksEntities.CROCODUCK.get(), CrocoduckRenderer::new);
    }
}