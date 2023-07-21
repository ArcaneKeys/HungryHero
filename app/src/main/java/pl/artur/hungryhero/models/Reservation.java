package pl.artur.hungryhero.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.time.LocalDateTime;

public class Reservation {
    @PropertyName("userId")
    private String userId;
    @PropertyName("tableId")
    private String tableId;
    @PropertyName("startTime")
    private String startTime;
    @PropertyName("endTime")
    private String endTime;
    @PropertyName("isActive")
    private boolean isActive;
    @PropertyName("date")
    private String date;
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

    public Reservation(String userId, String tableId, String startTime, String endTime, boolean isActive, String date, String createdAt) {
        this.userId = userId;
        this.tableId = tableId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
        this.date = date;
        this.createdAt = createdAt;
    }

    public Reservation() {}

    @PropertyName("userId")
    public String getUserId() {
        return userId;
    }

    @PropertyName("tableId")
    public String getTableId() {
        return tableId;
    }

    @PropertyName("startTime")
    public String getStartTime() {
        return startTime;
    }

    @PropertyName("endTime")
    public String getEndTime() {
        return endTime;
    }

    @PropertyName("isActive")
    public boolean isActive() {
        return isActive;
    }

    @PropertyName("date")
    public String getDate() { return date;}

    @PropertyName("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }
}
