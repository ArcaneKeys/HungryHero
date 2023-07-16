package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

public class Table {
    @PropertyName("number")
    private int number;
    @PropertyName("capacity")
    private int capacity;
    @PropertyName("isOccupied")
    private boolean isOccupied;

    public Table(int number, int capacity, boolean isOccupied) {
        this.number = number;
        this.capacity = capacity;
        this.isOccupied = isOccupied;
    }

    @PropertyName("number")
    public int getNumber() {
        return number;
    }

    @PropertyName("capacity")
    public int getCapacity() {
        return capacity;
    }

    @PropertyName("isOccupied")
    public boolean isOccupied() {
        return isOccupied;
    }
}
