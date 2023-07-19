package pl.artur.hungryhero.ui;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.User;

public class ChangeUserDataActivity extends BaseActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private DocumentReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseLayout(R.layout.activity_change_user_data);

        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        saveButton = findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mUserRef = mFirestore.collection("Users").document(mUser.getUid());

        // Pobierz obecne dane użytkownika z Firestore
        mUserRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    usernameEditText.setText(user.getUserName());
                    emailEditText.setText(user.getEmail());
                    phoneEditText.setText(user.getPhone());
                }
            }
        }).addOnFailureListener(e -> {
            // Obsługa błędu odczytu danych
            Toast.makeText(ChangeUserDataActivity.this, "Błąd odczytu danych użytkownika", Toast.LENGTH_SHORT).show();
        });

        saveButton.setOnClickListener(v -> saveUserData());
    }

    private void saveUserData() {
        String newUsername = usernameEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();
        String newPhone = phoneEditText.getText().toString();

        /// Zaktualizuj dane użytkownika w Firestore
        mUserRef.update("userName", newUsername,
                        "email", newEmail,
                        "phone", newPhone)
                .addOnSuccessListener(aVoid -> {
                    // Zaktualizowano dane użytkownika w Firestore

                    // Zaktualizuj dane użytkownika w profilu Firebase
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(newUsername)
                            .build();

                    mUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Zaktualizowano dane użytkownika w profilu Firebase
                                    Toast.makeText(ChangeUserDataActivity.this, "Zaktualizowano dane użytkownika", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // Obsługa błędu podczas aktualizacji danych użytkownika w profilu Firebase
                                    Toast.makeText(ChangeUserDataActivity.this, "Błąd podczas aktualizacji danych użytkownika", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    // Obsługa błędu podczas aktualizacji danych użytkownika w Firestore
                    Toast.makeText(ChangeUserDataActivity.this, "Błąd podczas aktualizacji danych użytkownika", Toast.LENGTH_SHORT).show();
                });
    }
}
