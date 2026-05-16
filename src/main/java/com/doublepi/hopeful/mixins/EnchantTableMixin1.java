package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.equipment.enchanting.EnchantingTableChanges;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantingTableBlock.class)
abstract class EnchantTableMixin1 {
    @Inject(method = "useWithoutItem", at= @At("HEAD"), cancellable = true)
    private void noMenu(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir){
        cir.setReturnValue(InteractionResult.FAIL);
        cir.cancel();
    }

    @Inject(method="animateTick", at = @At("HEAD"), cancellable = true)
    private void animateColored(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci){
        EnchantingTableChanges.catalystParticles(level, pos, random);
        ci.cancel();
    }
}