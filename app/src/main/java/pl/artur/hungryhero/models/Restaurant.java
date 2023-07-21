package pl.artur.hungryhero.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import java.util.List;

public class Restaurant {
    @PropertyName("name")
    private String name;
    @PropertyName("description")
    private String description;
    @PropertyName("localization")
    private Localization localization;
    @PropertyName("openingHours")
    private OpeningHours openingHours;
    @PropertyName("contact")
    private Contact contact;
    @PropertyName("menu")
    private List<Menu> menus;
    @PropertyName("tables")
    private List<Table> tables;
    @PropertyName("reservation")
    private List<Reservation> reservations;
    @PropertyName("reviews")
    private List<Reviews> reviews;
    private String restaurantId;

    @Exclude
    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Restaurant(String name, String description, Localization localization, OpeningHours openingHours, Contact contact, List<Menu> menus, List<Table> tables, List<Reservation> reservations, List<Reviews> reviews) {
        this.name = name;
        this.description = description;
        this.localization = localization;
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

    @PropertyName("localization")
    public Localization getLocalization() {
        return localization;
    }

    @PropertyName("openingHours")
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    @PropertyName("contact")
    public Contact getContact() {
        return contact;
    }

    @PropertyName("menu")
    public List<Menu> getMenus() {
        return menus;
    }

    @PropertyName("tables")
    public List<Table> getTables() {
        return tables;
    }

    @PropertyName("reservation")
    public List<Reservation> getReservations() {
        return reservations;
    }

    @PropertyName("reviews")
    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}
