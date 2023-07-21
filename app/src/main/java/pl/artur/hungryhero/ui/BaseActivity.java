package pl.artur.hungryhero.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // Dodaj obsługę kliknięcia dla ShapeableImageView
        ShapeableImageView userIcon = headerView.findViewById(R.id.user_icon);
        userIcon.setOnClickListener(v -> {
            // Przekierowanie do Layoutu zmiany danych użytkownika
            Intent intent = new Intent(BaseActivity.this, ChangeUserDataActivity.class);
            startActivity(intent);
        });

        mAuth = FirebaseManager.getAuthInstance();

        mUser = mAuth.getCurrentUser();
        String userUid = mUser.getUid();

        fetchUserDataFromFirestore(userUid);

        drawerManager = new DrawerManager(navButton, drawerLayout, navigationView, mAuth, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupFirestoreListener(mUser.getUid());
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
