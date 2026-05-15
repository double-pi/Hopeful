package com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.registries.ModCatalystEffectTypes;
import com.doublepi.hopeful.registries.ModParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record XPRequirementEffect(int increaseBy, boolean consumeOnSuccess, boolean consumeOnFail) implements CatalystEffect{
    public static final MapCodec<XPRequirementEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                    effect.group(
                            Codec.INT.fieldOf("increase_by").forGetter(XPRequirementEffect::increaseBy),
                            Codec.BOOL.optionalFieldOf("consume_on_success", true).forGetter(XPRequirementEffect::consumeOnSuccess),
                            Codec.BOOL.optionalFieldOf("consume_on_fail", false).forGetter(XPRequirementEffect::consumeOnFail)
                    ).apply(effect, XPRequirementEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, XPRequirementEffect> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    XPRequirementEffect::increaseBy,
                    ByteBufCodecs.BOOL,
                    XPRequirementEffect::consumeOnSuccess,
                    ByteBufCodecs.BOOL,
                    XPRequirementEffect::consumeOnFail,
                    XPRequirementEffect::new
            );

    @Override
    public ParticleOptions getParticle() {
        return ModParticles.XP_EFFECT.get();
    }

    @Override
    public ParticleDirection particleDirection() {
        return increaseBy < 0 ? ParticleDirection.TO_TABLE : ParticleDirection.TO_SELF;
    }

    @Override
    public void applyEffect(EnchantingState state, BlockPos pos) {
        state.requiredXPLevels += increaseBy;
        if(consumeOnSuccess) state.consumedXPLevelsOnSuccess += increaseBy;
        if(consumeOnFail) state.consumedXPLevelsOnFail += increaseBy;
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.XP_LEVELS_CATALYST_EFFECT_TYPE;
    }
}
