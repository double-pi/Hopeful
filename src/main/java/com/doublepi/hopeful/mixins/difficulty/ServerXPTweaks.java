package com.doublepi.hopeful.mixins.difficulty;

import com.doublepi.hopeful.registries.ModGamerules;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerXPTweaks extends Player {


    public ServerXPTweaks(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "restoreFrom",at = @At("TAIL"))
    public void _OnRestoreFrom(ServerPlayer player, boolean restoreAll, CallbackInfo ci) {

        if(level().isClientSide()) return;
        ServerLevel level = (ServerLevel) this.level();
        boolean keepExperience = level.getGameRules().get(ModGamerules.KEEP_EXP.get());
        boolean keepInventory = level.getGameRules().get(GameRules.KEEP_INVENTORY);

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
