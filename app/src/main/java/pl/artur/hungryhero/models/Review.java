package pl.artur.hungryhero.models;

public class Review {
    private String userId;
    private double rating;
    private String comment;
    private String author;

    public Review(String userId, double rating, String comment, String author) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.author = author;
    }

    public String getUserId() {
        return userId;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getAuthor() {
        return author;
    }
}
