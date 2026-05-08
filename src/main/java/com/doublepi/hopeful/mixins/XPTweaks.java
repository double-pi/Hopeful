package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.registries.ModGamerules;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class XPTweaks extends LivingEntity{

    @Shadow public int totalExperience;

    protected XPTweaks(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method="getXpNeededForNextLevel",at = @At("HEAD"),cancellable = true)
    public void injected(CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(64);
        cir.cancel();
    }

    @Inject(method = {"getBaseExperienceReward"},at = {@At("HEAD")},cancellable = true)
    public void _onGetExperience(CallbackInfoReturnable<Integer> cir) {
        boolean keepExperience = this.level().getGameRules().getBoolean(ModGamerules.KEEP_EXP);
        if (keepExperience) {
            cir.setReturnValue(0);
        }else{
            cir.setReturnValue(this.totalExperience);
        }
    }
}
