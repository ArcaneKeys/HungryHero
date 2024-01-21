package pl.artur.hungryhero.ui;

import android.content.Intent;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import pl.artur.hungryhero.R;

public class RestaurantDrawerManager {
    private ImageButton navButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private AppCompatActivity activity;

    public RestaurantDrawerManager(ImageButton navButton, DrawerLayout drawerLayout, NavigationView navigationView, FirebaseAuth mAuth, AppCompatActivity activity) {
        this.navButton = navButton;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;
        this.activity = activity;
        this.mAuth = mAuth;

        initListeners();
    }

    private void initListeners() {
        navButton.setOnClickListener(v -> {
            if (drawerLayout.isOpen()) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_add_edit_contact) {
                startNewActivity(AddOrEditContactActivity.class);
            } else if (id == R.id.nav_add_edit_localization) {
                startNewActivity(AddOrEditLocalizationActivity.class);
            } else if (id == R.id.nav_add_edit_info) {
                startNewActivity(AddOrEditInfoActivity.class);
            } else if (id == R.id.nav_add_edit_opening_hours) {
                startNewActivity(AddOrEditOpeningHoursActivity.class);
            } else if (id == R.id.nav_add_edit_tables) {
                startNewActivity(AddOrEditTablesActivity.class);
            } else if (id == R.id.nav_add_edit_menu) {
                startNewActivity(MenuCategoriesActivity.class);
            } else if (id == R.id.nav_show_review) {
                startNewActivity(ReviewsActivity.class);
            } else if (id == R.id.nav_show_reservation) {
                startNewActivity(AllReservationsActivity.class);
            } else if (id == R.id.nav_show_restaurant) {
                startNewActivity(RestaurantDetailsActivity.class);
            } else if (id == R.id.nav_generate_report) {
                startNewActivity(ReportActivity.class);
            } else if (id == R.id.nav_item_logout_restaurant) {
                logout();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void logout() {
        mAuth.signOut();

        Intent intent = new Intent(activity, FirebaseLoginActivity.class);
        activity.startActivity(intent);
    }

    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(activity, activityClass);
        activity.startActivity(intent);
    }
}

