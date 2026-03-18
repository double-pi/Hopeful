package com.doublepi.hopeful.content.smithing;

import com.doublepi.hopeful.content.scrolls.Scroll;
import com.doublepi.hopeful.content.scrolls.ScrollHelper;
import com.doublepi.hopeful.registries.ModDataComponentTypes;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class SmithingScrollRecipe extends SimpleSmithingRecipe{
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;

    public SmithingScrollRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition) {
        super(commonInfo);
        this.template = template;
        this.base = base;
        this.addition = addition;
    }
    public static final MapCodec<SmithingScrollRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec((i) -> 
            i.group(CommonInfo.MAP_CODEC.forGetter((o) -> o.commonInfo),
                    Ingredient.CODEC.fieldOf("template").forGetter((o) -> o.template),
                    Ingredient.CODEC.fieldOf("base").forGetter((o) -> o.base), 
                    Ingredient.CODEC.fieldOf("addition").forGetter((o) -> o.addition)
                    .apply(i, SmithingScrollRecipe::new)));
    public static final StreamCodec<RegistryFriendlyByteBuf, SmithingScrollRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, (o) -> o.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC, (o) -> o.template,
            Ingredient.CONTENTS_STREAM_CODEC, (o) -> o.base,
            Ingredient.CONTENTS_STREAM_CODEC, (o) -> o.addition,
            SmithingScrollRecipe::new);;
    public static final RecipeSerializer<SmithingScrollRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC,STREAM_CODEC);
    public boolean matches(SmithingRecipeInput input, Level level) {
        return input.template().has(ModDataComponentTypes.SCROLL);
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return Optional.of(this.template);
    }

    @Override
    public Ingredient baseIngredient() {
        return this.base;
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return Optional.of(addition);
    }

    public ItemStack assemble(SmithingRecipeInput input) {
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
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(this.template, this.base, this.addition));
    }

    public RecipeSerializer<SmithingScrollRecipe> getSerializer() {
        return SERIALIZER;
    }
    

}
