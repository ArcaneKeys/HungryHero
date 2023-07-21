package pl.artur.hungryhero.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.RestaurantAdapter;
import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;

public class MainActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> allRestaurants = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference restaurantRef = db.collection("Restaurant");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseLayout(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        restaurantAdapter = new RestaurantAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(restaurantAdapter);

        fetchAllRestaurants();
    }

    @Override
    protected void onStart() {
        super.onStart();
        restaurantRef.addSnapshotListener(this, (value, error) -> {
            if (error != null)
                return;

            if (value == null)
                return;
            allRestaurants.clear();
            for (QueryDocumentSnapshot documentSnapshot : value){
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                restaurant.setRestaurantId(documentSnapshot.getId());

                fetchTablesForRestaurant(restaurant.getRestaurantId(), restaurant);

                allRestaurants.add(restaurant);
            }
        });
    }

    private void fetchAllRestaurants() {
        restaurantRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    allRestaurants.clear();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                        restaurant.setRestaurantId(documentSnapshot.getId());

                        fetchTablesForRestaurant(restaurant.getRestaurantId(), restaurant);

                        allRestaurants.add(restaurant);
                    }
                    restaurantAdapter.setRestaurantList(allRestaurants);
                    restaurantAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> {
                    Log.d("RESTAURANT FETCH ERROR", e.toString());
                    Toast.makeText(this, "Błąd pobierania danych restauracji", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchTablesForRestaurant(String resId, Restaurant restaurant) {
        restaurantRef.document(resId).collection("tables")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Table> tables = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Table table = documentSnapshot.toObject(Table.class);
                        table.setTableId(documentSnapshot.getId());
                        tables.add(table);
                    }
                    restaurant.setTables(tables);
                    restaurantAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.d("TABLES FETCH ERROR", e.toString());
                });
    }
}