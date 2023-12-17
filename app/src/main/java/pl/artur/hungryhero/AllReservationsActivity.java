package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.adapters.ReservationPagerAdapter;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AllReservationsActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ReservationPagerAdapter pagerAdapter;
    private TreeMap<Long, List<Reservation>> groupedReservations = new TreeMap<>();


    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reservations);

        viewPager = findViewById(R.id.viewPager);
        fetchAllReservations();
    }

    private void fetchAllReservations() {
        firebaseHelper.fetchAllReservations(new FirebaseHelper.OnReservationsFetchedListener() {
            @Override
            public void onReservationsFetched(List<Reservation> reservations) {
                groupReservationsByDate(reservations);
                pagerAdapter = new ReservationPagerAdapter(AllReservationsActivity.this, groupedReservations);
                viewPager.setAdapter(pagerAdapter);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void groupReservationsByDate(List<Reservation> reservations) {
        groupedReservations.clear();

        for (Reservation reservation : reservations) {
            long date = reservation.getDate();
            if (!groupedReservations.containsKey(date)) {
                groupedReservations.put(date, new ArrayList<>());
            }
            groupedReservations.get(date).add(reservation);
        }

        for (List<Reservation> dailyReservations : groupedReservations.values()) {
            Collections.sort(dailyReservations, Comparator.comparing(Reservation::getStartTime));
        }
    }
}