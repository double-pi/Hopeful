package com.doublepi.hopeful.equipment.enchanting;

import com.doublepi.hopeful.equipment.enchanting.catalyst.CatalystHelper;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.equipment.scrolls.ScrollItem;
import com.doublepi.hopeful.registries.ModAttachments;
import com.doublepi.hopeful.registries.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class EnchantingTableChanges {
    public static void catalystParticles(Level level, BlockPos pos, RandomSource random){
        List<BlockPos> posStream = CatalystHelper.getUsedCatalysts(level, pos, 0).toList();
        posStream.forEach(catPos->{
            Holder<Block> block = level.getBlockState(catPos).getBlockHolder();
            var catalyst = CatalystHelper.getCatalystFromBlock(block,level,new EnchantingState(0));
            catalyst.ifPresent(cat->{
                for(CatalystEffect e : cat.value().effects()){
                    if (random.nextInt(20) == 0) {
                        switch (e.particleDirection()){
                            case TO_TABLE -> level.addParticle(e.getParticle(),
                                    catPos.getX() + 0.5f,
                                    catPos.getY() + 0.5f,
                                    catPos.getZ() + 0.5f,
                                    (pos.getX()-catPos.getX())/5f,
                                    (pos.getY()-catPos.getY())/5f,
                                    (pos.getZ()-catPos.getZ())/5f);
                            case TO_SELF -> level.addParticle(e.getParticle(),
                                    pos.getX() + 0.5f,
                                    pos.getY() + 0.5f,
                                    pos.getZ() + 0.5f,
                                    (catPos.getX()-pos.getX())/5f,
                                    (catPos.getY()-pos.getY())/5f,
                                    (catPos.getZ()-pos.getZ())/5f);
                            case SELF_ONLY -> {
                                for (int i = 0; i < 3; i++) {
                                    float angle = (float) (random.nextFloat() * 2 * Math.PI);
                                    level.addParticle(e.getParticle(),
                                            catPos.getX() + 0.5 + Math.cos(angle),
                                            catPos.getY() + 0.5f,
                                            catPos.getZ() + 0.5 + Math.sin(angle),
                                            0,
                                            0.1,
                                            0);
                                }
                            }
                        }
                    }
                }

            });
        });
    }

    public static ItemInteractionResult useItemOn(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.isEmpty()) return ItemInteractionResult.FAIL;
        EnchantingState state = CatalystHelper.evaluateEnchantingState(level, pos, player);
        EnchantingState.FailReason failReason = state.findEnchantFailReason(player, stack);
        if(failReason == EnchantingState.FailReason.NONE){
            player.makeSound(SoundEvents.ENCHANTMENT_TABLE_USE);
            ParticleUtils.spawnParticles(level, pos, 30,
                    0.5, 2, true, ParticleTypes.ENCHANT);
            ScrollHelper.addOrSpawn(player, ScrollItem.createFromScroll(CatalystHelper.generateScroll(state)));
            if(!player.hasInfiniteMaterials()) {
                player.giveExperienceLevels(-Math.max(0, state.consumedXPLevelsOnSuccess));
            }
        }else{
            player.makeSound(SoundEvents.AMETHYST_CLUSTER_BREAK);
            player.displayClientMessage(Component.translatable(failReason.translationKey)
                    .withStyle(Style.EMPTY.withItalic(true)), true);
            ParticleUtils.spawnParticles(level, pos, 10,
                    0.5, 0.3, true, ParticleTypes.POOF);
        }
        if(failReason.failConsequences){
            if(!player.hasInfiniteMaterials()) {
                player.giveExperienceLevels(-Math.max(0,state.consumedXPLevelsOnFail));
            }
            CatalystHelper.morphCatalysts(level, state, failReason == EnchantingState.FailReason.NONE);
        }
        player.setData(ModAttachments.HOPEFUL_ENCHANT_SEED, player.getRandom().nextInt());
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        if(failReason.consumeItem) {
            stack.consume(1, player);
            return ItemInteractionResult.CONSUME;
        }
        return ItemInteractionResult.FAIL;
    }
}
