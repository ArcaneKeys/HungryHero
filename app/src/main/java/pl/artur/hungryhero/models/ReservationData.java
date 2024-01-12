package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ReservationData implements Parcelable {
    private Reservation reservation;
    private User user;
    private Restaurant restaurant;

    public ReservationData(Reservation reservation, User user, Restaurant restaurant) {
        this.reservation = reservation;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    protected ReservationData(Parcel in) {
        reservation = in.readParcelable(Reservation.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        restaurant = in.readParcelable(Restaurant.class.getClassLoader());
    }

    public static final Creator<ReservationData> CREATOR = new Creator<ReservationData>() {
        @Override
        public ReservationData createFromParcel(Parcel in) {
            return new ReservationData(in);
        }

        @Override
        public ReservationData[] newArray(int size) {
            return new ReservationData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(reservation, flags);
        parcel.writeParcelable(user, flags);
        parcel.writeParcelable(restaurant, flags);
    }
}
