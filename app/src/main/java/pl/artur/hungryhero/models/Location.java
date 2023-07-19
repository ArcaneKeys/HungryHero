package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

public class Location {
    @PropertyName("latitude")
    private double latitude;
    @PropertyName("longitude")
    private double longitude;
    @PropertyName("city")
    private String city;
    @PropertyName("postal_code")
    private String postalCode;
    @PropertyName("street")
    private String street;
    @PropertyName("house_number")
    private String houseNumber;

    public Location(double latitude, double longitude, String city, String postalCode, String street, String houseNumber) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public Location() {}

    @PropertyName("latitude")
    public double getLatitude() {
        return latitude;
    }

    @PropertyName("longitude")
    public double getLongitude() {
        return longitude;
    }

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
}
