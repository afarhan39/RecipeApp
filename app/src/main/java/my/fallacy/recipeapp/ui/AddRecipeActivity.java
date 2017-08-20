package my.fallacy.recipeapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.fallacy.recipeapp.R;
import my.fallacy.recipeapp.handler.DatabaseHandler;
import my.fallacy.recipeapp.handler.XMLPullParserHandler;
import my.fallacy.recipeapp.model.Recipe;
import my.fallacy.recipeapp.model.RecipeType;

/**
 * Created by amirf on 20/08/2017.
 */

public class AddRecipeActivity extends AppCompatActivity {

    @BindView(R.id.et_recipe_name) EditText etRecipeName;
    @BindView(R.id.et_recipe_ingredients) EditText etRecipeIngredients;
    @BindView(R.id.et_recipe_steps) EditText etRecipeSteps;
    @BindView(R.id.s_recipe_type) Spinner sRecipeType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_recipe);
        ButterKnife.bind(this);
        setupToolbar();
        setupSpinnerFilter();
    }

    private void setupSpinnerFilter() {
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            List<RecipeType> recipeTypes = parser.parse(getAssets().open("recipetypes.xml"));

            ArrayAdapter<RecipeType> spinnerArrayAdapter =
                    new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, recipeTypes);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sRecipeType.setAdapter(spinnerArrayAdapter);
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.f_done)
    public void ocFDone() {

        if (validateForm())
        {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            db.createRecipe(new Recipe(
                    etRecipeName.getText().toString(),
                    etRecipeIngredients.getText().toString(),
                    etRecipeSteps.getText().toString(),
                    sRecipeType.getSelectedItem().toString()));

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    //to check whether all field is not null, preventing null error
    private boolean validateForm() {
        boolean result = true;
        if (etRecipeName.getText().toString().isEmpty()) {
            etRecipeName.setError("Required");
            result = false;
        } else {
            etRecipeName.setError(null);
        }
        if (etRecipeIngredients.getText().toString().isEmpty()) {
            etRecipeIngredients.setError("Required");
            result = false;
        } else {
            etRecipeIngredients.setError(null);
        }
        if (etRecipeSteps.getText().toString().isEmpty()) {
            etRecipeSteps.setError("Required");
            result = false;
        } else {
            etRecipeSteps.setError(null);
        }

        return result;
    }
}
