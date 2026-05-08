package com.doublepi.hopeful.modules.equipment.enchanting.altar;

import com.doublepi.hopeful.registries.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends BaseEntityBlock {
    public AltarBlock(Properties properties){
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(AltarBlock::new);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AltarBE(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos,
                            BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            if(pLevel.getBlockEntity(pPos) instanceof AltarBE altarBE) {
                Containers.dropContents(pLevel, pPos, altarBE);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack inHand, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player player, InteractionHand pHand, BlockHitResult pHitResult) {
        /*
            altar \ player | rclick (push)    shift-rclick (pull)
            ---------------+-----------------------------
                different  | fail             pull
                matching   | push             pull
                empty      | push             fail
         */
        if(pLevel.getBlockEntity(pPos) instanceof AltarBE altarBE) {
            boolean sneaking = player.isCrouching();

            ItemStack inAltar = altarBE.getItem(0);
            boolean areItemsMatching = ItemStack.isSameItemSameComponents(inHand, inAltar);

//            if(sneaking){
//                if(!areItemsMatching)
//            }else{

            //}
            if(altarBE.isEmpty() && !inHand.isEmpty()) {
                altarBE.setItem(0, inHand);
                inHand.shrink(1);
                pLevel.playSound(player, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            } else if(inHand.isEmpty()) {
                ItemStack stackOnPedestal = altarBE.getItem(0);
                player.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                altarBE.clearContent();
                pLevel.playSound(player, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }
        return createTickerHelper(pBlockEntityType, ModBlocks.ALTAR_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }
}
