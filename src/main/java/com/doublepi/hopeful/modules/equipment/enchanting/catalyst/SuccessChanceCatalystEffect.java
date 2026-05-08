package com.doublepi.hopeful.modules.equipment.enchanting.catalyst;

import com.doublepi.hopeful.registries.ModCatalystEffectTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
    public void applyEffect(Level level, Player player, BlockPos pos) {

    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.SUCCESS_CHANCE_CATALYST_EFFECT_TYPE;
    }
}
