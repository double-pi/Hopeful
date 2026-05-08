package com.doublepi.hopeful.mixins.equipment;

import com.doublepi.hopeful.modules.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.CatalystHelper;
import com.doublepi.hopeful.modules.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.modules.equipment.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
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

import java.util.ArrayList;

@Mixin(BlockBehaviour.class)
abstract class ChangeEnchantTable {
    @Inject(method = "useItemOn", at = @At("TAIL"), cancellable = true)
    private void newFunctionality(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!((Object) this instanceof EnchantingTableBlock)) return;
        cir.setReturnValue(CatalystHelper.enchantTableFunctionality(stack, blockState, level, pos, player, hand, hitResult));

    }
}
