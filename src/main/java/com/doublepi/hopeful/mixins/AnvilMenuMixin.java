package com.doublepi.hopeful.mixins;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @ModifyArg(method = "createResultInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"))
    public int fullRepair(int i){
        return 0; // Thanks for Fabrication for the help!
    }
}
