package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Restaurant;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private TextView test_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        test_text_view = findViewById(R.id.test_text_view);

        Intent intent = getIntent();
        if (intent != null) {
            Restaurant restaurant = intent.getParcelableExtra("restaurant");
            if (restaurant != null) {
                test_text_view.setText(restaurant.getName());
            }
        }
    }
}