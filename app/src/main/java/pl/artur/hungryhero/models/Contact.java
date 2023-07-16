package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

public class Contact {
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
}
