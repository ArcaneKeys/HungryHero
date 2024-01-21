package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class RestaurantMainActivity extends AppCompatActivity {

    private Button btnAddOrEditContact, btnAddOrEditLocalization, btnAddOrEditInfo,
            btnAddOrEditOpeningHours, btnAddOrEditTables, btnAddOrEditMenu;
    private Button btnShowReview, btnShowReservation, btnShowRestaurant, btnGenerateReport;

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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        setSupportActionBar(toolbar);

        headerView = navigationView.getHeaderView(0);
        restaurant_email = headerView.findViewById(R.id.restaurant_email);
        restaurant_name = headerView.findViewById(R.id.restaurant_name);

        ShapeableImageView restaurantIcon = headerView.findViewById(R.id.restaurant_icon);
        restaurantIcon.setOnClickListener(v -> {
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

                    firebaseHelper.getUserDocumentRef().addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
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
            Intent intent = new Intent(RestaurantMainActivity.this, AddOrEditContactActivity.class);
            startActivity(intent);
        });

        btnAddOrEditLocalization.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, AddOrEditLocalizationActivity.class);
            startActivity(intent);
        });

        btnAddOrEditInfo.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, AddOrEditInfoActivity.class);
            startActivity(intent);
        });

        btnAddOrEditOpeningHours.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, AddOrEditOpeningHoursActivity.class);
            startActivity(intent);
        });

        btnAddOrEditTables.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, AddOrEditTablesActivity.class);
            startActivity(intent);
        });

        btnAddOrEditMenu.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, MenuCategoriesActivity.class);
            startActivity(intent);
        });

        btnShowReview.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, ReviewsActivity.class);
            startActivity(intent);
        });

        btnShowReservation.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, AllReservationsActivity.class);
            startActivity(intent);
        });

        btnShowRestaurant.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, RestaurantDetailsActivity.class);
            startActivity(intent);
        });

        btnGenerateReport.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantMainActivity.this, ReportActivity.class);
            startActivity(intent);
        });

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

        btnGenerateReport = findViewById(R.id.btnGenerateReport);
    }

}

