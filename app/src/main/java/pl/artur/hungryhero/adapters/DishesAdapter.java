package pl.artur.hungryhero.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import pl.artur.hungryhero.AddDishActivity;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.MenuItem;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> {
    private List<MenuItem> menuItems;
    private String menuId;

    public DishesAdapter(List<MenuItem> menuItems, String menuId) {
        this.menuItems = menuItems;
        this.menuId = menuId;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    class DishViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, dishPrice, dishDescription, dishIngredients;
        ImageView dishImage, expandIcon;
        CardView cardView;
        boolean isExpanded;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.textDishName);
            dishPrice = itemView.findViewById(R.id.textDishPrice);
            dishDescription = itemView.findViewById(R.id.textDishDescription);
            dishIngredients = itemView.findViewById(R.id.textDishIngredients);
            dishImage = itemView.findViewById(R.id.dishImage);
            expandIcon = itemView.findViewById(R.id.buttonExpand);
            cardView = itemView.findViewById(R.id.cardViewDish);
            ImageButton buttonEdit = itemView.findViewById(R.id.buttonEdit);

            buttonEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MenuItem menuItem = menuItems.get(position);
                    Intent intent = new Intent(itemView.getContext(), AddDishActivity.class);
                    intent.putExtra("menuItemId", menuItem.getItemId());
                    intent.putExtra("menuId", menuId);
                    intent.putExtra("menuItem", menuItem);
                    itemView.getContext().startActivity(intent);
                }
            });

            cardView.setOnClickListener(v -> toggleExpansion());
        }

        public void bind(MenuItem menuItem) {
            dishName.setText(menuItem.getDishName());
            dishPrice.setText(String.valueOf(menuItem.getPrice()));
            dishDescription.setText(menuItem.getDescription());
            dishIngredients.setText("Ingredients: " + String.join(", ", menuItem.getIngredients()));
            Glide.with(dishImage.getContext())
                    .load(menuItem.getPhotoUrl())
                    .into(dishImage);
            dishIngredients.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            expandIcon.setImageResource(isExpanded ? R.drawable.baseline_expand_less_24 : R.drawable.baseline_expand_more_24);
        }

        private void toggleExpansion() {
            isExpanded = !isExpanded;
            notifyItemChanged(getAdapterPosition());
        }
    }
}

