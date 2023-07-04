package pl.artur.hungryhero.models;

public class Table {
    private int number;
    private int capacity;
    private boolean isOccupied;

    public Table(int number, int capacity, boolean isOccupied) {
        this.number = number;
        this.capacity = capacity;
        this.isOccupied = isOccupied;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
}
