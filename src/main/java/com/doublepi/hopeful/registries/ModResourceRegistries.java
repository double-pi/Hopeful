package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.modules.enchanting.scrolls.Scroll;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class ModResourceRegistries {
    public static final ResourceKey<Registry<Scroll>> SCROLL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(
                    HopefulMod.MODID, "scroll"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(SCROLL_REGISTRY_KEY, Scroll.CODEC, Scroll.CODEC, builder -> builder.maxId(512).sync(true));
    }
}
