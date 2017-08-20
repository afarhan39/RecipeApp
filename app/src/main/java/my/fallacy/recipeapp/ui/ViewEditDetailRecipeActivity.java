package my.fallacy.recipeapp.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class ViewEditDetailRecipeActivity extends AppCompatActivity {

    @BindView(R.id.et_recipe_name) EditText etRecipeName;
    @BindView(R.id.et_recipe_ingredients) EditText etRecipeIngredients;
    @BindView(R.id.et_recipe_steps) EditText etRecipeSteps;
    @BindView(R.id.s_recipe_type) Spinner sRecipeType;

    @BindView(R.id.f_edit) FloatingActionButton fEdit;
    @BindView(R.id.f_done) FloatingActionButton fDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_update_recipe);
        ButterKnife.bind(this);
        setupSpinnerFilter();
        setupRecipe();
        setupToolbar();

        fDone.setVisibility(View.GONE);
        etRecipeName.setEnabled(false);
        etRecipeIngredients.setEnabled(false);
        etRecipeSteps.setEnabled(false);
        sRecipeType.setEnabled(false);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //when execute, enable edittext to be enabled back
    @OnClick(R.id.f_edit)
    public void ocFEdit() {

        fEdit.setVisibility(View.GONE);
        fDone.setVisibility(View.VISIBLE);
        etRecipeName.setEnabled(true);
        etRecipeIngredients.setEnabled(true);
        etRecipeSteps.setEnabled(true);
        sRecipeType.setEnabled(true);

    }

    //when execute, save entry into SQLite db
    @OnClick(R.id.f_done)
    public void ocFDone() {

        if (validateForm()) {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            db.editRecipe(
                    getIntent().getIntExtra("recipe_ID", 0),
                    etRecipeName.getText().toString(),
                    etRecipeIngredients.getText().toString(),
                    etRecipeSteps.getText().toString(),
                    sRecipeType.getSelectedItem().toString());

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    //provide view detail for recipe
    private void setupRecipe() {
        int recipeID = getIntent().getIntExtra("recipe_ID", 0);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Recipe recipe = db.getRecipe(recipeID);

        etRecipeName.setText(recipe.getName());
        etRecipeIngredients.setText(recipe.getIngredients());
        etRecipeSteps.setText(recipe.getSteps());
        setSpinnerRecipeType(recipe.getType());
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

    //function to gain text from SQLite db to be put inside Spinner
    private void setSpinnerRecipeType (String recipeType) {
        sRecipeType.setSelection(((ArrayAdapter<String>)sRecipeType.getAdapter()).getPosition(recipeType));
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
}
