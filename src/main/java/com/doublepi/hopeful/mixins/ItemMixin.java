package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.content.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModResourceRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
        var allScrolls = level.holderLookup(ModResourceRegistries.SCROLL_REGISTRY_KEY).listElements().toList();
        var listOfScrolls = new ArrayList<ItemStack>();

        assert enchants != null;
        enchants.keySet().forEach(enchantmentHolder ->
            allScrolls.forEach(scrollReference -> {

            if(scrollReference.value().enchantments().contains(enchantmentHolder)){
                for (int i = 0; i < enchants.getLevel(enchantmentHolder); i++) {
                    listOfScrolls.add(ScrollItem.createFromScroll(scrollReference.value()));
                }
            }
        }));
        stack.setCount(0);
        listOfScrolls.forEach(entity::spawnAtLocation);

    }

    @Inject(method="inventoryTick", at=@At("HEAD"))
    public void fixTools(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, CallbackInfo ci){
        if(!(stack.has(DataComponents.ENCHANTMENTS)) || stack.has(ModDataComponentTypes.ENCHANTABILITY_STATUS))
            return;
        if(!(entity instanceof LivingEntity p))
            return;
        if(level.isClientSide)
            return;

        var enchants = stack.get(DataComponents.ENCHANTMENTS);
        var allScrolls = level.holderLookup(ModResourceRegistries.SCROLL_REGISTRY_KEY).listElements().toList();
        var listOfScrolls = new ArrayList<ItemStack>();

        assert enchants != null;
        enchants.keySet().forEach(enchantmentHolder ->
            allScrolls.forEach(scrollReference -> {
                    if(scrollReference.value().enchantments().contains(enchantmentHolder)){
                        for (int i = 0; i < enchants.getLevel(enchantmentHolder); i++) {
                            listOfScrolls.add(ScrollItem.createFromScroll(scrollReference.value()));
                        }
                    }
                }));
        stack.set(DataComponents.ENCHANTMENTS,ItemEnchantments.EMPTY);
        listOfScrolls.forEach(entity::spawnAtLocation);
    }
}
