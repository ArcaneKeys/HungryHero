package pl.artur.hungryhero.models;

public class User {
    private String accountType;
    private String email;
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
