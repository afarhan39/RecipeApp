package my.fallacy.recipeapp.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import my.fallacy.recipeapp.model.Recipe;

/**
 * Created by amirf on 20/08/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version and Name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyRecipeApp";


    // Recipe model
    private static final String TABLE_RECIPE = "recipes";
    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_STEPS = "steps";
    private static final String RECIPE_TYPE = "type";

    // Table creation
    private static final String CREATE_TABLE_RECIPE =
            "CREATE TABLE " + TABLE_RECIPE + "("
            + RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + RECIPE_NAME + " TEXT,"
            + RECIPE_INGREDIENTS + " TEXT,"
            + RECIPE_STEPS + " TEXT,"
            + RECIPE_TYPE + " TEXT" + ")";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPE);

        ContentValues contentValues = new ContentValues();
        // Dummy Data 1
        contentValues.put(RECIPE_NAME, "Chocolate Cake");
        contentValues.put(RECIPE_INGREDIENTS, "- Chocolate\n- Flour\n- Milk\n- Sugar\n- Eggs");
        contentValues.put(RECIPE_STEPS, "1. Mix Eggs with Milk\n2. Mix Flour with Chocolate and Sugar\n3. Mix all together\n4. Bake for 40min");
        contentValues.put(RECIPE_TYPE, "Make Ahead");
        db.insert(TABLE_RECIPE, null, contentValues);

        // Dummy Data 2
        contentValues.put(RECIPE_NAME, "Carrot Cake");
        contentValues.put(RECIPE_INGREDIENTS, "- Chocolate\n- Flour\n- Milk\n- Sugar\n- Eggs");
        contentValues.put(RECIPE_STEPS, "1. Mix Eggs with Milk\n2. Mix Flour with Chocolate and Sugar\n3. Mix all together\n4. Bake for 40min");
        contentValues.put(RECIPE_TYPE, "Vegetarian");
        db.insert(TABLE_RECIPE, null, contentValues);

        // Dummy Data 3
        contentValues.put(RECIPE_NAME, "Fibre Cake");
        contentValues.put(RECIPE_INGREDIENTS, "- Chocolate\n- Flour\n- Milk\n- Sugar\n- Eggs");
        contentValues.put(RECIPE_STEPS, "1. Mix Eggs with Milk\n2. Mix Flour with Chocolate and Sugar\n3. Mix all together\n4. Bake for 40min");
        contentValues.put(RECIPE_TYPE, "Healthy");
        db.insert(TABLE_RECIPE, null, contentValues);

        // Dummy Data 4
        contentValues.put(RECIPE_NAME, "Cup Cake");
        contentValues.put(RECIPE_INGREDIENTS, "- Chocolate\n- Flour\n- Milk\n- Sugar\n- Eggs");
        contentValues.put(RECIPE_STEPS, "1. Mix Eggs with Milk\n2. Mix Flour with Chocolate and Sugar\n3. Mix all together\n4. Bake for 40min");
        contentValues.put(RECIPE_TYPE, "Fast Food");
        db.insert(TABLE_RECIPE, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        onCreate(db);
    }

    public Recipe getRecipe(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE,
                new String[] {
                        RECIPE_ID,
                        RECIPE_NAME,
                        RECIPE_INGREDIENTS,
                        RECIPE_STEPS,
                        RECIPE_TYPE
                },
                RECIPE_ID + "= ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();

        // save to recipe before close
        Recipe recipe = new Recipe(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));

        cursor.close();

        return recipe;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<Recipe>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));

                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return recipeList;
    }

    public List<Recipe> getFilteredRecipes(String filter) {
        List<Recipe> recipeList = new ArrayList<Recipe>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPE + " WHERE "
                + RECIPE_TYPE + " LIKE ? ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[] {filter});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));

                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return recipeList;
    }

    public void createRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, recipe.getName());
        values.put(RECIPE_INGREDIENTS, recipe.getIngredients());
        values.put(RECIPE_STEPS, recipe.getSteps());
        values.put(RECIPE_TYPE, recipe.getType());

        db.insert(TABLE_RECIPE, null, values);
        db.close();
    }

    public int editRecipe(int id, String name, String ingredients, String steps, String type) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, name);
        values.put(RECIPE_INGREDIENTS, ingredients);
        values.put(RECIPE_STEPS, steps);
        values.put(RECIPE_TYPE, type);

        int row = db.update(
                TABLE_RECIPE,
                values,
                RECIPE_ID + "= ?",
                new String[] { String.valueOf(id)});

        db.close();

        return row;
    }

    public void deleteRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TABLE_RECIPE,
                RECIPE_ID + "= ?",
                new String[] { String.valueOf(recipe.getId())});
        db.close();
    }

    public int getRecipesCount() {
        String countQuery = "SELECT * FROM " + TABLE_RECIPE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }
}