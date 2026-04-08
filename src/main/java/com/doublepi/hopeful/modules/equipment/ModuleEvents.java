package com.doublepi.hopeful.modules.equipment;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.registries.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

@EventBusSubscriber(modid = HopefulMod.MODID)
public class ModuleEvents {
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
