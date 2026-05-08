package com.doublepi.hopeful.modules.equipment.enchanting.altar;

import com.doublepi.hopeful.registries.ModBlocks;
import com.doublepi.hopeful.registries.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AltarBE extends BlockEntity implements Container {
    private float rotation;
    private int progress = 0;
    //private int maxProgress = 72;
    private final int MAX_PROGRESS = 72;

    public AltarBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.ALTAR_BE.get(), pPos, pBlockState);
    }

    // Save & Load methods
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("altar.progress", progress);
        ContainerHelper.saveAllItems(pTag, inventory, pRegistries);

    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        progress = pTag.getInt("altar.progress");
        ContainerHelper.loadAllItems(pTag, inventory, pRegistries);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(hasRecipe()) {
            increaseCraftingProgress();

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
            setChanged(level, pos, state);
            level.setBlock(pos, state, AltarBlock.UPDATE_CLIENTS);
        } else {
            resetProgress();
        }
    }

    private void resetProgress() { this.progress = 0; }

    private void craftItem() {
        inventory.getFirst().shrink(1);
        ItemEntity itemEntity = new ItemEntity(this.level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), new ItemStack(Items.ACACIA_LEAVES));
        this.level.addFreshEntity(itemEntity);
    }

    private boolean hasCraftingFinished() {
        return this.progress >= MAX_PROGRESS;
    }

    private void increaseCraftingProgress() {
        progress++;
    }


    private boolean hasRecipe() {
        return inventory.getFirst().is(ModTags.SCROLL_MATERIALS);

    }

    // Rendering methods
    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }


    // Container methods
    public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if(!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        setChanged();
        return inventory.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        setChanged();
        ItemStack stack = inventory.get(pSlot);
        stack.shrink(pAmount);
        return inventory.set(pSlot, stack);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        setChanged();
        return ContainerHelper.takeItem(inventory, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        setChanged();
        inventory.set(pSlot, pStack.copyWithCount(1));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }
}
