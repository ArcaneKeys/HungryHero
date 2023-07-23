package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

public class Contact implements Parcelable {
    @PropertyName("phone")
    private String phone;
    @PropertyName("email")
    private String email;
    @PropertyName("facebook")
    private String facebook;
    @PropertyName("website")
    private String website;
    @PropertyName("instagram")
    private String instagram;
    @PropertyName("webMenu")
    private String webMenu;

    public Contact(String phone, String email, String facebook, String website, String instagram, String webMenu) {
        this.phone = phone;
        this.email = email;
        this.facebook = facebook;
        this.website = website;
        this.instagram = instagram;
        this.webMenu = webMenu;
    }

    public Contact(){}

    @PropertyName("phone")
    public String getPhone() {
        return phone;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    @PropertyName("facebook")
    public String getFacebook() {
        return facebook;
    }

    @PropertyName("website")
    public String getWebsite() {
        return website;
    }

    @PropertyName("instagram")
    public String getInstagram() {
        return instagram;
    }

    @PropertyName("webMenu")
    public String getWebMenu() {
        return webMenu;
    }

    // Parcelable constructor
    protected Contact(Parcel in) {
        phone = in.readString();
        email = in.readString();
        facebook = in.readString();
        website = in.readString();
        instagram = in.readString();
        webMenu = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(facebook);
        parcel.writeString(website);
        parcel.writeString(instagram);
        parcel.writeString(webMenu);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Create a Parcelable.Creator for the Contact class
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
