package com.doublepi.hopeful.equipment.enchanting.alignment;

import com.doublepi.hopeful.equipment.enchanting.catalyst.Catalyst;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record Alignment(List<ModifiedCatalysts> modifiedCatalysts) {
    public static final Codec<Alignment> CODEC =
            RecordCodecBuilder.create(instance->instance.group(
                Codec.withAlternative(
                        RegistryFileCodec.create(ModRegistries.CATALYST_REGISTRY_KEY,Catalyst.CODEC)
                                .xmap(cat->new ModifiedCatalysts(cat,1,List.of())
                                    , modif->modif.catalyst),
                        ModifiedCatalysts.CODEC
                    ).listOf().fieldOf("catalysts").forGetter(Alignment::modifiedCatalysts)
                ).apply(instance, Alignment::new)
            );



    public record ModifiedCatalysts(Holder<Catalyst> catalyst, int count, List<CatalystEffect> addedEffects){
        public static final Codec<ModifiedCatalysts> CODEC =
                RecordCodecBuilder.create((instance ->instance.group(
                        RegistryFileCodec.create(ModRegistries.CATALYST_REGISTRY_KEY,Catalyst.CODEC)
                                .fieldOf("catalyst").forGetter(ModifiedCatalysts::catalyst),
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("count",1)
                                .forGetter(ModifiedCatalysts::count),
                        CatalystEffect.CODEC.listOf().optionalFieldOf("added_effects",List.of())
                                .forGetter(ModifiedCatalysts::addedEffects)
                ).apply(instance, ModifiedCatalysts::new)
                ));
    }
}
