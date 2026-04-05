package com.doublepi.hopeful.modules.enchanting.smithing;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.AdvancementRewards.Builder;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;

public class SmithingScrollRecipeBuilder {
    private final RecipeCategory category;
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SmithingScrollRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition) {
        this.category = category;
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public static SmithingScrollRecipeBuilder smithingTrim(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category) {
        return new SmithingScrollRecipeBuilder(category, template, base, addition);
    }

    public SmithingScrollRecipeBuilder unlocks(String key, Criterion<?> criterion) {
        this.criteria.put(key, criterion);
        return this;
    }

    public void save(RecipeOutput recipeOutput, ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        Advancement.Builder advancement$builder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(Builder.recipe(recipeId)).requirements(Strategy.OR);
        Objects.requireNonNull(advancement$builder);
        this.criteria.forEach(advancement$builder::addCriterion);
        SmithingTrimRecipe smithingtrimrecipe = new SmithingTrimRecipe(this.template, this.base, this.addition);
        recipeOutput.accept(recipeId, smithingtrimrecipe, advancement$builder.build(recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation location) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(location));
        }
    }
}
