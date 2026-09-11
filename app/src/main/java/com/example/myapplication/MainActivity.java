package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Test: Add pantry items
        dbHelper.addPantryItem("Eggs", 6, "unit", "2024-12-31");
        dbHelper.addPantryItem("Milk", 1, "L", "2024-12-20");
        dbHelper.addPantryItem("Cheese", 200, "g", "2024-12-25");
        dbHelper.addPantryItem("Bread", 1, "loaf", "2024-12-22");
        dbHelper.addPantryItem("Salt", 5, "pinch", "");

        // Test: Get all pantry items
        List<PantryItem> pantryItems = dbHelper.getAllPantryItems();
        Log.d("PANTRY", "=== Pantry Items ===");
        for (PantryItem item : pantryItems) {
            Log.d("PANTRY", item.getName() + ": " + item.getQuantity() + " " + item.getUnit());
        }

        // Test: Get suggested recipes
        List<Recipe> suggested = dbHelper.getSuggestedRecipes(pantryItems);
        Log.d("RECIPES", "=== You can make " + suggested.size() + " recipes! ===");
        for (Recipe recipe : suggested) {
            Log.d("RECIPES", "- " + recipe.getName());
        }
    }
}