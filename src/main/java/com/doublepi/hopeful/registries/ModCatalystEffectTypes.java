package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.catalyst_effect_types.SuccessChanceCatalystEffect;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ModCatalystEffectTypes {
    public static final CatalystEffect.Type<SuccessChanceCatalystEffect> SUCCESS_CHANCE_CATALYST_EFFECT_TYPE
            = register("success_chance", new CatalystEffect.Type<>(SuccessChanceCatalystEffect.MAP_CODEC, SuccessChanceCatalystEffect.STREAM_CODEC));

    public static <T extends CatalystEffect> CatalystEffect.Type<T> register(String id, CatalystEffect.Type<T> catalystEffect) {
        return Registry.register(ModRegistries.CATALYST_EFFECT_TYPE_REGISTRY,
                ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, id), catalystEffect);
    }
    public static void register() {}
}
