package pl.artur.hungryhero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.adapters.ReservationsAdapter;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class UserReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationsAdapter adapter;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reservations);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Reservation> reservations = new ArrayList<>();
        adapter = new ReservationsAdapter(reservations);
        recyclerView.setAdapter(adapter);

        String userId = firebaseHelper.getCurrentUid();

        firebaseHelper.fetchUserReservations(userId, new FirebaseHelper.OnReservationsFetchedListener() {
            @Override
            public void onReservationsFetched(List<Reservation> reservations) {
                Collections.sort(reservations, (r1, r2) -> Long.compare(r2.getDate(), r1.getDate()));
                adapter.setReservations(reservations);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }
}