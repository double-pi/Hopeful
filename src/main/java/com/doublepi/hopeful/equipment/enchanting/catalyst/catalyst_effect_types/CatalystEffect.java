package com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface CatalystEffect {
    Codec<CatalystEffect> CODEC =
            ModRegistries.CATALYST_EFFECT_TYPE_REGISTRY.byNameCodec()
                    .dispatch("type", CatalystEffect::getType, Type::codec);

    void applyEffect(EnchantingState state);
    int alignment();
    ParticleOptions getParticle();
    Type<? extends CatalystEffect> getType();
    record Type<T extends CatalystEffect>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {}
}
