package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.adapters.TableAdapter;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddOrEditTablesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private List<Table> tableList = new ArrayList<>();

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_tables);

        // Inicjalizacja RecyclerView
        recyclerView = findViewById(R.id.tablesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tableAdapter = new TableAdapter(this, tableList);
        recyclerView.setAdapter(tableAdapter);

        firebaseHelper.getTablesCollectionRef()
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    tableList.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Table table = snapshot.toObject(Table.class);
                        if (table != null) {
                            table.setTableId(snapshot.getId());
                        }
                        tableList.add(table);
                    }
                    tableAdapter.notifyDataSetChanged();
                });

        Button addTableButton = findViewById(R.id.addTableButton);
        addTableButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddOrEditTablesActivity.this, AddTableActivity.class);
            startActivity(intent);
        });
    }
}