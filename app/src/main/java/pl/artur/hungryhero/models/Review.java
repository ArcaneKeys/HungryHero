package pl.artur.hungryhero.models;

import com.google.firebase.database.PropertyName;

public class Review {
    @PropertyName("userId")
    private String userId;
    @PropertyName("rating")
    private double rating;
    @PropertyName("comment")
    private String comment;
    @PropertyName("author")
    private String author;

    public Review(String userId, double rating, String comment, String author) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.author = author;
    }

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
