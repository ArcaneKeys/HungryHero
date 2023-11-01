package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuItem implements Parcelable {
    @PropertyName("dishName")
    private String dishName;
    @PropertyName("description")
    private String description;
    @PropertyName("ingredients")
    private List<String> ingredients;
    @PropertyName("price")
    private double price;
    @PropertyName("photoUrl")
    private String photoUrl;

    private String itemId;

    @Exclude
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public MenuItem(String dishName, String description, List<String> ingredients, double price, String photoUrl) {
        this.dishName = dishName;
        this.description = description;
        this.ingredients = ingredients;
        this.price = price;
        this.photoUrl = photoUrl;
    }

    public MenuItem() {}

    @PropertyName("dishName")
    public String getDishName() {
        return dishName;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("ingredients")
    public List<String> getIngredients() {
        return ingredients;
    }

    @PropertyName("price")
    public double getPrice() {
        return price;
    }

    @PropertyName("photoUrl")
    public String getPhotoUrl() {
        return photoUrl;
    }

    // Parcelable constructor
    protected MenuItem(Parcel in) {
        dishName = in.readString();
        description = in.readString();
        ingredients = in.createStringArrayList();
        price = in.readDouble();
        photoUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dishName);
        parcel.writeString(description);
        parcel.writeStringList(ingredients);
        parcel.writeDouble(price);
        parcel.writeString(photoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Create a Parcelable.Creator for the MenuItem class
    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("dishName", dishName);
        map.put("description", description);
        map.put("ingredients", ingredients);
        map.put("price", price);
        map.put("photoUrl", photoUrl);
        return map;
    }
}
