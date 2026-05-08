package com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.equipment.scrolls.Scroll;
import com.doublepi.hopeful.registries.ModCatalystEffectTypes;
import com.doublepi.hopeful.registries.ModParticles;
import com.doublepi.hopeful.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ScrollWeightEffect(HolderSet<Scroll> scrolls, int increaseBy) implements CatalystEffect{
    public static final MapCodec<ScrollWeightEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                    effect.group(
                            RegistryCodecs.homogeneousList(ModRegistries.SCROLL_REGISTRY_KEY).fieldOf("scrolls").forGetter(ScrollWeightEffect::scrolls),
                            Codec.INT.fieldOf("increase_by").forGetter(ScrollWeightEffect::increaseBy)
                    ).apply(effect, ScrollWeightEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ScrollWeightEffect> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.holderSet(ModRegistries.SCROLL_REGISTRY_KEY),
                    ScrollWeightEffect::scrolls,
                    ByteBufCodecs.INT,
                    ScrollWeightEffect::increaseBy,
                    ScrollWeightEffect::new
            );

    @Override
    public int alignment() {
        return (int) Math.signum(increaseBy);
    }

    @Override
    public void applyEffect(EnchantingState state) {
        for (Holder<Scroll> scroll : scrolls) {
            int scrollIndex = state.scrolls.indexOf(scroll);
            if(scrollIndex == -1) { //shouldn't happen?
                state.scrolls.add(scroll);
                state.weights.add(increaseBy);
            }else{
                state.weights.set(scrollIndex,state.weights.get(scrollIndex)+increaseBy);
            }

        }
    }

    @Override
    public ParticleOptions getParticle() {
        return ModParticles.SCROLL_WEIGHT_EFFECT.get();
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return ModCatalystEffectTypes.SCROLL_WEIGHT_CATALYST_EFFECT_TYPE;
    }
}
