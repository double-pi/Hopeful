package com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types;

import com.doublepi.hopeful.equipment.enchanting.EnchantingState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientRequirementEffect(Ingredient ingredient, float consumeOnSuccess, float consumeOnFail) implements CatalystEffect{
    public static final MapCodec<IngredientRequirementEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            effect ->
                    effect.group(
                            Ingredient.CODEC.fieldOf("ingredient").forGetter(IngredientRequirementEffect::ingredient),
                            Codec.FLOAT.optionalFieldOf("consume_on_success",1f).forGetter(IngredientRequirementEffect::consumeOnSuccess),
                            Codec.FLOAT.optionalFieldOf("consume_on_fail",0f).forGetter(IngredientRequirementEffect::consumeOnFail)
                    ).apply(effect, IngredientRequirementEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientRequirementEffect> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    IngredientRequirementEffect::ingredient,
                    ByteBufCodecs.FLOAT,
                    IngredientRequirementEffect::consumeOnSuccess,
                    ByteBufCodecs.FLOAT,
                    IngredientRequirementEffect::consumeOnFail,
                    IngredientRequirementEffect::new
            );

    @Override
    public void applyEffect(EnchantingState state, BlockPos pos) {

    }

    @Override
    public ParticleDirection particleDirection() {
        return null;
    }

    @Override
    public ParticleOptions getParticle() {
        return null;
    }

    @Override
    public Type<? extends CatalystEffect> getType() {
        return null;
    }
}
