package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

public class OpeningHours {
    @PropertyName("monday")
    private String monday;
    @PropertyName("tuesday")
    private String tuesday;
    @PropertyName("wednesday")
    private String wednesday;
    @PropertyName("thursday")
    private String thursday;
    @PropertyName("friday")
    private String friday;
    @PropertyName("saturday")
    private String saturday;
    @PropertyName("sunday")
    private String sunday;

    public OpeningHours(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    @PropertyName("monday")
    public String getMonday() {
        return monday;
    }

    @PropertyName("tuesday")
    public String getTuesday() {
        return tuesday;
    }

    @PropertyName("wednesday")
    public String getWednesday() {
        return wednesday;
    }

    @PropertyName("thursday")
    public String getThursday() {
        return thursday;
    }

    @PropertyName("friday")
    public String getFriday() {
        return friday;
    }

    @PropertyName("saturday")
    public String getSaturday() {
        return saturday;
    }

    @PropertyName("sunday")
    public String getSunday() {
        return sunday;
    }
}
