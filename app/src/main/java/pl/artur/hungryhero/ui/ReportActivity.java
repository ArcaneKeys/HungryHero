package pl.artur.hungryhero.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.ReservationData;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class ReportActivity extends AppCompatActivity {

    @Inject
    FirebaseHelper firebaseHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fetchReservationsForCurrentMonth();
    }

    private void fetchReservationsForCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        firebaseHelper.fetchReservationsForMonth(year, month, new FirebaseHelper.OnReservationsFetchedListener() {
            @Override
            public void onReservationsFetched2(List<Reservation> reservations) {
                Map<Integer, Integer> reservationsCountByDay = new HashMap<>();
                for (Reservation reservation : reservations) {
                    Calendar reservationDate = Calendar.getInstance();
                    reservationDate.setTimeInMillis(reservation.getDate());
                    int dayOfMonth = reservationDate.get(Calendar.DAY_OF_MONTH);
                    reservationsCountByDay.put(dayOfMonth, reservationsCountByDay.getOrDefault(dayOfMonth, 0) + 1);
                }

                generateChart(reservationsCountByDay, year, month);
            }

            @Override
            public void onReservationsFetched(List<ReservationData> reservationDataList) {
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void generateChart(Map<Integer, Integer> reservationsCountByDay, int year, int month) {
        BarChart chart = findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();

        for (int day = 1; day <= 31; day++) {
            entries.add(new BarEntry(day, reservationsCountByDay.getOrDefault(day, 0)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Liczba Rezerwacji na Dzień");
        BarData barData = new BarData(dataSet);
        dataSet.setDrawValues(true);

        configureXAxis(chart, month, year);

        chart.setData(barData);
        chart.invalidate();
        setChartTitle(chart, year, month);
    }

    private void setChartTitle(BarChart chart, int year, int month) {
        String monthName = new DateFormatSymbols().getMonths()[month];
        chart.getDescription().setText("Rezerwacje dla: " + monthName + " " + year);
    }

    private List<String> getDaysOfMonth(int month, int year) {
        List<String> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
        }
        return days;
    }

    private int getDaysInMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void configureXAxis(BarChart chart, int month, int year) {
        XAxis xAxis = chart.getXAxis();
        int daysInMonth = getDaysInMonth(month, year);

        List<String> xAxisValues = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            xAxisValues.add(String.valueOf(i));
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(xAxisValues.size(), true);
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