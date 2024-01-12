package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class EditTableActivity extends AppCompatActivity {

    private EditText editTextTableNumber, editTextTableCapacity;
    private Button buttonUpdateTable;
    private String tableId;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table);

        editTextTableNumber = findViewById(R.id.editTextTableNumber);
        editTextTableCapacity = findViewById(R.id.editTextTableCapacity);
        buttonUpdateTable = findViewById(R.id.buttonUpdateTable);
        Button buttonDeleteTable = findViewById(R.id.buttonDeleteTable);

        Intent intent = getIntent();
        tableId = intent.getStringExtra("tableId");

        firebaseHelper.getTableDocument(tableId)
                .addOnSuccessListener(documentSnapshot -> {
                    Table table = documentSnapshot.toObject(Table.class);
                    if (table != null) {
                        editTextTableNumber.setText(String.valueOf(table.getNumber()));
                        editTextTableCapacity.setText(String.valueOf(table.getCapacity()));
                    }
                })
                .addOnFailureListener(e -> {
                });

        buttonUpdateTable.setOnClickListener(v -> {
            String tableNumber = editTextTableNumber.getText().toString().trim();
            String tableCapacity = editTextTableCapacity.getText().toString().trim();

            if (!TextUtils.isEmpty(tableNumber) && !TextUtils.isEmpty(tableCapacity)) {
                String number = tableNumber;
                int capacity = Integer.parseInt(tableCapacity);

                Table updatedTable = new Table(number, capacity);
                firebaseHelper.updateTable(tableId, updatedTable);
                finish();
            }
        });

        buttonDeleteTable.setOnClickListener(v -> onDeleteButtonClicked(tableId));
    }

    public void onDeleteButtonClicked(String tableId) {
        firebaseHelper.deleteTable(tableId)
                .addOnSuccessListener(aVoid -> {
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }
}