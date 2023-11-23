package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.time.LocalDateTime;

public class Reservation implements Parcelable {
    @PropertyName("userId")
    private String userId;
    @PropertyName("startTime")
    private String startTime;
    @PropertyName("endTime")
    private String endTime;
    @PropertyName("date")
    private int date;
    @PropertyName("createdAt")
    private String createdAt;

    private String reservationId;

    @Exclude
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public Reservation(String userId, String startTime, String endTime, int date, String createdAt) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.createdAt = createdAt;
    }

    public Reservation() {}

    @PropertyName("userId")
    public String getUserId() {
        return userId;
    }

    @PropertyName("startTime")
    public String getStartTime() {
        return startTime;
    }

    @PropertyName("endTime")
    public String getEndTime() {
        return endTime;
    }

    @PropertyName("date")
    public int getDate() { return date;}

    @PropertyName("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    // Parcelable constructor
    protected Reservation(Parcel in) {
        userId = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        date = in.readInt();
        createdAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeInt(date);
        parcel.writeString(createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Create a Parcelable.Creator for the Reservation class
    public static final Parcelable.Creator<Reservation> CREATOR = new Parcelable.Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };
}
