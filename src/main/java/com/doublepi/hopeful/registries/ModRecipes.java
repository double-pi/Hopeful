package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.content.smithing.SmithingScrollRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, HopefulMod.MODID);
//    public static final DeferredRegister<RecipeType<?>> TYPES =
//            DeferredRegister.create(Registries.RECIPE_TYPE, HopefulMod.MODID);

    public static final Supplier<RecipeSerializer<SmithingScrollRecipe>> SMITHING_SCROLL_SERIALIZER =
            RECIPE_SERIALIZERS.register("smithing_scroll", SmithingScrollRecipe.Serializer::new);

//    public static final DeferredHolder<RecipeType<?>, RecipeType<SmithingScrollRecipe>> SMITHING_SCROLL_TYPE =
//            TYPES.register("smithing_scroll", () -> new RecipeType<>() {
//                @Override
//                public String toString() {
//                    return "smithing_scroll";
//                }
//            });

    public static void register(IEventBus event){
        RECIPE_SERIALIZERS.register(event);
//        TYPES.register(event);
    }
}
