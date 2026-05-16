package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.registries.ModAttachments;
import com.doublepi.hopeful.registries.ModGamerules;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class XPTweaks extends LivingEntity{

    protected XPTweaks(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method="getXpNeededForNextLevel",at = @At("HEAD"),cancellable = true)
    public void injected(CallbackInfoReturnable<Integer> cir){
        Player thisPlayer = (Player)((Object)this);
        System.out.println(thisPlayer.getData(ModAttachments.XP_PER_LEVEL));
        cir.setReturnValue(thisPlayer.getData(ModAttachments.XP_PER_LEVEL));
        cir.cancel();
    }

    @Inject(method = {"getBaseExperienceReward"},at = {@At("HEAD")},cancellable = true)
    public void _onGetExperience(CallbackInfoReturnable<Integer> cir) { //dropped xp
        Player thisPlayer = (Player)((Object)this);
        GameRules gamerules = thisPlayer.level().getGameRules();
        int percentageLost = gamerules.getInt(ModGamerules.PERCENTAGE_XP_LOST);
        int percentageDropped = gamerules.getInt(ModGamerules.PERCENTAGE_XP_DROPPED);
        int xpPerLevel = thisPlayer.getData(ModAttachments.XP_PER_LEVEL);
        int actualTotalXP = (int) ((thisPlayer.experienceLevel +thisPlayer.experienceProgress)* xpPerLevel);

        cir.setReturnValue(actualTotalXP * (100-percentageLost) * percentageDropped / 100_00);

    }
}
