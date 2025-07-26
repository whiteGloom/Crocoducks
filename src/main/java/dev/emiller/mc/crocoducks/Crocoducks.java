package dev.emiller.mc.crocoducks;

import com.mojang.logging.LogUtils;
import dev.emiller.mc.crocoducks.client.model.CrocoduckModel;
import dev.emiller.mc.crocoducks.client.render.CrocoduckModelLayers;
import dev.emiller.mc.crocoducks.entity.CrocoduckEggEntity;
import dev.emiller.mc.crocoducks.entity.CrocoduckEntity;
import dev.emiller.mc.crocoducks.registries.CrocoduckEntityRenderers;
import dev.emiller.mc.crocoducks.registries.CrocoducksEntities;
import dev.emiller.mc.crocoducks.registries.CrocoducksItems;
import dev.emiller.mc.crocoducks.registries.CrocoducksSoundEvents;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Crocoducks.MODID)
public class Crocoducks {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "crocoducks";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Crocoducks(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        CrocoducksEntities.register(context);
        CrocoducksItems.register(context);
        CrocoducksSoundEvents.register(context);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("WGL HELLO from server starting");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DispenserBlock.registerBehavior(
                CrocoducksItems.CROCODUCK_EGG.get(), new AbstractProjectileDispenseBehavior() {
                    protected @NotNull Projectile getProjectile(
                            @NotNull Level p_123476_,
                            @NotNull Position p_123477_,
                            @NotNull ItemStack p_123478_
                    ) {
                        return Util.make(
                                new CrocoduckEggEntity(p_123476_, p_123477_.x(), p_123477_.y(), p_123477_.z()),
                                (p_123474_) -> p_123474_.setItem(p_123478_)
                        );
                    }
                }
        );
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
        public static void onSpawnZonesRegistration(SpawnPlacementRegisterEvent event) {
            CrocoducksEntities.registerSpawnRules(event);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
