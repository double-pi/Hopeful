package com.doublepi.hopeful.equipment.smithing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record Enchantability(int startingLevel, List<Integer> levelups) {
    public static final Codec<Enchantability> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("starting_level").forGetter(Enchantability::startingLevel),
            ExtraCodecs.POSITIVE_INT.listOf().optionalFieldOf("level_ups", List.of())
                    .forGetter(Enchantability::levelups)
    ).apply(instance, Enchantability::new));

}
