package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class Table implements Parcelable {
    @PropertyName("number")
    private int number;
    @PropertyName("capacity")
    private int capacity;
    @PropertyName("isOccupied")
    private boolean isOccupied;

    private String tableId;

    @Exclude
    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Table(int number, int capacity, boolean isOccupied) {
        this.number = number;
        this.capacity = capacity;
        this.isOccupied = isOccupied;
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

    @PropertyName("isOccupied")
    public boolean isOccupied() {
        return isOccupied;
    }

    // Parcelable constructor
    protected Table(Parcel in) {
        number = in.readInt();
        capacity = in.readInt();
        isOccupied = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(number);
        parcel.writeInt(capacity);
        parcel.writeByte((byte) (isOccupied ? 1 : 0));
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
