package pl.artur.hungryhero.models;

import java.util.List;

public class MenuItem {
    private String name;
    private String description;
    private List<String> ingredients;
    private double price;
    private String photoUrl;

    public MenuItem(String name, String description, List<String> ingredients, double price, String photoUrl) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.price = price;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public double getPrice() {
        return price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
