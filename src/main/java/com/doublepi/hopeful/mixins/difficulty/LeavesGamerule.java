package com.doublepi.hopeful.mixins.difficulty;

import com.doublepi.hopeful.registries.ModGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeavesGamerule {

    @Shadow protected abstract boolean decaying(BlockState state);

    @Inject(method = "tick", at = @At("RETURN"))
    public void tickInjected(BlockState state, ServerLevel level, BlockPos pos,
                             RandomSource random, CallbackInfo ci){
        //Credit to Levaltru on bsky for the idea!
        boolean leavesFall = level.getGameRules().getBoolean(ModGamerules.LEAVES_FALL);
        if(this.decaying(state) && leavesFall && FallingBlock.isFree(level.getBlockState(pos.below())))
            FallingBlockEntity.fall(level,pos,state);
    }
}
