package pl.artur.hungryhero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.artur.hungryhero.adapters.ReservationsAdapter;
import pl.artur.hungryhero.models.Reservation;

public class ReservationsFragment extends Fragment {
    private static final String ARG_RESERVATIONS = "reservations";
    private List<Reservation> reservations;

    public ReservationsFragment() {}

    public static ReservationsFragment newInstance(List<Reservation> reservations) {
        ReservationsFragment fragment = new ReservationsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_RESERVATIONS, new ArrayList<>(reservations));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservations = getArguments().getParcelableArrayList(ARG_RESERVATIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ReservationsAdapter(reservations));
        return view;
    }
}
