package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.adapters.MenuSectionAdapter;
import pl.artur.hungryhero.models.Menu;

public class MenusActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMenuSections;
    private MenuSectionAdapter menuSectionAdapter;
    private List<Menu> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        recyclerViewMenuSections = findViewById(R.id.recyclerViewMenuSections);
        recyclerViewMenuSections.setLayoutManager(new LinearLayoutManager(this));

        // Pobierz dane menu z poprzedniego widoku (RestaurantDetailsActivity)
        Intent intent = getIntent();
        if (intent != null) {
            String restaurantId = intent.getStringExtra("restaurantId");
            if (restaurantId != null) {
                fetchMenuData(restaurantId);
            }
        }
    }

    private void fetchMenuData(String restaurantId) {
        // Pobierz referencję do kolekcji "menu" dla danego restaurantId
        CollectionReference menuRef = FirebaseFirestore.getInstance().collection("Restaurant")
                .document(restaurantId).collection("menu");

        // Zdefiniuj zapytanie, które pobierze wszystkie dokumenty z kolekcji "menu"
        menuRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            menuList = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Menu menu = documentSnapshot.toObject(Menu.class);
                menuList.add(menu);
            }

            // Przygotuj dane do wyświetlenia w sekcjach
            Map<String, List<Menu>> menuSections = groupMenuItemsBySection(menuList);

            // Utwórz adapter dla sekcji i przypisz go do RecyclerView
            menuSectionAdapter = new MenuSectionAdapter(this, menuSections);
            recyclerViewMenuSections.setAdapter(menuSectionAdapter);
        }).addOnFailureListener(e -> {
            // Obsłuż błąd pobierania danych menu
            Toast.makeText(this, "Wystąpił błąd podczas pobierania menu.", Toast.LENGTH_SHORT).show();
        });
    }

    private Map<String, List<Menu>> groupMenuItemsBySection(List<Menu> menuList) {
        // Grupuj dane menu w sekcjach na podstawie menuName
        Map<String, List<Menu>> menuSections = new LinkedHashMap<>();
        for (Menu menu : menuList) {
            String menuName = menu.getMenuName();
            if (menuSections.containsKey(menuName)) {
                menuSections.get(menuName).add(menu);
            } else {
                List<Menu> menuItems = new ArrayList<>();
                menuItems.add(menu);
                menuSections.put(menuName, menuItems);
            }
        }
        return menuSections;
    }
}
