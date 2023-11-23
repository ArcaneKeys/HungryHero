package pl.artur.hungryhero.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.ui.RestaurantDetailsActivity;
import pl.artur.hungryhero.ui.UserMainActivity;
import pl.artur.hungryhero.utils.Utils;

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

        String openingHoursForCurrentDay = Utils.getOpeningHoursForDayOfWeek(restaurant.getOpeningHours(), dayOfWeek);

        List<Table> tables = restaurant.getTables();

        int maxCapacity = Utils.getMaxCapacity(tables);

        if (restaurant.getPhotoUrl() != null && !restaurant.getPhotoUrl().isEmpty()) {
            Glide.with(context)
                    .load(restaurant.getPhotoUrl())
                    .into(holder.imageRestaurant);
        } else {
            holder.imageRestaurant.setImageResource(R.mipmap.ic_salad_foreground);
        }

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

