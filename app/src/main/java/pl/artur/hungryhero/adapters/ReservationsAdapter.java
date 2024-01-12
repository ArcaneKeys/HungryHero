package pl.artur.hungryhero.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.ReservationData;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {

    private List<ReservationData> reservationDataList;

    public ReservationsAdapter(List<ReservationData> reservationDataList) {
        this.reservationDataList = reservationDataList;
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationViewHolder holder, int position) {
        ReservationData reservationData = reservationDataList.get(position);
        holder.bind(reservationData);
    }

    @Override
    public int getItemCount() {
        return reservationDataList.size();
    }

    public void setReservations(List<ReservationData> reservationDataList) {
        this.reservationDataList = reservationDataList;
        notifyDataSetChanged();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewReservationDate;
        private TextView textViewReservationTime;
        private TextView textViewNumberOfPeople;

        ReservationViewHolder(View itemView) {
            super(itemView);
            textViewReservationDate = itemView.findViewById(R.id.textViewReservationDate);
            textViewReservationTime = itemView.findViewById(R.id.textViewReservationTime);
            textViewNumberOfPeople = itemView.findViewById(R.id.textViewNumberOfPeople);
        }

        public void bind(ReservationData reservationData) {
            Reservation reservation = reservationData.getReservation();

            String endTIme = adjustEndTime(reservation.getEndTime());

            textViewReservationDate.setText("Data: " + convertDateToString(reservation.getDate()));
            textViewReservationTime.setText("Godziny: " + reservation.getStartTime() + " - " + endTIme);
            textViewNumberOfPeople.setText("Ilość osób: " + reservation.getNumberOfPeople());

            if (reservation.getDate() < System.currentTimeMillis()) {
                textViewReservationDate.setTextColor(Color.GRAY);
                textViewReservationTime.setTextColor(Color.GRAY);
                textViewNumberOfPeople.setTextColor(Color.GRAY);
            } else {
                textViewReservationDate.setTextColor(Color.BLACK);
                textViewReservationTime.setTextColor(Color.BLACK);
                textViewNumberOfPeople.setTextColor(Color.BLACK);
            }
        }

        private String convertDateToString(long dateInMillis) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return simpleDateFormat.format(new Date(dateInMillis));
        }

        private String adjustEndTime(String endTime) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                Date endDate = simpleDateFormat.parse(endTime);
                if (endDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endDate);
                    calendar.set(Calendar.MINUTE, 59);
                    return simpleDateFormat.format(calendar.getTime());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return endTime;
        }


    }
}
