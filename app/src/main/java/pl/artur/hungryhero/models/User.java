package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class User implements Parcelable {
    @PropertyName("accountType")
    private String accountType;
    @PropertyName("userName")
    private String userName;
    @PropertyName("email")
    private String email;
    @PropertyName("phone")
    private String phone;

    public User(String accountType, String userName, String email, String phone) {
        this.accountType = accountType;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
    }

    public User() {}

    @PropertyName("accountType")
    public String getAccountType() {
        return accountType;
    }

    @PropertyName("userName")
    public String getUserName() {
        return userName;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    @PropertyName("phone")
    public String getPhone() {
        return phone;
    }

    protected User(Parcel in) {
        accountType = in.readString();
        userName = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(accountType);
        parcel.writeString(userName);
        parcel.writeString(email);
        parcel.writeString(phone);
    }

}

