package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.modules.equipment.smithing.SmithingScrollRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, HopefulMod.MODID);


    public static void register(IEventBus event){
        RECIPE_SERIALIZERS.register("smithing_scroll", ()->
                SmithingScrollRecipe.SERIALIZER);
        RECIPE_SERIALIZERS.register(event);
    }
}
