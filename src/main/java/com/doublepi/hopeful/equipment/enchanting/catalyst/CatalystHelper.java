package com.doublepi.hopeful.equipment.enchanting.catalyst;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.equipment.scrolls.Scroll;
import com.doublepi.hopeful.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.equipment.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModAttachments;
import com.doublepi.hopeful.registries.ModRegistries;
import com.doublepi.hopeful.registries.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Optional;

public class CatalystHelper {
    public static Optional<Holder.Reference<Catalyst>> getCatalystFromBlock(Holder<Block> block, Level level){
        var stream = level.holderLookup(ModRegistries.CATALYST_REGISTRY_KEY).listElements();
        var reduced = stream.filter(c->c.value().blocks().contains(block));
        return reduced.findFirst();
    }

    public static EnchantingState evaluateEnchantingState(Level level, BlockPos pos, Player player) {
        AABB area = AABB.ofSize(pos.getCenter(), 5, 5, 5);
        EnchantingState state = new EnchantingState(level, player.getData(ModAttachments.HOPEFUL_ENCHANT_SEED));

        level.getBlockStates(area).forEach( blockState -> {
            Holder<Block> block = blockState.getBlockHolder();
            var catalyst = getCatalystFromBlock(block, level);
            catalyst.ifPresent(catalystReference ->{
                state.evaluateCatalyst(catalystReference.value());
            });
        });
        return state;
    }


    public static ItemInteractionResult enchantTableFunctionality(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult){
        if (!stack.is(ModTags.SCROLL_MATERIALS)) {
            player.displayClientMessage(Component.translatable("tooltip.hopeful.use_correct_material"), true);
            return ItemInteractionResult.FAIL;
        }else {
            EnchantingState state = CatalystHelper.evaluateEnchantingState(level, pos, player);
            String failReasonKey = state.findEnchantFailReason(player);
            if (failReasonKey == null) { // generate scroll
                ItemStack scrollItem = ScrollItem.createFromScroll(generateScroll(state));
                var stupidArray = new ArrayList<ItemStack>();
                stupidArray.add(scrollItem);
                ScrollHelper.addOrSpawn(player, stupidArray);
                player.makeSound(SoundEvents.ENCHANTMENT_TABLE_USE);
            }else{ // show reason
                player.displayClientMessage(
                        Component.translatable("tooltip.hopeful.enchant_failed").append(
                                Component.translatable(failReasonKey)), true);
                player.makeSound(SoundEvents.WAXED_SIGN_INTERACT_FAIL);
            }

            stack.consume(1, player);
            player.onEnchantmentPerformed(stack, state.consumedXPLevels);
            player.setData(ModAttachments.HOPEFUL_ENCHANT_SEED, player.getRandom().nextInt());
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return ItemInteractionResult.CONSUME;
        }
    }

    public static Holder<Scroll> generateScroll(EnchantingState state){
        int weightSum = state.weights.stream().mapToInt(i->Math.max(i,0)).sum();

        // Now choose a random item.
        int idx = 0;
        for (double r = state.rand.nextInt(weightSum); idx < state.weights.size() - 1; ++idx) {
            r -= Math.max(state.weights.get(idx),0);
            if (r <= 0.0) break;
        }
        return state.scrolls.get(idx);
    }
}
