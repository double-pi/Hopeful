package com.doublepi.hopeful.modules.difficulty;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.registries.ModGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;

@EventBusSubscriber(modid = HopefulMod.MODID)
public class ModuleEvents {

    @SubscribeEvent
    public static void saplingReplant(ItemExpireEvent event){
        ItemEntity itemEntity = event.getEntity();

        if(itemEntity.level().isClientSide()) return;
        ServerLevel level = (ServerLevel) itemEntity.level();
        if(!level.getGameRules().get(ModGamerules.SAPLINGS_REPLACE.value()))
            return;
        if(!itemEntity.getItem().is(ItemTags.SAPLINGS))
            return;
        BlockPos pos = event.getEntity().getOnPos();

        if(!level.getBlockState(pos).is(BlockTags.SUPPORTS_VEGETATION))
            return;
        if(!level.getBlockState(pos.above()).is(BlockTags.REPLACEABLE))
            return;
        Item saplingItem = itemEntity.getItem().getItem();
        if(saplingItem instanceof BlockItem blockItem) {
            level.setBlockAndUpdate(pos.above(), blockItem.getBlock().defaultBlockState());
        }
    }

}