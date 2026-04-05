package com.doublepi.hopeful.mixins.enchanting;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AnvilMenu.class)
public class AnvilTweaks {
    @ModifyArg(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"))
    public int fullRepair(int i){
        return 0; // Thanks for Fabrication for the help!
    }

    @ModifyArgs(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
    public void removeRepairCost(Args args){
        args.set(1,0);
    }
}
