package my.fallacy.recipeapp.model;

/**
 * Created by amirf on 20/08/2017.
 */

public class RecipeType {
    private String id;
    private String name;

    public RecipeType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
