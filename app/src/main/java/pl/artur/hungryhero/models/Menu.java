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
    @PropertyName("dishName")
    private String dishName;
    @PropertyName("description")
    private String description;
    @PropertyName("photoUrl")
    private String photoUrl;
    @PropertyName("price")
    private double price;
    @PropertyName("ingredients")
    private List<String> ingredients;
    private String menuId;

    @Exclude
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Menu(String menuName, String dishName, String description, String photoUrl, double price, List<String> ingredients) {
        this.menuName = menuName;
        this.dishName = dishName;
        this.description = description;
        this.photoUrl = photoUrl;
        this.price = price;
        this.ingredients = ingredients;
    }

    public Menu() {}

    @PropertyName("menuName")
    public String getMenuName() {
        return menuName;
    }
    @PropertyName("dishName")
    public String getDishName() {
        return dishName;
    }
    @PropertyName("description")
    public String getDescription() {
        return description;
    }
    @PropertyName("photoUrl")
    public String getPhotoUrl() {
        return photoUrl;
    }
    @PropertyName("price")
    public double getPrice() {
        return price;
    }
    @PropertyName("price")
    public List<String> getIngredients() {
        return ingredients;
    }

    // Parcelable constructor
    protected Menu(Parcel in) {
        menuName = in.readString();
        dishName = in.readString();
        description = in.readString();
        photoUrl = in.readString();
        price = in.readDouble();
        ingredients = new ArrayList<>();
        in.readList(ingredients, String.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(menuName);
        parcel.writeString(dishName);
        parcel.writeString(description);
        parcel.writeString(photoUrl);
        parcel.writeDouble(price);
        parcel.writeList(ingredients);
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
