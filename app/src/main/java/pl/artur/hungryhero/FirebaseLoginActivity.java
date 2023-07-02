package pl.artur.hungryhero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private Button loginButton;
    private Button registerButton;
    private EditText loginEmail;
    private EditText loginPassword;
    private EditText registerPassword;
    private EditText registerRepeatPassword;
    private EditText registerEmail;

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

        initVariables();

        loginButton.setOnClickListener(v -> {
            String emailText = getString(loginEmail);
            String passwordText = getString(loginPassword);

            if (emailText.isEmpty() || passwordText.isEmpty()){
                Toast.makeText(FirebaseLoginActivity.this, "Pola nie mogą być puste!", Toast.LENGTH_SHORT).show();
            } else if(!ValidationUtils.isValidEmail(emailText)){
                Toast.makeText(FirebaseLoginActivity.this, "Nieprawidłowy format emaila!", Toast.LENGTH_SHORT).show();
            } else{
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
                                    Toast.makeText(FirebaseLoginActivity.this, "Logowanie nieudane.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        registerButton.setOnClickListener(v -> {
            String emailText =  getString(registerEmail);
            String passwordText =  getString(registerPassword);
            String repeatPasswordText =  getString(registerRepeatPassword);

            if (emailText.isEmpty() || passwordText.isEmpty() || repeatPasswordText.isEmpty()){
                Toast.makeText(FirebaseLoginActivity.this, "Pola nie mogą być puste!", Toast.LENGTH_SHORT).show();
            } else if (!ValidationUtils.isValidEmail(emailText)){
                Toast.makeText(FirebaseLoginActivity.this, "Nieprawidłowy format emaila!", Toast.LENGTH_SHORT).show();
            } else if (!ValidationUtils.isValidPassword(passwordText)) {
                Toast.makeText(FirebaseLoginActivity.this, "Hasło musi zawierać minimum 6 znaków, dużą literę oraz cyfrę!", Toast.LENGTH_LONG).show();
            } else if (!ValidationUtils.arePasswordsMatching(passwordText, repeatPasswordText)) {
                Toast.makeText(FirebaseLoginActivity.this, "Hasła nie pasują do siebie!", Toast.LENGTH_SHORT).show();
            } else{
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

    public void initVariables(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginEmail = findViewById(R.id.loginEmailEditText);
        loginPassword = findViewById(R.id.loginPasswordEditText);

        registerEmail = findViewById(R.id.registerEmailEditText);
        registerPassword = findViewById(R.id.registerPasswordEditText);
        registerRepeatPassword = findViewById(R.id.repeatRegisterPasswordEditText);
    }

    public String getString(EditText edt){
        return edt.getText().toString().trim();
    }
}