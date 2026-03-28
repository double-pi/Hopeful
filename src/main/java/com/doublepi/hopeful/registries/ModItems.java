package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.content.scrolls.ScrollItem;
import com.doublepi.hopeful.content.scrolls.UnknownScrollItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HopefulMod.MODID);

    public static final DeferredItem<Item> SCROLL = ITEMS.registerItem("scroll",
            ScrollItem::new);

    public static final DeferredItem<Item> UNKNOWN_SCROLL = ITEMS.registerItem("unknown_scroll",
            UnknownScrollItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
