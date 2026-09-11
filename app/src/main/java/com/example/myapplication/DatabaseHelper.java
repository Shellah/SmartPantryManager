package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper - SQLite database for Smart Pantry Manager
 *
 * Tables:
 * - pantry: user's ingredients (name, quantity, unit, expiry)
 * - recipes: recipe names and steps
 * - recipe_ingredients: links ingredients to recipes
 *
 * Key feature: getSuggestedRecipes() implements strict-matching
 * A recipe is only suggested if ALL its ingredients are in the pantry
 * with at least the required quantity.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PANTRY = "pantry";
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_INGREDIENT_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";

    private static final String COLUMN_RECIPE_ID = "id";
    private static final String COLUMN_RECIPE_NAME = "name";
    private static final String COLUMN_RECIPE_STEPS = "steps";

    private static final String COLUMN_RECIPE_INGREDIENT_ID = "id";
    private static final String COLUMN_RECIPE_ID_FK = "recipe_id";
    private static final String COLUMN_INGREDIENT_NAME_FK = "ingredient_name";
    private static final String COLUMN_INGREDIENT_QUANTITY = "quantity";
    private static final String COLUMN_INGREDIENT_UNIT = "unit";

    private static final String CREATE_TABLE_PANTRY =
            "CREATE TABLE " + TABLE_PANTRY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                    COLUMN_UNIT + " TEXT, " +
                    COLUMN_EXPIRY_DATE + " TEXT)";

    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                    COLUMN_RECIPE_STEPS + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                    COLUMN_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_INGREDIENT_NAME_FK + " TEXT NOT NULL, " +
                    COLUMN_INGREDIENT_QUANTITY + " INTEGER NOT NULL, " +
                    COLUMN_INGREDIENT_UNIT + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_RECIPE_ID_FK + ") REFERENCES " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PANTRY);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);
        preloadRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    private void preloadRecipes(SQLiteDatabase db) {
        // Recipe 1: Spaghetti Bolognese
        long recipe1Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Spaghetti Bolognese",
                "1. Boil spaghetti\n2. Cook mince\n3. Add tomato sauce\n4. Simmer for 20 mins"));
        addRecipeIngredient(db, recipe1Id, "Spaghetti", 500, "g");
        addRecipeIngredient(db, recipe1Id, "Mince", 500, "g");
        addRecipeIngredient(db, recipe1Id, "Tomato Sauce", 400, "ml");
        addRecipeIngredient(db, recipe1Id, "Onion", 1, "unit");
        addRecipeIngredient(db, recipe1Id, "Garlic", 2, "cloves");

        // Recipe 2: Omelette
        long recipe2Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Omelette",
                "1. Beat eggs\n2. Add salt and pepper\n3. Cook in pan\n4. Fold and serve"));
        addRecipeIngredient(db, recipe2Id, "Eggs", 3, "unit");
        addRecipeIngredient(db, recipe2Id, "Salt", 1, "pinch");
        addRecipeIngredient(db, recipe2Id, "Pepper", 1, "pinch");
        addRecipeIngredient(db, recipe2Id, "Cheese", 50, "g");

        // Recipe 3: Grilled Cheese Sandwich
        long recipe3Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Grilled Cheese Sandwich",
                "1. Butter bread slices\n2. Add cheese\n3. Grill until golden brown"));
        addRecipeIngredient(db, recipe3Id, "Bread", 2, "slices");
        addRecipeIngredient(db, recipe3Id, "Cheese", 50, "g");
        addRecipeIngredient(db, recipe3Id, "Butter", 10, "g");

        // Recipe 4: Garden Salad
        long recipe4Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Garden Salad",
                "1. Chop vegetables\n2. Mix in bowl\n3. Add dressing"));
        addRecipeIngredient(db, recipe4Id, "Lettuce", 1, "unit");
        addRecipeIngredient(db, recipe4Id, "Tomato", 2, "unit");
        addRecipeIngredient(db, recipe4Id, "Cucumber", 1, "unit");
        addRecipeIngredient(db, recipe4Id, "Olive Oil", 2, "tbsp");

        // Recipe 5: Pasta with Pesto
        long recipe5Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Pasta with Pesto",
                "1. Cook pasta\n2. Mix with pesto\n3. Add parmesan"));
        addRecipeIngredient(db, recipe5Id, "Pasta", 200, "g");
        addRecipeIngredient(db, recipe5Id, "Pesto", 50, "g");
        addRecipeIngredient(db, recipe5Id, "Parmesan", 30, "g");

        // Recipe 6: Fruit Smoothie
        long recipe6Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Fruit Smoothie",
                "1. Blend fruits\n2. Add yogurt\n3. Blend again\n4. Serve chilled"));
        addRecipeIngredient(db, recipe6Id, "Banana", 1, "unit");
        addRecipeIngredient(db, recipe6Id, "Strawberries", 100, "g");
        addRecipeIngredient(db, recipe6Id, "Yogurt", 150, "ml");
        addRecipeIngredient(db, recipe6Id, "Milk", 100, "ml");

        // Recipe 7: Chicken Stir Fry
        long recipe7Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Chicken Stir Fry",
                "1. Cut chicken\n2. Cook in wok\n3. Add vegetables\n4. Add sauce"));
        addRecipeIngredient(db, recipe7Id, "Chicken Breast", 300, "g");
        addRecipeIngredient(db, recipe7Id, "Bell Pepper", 1, "unit");
        addRecipeIngredient(db, recipe7Id, "Broccoli", 100, "g");
        addRecipeIngredient(db, recipe7Id, "Soy Sauce", 2, "tbsp");

        // Recipe 8: Avocado Toast
        long recipe8Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Avocado Toast",
                "1. Toast bread\n2. Mash avocado\n3. Spread on toast\n4. Season"));
        addRecipeIngredient(db, recipe8Id, "Bread", 2, "slices");
        addRecipeIngredient(db, recipe8Id, "Avocado", 1, "unit");
        addRecipeIngredient(db, recipe8Id, "Salt", 1, "pinch");
        addRecipeIngredient(db, recipe8Id, "Lemon Juice", 1, "tbsp");

        // Recipe 9: Oatmeal
        long recipe9Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Oatmeal",
                "1. Boil milk/water\n2. Add oats\n3. Cook for 5 mins\n4. Add toppings"));
        addRecipeIngredient(db, recipe9Id, "Oats", 100, "g");
        addRecipeIngredient(db, recipe9Id, "Milk", 200, "ml");
        addRecipeIngredient(db, recipe9Id, "Honey", 1, "tbsp");
        addRecipeIngredient(db, recipe9Id, "Banana", 1, "unit");

        // Recipe 10: Vegetable Soup
        long recipe10Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Vegetable Soup",
                "1. Chop vegetables\n2. Boil in stock\n3. Blend\n4. Season"));
        addRecipeIngredient(db, recipe10Id, "Carrot", 2, "unit");
        addRecipeIngredient(db, recipe10Id, "Potato", 2, "unit");
        addRecipeIngredient(db, recipe10Id, "Onion", 1, "unit");
        addRecipeIngredient(db, recipe10Id, "Vegetable Stock", 500, "ml");

        // Recipe 11: Pancakes
        long recipe11Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Pancakes",
                "1. Mix flour, eggs, milk\n2. Cook in pan\n3. Flip when bubbly\n4. Serve with syrup"));
        addRecipeIngredient(db, recipe11Id, "Flour", 200, "g");
        addRecipeIngredient(db, recipe11Id, "Eggs", 2, "unit");
        addRecipeIngredient(db, recipe11Id, "Milk", 300, "ml");
        addRecipeIngredient(db, recipe11Id, "Sugar", 2, "tbsp");

        // Recipe 12: Tacos
        long recipe12Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Tacos",
                "1. Warm tortillas\n2. Cook mince\n3. Add seasoning\n4. Fill with toppings"));
        addRecipeIngredient(db, recipe12Id, "Mince", 400, "g");
        addRecipeIngredient(db, recipe12Id, "Taco Seasoning", 1, "packet");
        addRecipeIngredient(db, recipe12Id, "Tortillas", 6, "unit");
        addRecipeIngredient(db, recipe12Id, "Lettuce", 1, "unit");
        addRecipeIngredient(db, recipe12Id, "Cheese", 50, "g");

        // Recipe 13: Fried Rice
        long recipe13Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Fried Rice",
                "1. Cook rice\n2. Fry with vegetables\n3. Add egg\n4. Season with soy sauce"));
        addRecipeIngredient(db, recipe13Id, "Rice", 200, "g");
        addRecipeIngredient(db, recipe13Id, "Eggs", 2, "unit");
        addRecipeIngredient(db, recipe13Id, "Carrot", 1, "unit");
        addRecipeIngredient(db, recipe13Id, "Peas", 100, "g");
        addRecipeIngredient(db, recipe13Id, "Soy Sauce", 2, "tbsp");

        // Recipe 14: Omelette with Veggies
        long recipe14Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Omelette with Veggies",
                "1. Beat eggs\n2. Add chopped veggies\n3. Cook in pan\n4. Fold and serve"));
        addRecipeIngredient(db, recipe14Id, "Eggs", 3, "unit");
        addRecipeIngredient(db, recipe14Id, "Bell Pepper", 1, "unit");
        addRecipeIngredient(db, recipe14Id, "Onion", 1, "unit");
        addRecipeIngredient(db, recipe14Id, "Cheese", 30, "g");

        // Recipe 15: Yogurt Bowl
        long recipe15Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Yogurt Bowl",
                "1. Add yogurt to bowl\n2. Top with fruits\n3. Add honey\n4. Sprinkle nuts"));
        addRecipeIngredient(db, recipe15Id, "Yogurt", 200, "g");
        addRecipeIngredient(db, recipe15Id, "Banana", 1, "unit");
        addRecipeIngredient(db, recipe15Id, "Honey", 1, "tbsp");
        addRecipeIngredient(db, recipe15Id, "Strawberries", 50, "g");

        // Recipe 16: Cheese Toastie
        long recipe16Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Cheese Toastie",
                "1. Butter bread\n2. Add cheese\n3. Toast until melted"));
        addRecipeIngredient(db, recipe16Id, "Bread", 2, "slices");
        addRecipeIngredient(db, recipe16Id, "Cheese", 40, "g");
        addRecipeIngredient(db, recipe16Id, "Butter", 5, "g");

        // Recipe 17: Scrambled Eggs
        long recipe17Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Scrambled Eggs",
                "1. Beat eggs\n2. Cook in pan with butter\n3. Stir continuously\n4. Season and serve"));
        addRecipeIngredient(db, recipe17Id, "Eggs", 2, "unit");
        addRecipeIngredient(db, recipe17Id, "Butter", 10, "g");
        addRecipeIngredient(db, recipe17Id, "Salt", 1, "pinch");
        addRecipeIngredient(db, recipe17Id, "Pepper", 1, "pinch");

        // Recipe 18: Banana Milkshake
        long recipe18Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Banana Milkshake",
                "1. Blend banana and milk\n2. Add sugar\n3. Blend until smooth\n4. Serve cold"));
        addRecipeIngredient(db, recipe18Id, "Banana", 1, "unit");
        addRecipeIngredient(db, recipe18Id, "Milk", 250, "ml");
        addRecipeIngredient(db, recipe18Id, "Sugar", 1, "tbsp");

        // Recipe 19: Tomato Pasta
        long recipe19Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Tomato Pasta",
                "1. Cook pasta\n2. Fry onion and garlic\n3. Add tomato sauce\n4. Mix and serve"));
        addRecipeIngredient(db, recipe19Id, "Pasta", 200, "g");
        addRecipeIngredient(db, recipe19Id, "Tomato Sauce", 300, "ml");
        addRecipeIngredient(db, recipe19Id, "Onion", 1, "unit");
        addRecipeIngredient(db, recipe19Id, "Garlic", 1, "cloves");

        // Recipe 20: Simple Salad
        long recipe20Id = db.insert(TABLE_RECIPES, null, createRecipeValues("Simple Salad",
                "1. Chop lettuce and tomato\n2. Add cucumber\n3. Drizzle olive oil\n4. Toss and serve"));
        addRecipeIngredient(db, recipe20Id, "Lettuce", 1, "unit");
        addRecipeIngredient(db, recipe20Id, "Tomato", 1, "unit");
        addRecipeIngredient(db, recipe20Id, "Cucumber", 1, "unit");
        addRecipeIngredient(db, recipe20Id, "Olive Oil", 1, "tbsp");
    }

    private ContentValues createRecipeValues(String name, String steps) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_STEPS, steps);
        return values;
    }

    private void addRecipeIngredient(SQLiteDatabase db, long recipeId, String name, int quantity, String unit) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID_FK, recipeId);
        values.put(COLUMN_INGREDIENT_NAME_FK, name);
        values.put(COLUMN_INGREDIENT_QUANTITY, quantity);
        values.put(COLUMN_INGREDIENT_UNIT, unit);
        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }

    // ========== PANTRY CRUD OPERATIONS ==========

    public boolean addPantryItem(String name, int quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INGREDIENT_NAME, name.toLowerCase().trim());
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);
        long result = db.insert(TABLE_PANTRY, null, values);
        return result != -1;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> pantryItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null, COLUMN_INGREDIENT_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                PantryItem item = new PantryItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)));
                item.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                item.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT)));
                item.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)));
                pantryItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pantryItems;
    }

    public boolean updatePantryItem(int id, String name, int quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INGREDIENT_NAME, name.toLowerCase().trim());
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);
        int result = db.update(TABLE_PANTRY, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PANTRY, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // ========== RECIPE OPERATIONS ==========

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
                recipe.setId(recipeId);
                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME)));
                recipe.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_STEPS)));
                recipe.setIngredients(getRecipeIngredients(recipeId));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    private List<RecipeIngredient> getRecipeIngredients(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null,
                COLUMN_RECIPE_ID_FK + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                RecipeIngredient ingredient = new RecipeIngredient();
                ingredient.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME_FK)));
                ingredient.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_QUANTITY)));
                ingredient.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_UNIT)));
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }

    public Recipe getRecipe(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null,
                COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            Recipe recipe = new Recipe();
            recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID)));
            recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME)));
            recipe.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_STEPS)));
            recipe.setIngredients(getRecipeIngredients(recipeId));
            cursor.close();
            return recipe;
        }
        cursor.close();
        return null;
    }

    // ========== STRICT MATCHING LOGIC ==========

    public List<Recipe> getSuggestedRecipes(List<PantryItem> pantryItems) {
        List<Recipe> suggestedRecipes = new ArrayList<>();
        List<Recipe> allRecipes = getAllRecipes();

        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantryItems)) {
                suggestedRecipes.add(recipe);
            }
        }
        return suggestedRecipes;
    }

    private boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantryItems) {
        List<RecipeIngredient> requiredIngredients = recipe.getIngredients();

        for (RecipeIngredient required : requiredIngredients) {
            boolean found = false;
            for (PantryItem pantry : pantryItems) {
                if (namesMatch(pantry.getName(), required.getName())) {
                    if (pantry.getQuantity() >= required.getQuantity()) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns recipes that are missing exactly ONE ingredient from the pantry.
     * Used for the optional "Almost There" bonus list (Section 2.3 of the brief).
     * These are NOT strict matches and are shown in a clearly separate section.
     */
    public List<Recipe> getAlmostThereRecipes(List<PantryItem> pantryItems) {
        List<Recipe> almost = new ArrayList<>();
        List<Recipe> allRecipes = getAllRecipes();

        for (Recipe recipe : allRecipes) {
            int missing = countMissingIngredients(recipe, pantryItems);
            if (missing == 1) {
                almost.add(recipe);
            }
        }
        return almost;
    }

    private int countMissingIngredients(Recipe recipe, List<PantryItem> pantryItems) {
        int missing = 0;
        for (RecipeIngredient required : recipe.getIngredients()) {
            boolean found = false;
            for (PantryItem pantry : pantryItems) {
                if (namesMatch(pantry.getName(), required.getName())
                        && pantry.getQuantity() >= required.getQuantity()) {
                    found = true;
                    break;
                }
            }
            if (!found) missing++;
        }
        return missing;
    }

    /**
     * Normalizes ingredient names so that trivial real-world differences
     * (case, whitespace, singular/plural) don't break strict matching.
     * Examples that now match: "Tomato" / "tomatoes", "Onion" / "onions ".
     */
    private boolean namesMatch(String pantryName, String recipeName) {
        return normalize(pantryName).equals(normalize(recipeName));
    }

    private String normalize(String name) {
        if (name == null) return "";
        String s = name.toLowerCase().trim();
        // strip trailing punctuation
        s = s.replaceAll("[.,;:!]+$", "");
        // very simple singular/plural handling
        if (s.endsWith("ies") && s.length() > 3) {
            s = s.substring(0, s.length() - 3) + "y";   // berries -> berry
        } else if (s.endsWith("es") && s.length() > 2) {
            s = s.substring(0, s.length() - 2);          // tomatoes -> tomato
        } else if (s.endsWith("s") && s.length() > 1) {
            s = s.substring(0, s.length() - 1);          // onions -> onion
        }
        return s;
    }
}