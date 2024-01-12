package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.adapters.ReservationPagerAdapter;
import pl.artur.hungryhero.adapters.ReservationsAdapter;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.ReservationData;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class UserReservationsActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ReservationPagerAdapter pagerAdapter;
    private TreeMap<Long, List<ReservationData>> groupedReservations = new TreeMap<>();
    private TextView currentDateTextView;
    Toolbar toolbar;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reservations);

        viewPager = findViewById(R.id.viewPager);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String userId = firebaseHelper.getCurrentUid();

        firebaseHelper.fetchUserReservations(userId, new FirebaseHelper.OnReservationsFetchedListener() {
            @Override
            public void onReservationsFetched(List<ReservationData> reservationDataList) {
                groupReservationsByDate(reservationDataList);
                pagerAdapter = new ReservationPagerAdapter(UserReservationsActivity.this, groupedReservations);
                viewPager.setAdapter(pagerAdapter);
                setCurrentPageToNearestDate();
            }

            @Override
            public void onReservationsFetched2(List<Reservation> reservations) {

            }

            @Override
            public void onError(Exception e) {
            }
        });

        currentDateTextView = findViewById(R.id.currentDateTextView);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Long date = new ArrayList<>(groupedReservations.keySet()).get(position);
                updateCurrentDateTextView(date, currentDateTextView);
            }
        });

        Button openDatePickerButton = findViewById(R.id.openDatePickerButton);
        openDatePickerButton.setOnClickListener(v -> showDatePickerDialog());
    }

    private void groupReservationsByDate(List<ReservationData> reservationDataList) {
        groupedReservations.clear();

        for (ReservationData reservationData : reservationDataList) {
            Reservation reservation = reservationData.getReservation();
            long date = reservation.getDate();
            groupedReservations.computeIfAbsent(date, k -> new ArrayList<>()).add(reservationData);
        }

        for (List<ReservationData> dailyReservations : groupedReservations.values()) {
            Collections.sort(dailyReservations, Comparator.comparing(rd -> rd.getReservation().getStartTime()));
        }
    }

    private void setCurrentPageToNearestDate() {
        long currentDate = System.currentTimeMillis();
        long nearestDate = Long.MAX_VALUE;
        int nearestPageIndex = 0;
        int pageIndex = 0;

        for (Long date : groupedReservations.keySet()) {
            if (Math.abs(currentDate - date) < Math.abs(currentDate - nearestDate)) {
                nearestDate = date;
                nearestPageIndex = pageIndex;
            }
            pageIndex++;
        }

        viewPager.setCurrentItem(nearestPageIndex, false);
        updateCurrentDateTextView(nearestDate, currentDateTextView);
    }

    private void updateCurrentDateTextView(long date, TextView textView) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        textView.setText(dateFormat.format(new Date(date)));
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            calendar.set(Calendar.YEAR, selectedYear);
            calendar.set(Calendar.MONTH, selectedMonth);
            calendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long selectedDate = calendar.getTimeInMillis();
            setCurrentPageToDate(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void setCurrentPageToDate(long date) {
        if (groupedReservations.containsKey(date)) {
            int pageIndex = new ArrayList<>(groupedReservations.keySet()).indexOf(date);
            viewPager.setCurrentItem(pageIndex, false);
            updateCurrentDateTextView(date, currentDateTextView);
        }
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