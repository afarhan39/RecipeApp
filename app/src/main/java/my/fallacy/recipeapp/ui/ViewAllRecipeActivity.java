package my.fallacy.recipeapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import my.fallacy.recipeapp.R;
import my.fallacy.recipeapp.adapter.RecipeAdapter;
import my.fallacy.recipeapp.handler.DatabaseHandler;
import my.fallacy.recipeapp.handler.XMLPullParserHandler;
import my.fallacy.recipeapp.model.Recipe;
import my.fallacy.recipeapp.model.RecipeType;

import static butterknife.OnItemSelected.Callback.NOTHING_SELECTED;

/**
 * Created by amirf on 20/08/2017.
 */

public class ViewAllRecipeActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {


    @BindView(R.id.starter) TextView starter;
    @BindView(R.id.recipeRecyclerView) RecyclerView mRecipeRecyclerView;
    @BindView(R.id.s_recipe_type) Spinner mSRecipeType;

    private RecipeAdapter mRecipeAdapter;
    private LinearLayoutManager mLayoutManager;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
        setupToolbar();
        setupSpinnerFilter();
        setupAdapter("");
    }

    private void setupSpinnerFilter() {
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            List<RecipeType> recipeTypes = parser.parse(getAssets().open("recipetypes.xml"));

            ArrayAdapter<RecipeType> spinnerArrayAdapter =
                    new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, recipeTypes);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSRecipeType.setAdapter(spinnerArrayAdapter);
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAdapter("");
    }

    @OnClick(R.id.f_add)
    public void ocFAdd(View view) {
        startActivity(new Intent(this, AddRecipeActivity.class));
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupAdapter(String filter) {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<Recipe> recipeList = null;
        if (filter.equals(""))
            recipeList = db.getAllRecipes();
        else
            recipeList = db.getFilteredRecipes(filter);

        if (!recipeList.isEmpty())
            starter.setVisibility(View.GONE);


        mRecipeAdapter = new RecipeAdapter(getBaseContext(), recipeList, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecipeRecyclerView.setLayoutManager(mLayoutManager);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);
    }

    @Override
    public void onItemClick(Integer recipeID) {
        Intent intent = new Intent(this, ViewEditDetailRecipeActivity.class);
        intent.putExtra("recipe_ID", recipeID);
        startActivity(intent);

    }

    @Override
    public void onItemDelete(final Recipe recipe) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewAllRecipeActivity.this);
        alert.setTitle("Confirm Delete?");
        alert.setMessage("Confirm to delete recipe.");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.deleteRecipe(recipe);
                setupAdapter("");
                Toast.makeText(ViewAllRecipeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @OnItemSelected(R.id.s_recipe_type)
    public void oisRecipeTypeFilter(Spinner spinner, int position) {
        //had to do workaround because ButterKnife always execute on start, resulting not showing
        //all of exisiting lists
        if (i == 0)
            setupAdapter("");
        else {
            Toast.makeText(this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            setupAdapter(spinner.getSelectedItem().toString());
        }
        i++;
    }

    @OnItemSelected(value = R.id.s_recipe_type, callback = NOTHING_SELECTED)
    void onNothingSelected() {
        setupAdapter("");
    }

    @OnClick(R.id.b_recipe_clear)
    public void ocFilterClear() {
        setupAdapter("");
    }
}

