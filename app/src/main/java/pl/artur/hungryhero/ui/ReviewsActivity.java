package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.adapters.ReviewsAdapter;
import pl.artur.hungryhero.models.MenuItem;
import pl.artur.hungryhero.models.Reviews;
import pl.artur.hungryhero.module.helper.FirebaseHelper;
import pl.artur.hungryhero.utils.FirebaseManager;

@AndroidEntryPoint
public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReviews;
    private ReviewsAdapter reviewsAdapter;
    private List<Reviews> reviewsList = new ArrayList<>();;
    private Button buttonAddReview;
    private String restaurantId;
    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        buttonAddReview = findViewById(R.id.buttonAddReview);

        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        restaurantId = getIntent().getStringExtra("restaurantId");

        firebaseHelper.isRestaurant(isRestaurant -> {
            if (isRestaurant) {
                buttonAddReview.setVisibility(View.GONE);
            }
        });

        if (restaurantId == null || restaurantId.isEmpty()) {
            restaurantId = firebaseHelper.getCurrentUid();
            buttonAddReview.setVisibility(View.GONE);
        } else {
            buttonAddReview.setOnClickListener(v -> showAddReviewDialog(restaurantId));
        }

        fetchAllReviews(restaurantId);

        reviewsAdapter = new ReviewsAdapter(this, reviewsList);
        recyclerViewReviews.setAdapter(reviewsAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Opinie");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showAddReviewDialog(String restaurantId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_add_review, null);
        dialogBuilder.setView(dialogView);

        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText editTextReviewText = dialogView.findViewById(R.id.editTextReviewText);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        AlertDialog alertDialog = dialogBuilder.create();

        buttonSave.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = editTextReviewText.getText().toString();
            FirebaseUser currentUser = firebaseHelper.getCurrentUser();
            String author = currentUser.getDisplayName();
            String userId = currentUser.getUid();

            Reviews addReview = new Reviews(userId, rating, comment, author);

            firebaseHelper.addReview(restaurantId, addReview)
                    .addOnSuccessListener(documentReference -> {
                        addReview.setReviewId(documentReference.getId());
                        reviewsList.add(addReview);
                        reviewsAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Wystąpił błąd podczas dodawania recenzji.", Toast.LENGTH_SHORT).show()
                    );
        });

        alertDialog.show();
    }

    private void fetchAllReviews(String restaurantId) {
        firebaseHelper.getReviewsCollectionRef(restaurantId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    reviewsList.clear();
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                            Reviews review = snapshot.toObject(Reviews.class);
                            if (review != null) {
                                reviewsList.add(review);
                            }
                            reviewsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}