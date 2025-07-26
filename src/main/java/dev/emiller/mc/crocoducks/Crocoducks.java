package dev.emiller.mc.crocoducks;

import com.mojang.logging.LogUtils;
import dev.emiller.mc.crocoducks.client.model.CrocoduckModel;
import dev.emiller.mc.crocoducks.client.render.CrocoduckModelLayers;
import dev.emiller.mc.crocoducks.entity.CrocoduckEntity;
import dev.emiller.mc.crocoducks.registries.CrocoduckEntityRenderers;
import dev.emiller.mc.crocoducks.registries.CrocoducksEntities;
import dev.emiller.mc.crocoducks.registries.CrocoducksItems;
import dev.emiller.mc.crocoducks.registries.CrocoducksSoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Crocoducks.MODID)
public class Crocoducks {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "crocoducks";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Crocoducks(IEventBus modEventBus, ModContainer ignoredModContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        CrocoducksEntities.register(modEventBus);
        CrocoducksItems.register(modEventBus);
        CrocoducksSoundEvents.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("WGL HELLO from server starting");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DispenserBlock.registerProjectileBehavior(CrocoducksItems.CROCODUCK_EGG.get());
    }

    @EventBusSubscriber(modid = MODID)
    public static class ModEvents {
        @SubscribeEvent // on the mod event bus
        public static void createDefaultAttributes(EntityAttributeCreationEvent event) {
            event.put(CrocoducksEntities.CROCODUCK.get(), CrocoduckEntity.createAttributes().build());
        }

        @SubscribeEvent
        public static void buildContents(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
                event.accept(CrocoducksItems.CROCODUCK_EGG.get());
            }

            if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
                event.accept(CrocoducksItems.CROCODUCK_SPAWN_EGG.get());
            }
        }

        // You can use SubscribeEvent and let the Event Bus discover methods to call
        @SubscribeEvent
        public static void onSpawnZonesRegistration(RegisterSpawnPlacementsEvent event) {
            CrocoducksEntities.registerSpawnRules(event);
        }
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CrocoduckEntityRenderers.setupEntityRenderers();
        }

        @SubscribeEvent
        public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(CrocoduckModelLayers.CROCODUCK, CrocoduckModel::createBodyLayer);
        }
    }
}
