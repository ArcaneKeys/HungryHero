package pl.artur.hungryhero.adapters;

import android.app.AlertDialog;
import android.content.Intent;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import pl.artur.hungryhero.ui.AddDishActivity;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.MenuItem;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> {
    private List<MenuItem> menuItems;
    private String menuId;

    FirebaseHelper firebaseHelper;

    public DishesAdapter(List<MenuItem> menuItems, String menuId, FirebaseHelper firebaseHelper) {
        this.menuItems = menuItems;
        this.menuId = menuId;
        this.firebaseHelper = firebaseHelper;
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
        ImageButton buttonEdit;
        ImageView dishImage;
        Button buttonDelete;
        CardView cardView;
        boolean isExpanded;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.textDishName);
            dishPrice = itemView.findViewById(R.id.textDishPrice);
            dishDescription = itemView.findViewById(R.id.textDishDescription);
            dishIngredients = itemView.findViewById(R.id.textDishIngredients);
            dishImage = itemView.findViewById(R.id.dishImage);
            cardView = itemView.findViewById(R.id.cardViewDish);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            firebaseHelper.isRestaurant(isRestaurant -> {
                if (!isRestaurant) {
                    buttonEdit.setVisibility(View.GONE);
                } else {
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
                }
            });

            cardView.setOnClickListener(v -> toggleExpansion());
            buttonDelete.setOnClickListener(v -> dishDelete());
        }

        public void bind(MenuItem menuItem) {
            dishName.setText(menuItem.getDishName());
            dishPrice.setText(String.valueOf(menuItem.getPrice()) + " zł");
            dishDescription.setText(menuItem.getDescription());
            dishIngredients.setText("Składniki: " + String.join(", ", menuItem.getIngredients()));
            Glide.with(dishImage.getContext())
                    .load(menuItem.getPhotoUrl())
                    .into(dishImage);
        }

        private void toggleExpansion() {
            isExpanded = !isExpanded;
            dishIngredients.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            firebaseHelper.isRestaurant(isRestaurant -> {
                if (!isRestaurant) {
                    buttonDelete.setVisibility(View.GONE);
                } else {
                    buttonDelete.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            TransitionManager.beginDelayedTransition(cardView);
        }

        private void dishDelete(){
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle("Potwierdzenie");
                builder.setMessage("Czy na pewno chcesz usunąć?");

                builder.setPositiveButton("Tak", (dialog, which) -> {
                    MenuItem menuItem = menuItems.get(position);
                    firebaseHelper.deleteMenuItem(menuId, menuItem.getItemId());
                    menuItems.remove(position);
                    notifyItemRemoved(position);
                });
                builder.setNegativeButton("Nie", (dialog, which) -> {
                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}

