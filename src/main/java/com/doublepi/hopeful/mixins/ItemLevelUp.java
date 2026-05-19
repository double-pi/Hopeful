package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.equipment.level_up.ToolLevelHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemLevelUp {
    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", at= @At("HEAD"))
    public void updateToolProgress(int dmg, ServerLevel level, LivingEntity entity, Consumer<Item> p_348596_, CallbackInfo ci){
        ToolLevelHelper.updateToolProgress((ItemStack) ((Object)this), entity);
    }


}
