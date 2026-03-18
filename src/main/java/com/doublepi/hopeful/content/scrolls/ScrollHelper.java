package com.doublepi.hopeful.content.scrolls;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.content.smithing.Enchantability;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModEventBusEvents;
import com.doublepi.hopeful.registries.ModResourceRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ScrollHelper {

    public static void enchant(ItemStack item, Scroll scroll){
        for(Holder<Enchantment> enchantment : scroll.enchantments()){
            boolean itemSupportsEnchantment = item.supportsEnchantment(enchantment);
            boolean isNotMaxLevel = item.getEnchantmentLevel(enchantment)< scroll.maxLevel();
            if(itemSupportsEnchantment && isNotMaxLevel && getScore(item) < getMaxScore(item)){
                int newLevel = item.getEnchantmentLevel(enchantment) + 1;
                item.enchant(enchantment, newLevel);
            }
        }
        setScore(item, getScore(item) + scroll.scorePerLevel());
    }

    public static Stream<Holder.Reference<Scroll>> getAllScrolls(Level level){
        return level.holderLookup(ModResourceRegistries.SCROLL_REGISTRY_KEY).listElements();
    }

    public static boolean supportsScroll(ItemStack item, Scroll scroll){
        int maxScore = getMaxScore(item);
        int currentScore = getScore(item);
        if(maxScore == 0)
            return false;

        for(Holder<Enchantment> enchantment : scroll.enchantments()){
            if(!item.supportsEnchantment(enchantment))
                continue;

            if(item.getEnchantmentLevel(enchantment) >= scroll.maxLevel())
                continue;

            if(currentScore + scroll.scorePerLevel() <= maxScore && currentScore + scroll.scorePerLevel() >=0)
                return true;
        }

        return false;
    }

    public static int getMaxScore(ItemStack stack){
        Holder<Item> item = stack.typeHolder();
        if(item.getData(ModEventBusEvents.ITEM_ENCHANTABILITY_DATA) == null){
            int enchantability = stack.get(DataComponents.ENCHANTABLE).value();
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
        stack.set(ModDataComponentTypes.ENCHANTABILITY_STATUS,value);
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
            stacks.forEach(stack -> {
                entity.spawnAtLocation((ServerLevel) entity.level(),stack);
            });
            return;
        }

        stacks.forEach(stack->{
            boolean success = p.getInventory().add(stack);
            if(!success) {
                entity.spawnAtLocation((ServerLevel) entity.level(),stack).setNoPickUpDelay();
            }
        });
    }

}
