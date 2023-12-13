package pl.artur.hungryhero.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.ReservationActivity;
import pl.artur.hungryhero.models.Contact;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.module.helper.FirebaseHelper;
import pl.artur.hungryhero.utils.Utils;

@AndroidEntryPoint
public class RestaurantDetailsActivity extends AppCompatActivity {

    private ImageView imageRestaurant;
    private TextView textRestaurantName;
    private TextView textCapacity;
    private Button buttonReserve;
    private Button buttonFacebook;
    private Button buttonInstagram;
    private Button buttonWebsite;
    private Button buttonWebMenu;
    private TextView textDescription;
    private RecyclerView recyclerViewRestaurantDetails;
    private Button buttonMenus;
    private Button buttonReviews;
    private Restaurant restaurant;
    private List<Table> tables = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        init();

        String restaurantJson = getIntent().getStringExtra("restaurantJson");
        restaurant = new Gson().fromJson(restaurantJson, Restaurant.class);

        if (restaurant != null) {
            updateUIWithRestaurantAndTables(restaurant);
        } else {
            firebaseHelper.getRestaurantData().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        restaurant = document.toObject(Restaurant.class);
                        if (restaurant != null) {
                            restaurant.setRestaurantId(document.getId());
                            firebaseHelper.getTablesForRestaurant(restaurant.getRestaurantId()).addOnCompleteListener(tablesTask -> {
                                if (tablesTask.isSuccessful()) {
                                    tables.clear();
                                    for (DocumentSnapshot tableDoc : tablesTask.getResult()) {
                                        Table table = tableDoc.toObject(Table.class);
                                        if (table != null) {
                                            table.setTableId(tableDoc.getId());
                                            long todayTimestamp = Utils.getTodayTimestamp();
                                            firebaseHelper.getReservationForTables(tableDoc, todayTimestamp).addOnCompleteListener(reservationTask -> {
                                                if (reservationTask.isSuccessful()) {
                                                    reservations.clear();
                                                    for (DocumentSnapshot reservationDoc: reservationTask.getResult()) {
                                                        Reservation reservation = reservationDoc.toObject(Reservation.class);
                                                        if (reservation != null) {
                                                            reservation.setReservationId(reservationDoc.getId());
                                                            reservations.add(reservation);
                                                        }
                                                    }
                                                    table.setReservations(reservations);
                                                }
                                            });
                                        }
                                        tables.add(table);
                                    }
                                    restaurant.setTables(tables);
                                    updateUIWithRestaurantAndTables(restaurant);
                                }
                            });
                        }
                    } else {
                        handleNoDataFound();
                    }
                } else {
                    handleTaskFailure(task.getException());
                }
            });
        }
    }

    protected void init(){
        imageRestaurant = findViewById(R.id.imageRestaurant);
        textRestaurantName = findViewById(R.id.textRestaurantName);
        textCapacity = findViewById(R.id.textCapacity);
        buttonReserve = findViewById(R.id.buttonReserve);
        textDescription = findViewById(R.id.textDescription);
        buttonMenus = findViewById(R.id.buttonMenus);
        buttonReviews = findViewById(R.id.buttonReviews);

        buttonFacebook = findViewById(R.id.buttonFacebook);
        buttonInstagram = findViewById(R.id.buttonInstagram);
        buttonWebsite = findViewById(R.id.buttonWebsite);
        buttonWebMenu = findViewById(R.id.buttonWebMenu);
    }

    private void openLink(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }

    private void updateUIWithRestaurantAndTables(Restaurant restaurant) {
        tables = restaurant.getTables();
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String openingHoursForCurrentDay =  Utils.getOpeningHoursForDayOfWeek(restaurant.getOpeningHours(), dayOfWeek);
        String textRestaurantNameOpeningHour = restaurant.getName() + ", " + openingHoursForCurrentDay;
        int maxCapacity = Utils.getMaxCapacity(tables);
        String capacityText = "Liczba gości w przedziale od 1 do " + maxCapacity;
        String contactText = "";

        if (restaurant.getContact() != null) {
            Contact contact = restaurant.getContact();
            contactText = "\nTelefon: " + contact.getPhone() + "\nE-mail: " + contact.getEmail();

            if (contact.getFacebook() != null && !contact.getFacebook().isEmpty()) {
                buttonFacebook.setVisibility(View.VISIBLE);
                buttonFacebook.setOnClickListener(v -> openLink(contact.getFacebook()));
            } else {
                buttonFacebook.setVisibility(View.GONE);
            }

            if (contact.getInstagram() != null && !contact.getInstagram().isEmpty()) {
                buttonInstagram.setVisibility(View.VISIBLE);
                buttonInstagram.setOnClickListener(v -> openLink(contact.getInstagram()));
            } else {
                buttonInstagram.setVisibility(View.GONE);
            }

            if (contact.getWebsite() != null && !contact.getWebsite().isEmpty()) {
                buttonWebsite.setVisibility(View.VISIBLE);
                buttonWebsite.setOnClickListener(v -> openLink(contact.getWebsite()));
            } else {
                buttonWebsite.setVisibility(View.GONE);
            }

            if (contact.getWebMenu() != null && !contact.getWebMenu().isEmpty()) {
                buttonWebMenu.setVisibility(View.VISIBLE);
                buttonWebMenu.setOnClickListener(v -> openLink(contact.getWebMenu()));
            } else {
                buttonWebMenu.setVisibility(View.GONE);
            }

        }

        if (restaurant.getPhotoUrl() != null && !restaurant.getPhotoUrl().isEmpty()) {
            Glide.with(this)
                    .load(restaurant.getPhotoUrl())
                    .into(imageRestaurant);
        } else {
            imageRestaurant.setImageResource(R.mipmap.ic_salad_foreground);
        }

        buttonReviews.setOnClickListener(v -> {
            Intent intentReviews = new Intent(RestaurantDetailsActivity.this, ReviewsActivity.class);
            intentReviews.putExtra("restaurantId", restaurant.getRestaurantId());
            startActivity(intentReviews);
        });

        buttonMenus.setOnClickListener(v -> {
            Intent intentMenus = new Intent(RestaurantDetailsActivity.this, MenuCategoriesActivity.class);
            intentMenus.putExtra("restaurantId", restaurant.getRestaurantId());
            startActivity(intentMenus);
        });

        buttonReserve.setOnClickListener(v -> {
            Intent intentReservation = new Intent(RestaurantDetailsActivity.this, ReservationActivity.class);
            String restaurantJson = new Gson().toJson(restaurant);
            intentReservation.putExtra("restaurant", restaurantJson);
            startActivity(intentReservation);
        });

        textRestaurantName.setText(textRestaurantNameOpeningHour);
        textCapacity.setText(capacityText);
        textDescription.setText(restaurant.getDescription() + contactText);

        Toolbar toolbar = findViewById(R.id.toolbar_restaurant);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(restaurant.getName());
    }

    private void handleNoDataFound() {
        Toast.makeText(this, "Data for the restaurant not found.", Toast.LENGTH_SHORT).show();
    }

    private void handleTaskFailure(@Nullable Exception e) {
        Toast.makeText(this, "Failed to load data: " + (e != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
    }
}