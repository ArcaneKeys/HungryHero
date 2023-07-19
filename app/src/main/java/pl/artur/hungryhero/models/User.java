package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

public class User {
    @PropertyName("accountType")
    private String accountType;
    @PropertyName("userName")
    private String userName;
    @PropertyName("email")
    private String email;
    @PropertyName("phone")
    private String phone;
    @PropertyName("uid")
    private String uid;

    public User(String accountType, String userName, String email, String phone, String uid) {
        this.accountType = accountType;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
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

    @PropertyName("uid")
    public String getUid() {
        return uid;
    }
}

