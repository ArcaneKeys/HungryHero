package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.adapters.MenuCategoryAdapter;
import pl.artur.hungryhero.models.Menu;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class MenuCategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMenuCategories;
    private MenuCategoryAdapter menuCategoryAdapter;
    private List<Menu> menuCategoriesList = new ArrayList<>();
    private FloatingActionButton fabAddMenuCategory;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_categories);

        fabAddMenuCategory = findViewById(R.id.fabAddMenuCategory);

        recyclerViewMenuCategories = findViewById(R.id.recyclerViewMenuCategories);
        recyclerViewMenuCategories.setLayoutManager(new LinearLayoutManager(this));

        menuCategoryAdapter = new MenuCategoryAdapter(this, menuCategoriesList);
        recyclerViewMenuCategories.setAdapter(menuCategoryAdapter);

        firebaseHelper.isRestaurant(isRestaurant -> {
            if (!isRestaurant) {
                String restaurantId = getIntent().getStringExtra("restaurantId");
                fabAddMenuCategory.setVisibility(View.GONE);
                fetchMenuCategories(restaurantId);
            } else {
                fetchMenuCategories();
            }
        });

        fabAddMenuCategory.setOnClickListener(v -> {
            Intent intent = new Intent(MenuCategoriesActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        menuCategoryAdapter.setOnMenuCategoryClickListener((menuId, menuName) -> {
            Intent intent = new Intent(MenuCategoriesActivity.this, DishesActivity.class);
            intent.putExtra("menuId", menuId);
            intent.putExtra("menuName", menuName);
            startActivity(intent);
        });
    }

    private void fetchMenuCategories() {
        firebaseHelper.getMenuCategoriesCollectionRef()
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    menuCategoriesList.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Menu menuCategory = snapshot.toObject(Menu.class);
                        if (menuCategory != null) {
                            menuCategory.setMenuId(snapshot.getId());
                        }
                        menuCategoriesList.add(menuCategory);
                    }
                    menuCategoryAdapter.notifyDataSetChanged();
                });
    }

    private void fetchMenuCategories(String restaurantId) {
        firebaseHelper.getMenuCategoriesCollectionRef(restaurantId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    menuCategoriesList.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Menu menuCategory = snapshot.toObject(Menu.class);
                        if (menuCategory != null) {
                            menuCategory.setMenuId(snapshot.getId());
                        }
                        menuCategoriesList.add(menuCategory);
                    }
                    menuCategoryAdapter.notifyDataSetChanged();
                });
    }

}