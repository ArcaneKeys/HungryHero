package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddTableActivity extends AppCompatActivity {

    private EditText editTextTableNumber, editTextTableCapacity;
    private Button buttonAddTable;
    private Toolbar toolbar;
    @Inject
    FirebaseHelper firebaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);

        editTextTableNumber = findViewById(R.id.editTextTableNumber);
        editTextTableCapacity = findViewById(R.id.editTextTableCapacity);
        buttonAddTable = findViewById(R.id.buttonAddTable);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonAddTable.setOnClickListener(v -> addTable());
    }

    private void addTable() {
        String number = editTextTableNumber.getText().toString();
        int capacity = Integer.parseInt(editTextTableCapacity.getText().toString());
        Table newTable = new Table(number, capacity);
        firebaseHelper.addTable(newTable)
                .addOnSuccessListener(aVoid -> finish())
                .addOnFailureListener(e -> {
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}