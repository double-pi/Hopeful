package com.doublepi.hopeful.equipment.smithing;

import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModEvents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class ToolLevelHelper {
    public static int getDefaultLevel(ItemStack stack){
        Holder<Item> item = stack.getItemHolder();
        if(item.getData(ModEvents.ITEM_ENCHANTABILITY_DATA) == null){
            if(!item.is(Tags.Items.ENCHANTABLES))
                return 0;
            int enchantability = stack.getEnchantmentValue();
            if(stack.getMaxStackSize()!=1) return 0;
            if(enchantability==0) return 5;
            return (int)(enchantability * 0.5);
        }else{
            return item.getData(ModEvents.ITEM_ENCHANTABILITY_DATA).startingLevel();
        }
    }

    public static void updateDataComponents(ItemStack stack){
        stack.set(ModDataComponentTypes.ADDED_TOOL_LEVEL, getCurrentLevel(stack) - getDefaultLevel(stack));
        stack.set(ModDataComponentTypes.ENCHANTABILITY_STATUS, getUsedLevels(stack));
    }
    public static int getCurrentLevel(ItemStack stack){
        return stack.getOrDefault(ModDataComponentTypes.ADDED_TOOL_LEVEL, 0) +getDefaultLevel(stack);
    }
    public static int getUsedLevels(ItemStack stack){
        return stack.getOrDefault(ModDataComponentTypes.ENCHANTABILITY_STATUS,0);
    }
    public static void setUsedLevels(ItemStack stack, int value){
        stack.set(ModDataComponentTypes.ENCHANTABILITY_STATUS,Math.max(0,value));
    }

    public static void updateToolProgress(ItemStack stack, Entity entity){
        Enchantability enchantability = stack.getItemHolder().getData(ModEvents.ITEM_ENCHANTABILITY_DATA);
        if(enchantability == null) return;
        if(enchantability.levelups().isEmpty()) return;
        // increase progress
        int currentProgress = stack.getOrDefault(ModDataComponentTypes.LEVELUP_PROGRESS,0);
        stack.set(ModDataComponentTypes.LEVELUP_PROGRESS, currentProgress + 1);

        updateDataComponents(stack);
        // get starting level
        int startingToolLevel = getDefaultLevel(stack);
        int currentToolLevel = getCurrentLevel(stack);


        List<Integer> levelUpMilestones = enchantability.levelups();
        List<Integer> subList = levelUpMilestones.subList(0, currentToolLevel - startingToolLevel + 1);
        int requirement = subList.stream().mapToInt(a->a).sum();
        if(currentProgress +1 >= requirement){
            entity.sendSystemMessage(Component.translatable("tooltip.hopeful.tool_leveled_up", stack.getDisplayName(), currentToolLevel));
            stack.set(ModDataComponentTypes.ADDED_TOOL_LEVEL, currentToolLevel - startingToolLevel + 1);
        }
    }
}
