package com.doublepi.hopeful.content.smithing;

import com.doublepi.hopeful.content.scrolls.Scroll;
import com.doublepi.hopeful.content.scrolls.ScrollHelper;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModRecipes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record SmithingScrollRecipe(Ingredient template, Ingredient base,
                                   Ingredient addition) implements SmithingRecipe {


    public boolean matches(SmithingRecipeInput input, Level level) {
        return input.template().has(ModDataComponentTypes.SCROLL);
    }

    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {

        ItemStack equipment = input.base();
        ItemStack template = input.template();

        if(!template.has(ModDataComponentTypes.SCROLL))
            return ItemStack.EMPTY;
        if(equipment.isEmpty())
            return ItemStack.EMPTY;
        if(!input.addition().is(Items.LAPIS_LAZULI))
            return ItemStack.EMPTY;

        Scroll scroll = template.get(ModDataComponentTypes.SCROLL).value();
        if(!(ScrollHelper.supportsScroll(equipment,scroll)))
            return ItemStack.EMPTY;

        var result = equipment.copyWithCount(1);
        ScrollHelper.enchant(result, scroll);
        return result;

    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.SMITHING;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return base.getItems()[0];
    }

    public boolean isTemplateIngredient(ItemStack stack) {
        return this.template.test(stack);
    }

    public boolean isBaseIngredient(ItemStack stack) {
        return this.base.test(stack);
    }

    public boolean isAdditionIngredient(ItemStack stack) {
        return this.addition.test(stack);
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMITHING_SCROLL_SERIALIZER.get();
    }

    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::hasNoItems);
    }

    public static class Serializer implements RecipeSerializer<SmithingScrollRecipe> {
        private static final MapCodec<SmithingScrollRecipe> CODEC =
                RecordCodecBuilder.mapCodec((p_301227_) ->
                        p_301227_.group(
                                Ingredient.CODEC.fieldOf("template").forGetter((p_301070_) -> p_301070_.template),
                                Ingredient.CODEC.fieldOf("base").forGetter((p_300969_) -> p_300969_.base),
                                Ingredient.CODEC.fieldOf("addition").forGetter((p_300977_) -> p_300977_.addition)
                        ).apply(p_301227_, SmithingScrollRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, SmithingScrollRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        public MapCodec<SmithingScrollRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, SmithingScrollRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SmithingScrollRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            return new SmithingScrollRecipe(ingredient, ingredient1, ingredient2);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SmithingScrollRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.template);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.base);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.addition);
        }
    }
}
