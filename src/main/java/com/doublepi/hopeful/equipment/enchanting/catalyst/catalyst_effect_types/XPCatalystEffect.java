package com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.registries.ModCatalystEffectTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record XPCatalystEffect(int increaseBy, boolean consumeOnSuccess, boolean consumeOnFail) implements CatalystEffect{
    public static final MapCodec<XPCatalystEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                    effect.group(
                            Codec.INT.fieldOf("increase_by").forGetter(XPCatalystEffect::increaseBy),
                            Codec.BOOL.optionalFieldOf("consume_on_success", true).forGetter(XPCatalystEffect::consumeOnSuccess),
                            Codec.BOOL.optionalFieldOf("consume_on_fail", false).forGetter(XPCatalystEffect::consumeOnFail)
                    ).apply(effect, XPCatalystEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, XPCatalystEffect> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    XPCatalystEffect::increaseBy,
                    ByteBufCodecs.BOOL,
                    XPCatalystEffect::consumeOnSuccess,
                    ByteBufCodecs.BOOL,
                    XPCatalystEffect::consumeOnFail,
                    XPCatalystEffect::new
            );

    @Override
    public ParticleOptions getParticle() {
        return ParticleTypes.SNEEZE;
    }

    @Override
    public void applyEffect(EnchantingState state) {
        state.requiredXPLevels += increaseBy;
        if(consumeOnSuccess) state.consumedXPLevelsOnSuccess += increaseBy;
        if(consumeOnFail) state.consumedXPLevelsOnFail += increaseBy;
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.XP_LEVELS_CATALYST_EFFECT_TYPE;
    }
}
