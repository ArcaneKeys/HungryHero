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

    @PropertyName("accountType")
    public String getType() {
        return accountType;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    @PropertyName("uid")
    public String getUid() {
        return uid;
    }
}
