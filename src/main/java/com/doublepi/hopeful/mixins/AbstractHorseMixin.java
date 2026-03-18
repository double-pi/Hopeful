package com.doublepi.hopeful.mixins;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin{
//    @Inject(method="randomizeAttributes",at=@At("TAIL"))
//    public void finalize1(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir){
//        RandomSource random = level.getRandom();
//        AbstractHorse thisHorse = (AbstractHorse) (Object) this;
//
//        thisHorse.getAttribute(Attributes.MAX_HEALTH).setBaseValue(12 + 2 * random.nextGaussian());
//        thisHorse.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.45f + 0.15 * random.nextGaussian());
//        thisHorse.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(0.4f + 0.15 * random.nextGaussian());
//
//    }
    // TODO: Figure out mixin - use https://github.com/NordAct/Leaf-Me-Alone/blob/26.1-independent/src/main/resources/leafmealone.mixins.json
    @Inject(method = "createOffspringAttribute", at=@At("HEAD"))
    private static double createOffspringAttribute(double value1, double value2, double min, double max, RandomSource random) {
        if(max<=min)
            throw new IllegalArgumentException("Incorrect Range for Attribute");
        double deviation = (max-min)/10;
        return (value1+value2)/2+deviation* random.nextGaussian();
    }


}
