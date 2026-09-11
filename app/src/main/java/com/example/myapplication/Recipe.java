package com.example.myapplication;

import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String steps;
    private List<RecipeIngredient> ingredients;

    public Recipe() {}

    public Recipe(String name, String steps, List<RecipeIngredient> ingredients) {
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}