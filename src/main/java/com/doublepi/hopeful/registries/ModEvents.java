package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.equipment.smithing.Enchantability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = HopefulMod.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void syncXPLevels(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        int oldValue = player.hasData(ModAttachments.XP_PER_LEVEL)? player.getData(ModAttachments.XP_PER_LEVEL) : 64;
        int newValue = player.level().getGameRules().getInt(ModGamerules.XP_PER_LEVEL);
        if(oldValue==newValue) return;

        int totalXP = Math.round((player.experienceLevel + player.experienceProgress) * oldValue);
        player.setData(ModAttachments.XP_PER_LEVEL, newValue);
        player.experienceLevel = totalXP / newValue;
        player.experienceProgress = (1.0f*totalXP % newValue) / newValue;
    }
    @SubscribeEvent
    public static void repairAnvil(UseItemOnBlockEvent event){
        ItemStack itemStack = event.getItemStack();
        if(itemStack.is(ModTags.ANVIL_MENDS) && event.getPlayer().isCrouching()) {
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

    public static final DataMapType<Item, Enchantability> ITEM_ENCHANTABILITY_DATA = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "equipment"),
            Registries.ITEM,
            Enchantability.CODEC
    ).synced(Enchantability.CODEC, true).build();

    @SubscribeEvent
    public static void registerDataMap(RegisterDataMapTypesEvent e){
        e.register(ITEM_ENCHANTABILITY_DATA);
    }
}
