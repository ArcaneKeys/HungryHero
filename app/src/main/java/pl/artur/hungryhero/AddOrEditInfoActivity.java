package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddOrEditInfoActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private Button buttonSave;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_info);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);

        firebaseHelper.getRestaurantData().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");

                editTextName.setText(name);
                editTextDescription.setText(description);
            } else {
                firebaseHelper.createEmptyRestaurantDocument();
            }
        });

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();

            firebaseHelper.updateRestaurantData(name, description)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddOrEditInfoActivity.this, "Dane zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditInfoActivity.this, "Błąd podczas aktualizacji danych", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}