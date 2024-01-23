package pl.artur.hungryhero.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.adapters.ReservationsAdapter;
import pl.artur.hungryhero.models.ReservationData;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class ReservationsFragment extends Fragment {
    private static final String ARG_RESERVATIONS = "reservations";
    private List<ReservationData> reservationDataList;

    private RecyclerView recyclerView;
    private boolean isRestaurant;

    @Inject
    FirebaseHelper firebaseHelper;

    public ReservationsFragment() {}

    public static ReservationsFragment newInstance(List<ReservationData> reservationDataList) {
        ReservationsFragment fragment = new ReservationsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_RESERVATIONS, new ArrayList<>(reservationDataList));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservationDataList = getArguments().getParcelableArrayList(ARG_RESERVATIONS);
        }

        firebaseHelper.isRestaurant(isRestaurant -> {
            this.isRestaurant = isRestaurant;
            updateAdapter();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ReservationsAdapter(reservationDataList, this.isRestaurant));
        return view;
    }

    private void updateAdapter() {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            ((ReservationsAdapter) recyclerView.getAdapter()).setIsRestaurant(this.isRestaurant);
        }
    }
}
