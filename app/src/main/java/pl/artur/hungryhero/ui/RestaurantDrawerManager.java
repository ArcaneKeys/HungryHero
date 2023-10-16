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
            if (id == R.id.nav_item_logout_restaurant) {
                logout();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void logout() {
        // Wywołaj funkcję wylogowania z Firebase Authentication
        mAuth.signOut();

        // Przejdź do FirebaseLoginActivity lub innej aktywności logowania
        Intent intent = new Intent(activity, FirebaseLoginActivity.class);
        activity.startActivity(intent);
        activity.finish(); // Zamknij bieżącą aktywność, aby nie można było cofnąć do ekranu głównego po wylogowaniu
    }
}

