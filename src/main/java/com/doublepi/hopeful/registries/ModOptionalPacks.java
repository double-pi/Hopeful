package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import javax.annotation.Nonnull;

public class ModOptionalPacks {


    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event){
        if(event.getPackType() == PackType.SERVER_DATA){
            event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID,"datapacks/hopeful_unlimited"),
                    PackType.SERVER_DATA,
                    Component.translatable("dataPack.hopeful.hopeful_unlimited.name"),
                    createSource(false), false, Pack.Position.TOP);
//            event.addPackFinders(id("resourcepacks/villager_trades"), PackType.SERVER_DATA,
//                    Component.translatable("dataPack.enchiridion.villager_trades.name"),
//                    createSource(true), false, Pack.Position.TOP);
        }
    }

    public static PackSource createSource(boolean enabledByDefault) {
        return new PackSource() {
            @Override
            public @Nonnull Component decorate(@Nonnull Component component) {
                return Component.translatable("pack.hopeful.builtin", component);
            }

            @Override
            public boolean shouldAddAutomatically() {
                return enabledByDefault;
            }
        };
    }
}
