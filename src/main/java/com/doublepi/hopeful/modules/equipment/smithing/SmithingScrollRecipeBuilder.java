package com.doublepi.hopeful.modules.equipment.smithing;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;

public class SmithingScrollRecipeBuilder {
    private final RecipeCategory category;
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();

    public SmithingScrollRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern) {
        this.category = category;
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public static SmithingScrollRecipeBuilder smithingTrim(Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern, RecipeCategory category) {
        return new SmithingScrollRecipeBuilder(category, template, base, addition, pattern);
    }

    public SmithingScrollRecipeBuilder unlocks(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        SmithingScrollRecipe recipe = new SmithingScrollRecipe(new Recipe.CommonInfo(true), this.template, this.base, this.addition);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}
