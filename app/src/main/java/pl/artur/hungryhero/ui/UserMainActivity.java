package pl.artur.hungryhero.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.adapters.RestaurantAdapter;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.utils.FirebaseManager;


public class UserMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> allRestaurants = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference restaurantRef = db.collection("Restaurant");

    private DrawerManager drawerManager;
    private TextView userName;
    private TextView userEmail;
    private View headerView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageButton navButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // Inicjalizacja zmiennych
        navButton = findViewById(R.id.nav_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);

        restaurantAdapter = new RestaurantAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(restaurantAdapter);

        fetchAllRestaurants();

        setSupportActionBar(toolbar); // Ustawienie paska narzędzi

        headerView = navigationView.getHeaderView(0);
        userEmail = headerView.findViewById(R.id.user_email);
        userName = headerView.findViewById(R.id.user_name);

        // Dodaj obsługę kliknięcia dla ShapeableImageView
        ShapeableImageView userIcon = headerView.findViewById(R.id.user_icon);
        userIcon.setOnClickListener(v -> {
            // Przekierowanie do Layoutu zmiany danych użytkownika
            Intent intent = new Intent(UserMainActivity.this, ChangeUserDataActivity.class);
            startActivity(intent);
        });

        mAuth = FirebaseManager.getAuthInstance();

        mUser = mAuth.getCurrentUser();
        String userUid = mUser.getUid();

        fetchUserDataFromFirestore(userUid);

        drawerManager = new DrawerManager(navButton, drawerLayout, navigationView, mAuth, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupFirestoreListener(mUser.getUid());

        restaurantRef.addSnapshotListener(this, (value, error) -> {
            if (error != null)
                return;

            if (value == null)
                return;
            allRestaurants.clear();
            for (QueryDocumentSnapshot documentSnapshot : value){
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                restaurant.setRestaurantId(documentSnapshot.getId());

                fetchTablesForRestaurant(restaurant.getRestaurantId(), restaurant);

                allRestaurants.add(restaurant);
            }
        });
    }

    private void fetchAllRestaurants() {
        restaurantRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    allRestaurants.clear();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                        restaurant.setRestaurantId(documentSnapshot.getId());

                        fetchTablesForRestaurant(restaurant.getRestaurantId(), restaurant);

                        allRestaurants.add(restaurant);
                    }
                    restaurantAdapter.setRestaurantList(allRestaurants);
                    restaurantAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> {
                    Log.d("RESTAURANT FETCH ERROR", e.toString());
                    Toast.makeText(this, "Błąd pobierania danych restauracji", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchTablesForRestaurant(String resId, Restaurant restaurant) {
        restaurantRef.document(resId).collection("tables")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Table> tables = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Table table = documentSnapshot.toObject(Table.class);
                        table.setTableId(documentSnapshot.getId());
                        tables.add(table);
                    }
                    restaurant.setTables(tables);
                    restaurantAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.d("TABLES FETCH ERROR", e.toString());
                });
    }

    private void setupFirestoreListener(String userUid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userUid);

        userRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                // Obsłuż błąd nasłuchiwania zmian
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                // Pobierz zaktualizowane dane z dokumentu Firestore i zaktualizuj widok
                String useremail = snapshot.getString("email");
                String username = snapshot.getString("userName");

                userEmail.setText(useremail);
                userName.setText(username);
            }
        });
    }

    private void fetchUserDataFromFirestore(String userUid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userUid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Pobierz dane z dokumentu Firestore i zaktualizuj widok
                    String useremail = document.getString("email");
                    String username = document.getString("userName");

                    userEmail.setText(useremail);
                    userName.setText(username);

                    // Ustaw nasłuchiwanie zmian w dokumencie Firestore
                    setupFirestoreListener(userUid);
                }
            } else {
                // Obsłuż błąd pobierania danych
            }
        });
    }
}