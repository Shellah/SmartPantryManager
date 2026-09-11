package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView tvNoRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewRecipes);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        List<PantryItem> pantryItems = dbHelper.getAllPantryItems();
        List<Recipe> suggested = dbHelper.getSuggestedRecipes(pantryItems);

        if (suggested.isEmpty()) {
            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoRecipes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new RecipeAdapter(suggested, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }
}