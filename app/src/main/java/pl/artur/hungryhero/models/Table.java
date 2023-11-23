package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.List;

public class Table implements Parcelable {
    @PropertyName("number")
    private int number;
    @PropertyName("capacity")
    private int capacity;
    @PropertyName("reservation")
    private List<Reservation> reservations;
    private String tableId;

    @Exclude
    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Table(int number, int capacity, List<Reservation> reservations) {
        this.number = number;
        this.capacity = capacity;
        this.reservations = reservations;
    }

    public Table(int number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public Table() {}

    @PropertyName("number")
    public int getNumber() {
        return number;
    }

    @PropertyName("capacity")
    public int getCapacity() {
        return capacity;
    }

    @PropertyName("reservation")
    public List<Reservation> getReservations() {
        return reservations;
    }

    // Parcelable constructor
    protected Table(Parcel in) {
        number = in.readInt();
        capacity = in.readInt();
        reservations = in.createTypedArrayList(Reservation.CREATOR);
        tableId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(number);
        parcel.writeInt(capacity);
        parcel.writeTypedList(reservations);
        parcel.writeString(tableId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Create a Parcelable.Creator for the Table class
    public static final Parcelable.Creator<Table> CREATOR = new Parcelable.Creator<Table>() {
        @Override
        public Table createFromParcel(Parcel in) {
            return new Table(in);
        }

        @Override
        public Table[] newArray(int size) {
            return new Table[size];
        }
    };
}
