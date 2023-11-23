package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.artur.hungryhero.adapters.TableAdapter;
import pl.artur.hungryhero.adapters.TableReviewAdapter;
import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;

public class ReservationActivity extends AppCompatActivity {

    Restaurant restaurant;
    private ImageView imageReservation;
    private TextView textViewSelectDate;
    private RecyclerView tablesRecyclerView;
    private TableReviewAdapter tableAdapter;

    private Map<String, List<Reservation>> tableReservationsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        init();

        restaurant = getIntent().getParcelableExtra("restaurant");

        if (restaurant != null){

            if (restaurant.getPhotoUrl() != null && !restaurant.getPhotoUrl().isEmpty()) {
                Glide.with(this)
                        .load(restaurant.getPhotoUrl())
                        .into(imageReservation);
            } else {
                imageReservation.setImageResource(R.mipmap.ic_salad_foreground);
            }

            textViewSelectDate.setOnClickListener(v -> showDatePickerDialog());

            tableAdapter = new TableReviewAdapter(restaurant.getTables(), tableReservationsMap);
            tablesRecyclerView.setAdapter(tableAdapter);
            tablesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
}