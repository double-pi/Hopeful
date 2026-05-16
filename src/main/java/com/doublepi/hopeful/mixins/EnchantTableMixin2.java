package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.equipment.enchanting.EnchantingTableChanges;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
abstract class EnchantTableMixin2 {
    @Inject(method = "useItemOn", at = @At("TAIL"), cancellable = true)
    private void newFunctionality(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!((Object) this instanceof EnchantingTableBlock)) return;
        cir.setReturnValue(EnchantingTableChanges.useItemOn(stack, blockState, level, pos, player, hand, hitResult));

    }
}
