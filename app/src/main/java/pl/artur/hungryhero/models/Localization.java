package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;
import com.google.firebase.firestore.GeoPoint;

public class Localization {
    @PropertyName("city")
    private String city;
    @PropertyName("postalCode")
    private String postalCode;
    @PropertyName("street")
    private String street;
    @PropertyName("houseNumber")
    private String houseNumber;
    @PropertyName("coordinates")
    private GeoPoint coordinates;
    public Localization(String city, String postalCode, String street, String houseNumber, GeoPoint coordinates) {
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public Localization() {}

    @PropertyName("city")
    public String getCity() {
        return city;
    }

    @PropertyName("postal_code")
    public String getPostalCode() {
        return postalCode;
    }

    @PropertyName("street")
    public String getStreet() {
        return street;
    }

    @PropertyName("house_number")
    public String getHouseNumber() {
        return houseNumber;
    }

    @PropertyName("coordinates")
    public GeoPoint getCoordinates() {
        return coordinates;
    }
}
