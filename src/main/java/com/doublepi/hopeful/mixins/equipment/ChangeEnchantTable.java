package com.doublepi.hopeful.mixins.equipment;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.modules.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.modules.equipment.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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
    private void newFunctionality(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!((Object) this instanceof EnchantingTableBlock)) return;

        if(level.isClientSide())
            cir.setReturnValue(stack.is(ModTags.SCROLL_MATERIALS)? ItemInteractionResult.CONSUME : ItemInteractionResult.FAIL);
        else {
            if (!stack.is(ModTags.SCROLL_MATERIALS)) {
                player.displayClientMessage(Component.translatable("tooltip.hopeful.use_correct_material"), true);
                cir.setReturnValue(ItemInteractionResult.FAIL);
            }

            ScrollHelper.evaluateCatalysts(level, pos, player);
            var allScrolls = ScrollHelper.getAllScrolls(level).toList();
            ItemStack scrollItem = ScrollItem.createFromScroll(allScrolls.get((int) (Math.random() * allScrolls.size())));
            var stupidArray = new ArrayList<ItemStack>();
            stupidArray.add(scrollItem);
            ScrollHelper.addOrSpawn(player, stupidArray);

            stack.consume(1, player);
            player.makeSound(SoundEvents.ENCHANTMENT_TABLE_USE);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            cir.setReturnValue(ItemInteractionResult.CONSUME);
        }

    }
}
