package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;

public class Menu implements Parcelable {
    @PropertyName("name")
    private String name;
    @PropertyName("items")
    private List<MenuItem> items;

    private String menuId;

    @Exclude
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Menu(String name, List<MenuItem> items) {
        this.name = name;
        this.items = items;
    }

    public Menu() {}

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("items")
    public List<MenuItem> getItems() {
        return items;
    }

    // Parcelable constructor
    protected Menu(Parcel in) {
        name = in.readString();
        items = new ArrayList<>();
        in.readList(items, MenuItem.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Create a Parcelable.Creator for the Menu class
    public static final Parcelable.Creator<Menu> CREATOR = new Parcelable.Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };
}
