package dev.emiller.mc.crocoducks.registries;

import dev.emiller.mc.crocoducks.Crocoducks;
import dev.emiller.mc.crocoducks.entity.CrocoduckEggEntity;
import dev.emiller.mc.crocoducks.entity.CrocoduckEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrocoducksEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES_TYPES = DeferredRegister.create(
            ForgeRegistries.ENTITY_TYPES,
            Crocoducks.MODID
    );
    public static final RegistryObject<EntityType<CrocoduckEntity>> CROCODUCK = ENTITIES_TYPES.register(
            "crocoduck", () -> EntityType.Builder.of(CrocoduckEntity::new, MobCategory.CREATURE)
                                                 .sized(0.4F, 0.7F)
                                                 .clientTrackingRange(10)
                                                 .build(Crocoducks.MODID + ":crocoduck")
    );
    public static final RegistryObject<EntityType<CrocoduckEggEntity>> CROCODUCK_EGG = ENTITIES_TYPES.register(
            "crocoduck_egg", () -> EntityType.Builder.<CrocoduckEggEntity>of(CrocoduckEggEntity::new, MobCategory.MISC)
                                                     .sized(0.25F, 0.25F)
                                                     .clientTrackingRange(4)
                                                     .updateInterval(10)
                                                     .build(Crocoducks.MODID + ":crocoduck_egg")
    );

    public static void register(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        ENTITIES_TYPES.register(bus);
    }

    public static void registerSpawnRules(SpawnPlacementRegisterEvent event) {
        event.register(
                CROCODUCK.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE
        );
    }
}
