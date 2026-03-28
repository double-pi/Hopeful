package com.doublepi.hopeful.content.events;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.registries.ModGamerules;
import com.doublepi.hopeful.registries.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

@EventBusSubscriber(modid = HopefulMod.MODID)
public class GameEvents {

    @SubscribeEvent
    public static void saplingReplant(ItemExpireEvent event){
        ItemEntity itemEntity = event.getEntity() ;
        if(!itemEntity.getServer().getGameRules().getBoolean(ModGamerules.SAPLINGS_REPLACE))
            return;
        if(!itemEntity.getItem().is(ItemTags.SAPLINGS))
            return;
        BlockPos pos = event.getEntity().getOnPos();
        Level level = event.getEntity().level();
        if(!level.getBlockState(pos).is(BlockTags.DIRT))
            return;
        if(!level.getBlockState(pos.above()).is(BlockTags.REPLACEABLE))
            return;
        Item saplingItem = itemEntity.getItem().getItem();
        if(saplingItem instanceof BlockItem blockItem) {
            level.setBlockAndUpdate(pos.above(), blockItem.getBlock().defaultBlockState());
        }
    }

    @SubscribeEvent
    public static void removeRepairCost(AnvilRepairEvent event){
        event.getOutput().remove(DataComponents.REPAIR_COST);
    }

    @SubscribeEvent
    public static void repairAnvil(UseItemOnBlockEvent event){
        ItemStack itemStack = event.getItemStack();
        if(itemStack.is(ModTags.Items.ANVIL_MENDS) && event.getPlayer().isCrouching()) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getPlayer();
            BlockState state = level.getBlockState(pos);
            boolean flag = false;
            if (!level.isClientSide() && state.is(Blocks.CHIPPED_ANVIL)){
                level.destroyBlock(pos,false);
                level.setBlockAndUpdate(pos,Blocks.ANVIL.withPropertiesOf(state));
                flag = true;
            }
            if (!level.isClientSide() && state.is(Blocks.DAMAGED_ANVIL)){
                level.destroyBlock(pos,false);
                level.setBlockAndUpdate(pos,Blocks.CHIPPED_ANVIL.withPropertiesOf(state));
                flag = true;
            }
            if(flag){
                player.playSound(SoundEvents.ANVIL_PLACE,0.5F,
                        0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                player.getCooldowns().addCooldown(itemStack.getItem(), 10);
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                itemStack.consume(1, player);
            }
        }
    }


}
