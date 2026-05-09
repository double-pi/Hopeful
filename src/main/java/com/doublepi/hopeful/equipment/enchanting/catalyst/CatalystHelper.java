package com.doublepi.hopeful.equipment.enchanting.catalyst;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.equipment.enchanting.EnchantingState.FailReason;
import com.doublepi.hopeful.equipment.scrolls.Scroll;
import com.doublepi.hopeful.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.equipment.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModAttachments;
import com.doublepi.hopeful.registries.ModGamerules;
import com.doublepi.hopeful.registries.ModRegistries;
import com.doublepi.hopeful.registries.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class CatalystHelper {
    public static Optional<Holder.Reference<Catalyst>> getCatalystFromBlock(Holder<Block> block, Level level, EnchantingState state) {
        var stream = level.holderLookup(ModRegistries.CATALYST_REGISTRY_KEY).listElements();
        var reduced = stream
                .filter(c -> c.value().blocks().contains(block)) // all blocks that are catalyst
                .filter(c -> state.allCatalysts.getOrDefault(c.value(), 0) < c.value().limit()); // all catalysts that are didn't reach limit
        return reduced.findFirst();
    }

    public static EnchantingState evaluateEnchantingState(Level level, BlockPos pos, Player player) {
        EnchantingState state = new EnchantingState(player.getData(ModAttachments.HOPEFUL_ENCHANT_SEED));
        Stream<BlockPos> positions = getUsedCatalysts(level, pos,
                player.getData(ModAttachments.HOPEFUL_ENCHANT_SEED));
        positions.forEach(blockPos -> {
            var catalyst = getCatalystFromBlock(level.getBlockState(blockPos).getBlockHolder(), level, state);
            catalyst.ifPresent(cat -> {
                state.evaluateCatalyst(cat.value());
            });
        });
        return state;
    }

    public static Stream<BlockPos> getUsedCatalysts(Level level, BlockPos pos, int enchantSeed) {
        ArrayList<BlockPos> positions = new ArrayList<>();
        EnchantingState state = new EnchantingState(enchantSeed);
        int range = level.getGameRules().getInt(ModGamerules.ENCHANTING_TABLE_RANGE);
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos testPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    Holder<Block> block = level.getBlockState(testPos).getBlockHolder();
                    var catalyst = getCatalystFromBlock(block, level, state);
                    if (catalyst.isPresent()) {
                        positions.add(testPos);
                        state.recordCatalyst(catalyst.get().value());
                    }
                }
            }
        }
        return positions.stream();
    }


    public static ItemInteractionResult enchantTableFunctionality(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        FailReason failReason;
        if (!stack.is(ModTags.SCROLL_MATERIALS)) {
            player.displayClientMessage(Component.translatable("tooltip.hopeful.use_correct_material"), true);
            return ItemInteractionResult.FAIL;
        } else {
            EnchantingState state = CatalystHelper.evaluateEnchantingState(level, pos, player);
            failReason = state.findEnchantFailReason(player);
            if (failReason == FailReason.NONE) { // generate scroll
                ItemStack scrollItem = ScrollItem.createFromScroll(generateScroll(state));
                var stupidArray = new ArrayList<ItemStack>();
                stupidArray.add(scrollItem);
                ScrollHelper.addOrSpawn(player, stupidArray);
                player.makeSound(SoundEvents.ENCHANTMENT_TABLE_USE);
                if(!player.hasInfiniteMaterials())
                    player.giveExperienceLevels(-state.consumedXPLevelsOnSuccess);
                ParticleUtils.spawnParticles(level, pos, 30,
                        0.5, 2, true, ParticleTypes.ENCHANT);
            } else { // show reason
                player.displayClientMessage(
                        Component.translatable("tooltip.hopeful.enchant_failed").append(
                                Component.translatable(failReason.translationKey)), true);
                player.makeSound(SoundEvents.AMETHYST_CLUSTER_BREAK);
                ParticleUtils.spawnParticles(level, pos, 10,
                        0.5, 0.3, true, ParticleTypes.POOF);
                if(!player.hasInfiniteMaterials())
                    player.giveExperienceLevels(-state.consumedXPLevelsOnFail);
            }

            if (failReason.consumeItem)
                stack.consume(1, player);

            player.setData(ModAttachments.HOPEFUL_ENCHANT_SEED, player.getRandom().nextInt());
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return ItemInteractionResult.CONSUME;
        }
    }

    public static Holder<Scroll> generateScroll(EnchantingState state) {
        int weightSum = state.weights.stream().mapToInt(i -> Math.max(i, 0)).sum();

        // Now choose a random item.
        int idx = 0;
        for (double r = state.rand.nextInt(weightSum); idx < state.weights.size() - 1; ++idx) {
            r -= Math.max(state.weights.get(idx), 0);
            if (r <= 0.0) break;
        }
        return state.scrolls.get(idx);
    }
}
