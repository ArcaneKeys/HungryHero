package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Table implements Parcelable {
    @PropertyName("number")
    private String number;
    @PropertyName("capacity")
    private int capacity;
    @PropertyName("reservation")
    private List<Reservation> reservations;
    private String tableId;
    @Exclude
    private List<String> selectedHours = new ArrayList<>();

    public List<String> getSelectedHours() {
        return selectedHours;
    }

    public void setSelectedHours(List<String> selectedHours) {
        this.selectedHours = selectedHours;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Table(String number, int capacity, List<Reservation> reservations) {
        this.number = number;
        this.capacity = capacity;
        this.reservations = reservations;
    }

    public Table(String number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public Table() {}

    @PropertyName("number")
    public String getNumber() {
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
        number = in.readString();
        capacity = in.readInt();
        reservations = in.createTypedArrayList(Reservation.CREATOR);
        tableId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(number);
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
