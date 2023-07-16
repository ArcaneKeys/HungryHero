package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

public class Location {
    @PropertyName("latitude")
    private double latitude;
    @PropertyName("longitude")
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @PropertyName("latitude")
    public double getLatitude() {
        return latitude;
    }

    @PropertyName("longitude")
    public double getLongitude() {
        return longitude;
    }
}
