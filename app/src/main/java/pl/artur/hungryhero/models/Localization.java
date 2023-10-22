package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.PropertyName;
import com.google.firebase.firestore.GeoPoint;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Localization implements Parcelable {
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
        this.coordinates = coordinates;
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

    // Parcelable constructor
    protected Localization(Parcel in) {
        city = in.readString();
        postalCode = in.readString();
        street = in.readString();
        houseNumber = in.readString();
        double latitude = in.readDouble();
        double longitude = in.readDouble();
        coordinates = new GeoPoint(latitude, longitude);
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeString(postalCode);
        parcel.writeString(street);
        parcel.writeString(houseNumber);
        parcel.writeDouble(coordinates.getLatitude());
        parcel.writeDouble(coordinates.getLongitude());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Create a Parcelable.Creator for the Localization class
    public static final Parcelable.Creator<Localization> CREATOR = new Parcelable.Creator<Localization>() {
        @Override
        public Localization createFromParcel(Parcel in) {
            return new Localization(in);
        }

        @Override
        public Localization[] newArray(int size) {
            return new Localization[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("city", city);
        map.put("postalCode", postalCode);
        map.put("street", street);
        map.put("houseNumber", houseNumber);
        map.put("coordinates", coordinates);
        return map;
    }

}
