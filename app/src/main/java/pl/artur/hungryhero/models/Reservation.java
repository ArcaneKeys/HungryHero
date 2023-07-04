package pl.artur.hungryhero.models;

import java.time.LocalDateTime;

public class Reservation {
    private String userId;
    private String tableId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private LocalDateTime createdAt;

    public Reservation(String userId, String tableId, LocalDateTime startTime, LocalDateTime endTime, boolean isActive, LocalDateTime createdAt) {
        this.userId = userId;
        this.tableId = tableId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getTableId() {
        return tableId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
