package pl.artur.hungryhero.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class Reviews implements Parcelable {
    @PropertyName("userId")
    private String userId;
    @PropertyName("rating")
    private double rating;
    @PropertyName("comment")
    private String comment;
    @PropertyName("author")
    private String author;

    private String reviewId;

    @Exclude
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public Reviews(String userId, double rating, String comment, String author) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.author = author;
    }

    public Reviews() {}

    @PropertyName("userId")
    public String getUserId() {
        return userId;
    }

    @PropertyName("rating")
    public double getRating() {
        return rating;
    }

    @PropertyName("comment")
    public String getComment() {
        return comment;
    }

    @PropertyName("author")
    public String getAuthor() {
        return author;
    }

    // Parcelable constructor
    protected Reviews(Parcel in) {
        userId = in.readString();
        rating = in.readDouble();
        comment = in.readString();
        author = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeDouble(rating);
        parcel.writeString(comment);
        parcel.writeString(author);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Create a Parcelable.Creator for the Reviews class
    public static final Parcelable.Creator<Reviews> CREATOR = new Parcelable.Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };
}
