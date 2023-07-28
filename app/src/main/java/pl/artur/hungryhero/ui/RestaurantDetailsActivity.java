package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Contact;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.utils.Utils;

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
                // Only Reviews
                buttonReviews.setOnClickListener(v -> {
                    Intent intentReviews = new Intent(RestaurantDetailsActivity.this, ReviewsActivity.class);
                    intentReviews.putExtra("restaurantId", restaurant.getRestaurantId());
                    startActivity(intentReviews);
                });

                textRestaurantName.setText(textRestaurantNameOpeningHour);
                textCapacity.setText(capacityText);
                textDescription.setText(restaurant.getDescription() + contactText);

                // Ustaw tytuł na pasku narzędziowym
                Toolbar toolbar = findViewById(R.id.toolbar_restaurant);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(restaurant.getName());
            }
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
}