package pl.artur.hungryhero;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.ui.ChangeUserDataActivity;
import pl.artur.hungryhero.ui.DrawerManager;
import pl.artur.hungryhero.ui.RestaurantDetailsActivity;
import pl.artur.hungryhero.utils.FirebaseManager;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context context;
    private List<Restaurant> restaurantList;

    public RestaurantAdapter(Context context) {
        this.context = context;
        this.restaurantList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Pobierz odpowiednie godziny otwarcia na podstawie numeru dnia tygodnia
        String openingHoursForCurrentDay = getOpeningHoursForDayOfWeek(restaurant.getOpeningHours(), dayOfWeek);

        // Otrzymujemy listę wszystkich stolików z danej restauracji
        List<Table> tables = restaurant.getTables();

        // Pobieramy maksymalną pojemność ze stolików
        int maxCapacity = getMaxCapacity(tables);

        // Ustawiamy dane dla poszczególnych elementów layoutu
        holder.imageRestaurant.setImageResource(R.mipmap.ic_salad_foreground);
        holder.textRestaurantName.setText(restaurant.getName());
        holder.textOpeningHours.setText("Opening Hours: " + openingHoursForCurrentDay);
        holder.textCapacity.setText("Capacity: " + maxCapacity);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantDetailsActivity.class);
            intent.putExtra("restaurant", restaurant);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    private String getOpeningHoursForDayOfWeek(OpeningHours openingHours, int dayOfWeek) {
        if (openingHours == null) {
            return "N/A"; // Jeśli brak danych o godzinach otwarcia, zwróć "N/A" (Not Available)
        }

            switch (dayOfWeek) {
            case Calendar.MONDAY:
                return openingHours.getMonday();
            case Calendar.TUESDAY:
                return openingHours.getTuesday();
            case Calendar.WEDNESDAY:
                return openingHours.getWednesday();
            case Calendar.THURSDAY:
                return openingHours.getThursday();
            case Calendar.FRIDAY:
                return openingHours.getFriday();
            case Calendar.SATURDAY:
                return openingHours.getSaturday();
            case Calendar.SUNDAY:
                return openingHours.getSunday();
            default:
                return "Closed"; // Domyślna wartość w przypadku braku danych dla danego dnia tygodnia
        }
    }

    private int getMaxCapacity(List<Table> tables) {
        int maxCapacity = 0;

        if (tables != null) {
            for (Table table : tables) {
                int capacity = table.getCapacity();
                if (capacity > maxCapacity) {
                    maxCapacity = capacity;
                }
            }
        }

        return maxCapacity;
    }


    static class RestaurantViewHolder extends RecyclerView.ViewHolder {

        ImageView imageRestaurant;
        TextView textRestaurantName;
        TextView textOpeningHours;
        TextView textCapacity;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            imageRestaurant = itemView.findViewById(R.id.image_restaurant);
            textRestaurantName = itemView.findViewById(R.id.text_restaurant_name);
            textOpeningHours = itemView.findViewById(R.id.text_opening_hours);
            textCapacity = itemView.findViewById(R.id.text_capacity);
        }
    }
}

