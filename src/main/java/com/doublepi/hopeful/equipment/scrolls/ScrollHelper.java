package com.doublepi.hopeful.equipment.scrolls;

import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModEventBusEvents;
import com.doublepi.hopeful.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ScrollHelper {

    public static void enchant(ItemStack item, Scroll scroll){
        for(Holder<Enchantment> enchantment : scroll.enchantments()){
            boolean itemSupportsEnchantment = item.supportsEnchantment(enchantment);
            boolean isNotMaxLevel = item.getEnchantmentLevel(enchantment)< enchantment.value().getMaxLevel();
            if(itemSupportsEnchantment && isNotMaxLevel
                    && getScore(item) + scroll.requiredToolXP() <= getMaxScore(item)){
                int newLevel = item.getEnchantmentLevel(enchantment) + 1;
                item.enchant(enchantment, newLevel);
            }
        }
        setScore(item, getScore(item) + scroll.requiredToolXP());
    }

    public static Stream<Holder.Reference<Scroll>> getAllScrolls(Level level){
        return level.holderLookup(ModRegistries.SCROLL_REGISTRY_KEY).listElements();
    }

    public static boolean supportsScroll(ItemStack item, Scroll scroll){
        int maxScore = getMaxScore(item);
        int currentScore = getScore(item);
        if(maxScore == 0)
            return false;

        for(Holder<Enchantment> enchantment : scroll.enchantments()){
            if(!item.supportsEnchantment(enchantment))
                continue;

            if(item.getEnchantmentLevel(enchantment) >= enchantment.value().getMaxLevel())
                continue;

            if(currentScore + scroll.requiredToolXP() <= maxScore)
                return true;
        }

        return false;
    }

    public static int getMaxScore(ItemStack stack){
        Holder<Item> item = stack.getItemHolder();
        if(item.getData(ModEventBusEvents.ITEM_ENCHANTABILITY_DATA) == null){
            if(!item.is(Tags.Items.ENCHANTABLES))
                return 0;
            int enchantability = stack.getEnchantmentValue();
            if(stack.getMaxStackSize()!=1) return 0;
            if(enchantability==0) return 5;
            return (int)(enchantability * 0.5);
        }else{
        return item.getData(ModEventBusEvents.ITEM_ENCHANTABILITY_DATA).enchantability();
}
    }

    public static int getScore(ItemStack stack){
        if(!stack.has(ModDataComponentTypes.ENCHANTABILITY_STATUS)){
            stack.set(ModDataComponentTypes.ENCHANTABILITY_STATUS,0);
        }
        return stack.get(ModDataComponentTypes.ENCHANTABILITY_STATUS);
    }

    public static void setScore(ItemStack stack, int value){
        stack.set(ModDataComponentTypes.ENCHANTABILITY_STATUS,Math.max(0,value));
    }

    public static Holder<Scroll> getFromEnchant(Holder<Enchantment> holder, Level level){
        var scrolls = getAllScrolls(level).toList();
        for (int i = 0; i < scrolls.size(); i++) {
            if(scrolls.get(i).value().enchantments().contains(holder)) return scrolls.get(i);
        }
        return null;
    }

    public static void addOrSpawn(Entity entity, ArrayList<ItemStack> stacks){
        if(!(entity instanceof Player p)) {
            stacks.forEach(entity::spawnAtLocation);
            return;
        }

        stacks.forEach(stack->{
            boolean success = p.getInventory().add(stack);
            if(!success) {
                entity.spawnAtLocation(stack).setNoPickUpDelay();
            }
        });
    }


    public static int evalStatus(ItemStack stack, Level level){
        if(!stack.isEnchanted()) return 0;
        var enchants = stack.get(DataComponents.ENCHANTMENTS);
        assert enchants != null;
        var enchantList = enchants.keySet().stream().toList();
        int status = 0;
        for (Holder<Enchantment> enchant: enchantList){
            status += ScrollHelper.getFromEnchant(enchant, level).value().requiredToolXP() * stack.getEnchantmentLevel(enchant);
        }
        return status;
    }
}
