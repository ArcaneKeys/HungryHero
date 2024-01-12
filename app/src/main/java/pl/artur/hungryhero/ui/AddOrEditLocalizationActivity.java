package pl.artur.hungryhero.ui;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Localization;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddOrEditLocalizationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText cityEditText, postalCodeEditText, streetEditText, houseNumberEditText, latitudeEditText, longitudeEditText;
    private EditText addressEditText;
    private Toolbar toolbar;

    private GoogleMap mMap;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_localization);

        Places.initialize(getApplicationContext(), "AIzaSyD-sbzuEtODpf7WeDERDoU7liSb4NnxA3s");

        initializeUI();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFullScreen);
        mapFragment.getMapAsync(this);

        firebaseHelper.getRestaurantData().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                if (restaurant != null) {
                    Localization localization = restaurant.getLocalization();
                    if (localization != null) {
                        fillFormFields(localization);
                    }
                }
                else {
                    Toast.makeText(AddOrEditLocalizationActivity.this, "Błąd podczas konwertowania dokumentu na obiekt Restaurant.", Toast.LENGTH_SHORT).show();
                }
            } else {
                firebaseHelper.createEmptyRestaurantDocument();
            }
        });

    }

    private void fillFormFields(Localization localization) {
        cityEditText.setText(localization.getCity() != null ? localization.getCity() : "");
        postalCodeEditText.setText(localization.getPostalCode() != null ? localization.getPostalCode() : "");
        streetEditText.setText(localization.getStreet() != null ? localization.getStreet() : "");
        houseNumberEditText.setText(localization.getHouseNumber() != null ? localization.getHouseNumber() : "");


        GeoPoint coordinates = localization.getCoordinates();
        if (coordinates != null) {
            latitudeEditText.setText(String.valueOf(coordinates.getLatitude()));
            longitudeEditText.setText(String.valueOf(coordinates.getLongitude()));
            updateMapLocation(coordinates.getLatitude(), coordinates.getLongitude());
        } else {
            latitudeEditText.setText("");
            longitudeEditText.setText("");
        }

        String fullAddress = String.format(
                "%s %s, %s %s",
                localization.getStreet() != null ? localization.getStreet() : "",
                localization.getHouseNumber() != null ? localization.getHouseNumber() : "",
                localization.getPostalCode() != null ? localization.getPostalCode() : "",
                localization.getCity() != null ? localization.getCity() : ""
        );
        addressEditText.setText(fullAddress.trim());

    }

    private void initializeUI() {
        cityEditText = findViewById(R.id.cityEditText);
        postalCodeEditText = findViewById(R.id.postalCodeEditText);
        streetEditText = findViewById(R.id.streetEditText);
        houseNumberEditText = findViewById(R.id.houseNumberEditText);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        addressEditText = findViewById(R.id.addressEditText);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button saveLocalizationButton = findViewById(R.id.saveLocalizationButton);
        Button getLocalizationButton = findViewById(R.id.getLocalizationButton);

        addressEditText.setFocusable(false);
        addressEditText.setOnClickListener(v -> startAutocompleteActivity());

        getLocalizationButton.setOnClickListener(v -> {
            try {
                double latitude = Double.parseDouble(latitudeEditText.getText().toString());
                double longitude = Double.parseDouble(longitudeEditText.getText().toString());
                updateMapLocation(latitude, longitude);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        saveLocalizationButton.setOnClickListener(v -> {
            Localization localization = getLocalizationFromForm();
            firebaseHelper.updateLocalizationData(localization)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddOrEditLocalizationActivity.this, "Dane zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditLocalizationActivity.this, "Błąd podczas aktualizacji danych", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void startAutocompleteActivity() {
        List<Place.Field> fields = Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG
        );

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        autocompleteActivityResultLauncher.launch(intent);
    }

    private Localization getLocalizationFromForm() {
        String city = cityEditText.getText().toString().trim();
        String postalCode = postalCodeEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        String houseNumber = houseNumberEditText.getText().toString().trim();

        double latitude = 0;
        double longitude = 0;

        try {
            latitude = Double.parseDouble(latitudeEditText.getText().toString().trim());
            longitude = Double.parseDouble(longitudeEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Log.e(TAG, "Błąd konwersji szerokości/długości geograficznej na liczbę", e);
        }

        GeoPoint coordinates = new GeoPoint(latitude, longitude);

        return new Localization(city, postalCode, street, houseNumber, coordinates);
    }


    private final ActivityResultLauncher<Intent> autocompleteActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                            Place place = Autocomplete.getPlaceFromIntent(data);
                            processSelectedPlace(place);
                        } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && data != null) {
                            Status status = Autocomplete.getStatusFromIntent(data);
                            Log.i(TAG, status.getStatusMessage());
                        }
                    }
            );

    private void processSelectedPlace(Place place) {
        addressEditText.setText(place.getAddress());
        AddressComponents addressComponents = place.getAddressComponents();
        if (addressComponents != null) {
            for (AddressComponent component : addressComponents.asList()) {
                String type = component.getTypes().get(0);
                switch (type) {
                    case "locality":
                        cityEditText.setText(component.getName());
                        break;
                    case "route":
                        streetEditText.setText(component.getName());
                        break;
                    case "postal_code":
                        postalCodeEditText.setText(component.getName());
                        break;
                    case "premise":
                    case "street_number":
                        houseNumberEditText.setText(component.getName());
                        break;
                }
            }
        }

        LatLng latLng = place.getLatLng();
        if (latLng != null) {
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;
            latitudeEditText.setText(String.valueOf(latitude));
            longitudeEditText.setText(String.valueOf(longitude));
            updateMapLocation(latitude, longitude);
        }
    }

    private void updateMapLocation(double latitude, double longitude) {
        LatLng location = new LatLng(latitude, longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location).title("Wybrana lokalizacja"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Wybrana lokalizacja"));
            latitudeEditText.setText(String.valueOf(latLng.latitude));
            longitudeEditText.setText(String.valueOf(latLng.longitude));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}