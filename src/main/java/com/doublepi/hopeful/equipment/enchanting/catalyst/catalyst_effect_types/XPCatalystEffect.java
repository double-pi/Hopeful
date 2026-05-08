package com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.registries.ModCatalystEffectTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record XPCatalystEffect(int increaseBy, boolean consume) implements CatalystEffect{
    public static final MapCodec<XPCatalystEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                    effect.group(
                            Codec.INT.fieldOf("increase_by").forGetter(XPCatalystEffect::increaseBy),
                            Codec.BOOL.fieldOf("consume_levels").forGetter(XPCatalystEffect::consume)
                    ).apply(effect, XPCatalystEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, XPCatalystEffect> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    XPCatalystEffect::increaseBy,
                    ByteBufCodecs.BOOL,
                    XPCatalystEffect::consume,
                    XPCatalystEffect::new
            );


    @Override
    public void applyEffect(EnchantingState state) {
        state.requiredXPLevels += increaseBy;
        if(consume) state.consumedXPLevels += increaseBy;
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.XP_LEVELS_CATALYST_EFFECT_TYPE;
    }
}
