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
import net.minecraft.world.entity.EntityType;

public record SummonEntityEffect(Holder<EntityType<?>> entity, float chanceOnSuccess, float chanceOnFail) implements CatalystEffect{
    public static final MapCodec<SummonEntityEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                    effect.group(
                            RegistryFixedCodec.create(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(SummonEntityEffect::entity),
                            Codec.FLOAT.optionalFieldOf("chance_on_success",1f).forGetter(SummonEntityEffect::chanceOnSuccess),
                            Codec.FLOAT.optionalFieldOf("chance_on_fail", 1f).forGetter(SummonEntityEffect::chanceOnFail)
                    ).apply(effect, SummonEntityEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SummonEntityEffect> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.fromCodec(RegistryFixedCodec.create(Registries.ENTITY_TYPE)),
                    SummonEntityEffect::entity,
                    ByteBufCodecs.FLOAT,
                    SummonEntityEffect::chanceOnSuccess,
                    ByteBufCodecs.FLOAT,
                    SummonEntityEffect::chanceOnFail,
                    SummonEntityEffect::new
            );

    @Override
    public void applyEffect(EnchantingState state, BlockPos pos) {
        state.summonables.add(this);
    }

    @Override
    public ParticleDirection particleDirection() {
        return ParticleDirection.SELF_ONLY;
    }

    @Override
    public ParticleOptions getParticle() {
        return ModParticles.SUMMON_ENTITY_EFFECT.get();
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.SUMMON_ENTITY_CATALYST_EFFECT_TYPE;
    }
}
