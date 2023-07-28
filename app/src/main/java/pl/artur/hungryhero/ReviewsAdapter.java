package pl.artur.hungryhero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.artur.hungryhero.models.Reviews;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private List<Reviews> reviewsList;

    public ReviewsAdapter(Context context, List<Reviews> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reviews review = reviewsList.get(position);
        holder.textAuthor.setText(review.getAuthor());
        holder.textRating.setRating((float) review.getRating());
        holder.textReview.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar textRating;
        TextView textReview;
        TextView textAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textRating = itemView.findViewById(R.id.textRating);
            textReview = itemView.findViewById(R.id.textReview);
            textAuthor = itemView.findViewById(R.id.textAuthor);
        }
    }
}