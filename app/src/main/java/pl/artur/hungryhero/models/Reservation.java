package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

import java.time.LocalDateTime;

public class Reservation {
    @PropertyName("userId")
    private String userId;
    @PropertyName("tableId")
    private String tableId;
    @PropertyName("startTime")
    private LocalDateTime startTime;
    @PropertyName("endTime")
    private LocalDateTime endTime;
    @PropertyName("isActive")
    private boolean isActive;
    @PropertyName("createdAt")
    private LocalDateTime createdAt;

    public Reservation(String userId, String tableId, LocalDateTime startTime, LocalDateTime endTime, boolean isActive, LocalDateTime createdAt) {
        this.userId = userId;
        this.tableId = tableId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    @PropertyName("userId")
    public String getUserId() {
        return userId;
    }

    @PropertyName("tableId")
    public String getTableId() {
        return tableId;
    }

    @PropertyName("startTime")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @PropertyName("endTime")
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @PropertyName("isActive")
    public boolean isActive() {
        return isActive;
    }

    @PropertyName("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
