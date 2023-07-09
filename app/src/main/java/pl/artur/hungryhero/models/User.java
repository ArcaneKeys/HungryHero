package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

public class User {
    @PropertyName("accountType")
    private String accountType;
    @PropertyName("email")
    private String email;
    @PropertyName("uid")
    private String uid;

    public User(String accountType, String email, String uid) {
        this.accountType = accountType;
        this.email = email;
        this.uid = uid;
    }

    public String getType() {
        return accountType;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
}
