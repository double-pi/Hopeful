package com.doublepi.hopeful.equipment.scrolls;

import com.doublepi.hopeful.equipment.smithing.ToolLevelHelper;
import com.doublepi.hopeful.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ScrollHelper {

    public static void enchant(ItemStack item, Scroll scroll){
        for(Holder<Enchantment> enchantment : scroll.enchantments()){
            boolean itemSupportsEnchantment = item.supportsEnchantment(enchantment);
            boolean isNotMaxLevel = item.getEnchantmentLevel(enchantment)< enchantment.value().getMaxLevel();
            if(itemSupportsEnchantment && isNotMaxLevel
                    && ToolLevelHelper.getUsedLevels(item) + scroll.requiredToolXP() <= ToolLevelHelper.getCurrentLevel(item)){
                int newLevel = item.getEnchantmentLevel(enchantment) + 1;
                item.enchant(enchantment, newLevel);
            }
        }
        ToolLevelHelper.setUsedLevels(item, ToolLevelHelper.getUsedLevels(item) + scroll.requiredToolXP());
    }

    public static Stream<Holder.Reference<Scroll>> getAllScrolls(Level level){
        return level.holderLookup(ModRegistries.SCROLL_REGISTRY_KEY).listElements();
    }

    public static boolean supportsScroll(ItemStack item, Scroll scroll){
        int maxScore = ToolLevelHelper.getCurrentLevel(item);
        int currentScore = ToolLevelHelper.getUsedLevels(item);
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

    public static Holder<Scroll> getFromEnchant(Holder<Enchantment> holder, Level level){
        var scrolls = getAllScrolls(level).toList();
        for (Holder.Reference<Scroll> scroll : scrolls) {
            if (scroll.value().enchantments().contains(holder)) return scroll;
        }
        return null;
    }

    public static void addOrSpawn(Entity entity, ArrayList<ItemStack> stacks){
        stacks.forEach(stack-> addOrSpawn(entity, stack));
    }
    public static void addOrSpawn(Entity entity, ItemStack stack){
        if(!(entity instanceof Player p))
            return;
        boolean success = p.getInventory().add(stack);
        if(!success) {
            entity.spawnAtLocation(stack).setNoPickUpDelay();
        }
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
