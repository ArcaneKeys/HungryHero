package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.utils.Utils;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ImageView imageRestaurant;
    private TextView textRestaurantName;
    private TextView textCapacity;
    private Button buttonReserve;
    private TextView textDescription;
    private RecyclerView recyclerViewRestaurantDetails;
    private Button buttonMenus;
    private Button buttonShowOnMap;
    private Button buttonReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        init();

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Intent intent = getIntent();
        if (intent != null) {
            Restaurant restaurant = intent.getParcelableExtra("restaurant");
            if (restaurant != null) {
                // Set the image using Picasso or Glide library if the image URL is available
                // For example, Picasso.get().load(restaurant.getImageUrl()).into(imageRestaurant);

                // Pobierz odpowiednie godziny otwarcia na podstawie numeru dnia tygodnia
                String openingHoursForCurrentDay =  Utils.getOpeningHoursForDayOfWeek(restaurant.getOpeningHours(), dayOfWeek);

                String textRestaurantNameOpeningHour = restaurant.getName() + ", " + openingHoursForCurrentDay;

                // Otrzymujemy listę wszystkich stolików z danej restauracji
                List<Table> tables = restaurant.getTables();

                // Pobieramy maksymalną pojemność ze stolików
                int maxCapacity = Utils.getMaxCapacity(tables);

                String capacityText = "Liczba gości w przedziale od 1 do " + maxCapacity;

                textRestaurantName.setText(textRestaurantNameOpeningHour);
                textCapacity.setText(capacityText);
            }
        }
    }

    protected void init(){
        imageRestaurant = findViewById(R.id.imageRestaurant);
        textRestaurantName = findViewById(R.id.textRestaurantName);
        textCapacity = findViewById(R.id.textCapacity);
        buttonReserve = findViewById(R.id.buttonReserve);
        textDescription = findViewById(R.id.textDescription);
        recyclerViewRestaurantDetails = findViewById(R.id.recyclerViewRestaurantDetails);
        buttonMenus = findViewById(R.id.buttonMenus);
        buttonShowOnMap = findViewById(R.id.buttonShowOnMap);
        buttonReviews = findViewById(R.id.buttonReviews);
    }
}