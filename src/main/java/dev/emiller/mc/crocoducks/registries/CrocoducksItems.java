package dev.emiller.mc.crocoducks.registries;

import dev.emiller.mc.crocoducks.Crocoducks;
import dev.emiller.mc.crocoducks.item.CrocoduckEggItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrocoducksItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Crocoducks.MODID);
    public static final RegistryObject<Item> CROCODUCK_EGG = ITEMS.register(
            "crocoduck_egg",
            () -> new CrocoduckEggItem(new Item.Properties().stacksTo(
                    16))
    );

    public static final RegistryObject<Item> CROCODUCK_SPAWN_EGG = ITEMS.register(
            "crocoduck_spawn_egg",
            () -> new ForgeSpawnEggItem(
                    CrocoducksEntities.CROCODUCK,
                    0x3A5F3A,
                    0xA2C858,
                    new Item.Properties().stacksTo(16)
            )
    );

    public static void register(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        ITEMS.register(bus);
    }
}
