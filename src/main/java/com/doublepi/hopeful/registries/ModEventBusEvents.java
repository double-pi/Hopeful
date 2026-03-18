package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.content.mourner.MournerEntity;
//import com.doublepi.hopeful.content.mourner.MournerModel;
import com.doublepi.hopeful.content.smithing.Enchantability;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = HopefulMod.MODID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
//        event.registerLayerDefinition(ModModelLayers.MOURNER, MournerModel::createBodyLayer);
//        event.registerLayerDefinition(ModModelLayers.MOURNER_OUTER, MournerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        //event.put(ModEntities.MOURNER.get(), MournerEntity.createAttributes().build());
    }

    public static final DataMapType<Item, Enchantability> ITEM_ENCHANTABILITY_DATA = DataMapType.builder(
            Identifier.fromNamespaceAndPath(HopefulMod.MODID, "enchantability"),
            Registries.ITEM,
            Enchantability.CODEC
        ).synced(Enchantability.CODEC, true).build();

    @SubscribeEvent
    public static void registerDataMap(RegisterDataMapTypesEvent e){
        e.register(ITEM_ENCHANTABILITY_DATA);
    }
}
