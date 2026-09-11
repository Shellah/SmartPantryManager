package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeName, tvIngredients, tvSteps;
    private Button btnBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        dbHelper = new DatabaseHelper(this);

        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvSteps = findViewById(R.id.tvSteps);
        btnBack = findViewById(R.id.btnBack);

        // Get recipe ID from intent
        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId != -1) {
            Recipe recipe = dbHelper.getRecipe(recipeId);
            if (recipe != null) {
                displayRecipe(recipe);
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayRecipe(Recipe recipe) {
        tvRecipeName.setText(recipe.getName());

        // Build ingredients list
        StringBuilder ingredientsText = new StringBuilder();
        List<RecipeIngredient> ingredients = recipe.getIngredients();
        if (ingredients != null && !ingredients.isEmpty()) {
            for (RecipeIngredient ing : ingredients) {
                ingredientsText.append("• ")
                        .append(ing.getName())
                        .append(" - ")
                        .append(ing.getQuantity())
                        .append(" ")
                        .append(ing.getUnit())
                        .append("\n");
            }
        } else {
            ingredientsText.append("No ingredients listed.");
        }
        tvIngredients.setText(ingredientsText.toString().trim());

        // Steps
        tvSteps.setText(recipe.getSteps());
    }
}