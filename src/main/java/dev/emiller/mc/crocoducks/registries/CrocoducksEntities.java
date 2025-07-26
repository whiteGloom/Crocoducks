package dev.emiller.mc.crocoducks.registries;

import dev.emiller.mc.crocoducks.Crocoducks;
import dev.emiller.mc.crocoducks.entity.CrocoduckEggEntity;
import dev.emiller.mc.crocoducks.entity.CrocoduckEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

public class CrocoducksEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES_TYPES = DeferredRegister.create(
            Registries.ENTITY_TYPE,
            Crocoducks.MODID
    );
    public static final DeferredHolder<EntityType<?>, EntityType<CrocoduckEntity>> CROCODUCK = ENTITIES_TYPES.register(
            "crocoduck", () -> EntityType.Builder.of(CrocoduckEntity::new, MobCategory.CREATURE)
                                                 .sized(0.4F, 0.7F)
                                                 .clientTrackingRange(10)
                                                 .build(Crocoducks.MODID + ":crocoduck")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<CrocoduckEggEntity>> CROCODUCK_EGG = ENTITIES_TYPES.register(
            "crocoduck_egg", () -> EntityType.Builder.<CrocoduckEggEntity>of(CrocoduckEggEntity::new, MobCategory.MISC)
                                                     .sized(0.25F, 0.25F)
                                                     .clientTrackingRange(4)
                                                     .updateInterval(10)
                                                     .build(Crocoducks.MODID + ":crocoduck_egg")
    );

    public static void register(IEventBus bus) {
        ENTITIES_TYPES.register(bus);
    }

    public static void registerSpawnRules(RegisterSpawnPlacementsEvent event) {
        event.register(
                CROCODUCK.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}
