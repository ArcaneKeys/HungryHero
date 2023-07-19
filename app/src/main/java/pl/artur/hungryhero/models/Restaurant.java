package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;
import java.util.List;

public class Restaurant {
    @PropertyName("name")
    private String name;
    @PropertyName("description")
    private String description;
    @PropertyName("location")
    private Location location;
    @PropertyName("openingHours")
    private OpeningHours openingHours;
    @PropertyName("contact")
    private Contact contact;
    @PropertyName("menus")
    private List<Menu> menus;
    @PropertyName("tables")
    private List<Table> tables;
    @PropertyName("reservations")
    private List<Reservation> reservations;
    @PropertyName("reviews")
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

    public Restaurant() {}

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("location")
    public Location getLocation() {
        return location;
    }

    @PropertyName("openingHours")
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    @PropertyName("contact")
    public Contact getContact() {
        return contact;
    }

    @PropertyName("menus")
    public List<Menu> getMenus() {
        return menus;
    }

    @PropertyName("tables")
    public List<Table> getTables() {
        return tables;
    }

    @PropertyName("reservations")
    public List<Reservation> getReservations() {
        return reservations;
    }

    @PropertyName("reviews")
    public List<Review> getReviews() {
        return reviews;
    }
}
