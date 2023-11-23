package pl.artur.hungryhero.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.Table;

public class TableReviewAdapter extends RecyclerView.Adapter<TableReviewAdapter.ViewHolder>{

    private final List<Table> tables;
    private final Map<String, List<Reservation>> tableReservationsMap;
    private String openHours;

    public TableReviewAdapter(List<Table> tables, Map<String, List<Reservation>> tableReservationsMap) {
        this.tables = tables;
        this.tableReservationsMap = tableReservationsMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Table table = tables.get(position);
        List<Reservation> reservations = tableReservationsMap.get(table.getTableId());

        holder.capacityTextView.setText("Miejsca: " + table.getCapacity());
        holder.hoursChipGroup.removeAllViews();

        if (isValidOpenHours(openHours)) {
            List<String> availableHours = getAvailableHoursList(openHours, reservations);

            for (String hour : availableHours) {
                Chip chip = new Chip(holder.hoursChipGroup.getContext());
                chip.setText(hour);
                chip.setCheckable(true);

                chip.setOnClickListener(v -> {
                    boolean isChecked = chip.isChecked();
                    handleChipClick(holder.hoursChipGroup, availableHours, hour, isChecked);
                });

                holder.hoursChipGroup.addView(chip);
            }
        }
    }

    private void handleChipClick(ChipGroup chipGroup, List<String> availableHours, String selectedHour, boolean isChecked) {
        int clickedIndex = availableHours.indexOf(selectedHour);
        int closestSelectedIndexBefore = findClosestSelectedIndexBefore(clickedIndex, chipGroup);

        if (isChecked) {
            if (isContinuousSelection(clickedIndex, closestSelectedIndexBefore, availableHours)) {
                selectChipsInRange(chipGroup, closestSelectedIndexBefore, clickedIndex);
            } else {
                deselectAllChipsExcept(chipGroup, clickedIndex);
            }
        } else {
            deselectChipsStartingFrom(chipGroup, clickedIndex);
        }
    }

    private int findClosestSelectedIndexBefore(int index, ChipGroup chipGroup) {
        for (int i = index - 1; i >= 0; i--) {
            if (((Chip) chipGroup.getChildAt(i)).isChecked()) {
                return i;
            }
        }
        return -1;
    }

    private boolean isContinuousSelection(int currentIndex, int closestSelectedIndex, List<String> availableHours) {
        if (closestSelectedIndex == -1) {
            return false;
        }

        for (int i = closestSelectedIndex + 1; i <= currentIndex; i++) {
            if (!availableHours.contains(availableHours.get(i))) {
                return false;
            }
        }
        return true;
    }

    private void deselectChipsStartingFrom(ChipGroup chipGroup, int startIndex) {
        for (int i = startIndex; i < chipGroup.getChildCount(); i++) {
            ((Chip) chipGroup.getChildAt(i)).setChecked(false);
        }
    }

    private void deselectAllChipsExcept(ChipGroup chipGroup, int exceptIndex) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            ((Chip) chipGroup.getChildAt(i)).setChecked(i == exceptIndex);
        }
    }

    private void selectChipsInRange(ChipGroup chipGroup, int startIndex, int endIndex) {
        for (int i = startIndex; i <= endIndex; i++) {
            ((Chip) chipGroup.getChildAt(i)).setChecked(true);
        }
    }

    private List<String> getAvailableHoursList(String openHours, List<Reservation> reservations) {
        List<String> hoursList = new ArrayList<>();
        Set<String> reservedHours = new HashSet<>();

        for (Reservation reservation : reservations) {
            reservedHours.addAll(getHoursFromReservation(reservation));
        }

        if (openHours != null && openHours.contains(" - ")) {
            String[] parts = openHours.split(" - ");
            String startHour = parts[0];
            String endHour = parts[1];
            try {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();

                String[] startParts = startHour.split(":");
                String[] endParts = endHour.split(":");

                start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startParts[0]));
                start.set(Calendar.MINUTE, Integer.parseInt(startParts[1]));
                end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endParts[0]));
                end.set(Calendar.MINUTE, Integer.parseInt(endParts[1]));

                if (start.after(end)) {
                    end.add(Calendar.DAY_OF_MONTH, 1);
                }

                while (start.before(end)) {
                    String hourString = String.format("%02d:%02d", start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE));
                    if (!reservedHours.contains(hourString)) {
                        hoursList.add(hourString);
                    }
                    start.add(Calendar.HOUR, 1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hoursList;
    }

    private Set<String> getHoursFromReservation(Reservation reservation) {
        Set<String> hours = new HashSet<>();
        try {
            String startTime = reservation.getStartTime();
            String endTime = reservation.getEndTime();

            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();

            String[] startParts = startTime.split(":");
            String[] endParts = endTime.split(":");

            startCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startParts[0]));
            startCalendar.set(Calendar.MINUTE, Integer.parseInt(startParts[1]));
            endCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endParts[0]));
            endCalendar.set(Calendar.MINUTE, Integer.parseInt(endParts[1]));

            while (startCalendar.before(endCalendar)) {
                String hourString = String.format("%02d:%02d", startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE));
                hours.add(hourString);
                startCalendar.add(Calendar.HOUR, 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hours;
    }

    private boolean isValidOpenHours(String openHours) {
        return openHours != null && !openHours.isEmpty() && !openHours.equals("Zamknięte");
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView capacityTextView;
        ChipGroup hoursChipGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            capacityTextView = itemView.findViewById(R.id.capacityTextView);
            hoursChipGroup = itemView.findViewById(R.id.hoursChipGroup);
        }

    }

}
