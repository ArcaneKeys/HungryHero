package pl.artur.hungryhero.models;

public class Contact {
    private String phone;
    private String email;
    private String facebook;
    private String website;
    private String instagram;
    private String webMenu;

    public Contact(String phone, String email, String facebook, String website, String instagram, String webMenu) {
        this.phone = phone;
        this.email = email;
        this.facebook = facebook;
        this.website = website;
        this.instagram = instagram;
        this.webMenu = webMenu;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getWebsite() {
        return website;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getWebMenu() {
        return webMenu;
    }
}
