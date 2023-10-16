package pl.artur.hungryhero.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.User;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class ChangeRestaurantDataActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private Button saveButton;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_data);

        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        saveButton = findViewById(R.id.save_button);

        firebaseHelper.getUserDocument().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    usernameEditText.setText(user.getUserName());
                    emailEditText.setText(user.getEmail());
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(ChangeRestaurantDataActivity.this, "Błąd odczytu danych użytkownika", Toast.LENGTH_SHORT).show();
        });

        saveButton.setOnClickListener(v -> saveUserData());
    }

    private void saveUserData() {
        String newUsername = usernameEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();

        firebaseHelper.getUserDocumentRef().update("userName", newUsername, "email", newEmail)
                .addOnSuccessListener(aVoid -> {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(newUsername)
                            .build();

                    firebaseHelper.updateUserProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangeRestaurantDataActivity.this, "Zaktualizowano dane użytkownika", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ChangeRestaurantDataActivity.this, "Błąd podczas aktualizacji danych użytkownika", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChangeRestaurantDataActivity.this, "Błąd podczas aktualizacji danych użytkownika", Toast.LENGTH_SHORT).show();
                });
    }
}
