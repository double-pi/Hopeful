package com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.registries.ModCatalystEffectTypes;
import com.doublepi.hopeful.registries.ModParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record MorphSelfEffect(BlockState morphTo, float chanceOnSuccess, float chanceOnFail) implements CatalystEffect{
    public static final Codec<BlockState> BLOCK_OR_BLOCKSTATE_CODEC =
            Codec.withAlternative(
                    BlockState.CODEC, RegistryFixedCodec.create(Registries.BLOCK)
                        .xmap(block-> block.value().defaultBlockState(),
                            state -> state.getBlockHolder())
            );
    public static final MapCodec<MorphSelfEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                    effect.group(
                            BLOCK_OR_BLOCKSTATE_CODEC.optionalFieldOf("block",Blocks.AIR.defaultBlockState()).forGetter(MorphSelfEffect::morphTo),
                            Codec.FLOAT.optionalFieldOf("chance_on_success",1f).forGetter(MorphSelfEffect::chanceOnSuccess),
                            Codec.FLOAT.optionalFieldOf("chance_on_fail", 0f).forGetter(MorphSelfEffect::chanceOnFail)
                    ).apply(effect, MorphSelfEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MorphSelfEffect> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.fromCodec(BLOCK_OR_BLOCKSTATE_CODEC),
                    MorphSelfEffect::morphTo,
                    ByteBufCodecs.FLOAT,
                    MorphSelfEffect::chanceOnSuccess,
                    ByteBufCodecs.FLOAT,
                    MorphSelfEffect::chanceOnFail,
                    MorphSelfEffect::new
            );
    
    public void applyEffect(EnchantingState state, BlockPos pos) {
        state.morphables.put(pos, this);
    }

    @Override
    public ParticleDirection particleDirection() {
        return ParticleDirection.SELF_ONLY;
    }

    @Override
    public ParticleOptions getParticle() {
        return ModParticles.MORPH_CHANCE_EFFECT.get();
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.MORPH_SELF_CATALYST_EFFECT_TYPE;
    }
}
