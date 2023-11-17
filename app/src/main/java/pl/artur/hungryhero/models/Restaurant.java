package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import java.util.List;

public class Restaurant implements Parcelable {
    @PropertyName("name")
    private String name;
    @PropertyName("description")
    private String description;
    @PropertyName("photoUrl")
    private String photoUrl;
    @PropertyName("localization")
    private Localization localization;
    @PropertyName("openingHours")
    private OpeningHours openingHours;
    @PropertyName("contact")
    private Contact contact;
    @PropertyName("menu")
    private List<Menu> menu;
    @PropertyName("tables")
    private List<Table> tables;
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

    public Restaurant(String name, String description, String photoUrl, Localization localization, OpeningHours openingHours, Contact contact, List<Menu> menu, List<Table> tables, List<Reviews> reviews) {
        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
        this.localization = localization;
        this.openingHours = openingHours;
        this.contact = contact;
        this.menu = menu;
        this.tables = tables;
        this.reviews = reviews;
    }

    public Restaurant() {}

    @PropertyName("photoUrl")
    public String getPhotoUrl() {
        return photoUrl;
    }

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
    public List<Menu> getMenu() {
        return menu;
    }

    @PropertyName("tables")
    public List<Table> getTables() {
        return tables;
    }

    @PropertyName("reviews")
    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    // Parcelable constructor
    protected Restaurant(Parcel in) {
        name = in.readString();
        description = in.readString();
        photoUrl = in.readString();
        localization = in.readParcelable(Localization.class.getClassLoader());
        openingHours = in.readParcelable(OpeningHours.class.getClassLoader());
        contact = in.readParcelable(Contact.class.getClassLoader());
        menu = in.createTypedArrayList(Menu.CREATOR);
        tables = in.createTypedArrayList(Table.CREATOR);
        reviews = in.createTypedArrayList(Reviews.CREATOR);
        restaurantId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(photoUrl);
        parcel.writeParcelable(localization, i);
        parcel.writeParcelable(openingHours, i);
        parcel.writeParcelable(contact, i);
        parcel.writeTypedList(menu);
        parcel.writeTypedList(tables);
        parcel.writeTypedList(reviews);
        parcel.writeString(restaurantId);
    }

    // Create a Parcelable.Creator for the Restaurant class
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
