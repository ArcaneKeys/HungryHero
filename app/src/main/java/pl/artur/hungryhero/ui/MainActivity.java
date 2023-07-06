package pl.artur.hungryhero.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.utils.FirebaseManager;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ImageButton navButton;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextView userName;
    private TextView userEmail;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();

        mAuth = FirebaseManager.getAuthInstance();

        mUser = mAuth.getCurrentUser();
        String useremail =  mUser.getEmail();
        String username =  mUser.getDisplayName();

        userEmail.setText(useremail);
        userName.setText(username);

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(item -> {
            // Obsługa kliknięcia elementu menu
            int id = item.getItemId();
            if (id == R.id.nav_item_logout) {
                logout();
            }

            drawer.closeDrawer(GravityCompat.START);

            return true;
        });

        navButton.setOnClickListener(v -> {
            if (drawer.isOpen()){
                drawer.closeDrawer(GravityCompat.START);
            } else{
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    public void initVariables(){
        navigationView = findViewById(R.id.navigation_view);
        navButton = findViewById(R.id.nav_button);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        headerView = navigationView.getHeaderView(0);
        userEmail = headerView.findViewById(R.id.user_email);
        userName = headerView.findViewById(R.id.user_name);
    }

    private void logout() {
        // Wywołaj funkcję wylogowania z Firebase Authentication
        mAuth.signOut();

        // Przejdź do FirebaseLoginActivity lub innej aktywności logowania
        Intent intent = new Intent(MainActivity.this, FirebaseLoginActivity.class);
        startActivity(intent);
        finish(); // Zamknij bieżącą aktywność, aby nie można było cofnąć do ekranu głównego po wylogowaniu
    }
}