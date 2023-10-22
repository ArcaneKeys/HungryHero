package pl.artur.hungryhero.ui;

import static pl.artur.hungryhero.utils.UsernameGenerator.generateUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.User;
import pl.artur.hungryhero.module.helper.FirebaseHelper;
import pl.artur.hungryhero.utils.FirebaseManager;
import pl.artur.hungryhero.utils.ValidationUtils;

@AndroidEntryPoint
public class FirebaseLoginActivity extends AppCompatActivity {

    @Inject
    FirebaseHelper firebaseHelper;

    private Button loginButton;
    private Button registerButton;
    private EditText loginEmail;
    private EditText loginPassword;
    private EditText registerPassword;
    private EditText registerRepeatPassword;
    private EditText registerEmail;
    private Spinner accountTypeSpinner;
    private final String[] accountTypes = {"Użytkownik", "Restauracja"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = firebaseHelper.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }

        setContentView(R.layout.activity_firebase_login);

        initVariables();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(adapter);
        accountTypeSpinner.setSelection(0);

        loginButton.setOnClickListener(v -> {
            String emailText = getString(loginEmail);
            String passwordText = getString(loginPassword);

            if (validateEmailAndPassword(emailText, passwordText)) {
                signInUser(emailText, passwordText);
            }
        });

        registerButton.setOnClickListener(v -> {
            String emailText =  getString(registerEmail);
            String passwordText =  getString(registerPassword);
            String repeatPasswordText =  getString(registerRepeatPassword);

            if (validateRegistrationData(emailText, passwordText, repeatPasswordText)) {
                createUserAndRegister(emailText, passwordText);
            }
        });
    }

    private boolean validateEmailAndPassword(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Pola nie mogą być puste!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(this, "Nieprawidłowy format emaila!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signInUser(String email, String password) {
        firebaseHelper.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(FirebaseLoginActivity.this, SplashActivity.class));
                    } else {
                        Toast.makeText(this, "Logowanie nieudane.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateRegistrationData(String email, String password, String repeatPassword) {
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(this, "Pola nie mogą być puste!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(this, "Nieprawidłowy format emaila!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ValidationUtils.isValidPassword(password)) {
            Toast.makeText(this, "Hasło musi zawierać minimum 6 znaków, dużą literę oraz cyfrę!", Toast.LENGTH_LONG).show();
            return false;
        } else if (!ValidationUtils.arePasswordsMatching(password, repeatPassword)) {
            Toast.makeText(this, "Hasła nie pasują do siebie!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createUserAndRegister(String email, String password) {
        firebaseHelper.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String selectedAccountType = accountTypeSpinner.getSelectedItem().toString();

                        String displayName = generateUsername();
                        UserProfileChangeRequest profileUpdates = firebaseHelper.createProfileUpdateWithDisplayName(displayName);

                        firebaseHelper.updateUserProfile(profileUpdates).addOnCompleteListener(task1 -> {

                        });

                        User putUser = new User(selectedAccountType, displayName, email, "");

                        firebaseHelper.setUserDocument(putUser)
                                .addOnSuccessListener(unused -> {
                                    if ("Restauracja".equals(selectedAccountType)) {
                                        firebaseHelper.createEmptyRestaurantDocument();
                                    }

                                    startActivity(new Intent(FirebaseLoginActivity.this, SplashActivity.class));
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Błąd rejestracji.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Rejestracja nieudana.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void switchToRegistration(View view) {
        ConstraintLayout loginLayout = findViewById(R.id.loginLayout);
        ConstraintLayout registrationLayout = findViewById(R.id.registrationLayout);

        loginLayout.setVisibility(View.GONE);
        registrationLayout.setVisibility(View.VISIBLE);
    }

    public void switchToLogin(View view) {
        ConstraintLayout loginLayout = findViewById(R.id.loginLayout);
        ConstraintLayout registrationLayout = findViewById(R.id.registrationLayout);

        loginLayout.setVisibility(View.VISIBLE);
        registrationLayout.setVisibility(View.GONE);
    }

    public void initVariables(){
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginEmail = findViewById(R.id.loginEmailEditText);
        loginPassword = findViewById(R.id.loginPasswordEditText);

        registerEmail = findViewById(R.id.registerEmailEditText);
        registerPassword = findViewById(R.id.registerPasswordEditText);
        registerRepeatPassword = findViewById(R.id.repeatRegisterPasswordEditText);

        accountTypeSpinner = findViewById(R.id.accountTypeSpinner);
    }

    public String getString(EditText edt){
        return edt.getText().toString().trim();
    }
}