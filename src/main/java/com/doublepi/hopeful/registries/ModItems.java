package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.modules.equipment.scrolls.ScrollItem;
import com.doublepi.hopeful.modules.equipment.scrolls.UnknownScrollItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HopefulMod.MODID);

    public static final DeferredItem<Item> SCROLL = ITEMS.register("scroll",
            ()-> new ScrollItem(new Item.Properties()));

    public static final DeferredItem<Item> UNKNOWN_SCROLL = ITEMS.register("unknown_scroll",
            ()-> new UnknownScrollItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
