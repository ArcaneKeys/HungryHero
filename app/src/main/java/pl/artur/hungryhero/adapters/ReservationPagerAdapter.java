package pl.artur.hungryhero.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pl.artur.hungryhero.ReservationsFragment;
import pl.artur.hungryhero.models.Reservation;

public class ReservationPagerAdapter extends FragmentStateAdapter {

    private TreeMap<Long, List<Reservation>> groupedReservations;

    public ReservationPagerAdapter(FragmentActivity fa, TreeMap<Long, List<Reservation>> groupedReservations) {
        super(fa);
        this.groupedReservations = groupedReservations;
    }

    @Override
    public Fragment createFragment(int position) {
        List<Reservation> reservationsForDate = new ArrayList<>(groupedReservations.values()).get(position);
        return ReservationsFragment.newInstance(reservationsForDate);
    }

    @Override
    public int getItemCount() {
        return groupedReservations.size();
    }
}
