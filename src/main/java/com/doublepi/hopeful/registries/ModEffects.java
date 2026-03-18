package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.content.mourner.MournedEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.awt.*;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, HopefulMod.MODID);

    public static final Holder<MobEffect> MOURNED = MOB_EFFECTS.register("mourned",
            ()-> new MournedEffect(MobEffectCategory.BENEFICIAL, Color.CYAN.getRGB()));

    public static void register(IEventBus bus){
        MOB_EFFECTS.register(bus);
    }

}
