package com.doublepi.hopeful.mixins;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.DoubleSupplier;
import java.util.function.IntUnaryOperator;

@Mixin(AbstractHorse.class)
public class AbstractHorseMixin {

    @Inject(method = "createOffspringAttribute", at=@At("HEAD"), cancellable = true)
    private static void createOffspringAttribute(double value1, double value2, double min, double max, RandomSource random, CallbackInfoReturnable<Double> ci) {
        if(max<=min)
            throw new IllegalArgumentException("Incorrect Range for Attribute");
        double deviation = (max-min)/10;
        ci.setReturnValue((value1+value2)/2+deviation* random.nextGaussian());
        ci.cancel();
    }
    //TODO: fix lol
    /**
     * @author Double Pi
     * @reason Better max health uwu
     */
    @Overwrite
    protected static float generateMaxHealth(IntUnaryOperator integerByBoundProvider) {
        return 150f + integerByBoundProvider.applyAsInt(10);
    }

    /**
     * @author Double Pi
     * @reason Better jump strength uwu
     */
    @Overwrite
    protected static double generateJumpStrength(DoubleSupplier probabilityProvider) {
        return 0.6d + probabilityProvider.getAsDouble() * 0.5;
    }

    /**
     * @author Double Pi
     * @reason Better speed uwu
     */
    @Overwrite
    protected static double generateSpeed(DoubleSupplier probabilityProvider) {
        return 0.7d + probabilityProvider.getAsDouble() * 0.25;
    }
}
