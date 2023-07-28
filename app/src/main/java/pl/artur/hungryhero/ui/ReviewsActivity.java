package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.ReviewsAdapter;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Reviews;
import pl.artur.hungryhero.utils.FirebaseManager;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReviews;
    private ReviewsAdapter reviewsAdapter;
    private List<Reviews> reviewsList = new ArrayList<>();;
    private Button buttonAddReview;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference restaurantRef = db.collection("Restaurant");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        buttonAddReview = findViewById(R.id.buttonAddReview);

        // Inicjalizuj RecyclerView
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        // Odczytaj dane recenzji z intentu
        Intent intent = getIntent();
        if (intent != null) {
            String restaurantId = intent.getStringExtra("restaurantId");
            if (restaurantId != null) {
                fetchAllReviews(restaurantId);

                buttonAddReview.setOnClickListener(v -> {
                    showAddReviewDialog(restaurantId);
                });
            }
        }

        // Stwórz adapter i przypisz go do RecyclerView
        reviewsAdapter = new ReviewsAdapter(this, reviewsList);
        recyclerViewReviews.setAdapter(reviewsAdapter);

        // Ustaw tytuł na pasku narzędziowym
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reviews");
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
            // Odczytaj rating i recenzję z widoku i zapisz je w bazie danych
            float rating = ratingBar.getRating();
            String comment = editTextReviewText.getText().toString();
            currentUser = FirebaseManager.getCurrentUser();
            String author = currentUser.getDisplayName();
            String userId = currentUser.getUid();

            Reviews addReview = new Reviews(userId, rating, comment, author);

            restaurantRef.document(restaurantId).collection("reviews").add(addReview)
                    .addOnSuccessListener(documentReference -> {
                        addReview.setReviewId(documentReference.getId());
                        reviewsList.add(addReview);
                        reviewsAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Wystąpił błąd podczas dodawania recenzji.", Toast.LENGTH_SHORT).show();
                    });
        });

        alertDialog.show();
    }

    private void fetchAllReviews(String restaurantId) {
        // Pobierz referencję do podkolekcji "reviews" dla konkretnego restaurantId
        CollectionReference reviewsRef = restaurantRef.document(restaurantId).collection("reviews");

        // Zdefiniuj zapytanie, które pobierze wszystkie dokumenty z podkolekcji "reviews"
        Task<QuerySnapshot> query = reviewsRef.get();

        query.addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                Reviews review = documentSnapshot.toObject(Reviews.class);
                reviewsList.add(review);
            }
            reviewsAdapter.notifyDataSetChanged();
        });
    }
}