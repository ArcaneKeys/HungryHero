package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.PropertyName;

public class OpeningHours implements Parcelable {
    @PropertyName("monday")
    private String monday;
    @PropertyName("tuesday")
    private String tuesday;
    @PropertyName("wednesday")
    private String wednesday;
    @PropertyName("thursday")
    private String thursday;
    @PropertyName("friday")
    private String friday;
    @PropertyName("saturday")
    private String saturday;
    @PropertyName("sunday")
    private String sunday;

    public OpeningHours(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public OpeningHours() {}

    @PropertyName("monday")
    public String getMonday() {
        return monday;
    }

    @PropertyName("tuesday")
    public String getTuesday() {
        return tuesday;
    }

    @PropertyName("wednesday")
    public String getWednesday() {
        return wednesday;
    }

    @PropertyName("thursday")
    public String getThursday() {
        return thursday;
    }

    @PropertyName("friday")
    public String getFriday() {
        return friday;
    }

    @PropertyName("saturday")
    public String getSaturday() {
        return saturday;
    }

    @PropertyName("sunday")
    public String getSunday() {
        return sunday;
    }

    // Parcelable constructor
    protected OpeningHours(Parcel in) {
        monday = in.readString();
        tuesday = in.readString();
        wednesday = in.readString();
        thursday = in.readString();
        friday = in.readString();
        saturday = in.readString();
        sunday = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(monday);
        parcel.writeString(tuesday);
        parcel.writeString(wednesday);
        parcel.writeString(thursday);
        parcel.writeString(friday);
        parcel.writeString(saturday);
        parcel.writeString(sunday);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Create a Parcelable.Creator for the OpeningHours class
    public static final Parcelable.Creator<OpeningHours> CREATOR = new Parcelable.Creator<OpeningHours>() {
        @Override
        public OpeningHours createFromParcel(Parcel in) {
            return new OpeningHours(in);
        }

        @Override
        public OpeningHours[] newArray(int size) {
            return new OpeningHours[size];
        }
    };
}
