package dev.emiller.mc.crocoducks.registries;


import dev.emiller.mc.crocoducks.Crocoducks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrocoducksSoundEvents {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(
            ForgeRegistries.SOUND_EVENTS,
            Crocoducks.MODID
    );
    public static final RegistryObject<SoundEvent> ENTITY_CROCODUCK_AMBIENT = SOUND_EVENTS.register(
            "entities.crocoduck.ambient",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Crocoducks.MODID, "entities.crocoduck.ambient"))
    );
    public static final RegistryObject<SoundEvent> ENTITY_CROCODUCK_BABY = SOUND_EVENTS.register(
            "entities.crocoduck.baby",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Crocoducks.MODID, "entities.crocoduck.baby"))
    );
    public static final RegistryObject<SoundEvent> ENTITY_CROCODUCK_BITE = SOUND_EVENTS.register(
            "entities.crocoduck.bite",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Crocoducks.MODID, "entities.crocoduck.bite"))
    );
    public static final RegistryObject<SoundEvent> ENTITY_CROCODUCK_DEATH = SOUND_EVENTS.register(
            "entities.crocoduck.death",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Crocoducks.MODID, "entities.crocoduck.death"))
    );
    public static final RegistryObject<SoundEvent> ENTITY_CROCODUCK_HURT = SOUND_EVENTS.register(
            "entities.crocoduck.hurt",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Crocoducks.MODID, "entities.crocoduck.hurt"))
    );
    public static final RegistryObject<SoundEvent> ENTITY_CROCODUCK_WATER = SOUND_EVENTS.register(
            "entities.crocoduck.water",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Crocoducks.MODID, "entities.crocoduck.water"))
    );

    public static void register(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        SOUND_EVENTS.register(bus);
    }
}
