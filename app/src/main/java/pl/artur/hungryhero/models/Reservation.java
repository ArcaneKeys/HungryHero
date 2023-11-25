package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Reservation implements Parcelable {
    @PropertyName("userId")
    private String userId;
    @PropertyName("startTime")
    private String startTime;
    @PropertyName("endTime")
    private String endTime;
    @PropertyName("numberOfPeople")
    private int numberOfPeople;
    @PropertyName("date")
    private long date;
    @PropertyName("createdAt")
    private String createdAt;
    @PropertyName("note")
    private String note;

    private String reservationId;

    @Exclude
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public Reservation(String userId, String startTime, String endTime, long date, String createdAt, int numberOfPeople) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.createdAt = createdAt;
        this.numberOfPeople = numberOfPeople;
    }

    public Reservation(String userId, String startTime, String endTime, long date, String createdAt, int numberOfPeople, String note) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.createdAt = createdAt;
        this.numberOfPeople = numberOfPeople;
        this.note = note;
    }

    public Reservation() {}

    @PropertyName("numberOfPeople")
    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    @PropertyName("note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

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
    public long getDate() { return date;}

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
        numberOfPeople = in.readInt();
        note = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeLong(date);
        parcel.writeString(createdAt);
        parcel.writeInt(numberOfPeople);
        parcel.writeString(note);
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("numberOfPeople", numberOfPeople);
        map.put("date", date);
        map.put("createdAt", createdAt);
        map.put("note", note);
        return map;
    }

}
