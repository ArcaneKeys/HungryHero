package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class Menu {
    @PropertyName("name")
    private String name;
    @PropertyName("items")
    private List<MenuItem> items;

    public Menu(String name, List<MenuItem> items) {
        this.name = name;
        this.items = items;
    }

    public Menu() {}

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("items")
    public List<MenuItem> getItems() {
        return items;
    }
}
