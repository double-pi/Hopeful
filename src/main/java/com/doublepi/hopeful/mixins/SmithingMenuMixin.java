package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.equipment.smithing.SmithingTableChanges;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    @Shadow
    @Final
    private Level level;

    public SmithingMenuMixin(@org.jetbrains.annotations.Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @Inject(method="createResult", at=@At("HEAD"),cancellable = true)
    void addXPCondition(CallbackInfo ci){
        if(!SmithingTableChanges.hasEnoughXP((SmithingMenu) ((Object)this),player)) ci.cancel();
    }

    @Inject(method="onTake",at=@At("HEAD"))
    void removeXP(Player player, ItemStack stack, CallbackInfo ci){
       SmithingTableChanges.takeXP((SmithingMenu) ((Object)this),player);
    }

}
