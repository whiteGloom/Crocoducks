package dev.emiller.mc.crocoducks.registries;

import dev.emiller.mc.crocoducks.Crocoducks;
import dev.emiller.mc.crocoducks.item.CrocoduckEggItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CrocoducksItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Crocoducks.MODID);
    public static final DeferredHolder<Item, CrocoduckEggItem> CROCODUCK_EGG = ITEMS.register(
            "crocoduck_egg",
            () -> new CrocoduckEggItem(
                    new Item.Properties().stacksTo(
                            16))
    );

    public static final DeferredHolder<Item, DeferredSpawnEggItem> CROCODUCK_SPAWN_EGG = ITEMS.register(
            "crocoduck_spawn_egg",
            () -> new DeferredSpawnEggItem(
                    CrocoducksEntities.CROCODUCK,
                    0x3A5F3A,
                    0xA2C858,
                    new Item.Properties().stacksTo(16)
            )
    );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
