package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.adapters.DishesAdapter;
import pl.artur.hungryhero.models.MenuItem;
import pl.artur.hungryhero.module.helper.FirebaseHelper;
import pl.artur.hungryhero.ui.RestaurantMainActivity;

@AndroidEntryPoint
public class DishesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DishesAdapter dishesAdapter;
    List<MenuItem> menuItems;
    TextView categoryNameTextView;
    ImageButton editCategoryButton;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);

        recyclerView = findViewById(R.id.recyclerViewDishes);
        categoryNameTextView = findViewById(R.id.categoryNameTextView);
        editCategoryButton = findViewById(R.id.editCategoryButton);

        String menuName = getIntent().getStringExtra("menuName");
        String menuId = getIntent().getStringExtra("menuId");
        categoryNameTextView.setText(menuName);

        menuItems = new ArrayList<>();

        dishesAdapter = new DishesAdapter(menuItems, menuId, firebaseHelper);
        recyclerView.setAdapter(dishesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchDishes(menuId);

        editCategoryButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DishesActivity.this);
            builder.setTitle("Edit Menu Name");

            final EditText input = new EditText(DishesActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String newMenuName = input.getText().toString();
                firebaseHelper.updateMenuName(menuId, newMenuName)
                        .addOnSuccessListener(aVoid -> {
                            categoryNameTextView.setText(newMenuName);
                        })
                        .addOnFailureListener(e -> {

                        });
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        FloatingActionButton addDishButton = findViewById(R.id.fabAddDish);
        addDishButton.setOnClickListener(v -> {
            Intent intent = new Intent(DishesActivity.this, AddDishActivity.class);
            intent.putExtra("menuId", menuId);
            startActivity(intent);
        });
    }

    private void fetchDishes(String menuId) {
        firebaseHelper.getDishesCollectionRef(menuId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    menuItems.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        MenuItem menuItem = snapshot.toObject(MenuItem.class);
                        if (menuItem != null) {
                            menuItem.setItemId(snapshot.getId());
                        }
                        menuItems.add(menuItem);
                    }
                    dishesAdapter.notifyDataSetChanged();
                });
    }
}