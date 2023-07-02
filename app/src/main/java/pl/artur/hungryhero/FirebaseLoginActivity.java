package pl.artur.hungryhero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Button loginButton;
    EditText email;
    EditText password;
    EditText passwordR;
    EditText emailR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Użytkownik jest zalogowany, przekieruj do MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Zakończ FirebaseLoginActivity
        }

        setContentView(R.layout.activity_firebase_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loginButton = findViewById(R.id.loginButton);

        email = findViewById(R.id.loginEmailEditText);
        password = findViewById(R.id.loginPasswordEditText);

        emailR = findViewById(R.id.registerEmailEditText);
        passwordR = findViewById(R.id.registerPasswordEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                mAuth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(FirebaseLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Użytkownik został pomyślnie zalogowany
                                    // Przejdź do kolejnej aktywności lub wykonaj inne operacje
                                    startActivity(new Intent(FirebaseLoginActivity.this, MainActivity.class));
                                } else {
                                    // Wystąpił błąd logowania
                                    Toast.makeText(FirebaseLoginActivity.this, "Logowanie nieudane.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = emailR.getText().toString();
                String passwordText = passwordR.getText().toString();

                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(FirebaseLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Nowy użytkownik został pomyślnie zarejestrowany
                                    // Przejdź do kolejnej aktywności lub wykonaj inne operacje
                                    startActivity(new Intent(FirebaseLoginActivity.this, MainActivity.class));
                                } else {
                                    // Wystąpił błąd rejestracji
                                    Toast.makeText(FirebaseLoginActivity.this, "Rejestracja nieudana.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void switchToRegistration(View view) {
        LinearLayout loginLayout = findViewById(R.id.loginLayout);
        LinearLayout registrationLayout = findViewById(R.id.registrationLayout);

        loginLayout.setVisibility(View.GONE);
        registrationLayout.setVisibility(View.VISIBLE);
    }

    public void switchToLogin(View view) {
        LinearLayout loginLayout = findViewById(R.id.loginLayout);
        LinearLayout registrationLayout = findViewById(R.id.registrationLayout);

        loginLayout.setVisibility(View.VISIBLE);
        registrationLayout.setVisibility(View.GONE);
    }
}