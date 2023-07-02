package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();

        logoutButton.setOnClickListener(v -> {
            // Wywołaj funkcję do wylogowania użytkownika
            logout();
        });
    }

    public void initVariables(){
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void logout() {
        // Wywołaj funkcję wylogowania z Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Przejdź do FirebaseLoginActivity lub innej aktywności logowania
        Intent intent = new Intent(MainActivity.this, FirebaseLoginActivity.class);
        startActivity(intent);
        finish(); // Zamknij bieżącą aktywność, aby nie można było cofnąć do ekranu głównego po wylogowaniu
    }
}