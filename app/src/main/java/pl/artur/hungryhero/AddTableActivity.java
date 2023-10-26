package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddTableActivity extends AppCompatActivity {

    private EditText editTextTableNumber, editTextTableCapacity;
    private Button buttonAddTable;
    @Inject
    FirebaseHelper firebaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);

        editTextTableNumber = findViewById(R.id.editTextTableNumber);
        editTextTableCapacity = findViewById(R.id.editTextTableCapacity);
        buttonAddTable = findViewById(R.id.buttonAddTable);

        buttonAddTable.setOnClickListener(v -> addTable());
    }

    private void addTable() {
        int number = Integer.parseInt(editTextTableNumber.getText().toString());
        int capacity = Integer.parseInt(editTextTableCapacity.getText().toString());
        Table newTable = new Table(number, capacity);
        firebaseHelper.addTable(newTable)
                .addOnSuccessListener(aVoid -> finish())
                .addOnFailureListener(e -> {
                });
    }
}