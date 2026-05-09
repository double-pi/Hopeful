package com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.registries.ModCatalystEffectTypes;
import com.doublepi.hopeful.registries.ModParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SuccessChanceEffect(float increaseBy) implements CatalystEffect{
    public static final MapCodec<SuccessChanceEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                effect.group(
                        Codec.FLOAT.fieldOf("increase_by").forGetter(SuccessChanceEffect::increaseBy)
                ).apply(effect, SuccessChanceEffect::new)
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, SuccessChanceEffect> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT,
                    SuccessChanceEffect::increaseBy,
                    SuccessChanceEffect::new
            );


    @Override
    public boolean particlesTowardsEnchantTable() {
        return increaseBy > 0;
    }

    @Override
    public void applyEffect(EnchantingState state) {
        state.successChance+=increaseBy;
    }

    @Override
    public ParticleOptions getParticle() {
        return ModParticles.SUCCESS_CHANCE_EFFECT.get();
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.SUCCESS_CHANCE_CATALYST_EFFECT_TYPE;
    }
}
