package com.doublepi.hopeful.mixins.gamerules;

import com.doublepi.hopeful.registries.ModGamerules;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerXPTweaks extends Player {

    public ServerXPTweaks(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "restoreFrom",at = @At("TAIL"))
    public void _OnRestoreFrom(ServerPlayer player, boolean keepEverything, CallbackInfo ci) {
        Level level = this.level();
        boolean keepExperience = level.getGameRules().getBoolean(ModGamerules.KEEP_EXP);
        boolean keepInventory = level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);

        if (keepInventory) {
            for(int i = 0; i < this.getInventory().getContainerSize(); ++i) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (!itemStack.isEmpty())
                    this.getInventory().setItem(i, itemStack);
            }

            this.experienceLevel = 0;
            this.totalExperience = 0;
            this.experienceProgress = 0;
        }
        if(keepExperience){
            this.experienceLevel = player.experienceLevel;
            this.totalExperience = player.totalExperience;
            this.experienceProgress = player.experienceProgress;
        }
    }
}
