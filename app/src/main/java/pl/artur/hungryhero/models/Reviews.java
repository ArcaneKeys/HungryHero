package pl.artur.hungryhero.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class Reviews {
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
}
