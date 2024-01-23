package pl.artur.hungryhero.adapters;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Localization;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.ReservationData;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {

    private List<ReservationData> reservationDataList;
    private boolean isRestaurant;

    public ReservationsAdapter(List<ReservationData> reservationDataList, boolean isRestaurant) {
        this.reservationDataList = reservationDataList;
        this.isRestaurant = isRestaurant;
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationViewHolder holder, int position) {
        ReservationData reservationData = reservationDataList.get(position);
        holder.bind(reservationData, isRestaurant);
    }

    @Override
    public int getItemCount() {
        return reservationDataList.size();
    }

    public void setReservations(List<ReservationData> reservationDataList) {
        this.reservationDataList = reservationDataList;
        notifyDataSetChanged();
    }

    public void setIsRestaurant(boolean isRestaurant) {
        this.isRestaurant = isRestaurant;
        notifyDataSetChanged();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewReservationDate;
        private TextView textViewReservationTime;
        private TextView textViewNumberOfPeople;

        private TextView textViewRestaurantName;
        private TextView textViewLocation;
        private TextView textViewUserEmail;
        private TextView textViewUserPhone;

        private MaterialButton btnNavigate;

        ReservationViewHolder(View itemView) {
            super(itemView);
            textViewReservationDate = itemView.findViewById(R.id.textViewReservationDate);
            textViewReservationTime = itemView.findViewById(R.id.textViewReservationTime);
            textViewNumberOfPeople = itemView.findViewById(R.id.textViewNumberOfPeople);

            textViewRestaurantName = itemView.findViewById(R.id.textViewRestaurantName);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewUserEmail = itemView.findViewById(R.id.textViewUserEmail);
            textViewUserPhone = itemView.findViewById(R.id.textViewUserPhone);

            btnNavigate = itemView.findViewById(R.id.btnNavigate);
        }

        public void bind(ReservationData reservationData, boolean isRestaurant) {
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

            if (!isRestaurant) {
                String name = "";
                String localizationText = "";
                Uri gmmIntentUri;

                if (reservationData.getRestaurant().getName() != null) {
                    name = reservationData.getRestaurant().getName();
                }

                if (reservationData.getRestaurant().getLocalization() != null) {
                    Localization localization = reservationData.getRestaurant().getLocalization();

                    if (localization.getStreet() != null) {
                        localizationText = localization.getCity()  + ", " + localization.getStreet() + " " + localization.getHouseNumber();
                    } else {
                        localizationText = localization.getCity()  + " "  + localization.getHouseNumber();
                    }

                    if (localization.getCoordinates() != null) {
                        double latitude = localization.getCoordinates().getLatitude();
                        double longitude = localization.getCoordinates().getLongitude();

                        gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                    } else {
                        StringBuilder addressBuilder = new StringBuilder(localizationText);
                        if (localization.getPostalCode() != null && !localization.getPostalCode().isEmpty()) {
                            addressBuilder.append(", ").append(localization.getPostalCode());
                        }

                        gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(addressBuilder.toString()));
                    }

                    btnNavigate.setOnClickListener(view -> {
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
                            view.getContext().startActivity(mapIntent);
                        } else {
                            Toast.makeText(view.getContext(), "Google Maps nie jest zainstalowane", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                textViewRestaurantName.setText("Nazwa: " + name);
                textViewLocation.setText("Lokalizacja: " + localizationText);

                textViewRestaurantName.setVisibility(View.VISIBLE);
                textViewLocation.setVisibility(View.VISIBLE);
                btnNavigate.setVisibility(View.VISIBLE);

                textViewUserEmail.setVisibility(View.GONE);
                textViewUserPhone.setVisibility(View.GONE);
            } else {
                String email = "";
                String phone = "";

                if (reservationData.getUser().getEmail() != null) {
                    email = reservationData.getUser().getEmail();
                }

                if (reservationData.getUser().getPhone() != null) {
                    phone = reservationData.getUser().getPhone();
                }

                if (!Objects.equals(phone, "")) {
                    textViewUserPhone.setText("Telefon: " + phone);
                    textViewUserPhone.setVisibility(View.VISIBLE);
                } else {
                    textViewUserPhone.setVisibility(View.GONE);
                }

                if (!Objects.equals(email, "")) {
                    textViewUserEmail.setText("Email: " + email);
                    textViewUserEmail.setVisibility(View.VISIBLE);
                } else {
                    textViewUserEmail.setVisibility(View.GONE);
                }

                textViewRestaurantName.setVisibility(View.GONE);
                textViewLocation.setVisibility(View.GONE);
                btnNavigate.setVisibility(View.GONE);
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
