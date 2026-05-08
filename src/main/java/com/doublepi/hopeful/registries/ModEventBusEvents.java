package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.equipment.smithing.Enchantability;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = HopefulMod.MODID)
public class ModEventBusEvents {

    public static final DataMapType<Item, Enchantability> ITEM_ENCHANTABILITY_DATA = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "enchantability"),
            Registries.ITEM,
            Enchantability.CODEC
        ).synced(Enchantability.CODEC, true).build();

    @SubscribeEvent
    public static void registerDataMap(RegisterDataMapTypesEvent e){
        e.register(ITEM_ENCHANTABILITY_DATA);
    }
}
