package pl.artur.hungryhero.models;

import java.util.List;

public class Menu {
    private String name;
    private List<MenuItem> items;

    public Menu(String name, List<MenuItem> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getItems() {
        return items;
    }
}
