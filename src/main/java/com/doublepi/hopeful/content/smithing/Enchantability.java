package com.doublepi.hopeful.content.smithing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Enchantability(int enchantability) {
    public static final Codec<Enchantability> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(0,512).fieldOf("enchantability").forGetter(Enchantability::enchantability)
    ).apply(instance, Enchantability::new));

}
