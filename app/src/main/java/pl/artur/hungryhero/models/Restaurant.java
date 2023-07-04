package pl.artur.hungryhero.models;

import java.util.List;

public class Restaurant {
    private String name;
    private String description;
    private Location location;
    private OpeningHours openingHours;
    private Contact contact;
    private List<Menu> menus;
    private List<Table> tables;
    private List<Reservation> reservations;
    private List<Review> reviews;

    public Restaurant(String name, String description, Location location, OpeningHours openingHours, Contact contact, List<Menu> menus, List<Table> tables, List<Reservation> reservations, List<Review> reviews) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.openingHours = openingHours;
        this.contact = contact;
        this.menus = menus;
        this.tables = tables;
        this.reservations = reservations;
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public Contact getContact() {
        return contact;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public List<Table> getTables() {
        return tables;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
