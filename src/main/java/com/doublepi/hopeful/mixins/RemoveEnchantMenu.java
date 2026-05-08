package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.equipment.enchanting.catalyst.CatalystHelper;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
@Mixin(EnchantingTableBlock.class)
abstract class RemoveEnchantMenu {
    @Inject(method = "useWithoutItem", at= @At("HEAD"), cancellable = true)
    private void noMenu(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir){
        cir.setReturnValue(InteractionResult.FAIL);
        cir.cancel();
    }

    @Inject(method="animateTick", at = @At("HEAD"), cancellable = true)
    private void animateColored(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci){
        List<BlockPos> posStream = CatalystHelper.getUsedCatalysts(level, pos, 0).toList();
        posStream.forEach(catPos->{
            Holder<Block> block = level.getBlockState(catPos).getBlockHolder();
            var catalyst = CatalystHelper.getCatalystFromBlock(block,level,new EnchantingState(0));
            catalyst.ifPresent(cat->{
                for(CatalystEffect e : cat.value().effects()){
                    if (random.nextInt(2) == 0) {
                        level.addParticle(e.getParticle(),
                                catPos.getX() + 0.5f,
                                catPos.getY() + 0.5f,
                                catPos.getZ() + 0.5f,
                                (pos.getX()-catPos.getX())/10f,
                                (pos.getY()-catPos.getY())/10f,
                                (pos.getZ()-catPos.getZ())/10f);
                    }
                }

            });
        });
        ci.cancel();
    }
}