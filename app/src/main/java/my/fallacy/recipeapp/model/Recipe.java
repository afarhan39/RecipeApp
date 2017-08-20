package my.fallacy.recipeapp.model;

/**
 * Created by amirf on 20/08/2017.
 */

public class Recipe {
    private int id;
    private String name;
    private String ingredients;
    private String steps;
    private String type;

    public Recipe(int id, String name, String ingredients, String steps, String type) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.type = type;
    }

    public Recipe(String name, String ingredients, String steps, String type) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.type = type;
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
