package com.md.recipedia.modal;


public class RecipeMenuModal {
    String recipe_name,recipe_serves,recipe_makes,recipe_preparation,recipe_cooking,recipe_ingredients,recipe_method;
int recipe__banner;

    public RecipeMenuModal() {
    }

    public RecipeMenuModal(String recipe_name, String recipe_serves, String recipe_makes, String recipe_preparation, String recipe_cooking, String recipe_ingredients, String recipe_method, int recipe__banner) {
        this.recipe_name = recipe_name;
        this.recipe_serves = recipe_serves;
        this.recipe_makes = recipe_makes;
        this.recipe_preparation = recipe_preparation;
        this.recipe_cooking = recipe_cooking;
        this.recipe_ingredients = recipe_ingredients;
        this.recipe_method = recipe_method;
        this.recipe__banner = recipe__banner;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public String getRecipe_serves() {
        return recipe_serves;
    }

    public String getRecipe_makes() {
        return recipe_makes;
    }

    public String getRecipe_preparation() {
        return recipe_preparation;
    }

    public String getRecipe_cooking() {
        return recipe_cooking;
    }

    public String getRecipe_ingredients() {
        return recipe_ingredients;
    }

    public String getRecipe_method() {
        return recipe_method;
    }

    public int getRecipe__banner() {
        return recipe__banner;
    }
}
