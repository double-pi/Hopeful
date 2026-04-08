package com.doublepi.hopeful.mixins.equipment;

import com.doublepi.hopeful.modules.equipment.scrolls.ScrollHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneMenu.class)
public class GrindstoneUpdate {
    @Inject(method="removeNonCursesFrom",at=@At("HEAD"),cancellable = true)
    private void removeAllEnchants(ItemStack item, CallbackInfoReturnable<ItemStack> cir){
        if (item.is(Items.ENCHANTED_BOOK)) {
            item = item.transmuteCopy(Items.BOOK);
        }
        item.set(DataComponents.REPAIR_COST, 0);
        ScrollHelper.setScore(item,0);
        item.remove(DataComponents.STORED_ENCHANTMENTS);
        item.set(DataComponents.ENCHANTMENTS,ItemEnchantments.EMPTY);

        cir.setReturnValue(item);
        cir.cancel();
    }
}
