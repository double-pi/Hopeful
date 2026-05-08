package com.doublepi.hopeful.modules.equipment.enchanting.catalyst;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record Catalyst(HolderSet<Block> blocks, int limit, List<CatalystEffect> effects) {
    public static final Codec<Catalyst> CODEC =
            RecordCodecBuilder.create((scrollInstance ->scrollInstance.group(
                    //TODO: Change to blockstates?
                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(Catalyst::blocks),
                    ExtraCodecs.POSITIVE_INT.fieldOf("limit").forGetter(Catalyst::limit),
                    CatalystEffect.CODEC.listOf().optionalFieldOf("effects",List.of()).forGetter(Catalyst::effects)
                    ).apply(scrollInstance, Catalyst::new)
            ));
}
