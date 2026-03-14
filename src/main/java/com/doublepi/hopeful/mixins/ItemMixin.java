package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.content.scrolls.ScrollHelper;
import com.doublepi.hopeful.content.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModResourceRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method="inventoryTick",at=@At("HEAD"))
    public void removingBooks(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, CallbackInfo ci){
        if(!(stack.getItem() instanceof EnchantedBookItem))
            return;
        if(level.isClientSide)
            return;

        var enchants = stack.get(DataComponents.STORED_ENCHANTMENTS);
        var allScrolls = ScrollHelper.getAllScrolls(level).toList();
        var listOfScrolls = new ArrayList<ItemStack>();

        assert enchants != null;
        ItemEnchantments.Mutable remaining = new ItemEnchantments.Mutable(enchants);

        enchants.keySet().forEach(enchantmentHolder ->
            allScrolls.forEach(scrollReference -> {

            if(scrollReference.value().enchantments().contains(enchantmentHolder)){
                for (int i = 0; i < enchants.getLevel(enchantmentHolder); i++) {
                    listOfScrolls.add(ScrollItem.createFromScroll(scrollReference));
                    remaining.set(enchantmentHolder,remaining.getLevel(enchantmentHolder)-1);
                }
            }
        }));
        if(remaining.keySet().isEmpty())
            stack.setCount(0);
        else
            stack.set(DataComponents.STORED_ENCHANTMENTS,remaining.toImmutable());

        ScrollHelper.addOrSpawn(entity,listOfScrolls);

    }

    @Inject(method="inventoryTick", at=@At("HEAD"))
    public void fixTools(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, CallbackInfo ci){
        if(!(stack.has(DataComponents.ENCHANTMENTS)) || stack.has(ModDataComponentTypes.ENCHANTABILITY_STATUS))
            return;
        if(level.isClientSide)
            return;

        var enchants = stack.get(DataComponents.ENCHANTMENTS);
        var allScrolls = ScrollHelper.getAllScrolls(level).toList();
        var listOfScrolls = new ArrayList<ItemStack>();

        assert enchants != null;
        ItemEnchantments.Mutable remaining = new ItemEnchantments.Mutable(enchants);

        enchants.keySet().forEach(enchantmentHolder ->
            allScrolls.forEach(scrollReference -> {
                    if(scrollReference.value().enchantments().contains(enchantmentHolder)){
                        for (int i = 0; i < enchants.getLevel(enchantmentHolder); i++) {
                            listOfScrolls.add(ScrollItem.createFromScroll(scrollReference));
                            remaining.set(enchantmentHolder,remaining.getLevel(enchantmentHolder)-1);
                        }
                    }
                }));
        stack.set(DataComponents.ENCHANTMENTS,remaining.toImmutable());
        ScrollHelper.addOrSpawn(entity,listOfScrolls);
    }


}
