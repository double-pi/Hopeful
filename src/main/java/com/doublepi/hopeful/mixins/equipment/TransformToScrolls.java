package com.doublepi.hopeful.mixins.equipment;

import com.doublepi.hopeful.modules.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.modules.equipment.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(Item.class)
public class TransformToScrolls {
    @Inject(method="inventoryTick",at=@At("HEAD"))
    public void removingBooks(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, CallbackInfo ci){
        if(level.isClientSide)
            return;
        if(!(stack.has(DataComponents.STORED_ENCHANTMENTS)))
            return;


        var enchants = stack.get(DataComponents.STORED_ENCHANTMENTS);
        var listOfScrolls = new ArrayList<ItemStack>();

        assert enchants != null;
        ItemEnchantments.Mutable remaining = new ItemEnchantments.Mutable(enchants);

        for (Holder<Enchantment> enchantHolder : enchants.keySet()) {
            var scroll = ScrollHelper.getFromEnchant(enchantHolder, level);
            if(scroll == null)
                continue;
            int enchantLevel = enchants.getLevel(enchantHolder);
            for (int i = 0; i < enchantLevel ; i++) {
                listOfScrolls.add(ScrollItem.createFromScroll(scroll));
            }

            remaining.set(enchantHolder, 0);
        }

        if(remaining.keySet().isEmpty())
            stack.setCount(0);
        else
            stack.set(DataComponents.STORED_ENCHANTMENTS,remaining.toImmutable());

        ScrollHelper.addOrSpawn(entity,listOfScrolls);

    }

    @Inject(method="inventoryTick", at=@At("HEAD"))
    public void fixTools(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, CallbackInfo ci){
        if(level.isClientSide()) return;
        if(!stack.isEnchanted()){
            stack.remove(ModDataComponentTypes.ENCHANTABILITY_STATUS);
            return;
        }
        if(stack.has(ModDataComponentTypes.ENCHANTABILITY_STATUS))
            return;


        var enchantObj = stack.get(DataComponents.ENCHANTMENTS);
        assert enchantObj != null;
        ItemEnchantments.Mutable remaining = new ItemEnchantments.Mutable(enchantObj);
        var listOfScrolls = new ArrayList<ItemStack>();

        for (Holder<Enchantment> enchantHolder : enchantObj.keySet()) {
            var scroll = ScrollHelper.getFromEnchant(enchantHolder, level);
            if(scroll == null)
                continue;
            int enchantLevel = enchantObj.getLevel(enchantHolder);
            for (int i = 0; i < enchantLevel ; i++) {
                listOfScrolls.add(ScrollItem.createFromScroll(scroll));
            }

            remaining.set(enchantHolder, 0);
        }

        stack.set(DataComponents.ENCHANTMENTS,remaining.toImmutable());
        ScrollHelper.addOrSpawn(entity,listOfScrolls);
    }


}
