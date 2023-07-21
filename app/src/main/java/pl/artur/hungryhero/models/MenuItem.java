package pl.artur.hungryhero.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.List;

public class MenuItem {
    @PropertyName("name")
    private String name;
    @PropertyName("description")
    private String description;
    @PropertyName("ingredients")
    private List<String> ingredients;
    @PropertyName("price")
    private double price;
    @PropertyName("photoUrl")
    private String photoUrl;

    private String itemId;

    @Exclude
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public MenuItem(String name, String description, List<String> ingredients, double price, String photoUrl) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.price = price;
        this.photoUrl = photoUrl;
    }

    public MenuItem() {}

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("ingredients")
    public List<String> getIngredients() {
        return ingredients;
    }

    @PropertyName("price")
    public double getPrice() {
        return price;
    }

    @PropertyName("photoUrl")
    public String getPhotoUrl() {
        return photoUrl;
    }
}
