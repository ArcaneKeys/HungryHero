package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.firestore.GeoPoint;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.models.Localization;
import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddOrEditOpeningHoursActivity extends AppCompatActivity {

    private EditText mondayEditText, tuesdayEditText, wednesdayEditText, thursdayEditText, fridayEditText, saturdayEditText, sundayEditText;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_opening_hours);

        initializeUI();

        firebaseHelper.getRestaurantData().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                if (restaurant != null) {
                    OpeningHours openingHours = restaurant.getOpeningHours();
                    if (openingHours != null) {
                        fillFormFields(openingHours);
                    }
                }
                else {
                    Toast.makeText(AddOrEditOpeningHoursActivity.this, "Błąd podczas konwertowania dokumentu na obiekt Restaurant.", Toast.LENGTH_SHORT).show();
                }
            } else {
                firebaseHelper.createEmptyRestaurantDocument();
            }
        });
    }

    private void fillFormFields(OpeningHours openingHours) {
        mondayEditText.setText(openingHours.getMonday() != null ? openingHours.getMonday() : "");
        tuesdayEditText.setText(openingHours.getTuesday() != null ? openingHours.getTuesday() : "");
        wednesdayEditText.setText(openingHours.getWednesday() != null ? openingHours.getWednesday() : "");
        thursdayEditText.setText(openingHours.getThursday() != null ? openingHours.getThursday() : "");
        fridayEditText.setText(openingHours.getFriday() != null ? openingHours.getFriday() : "");
        saturdayEditText.setText(openingHours.getSaturday() != null ? openingHours.getSaturday() : "");
        sundayEditText.setText(openingHours.getSunday() != null ? openingHours.getSunday() : "");
    }

    private OpeningHours getOpeningHoursFromForm() {
        String monday = String.valueOf(mondayEditText.getText());
        String tuesday = String.valueOf(tuesdayEditText.getText());
        String wednesday = String.valueOf(wednesdayEditText.getText());
        String thursday = String.valueOf(thursdayEditText.getText());
        String friday = String.valueOf(fridayEditText.getText());
        String saturday = String.valueOf(saturdayEditText.getText());
        String sunday = String.valueOf(sundayEditText.getText());

        return new OpeningHours(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    private void initializeUI() {
        mondayEditText = findViewById(R.id.mondayEditText);
        tuesdayEditText = findViewById(R.id.tuesdayEditText);
        wednesdayEditText = findViewById(R.id.wednesdayEditText);
        thursdayEditText = findViewById(R.id.thursdayEditText);
        fridayEditText = findViewById(R.id.fridayEditText);
        saturdayEditText = findViewById(R.id.saturdayEditText);
        sundayEditText = findViewById(R.id.sundayEditText);

        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            OpeningHours openingHours = getOpeningHoursFromForm();
            firebaseHelper.updateOpeningHoursData(openingHours)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddOrEditOpeningHoursActivity.this, "Dane zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditOpeningHoursActivity.this, "Błąd podczas aktualizacji danych", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    public void showTimePickerDialog(View view) {
        final EditText editText = (EditText) view;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_time_picker);
        dialog.setTitle("Wybierz godziny");

        final TimePicker startTimePicker = dialog.findViewById(R.id.startTimePicker);
        final TimePicker endTimePicker = dialog.findViewById(R.id.endTimePicker);
        Button closedButton = dialog.findViewById(R.id.closedButton);
        Button confirmButton = dialog.findViewById(R.id.confirmButton);

        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        String existingText = editText.getText().toString();
        if (!existingText.equals("Zamknięte") && existingText.contains("-")) {
            String[] times = existingText.split(" - ");
            if (times.length == 2) {
                String[] startTimes = times[0].split(":");
                String[] endTimes = times[1].split(":");
                if (startTimes.length == 2 && endTimes.length == 2) {
                    try {
                        int startHour = Integer.parseInt(startTimes[0]);
                        int startMinute = Integer.parseInt(startTimes[1]);
                        int endHour = Integer.parseInt(endTimes[0]);
                        int endMinute = Integer.parseInt(endTimes[1]);

                        startTimePicker.setHour(startHour);
                        startTimePicker.setMinute(startMinute);
                        endTimePicker.setHour(endHour);
                        endTimePicker.setMinute(endMinute);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        closedButton.setOnClickListener(v -> {
            editText.setText("Zamknięte");
            dialog.dismiss();
        });

        confirmButton.setOnClickListener(v -> {
            int startHour = startTimePicker.getHour();
            int startMinute = startTimePicker.getMinute();
            int endHour = endTimePicker.getHour();
            int endMinute = endTimePicker.getMinute();

            String timeRange = String.format(Locale.getDefault(), "%02d:%02d - %02d:%02d",
                    startHour, startMinute, endHour, endMinute);
            editText.setText(timeRange);
            dialog.dismiss();
        });

        dialog.show();
    }

}