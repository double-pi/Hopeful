package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.ScrollWeightEffect;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.SuccessChanceEffect;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.XPCatalystEffect;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ModCatalystEffectTypes {
    public static final CatalystEffect.Type<SuccessChanceEffect> SUCCESS_CHANCE_CATALYST_EFFECT_TYPE
            = register("success_chance", new CatalystEffect.Type<>(SuccessChanceEffect.MAP_CODEC, SuccessChanceEffect.STREAM_CODEC));
    public static final CatalystEffect.Type<XPCatalystEffect> XP_LEVELS_CATALYST_EFFECT_TYPE
            = register("xp_levels_requirement", new CatalystEffect.Type<>(XPCatalystEffect.MAP_CODEC, XPCatalystEffect.STREAM_CODEC));
    public static final CatalystEffect.Type<ScrollWeightEffect> SCROLL_WEIGHT_CATALYST_EFFECT_TYPE
            = register("scroll_weight", new CatalystEffect.Type<>(ScrollWeightEffect.MAP_CODEC, ScrollWeightEffect.STREAM_CODEC));
    //---------------------------------------------------------------------------
    public static <T extends CatalystEffect> CatalystEffect.Type<T> register(String id, CatalystEffect.Type<T> catalystEffect) {
        return Registry.register(ModRegistries.CATALYST_EFFECT_TYPE_REGISTRY,
                ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, id), catalystEffect);
    }
    public static void register() {}
}
