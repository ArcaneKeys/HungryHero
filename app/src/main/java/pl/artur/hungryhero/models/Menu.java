package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;

public class Menu implements Parcelable {
    @PropertyName("menuName")
    private String menuName;
    @PropertyName("menuItem")
    private List<MenuItem> menuItems;
    private String menuId;

    @Exclude
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Menu(String menuName, List<MenuItem> menuItem) {
        this.menuName = menuName;
        this.menuItems = menuItem;
    }

    public Menu(String menuName) {
        this.menuName = menuName;
    }

    public Menu() {}

    @PropertyName("menuName")
    public String getMenuName() {
        return menuName;
    }

    @PropertyName("menuItem")
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    // Parcelable constructor
    protected Menu(Parcel in) {
        menuName = in.readString();
        menuItems = in.readParcelable(MenuItem.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(menuName);
        parcel.writeList(menuItems);
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
