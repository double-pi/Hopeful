package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.content.scrolls.ScrollHelper;
import com.doublepi.hopeful.content.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method="inventoryTick", at=@At("HEAD"))
    public void fixBooks(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot, CallbackInfo cb) {
        if(level.isClientSide())
            return;
        if(!stack.has(DataComponents.STORED_ENCHANTMENTS))
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
    public void fixTools(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot, CallbackInfo cb) {
        if(!(stack.has(DataComponents.ENCHANTMENTS)) || stack.has(ModDataComponentTypes.ENCHANTABILITY_STATUS))
            return;
        if(level.isClientSide())
            return;

        var enchants = stack.get(DataComponents.ENCHANTMENTS);
        assert enchants != null;
        var enchantList = enchants.keySet().stream().toList();
        ItemEnchantments.Mutable remaining = new ItemEnchantments.Mutable(enchants);

        var listOfScrolls = new ArrayList<ItemStack>();

        int enchantability = ScrollHelper.getMaxScore(stack);
        int currentScore = 0;

        boolean overloaded = false;
        for (Holder<Enchantment> enchantHolder : enchantList) {
            var scroll = ScrollHelper.getFromEnchant(enchantHolder, level);
            for (int i = 0; i < enchants.getLevel(enchantHolder); i++) {
                if(currentScore + scroll.value().scorePerLevel() > enchantability || overloaded) {
                    overloaded = true;
                    listOfScrolls.add(ScrollItem.createFromScroll(scroll));
                    remaining.set(enchantHolder, remaining.getLevel(enchantHolder)-1);
                }else{
                    currentScore += scroll.value().scorePerLevel();
                }
            }
        }
        stack.set(DataComponents.ENCHANTMENTS,remaining.toImmutable());
        stack.set(ModDataComponentTypes.ENCHANTABILITY_STATUS,currentScore);
        ScrollHelper.addOrSpawn(entity,listOfScrolls);
    }


}
