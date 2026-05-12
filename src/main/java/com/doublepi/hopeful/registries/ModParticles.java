package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, HopefulMod.MODID);
    //TODO: Figure out particle options and make ColoredSGAParticle - and maybe cycle option between letters
    public static final Supplier<SimpleParticleType> XP_EFFECT =
            PARTICLE_TYPES.register("xp_catalyst_effect", ()-> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> SCROLL_WEIGHT_EFFECT =
            PARTICLE_TYPES.register("scroll_catalyst_effect", ()-> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> SUCCESS_CHANCE_EFFECT =
            PARTICLE_TYPES.register("success_catalyst_effect", ()-> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> MORPH_CHANCE_EFFECT =
            PARTICLE_TYPES.register("morph_catalyst_effect", ()-> new SimpleParticleType(true));

    public static void register(IEventBus modEventBus) {
        PARTICLE_TYPES.register(modEventBus);
    }
}
