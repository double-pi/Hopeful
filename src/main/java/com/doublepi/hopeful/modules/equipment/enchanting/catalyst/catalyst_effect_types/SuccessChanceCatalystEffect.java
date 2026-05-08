package com.doublepi.hopeful.modules.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.modules.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.registries.ModCatalystEffectTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SuccessChanceCatalystEffect(float increaseBy) implements CatalystEffect{
    public static final MapCodec<SuccessChanceCatalystEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                effect.group(
                        Codec.FLOAT.fieldOf("increase_by").forGetter(SuccessChanceCatalystEffect::increaseBy)
                ).apply(effect, SuccessChanceCatalystEffect::new)
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, SuccessChanceCatalystEffect> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT,
                    SuccessChanceCatalystEffect::increaseBy,
                    SuccessChanceCatalystEffect::new
            );


    @Override
    public void applyEffect(EnchantingState state) {
        state.successChance(increaseBy);
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.SUCCESS_CHANCE_CATALYST_EFFECT_TYPE;
    }
}
