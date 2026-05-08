package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.Catalyst;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.CatalystEffect;
import com.doublepi.hopeful.modules.equipment.scrolls.Scroll;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ModRegistries {
    public static final ResourceKey<Registry<Scroll>> SCROLL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(
                    HopefulMod.MODID, "scroll"));

    public static final ResourceKey<Registry<Catalyst>> CATALYST_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(
                    HopefulMod.MODID, "catalyst"));

    public static final ResourceKey<Registry<CatalystEffect.Type<?>>> CATALYST_EFFECT_TYPE_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(
                    HopefulMod.MODID, "catalyst_effect_types"));
    public static final Registry<CatalystEffect.Type<?>> CATALYST_EFFECT_TYPE_REGISTRY =
            new RegistryBuilder<>(CATALYST_EFFECT_TYPE_KEY)
                    .maxId(256).create();

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(SCROLL_REGISTRY_KEY, Scroll.CODEC, Scroll.CODEC, builder -> builder.maxId(512).sync(true));
        event.dataPackRegistry(CATALYST_REGISTRY_KEY, Catalyst.CODEC, Catalyst.CODEC, builder -> builder.maxId(512).sync(true));
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event){
        event.register(CATALYST_EFFECT_TYPE_REGISTRY);
    }
}
