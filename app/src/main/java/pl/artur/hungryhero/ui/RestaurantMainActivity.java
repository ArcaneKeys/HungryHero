package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class RestaurantMainActivity extends AppCompatActivity {

    private Button btnAddOrEditContact, btnAddOrEditLocalization, btnAddOrEditInfo,
            btnAddOrEditOpeningHours, btnAddOrEditTables, btnAddOrEditMenu;
    private Button btnShowReview, btnShowReservation, btnShowRestaurant;
    private Intent intent = null;

    private TextView restaurant_name;
    private TextView restaurant_email;
    private RestaurantDrawerManager drawerManager;
    private View headerView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageButton navButton;
    private Toolbar toolbar;

    @Inject
    FirebaseHelper firebaseHelper;

    @Inject
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_main);

        init();

        setSupportActionBar(toolbar);

        headerView = navigationView.getHeaderView(0);
        restaurant_email = headerView.findViewById(R.id.restaurant_email);
        restaurant_name = headerView.findViewById(R.id.restaurant_name);

        ShapeableImageView userIcon = headerView.findViewById(R.id.user_icon);
        userIcon.setOnClickListener(v -> {
            // Przekierowanie do Layoutu zmiany danych użytkownika
            Intent intent = new Intent(RestaurantMainActivity.this, ChangeRestaurantDataActivity.class);
            startActivity(intent);
        });

        firebaseHelper.getUserDocument().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String useremail = document.getString("email");
                    String username = document.getString("userName");

                    restaurant_email.setText(useremail);
                    restaurant_name.setText(username);

                    // Ustaw nasłuchiwanie zmian w dokumencie Firestore
                    firebaseHelper.getUserDocumentRef().addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            // Obsłuż błąd nasłuchiwania zmian
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            // Pobierz zaktualizowane dane z dokumentu Firestore i zaktualizuj widok
                            String email = snapshot.getString("email");
                            String name = snapshot.getString("userName");

                            restaurant_email.setText(email);
                            restaurant_name.setText(name);
                        }
                    });
                }
            }
        });

        drawerManager = new RestaurantDrawerManager(navButton, drawerLayout, navigationView, mAuth, this);

        btnAddOrEditContact.setOnClickListener(v -> {
            // intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        btnAddOrEditLocalization.setOnClickListener(v -> {
            // intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        btnAddOrEditInfo.setOnClickListener(v -> {
            // intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        btnAddOrEditOpeningHours.setOnClickListener(v -> {
            // intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        btnAddOrEditTables.setOnClickListener(v -> {
            // intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        btnAddOrEditMenu.setOnClickListener(v -> {
            // intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        btnShowReview.setOnClickListener(v -> {
//            intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        btnShowReservation.setOnClickListener(v -> {
//            intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        btnShowRestaurant.setOnClickListener(v -> {
//            intent = new Intent(RestaurantMainActivity.this, UserMainActivity.class);
        });

        if (intent != null){
            startActivity(intent);
            finish();
        }
    }

    private void init(){
        navButton = findViewById(R.id.restaurant_nav_button);
        drawerLayout = findViewById(R.id.restaurant_drawer_layout);
        navigationView = findViewById(R.id.restaurant_navigation_view);
        toolbar = findViewById(R.id.restaurant_toolbar);

        btnAddOrEditContact = findViewById(R.id.btnAddOrEditContact);
        btnAddOrEditLocalization = findViewById(R.id.btnAddOrEditLocalization);
        btnAddOrEditInfo = findViewById(R.id.btnAddOrEditInfo);
        btnAddOrEditOpeningHours = findViewById(R.id.btnAddOrEditOpeningHours);
        btnAddOrEditTables = findViewById(R.id.btnAddOrEditTables);
        btnAddOrEditMenu = findViewById(R.id.btnAddOrEditMenu);

        btnShowReview = findViewById(R.id.btnShowReview);
        btnShowReservation = findViewById(R.id.btnShowReservation);
        btnShowRestaurant = findViewById(R.id.btnShowRestaurant);
    }

}
