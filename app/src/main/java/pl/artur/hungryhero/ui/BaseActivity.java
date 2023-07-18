package pl.artur.hungryhero.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.utils.FirebaseManager;

public abstract class BaseActivity extends AppCompatActivity {
    protected DrawerManager drawerManager;
    protected TextView userName;
    protected TextView userEmail;
    protected View headerView;
    protected FirebaseAuth mAuth;
    protected FirebaseUser mUser;

    protected void setBaseLayout(int layoutResID) {
        setContentView(layoutResID);

        // Inicjalizacja zmiennych
        ImageButton navButton = findViewById(R.id.nav_button);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar); // Ustawienie paska narzędzi

        headerView = navigationView.getHeaderView(0);
        userEmail = headerView.findViewById(R.id.user_email);
        userName = headerView.findViewById(R.id.user_name);

        mAuth = FirebaseManager.getAuthInstance();

        mUser = mAuth.getCurrentUser();
        String useremail =  mUser.getEmail();
        String username =  mUser.getDisplayName();

        userEmail.setText(useremail);
        userName.setText(username);

        drawerManager = new DrawerManager(navButton, drawerLayout, navigationView, mAuth, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
