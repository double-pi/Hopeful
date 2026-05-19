package com.doublepi.hopeful.equipment.smithing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record Enchantability(int enchantability, List<Integer> levelups) {
    public static final Codec<Enchantability> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
            Codec.intRange(0,512).fieldOf("enchantability").forGetter(Enchantability::enchantability),
            ExtraCodecs.POSITIVE_INT.listOf().optionalFieldOf("level_ups", List.of())
                    .forGetter(Enchantability::levelups)
    ).apply(instance, Enchantability::new));

}
