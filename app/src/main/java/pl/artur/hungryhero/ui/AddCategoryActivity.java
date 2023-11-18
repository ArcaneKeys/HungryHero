package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Menu;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddCategoryActivity extends AppCompatActivity {

    private EditText editTextCategoryName;
    private Button buttonAddCategory;
    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);

        buttonAddCategory.setOnClickListener(v -> {
            String categoryName = editTextCategoryName.getText().toString();
            if (!categoryName.isEmpty()) {
                firebaseHelper.addMenuCategory(categoryName)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show();
                            finish();
                        });
            } else {
                editTextCategoryName.setError("Category name is required");
            }
        });
    }
}