package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.adapters.TableAdapter;
import pl.artur.hungryhero.adapters.TableReviewAdapter;
import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class ReservationActivity extends AppCompatActivity {

    Restaurant restaurant;
    private ImageView imageReservation;
    private TextView textViewSelectDate;
    private RecyclerView tablesRecyclerView;
    private TableReviewAdapter tableAdapter;
    private Button btnReserve;
    private Map<String, List<Reservation>> tableReservationsMap = new HashMap<>();
    private String userId;
    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        init();

        String restaurantJson = getIntent().getStringExtra("restaurant");
        restaurant = new Gson().fromJson(restaurantJson, Restaurant.class);

        if (restaurant != null){

            if (restaurant.getPhotoUrl() != null && !restaurant.getPhotoUrl().isEmpty()) {
                Glide.with(this)
                        .load(restaurant.getPhotoUrl())
                        .into(imageReservation);
            } else {
                imageReservation.setImageResource(R.mipmap.ic_salad_foreground);
            }

            userId = firebaseHelper.getCurrentUid();

            textViewSelectDate.setOnClickListener(v -> showDatePickerDialog());

            tableAdapter = new TableReviewAdapter(restaurant.getTables(), tableReservationsMap, userId);
            tablesRecyclerView.setAdapter(tableAdapter);
            tablesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            btnReserve.setOnClickListener(v -> {
                Map<String, Reservation> selectedReservations = tableAdapter.getSelectedReservations();
                if (selectedReservations.isEmpty()) {
                    Toast.makeText(ReservationActivity.this, "Nie wybrano żadnych rezerwacji.", Toast.LENGTH_LONG).show();
                } else {
                    showReservationDialog(new ArrayList<>(selectedReservations.values()));
                }
            });

        } else {
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar_reservation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void init() {
        imageReservation = findViewById(R.id.imageReservation);
        textViewSelectDate = findViewById(R.id.textViewSelectDate);
        tablesRecyclerView = findViewById(R.id.tablesRecyclerView);
        btnReserve = findViewById(R.id.btnReserve);
    }

    private void showReservationDialog(List<Reservation> reservations) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Szczegóły Rezerwacji");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int reservationNumber = 1;
        for (Reservation reservation : reservations) {
            TextView reservationDetails = new TextView(this);
            String dateAsString = convertDateToString(reservation.getDate());
            String endTimeAdjusted = adjustEndTime(reservation.getEndTime());

            String details = "Rezerwacja " + reservationNumber +
                    "\nData: " + dateAsString +
                    "\nIlość osób: " + reservation.getNumberOfPeople() +
                    "\nGodziny: " + reservation.getStartTime() + " - " + endTimeAdjusted;
            reservationDetails.setText(details);
            layout.addView(reservationDetails);
        }

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint("Uwagi do rezerwacji");
        layout.addView(input);

        builder.setView(layout);

        builder.setPositiveButton("Potwierdź", (dialog, which) -> {
            String notes = input.getText().toString();
            Map<String, Reservation> selectedReservations = tableAdapter.getSelectedReservations();
            AtomicInteger remainingReservations = new AtomicInteger(selectedReservations.size());
            for (Map.Entry<String, Reservation> entry : selectedReservations.entrySet()) {
                Reservation reservation = entry.getValue();
                reservation.setNote(notes);
                reservation.setRestaurantId(restaurant.getRestaurantId());
                // Zapisz rezerwację w Firebase
                firebaseHelper.addReservation(reservation, restaurant.getRestaurantId(), entry.getKey())
                        .addOnSuccessListener(collectionReference -> {
                            if (remainingReservations.decrementAndGet() == 0) {
                                Toast.makeText(ReservationActivity.this, "Wszystkie rezerwacje zapisane.", Toast.LENGTH_SHORT).show();
                                finish(); // Zakończ aktywność po pomyślnym zapisaniu wszystkich rezerwacji
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ReservationActivity.this, "Błąd zapisu rezerwacji.", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private String convertDateToString(long dateInt) {
        Date date = new Date(dateInt);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private String adjustEndTime(String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date endDate = sdf.parse(endTime);
            if (endDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.HOUR, 1);
                return sdf.format(calendar.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endTime;
    }

    private void showDatePickerDialog() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    handleDateSelection(selectedYear, selectedMonth, selectedDay);
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
        Calendar oneMonthAhead = Calendar.getInstance();
        oneMonthAhead.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMaxDate(oneMonthAhead.getTimeInMillis());

        datePickerDialog.show();
    }

    private void handleDateSelection(int year, int month, int day) {
        String selectedDate = day + "/" + (month + 1) + "/" + year;
        textViewSelectDate.setText(selectedDate);

        long selectedTimestamp = getTimestampFromDate(year, month, day);
        filterReservationsByDate(selectedTimestamp);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        handleDayOfWeek(dayOfWeek);

        tableAdapter.setSelectedDate(selectedDate);

        tableAdapter.notifyDataSetChanged();
    }

    private void handleDayOfWeek(int dayOfWeek) {
        OpeningHours openingHours = restaurant.getOpeningHours();
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                tableAdapter.setOpenHours(openingHours.getMonday());
                break;
            case Calendar.TUESDAY:
                tableAdapter.setOpenHours(openingHours.getTuesday());
                break;
            case Calendar.WEDNESDAY:
                tableAdapter.setOpenHours(openingHours.getWednesday());
                break;
            case Calendar.THURSDAY:
                tableAdapter.setOpenHours(openingHours.getThursday());
                break;
            case Calendar.FRIDAY:
                tableAdapter.setOpenHours(openingHours.getFriday());
                break;
            case Calendar.SATURDAY:
                tableAdapter.setOpenHours(openingHours.getSaturday());
                break;
            case Calendar.SUNDAY:
                tableAdapter.setOpenHours(openingHours.getSunday());
                break;
            default:
                tableAdapter.setOpenHours("Zamknięte");
        }
    }

    private void filterReservationsByDate(long timestamp) {
        if (restaurant != null && restaurant.getTables() != null) {
            for (Table table : restaurant.getTables()) {
                List<Reservation> filteredReservations = getReservationsForDate(table.getReservations(), timestamp);
                tableReservationsMap.put(table.getTableId(), filteredReservations);
            }

        }
    }

    private List<Reservation> getReservationsForDate(List<Reservation> reservations, long selectedDateTimestamp) {
        List<Reservation> filteredReservations = new ArrayList<>();
        if (reservations != null) {
            long dayStartTimestamp = getStartOfDayTimestamp(selectedDateTimestamp);
            long dayEndTimestamp = getEndOfDayTimestamp(selectedDateTimestamp);

            for (Reservation reservation : reservations) {
                long reservationTimestamp = reservation.getDate();

                if (reservationTimestamp >= dayStartTimestamp && reservationTimestamp <= dayEndTimestamp) {
                    filteredReservations.add(reservation);
                }
            }
        }
        return filteredReservations;
    }

    private long getStartOfDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long getEndOfDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    private long getTimestampFromDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTimeInMillis();
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