package pl.artur.hungryhero.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Menu;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Reviews;
import pl.artur.hungryhero.ui.MenusActivity;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<Menu> menuItems;

    public MenuAdapter(Context context, List<Menu> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menuItems.get(position);

        // Uzupełnij dane pojedynczego elementu dania w sekcji
        holder.textDishName.setText(menu.getDishName());
        holder.textDescription.setText(menu.getDescription());
        // ... Dodaj wyświetlanie innych pól, np. cena, składniki, itp.
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView textDishName;
        TextView textDescription;
        // ... Dodaj referencje do innych pól widoku, jeśli to konieczne

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            textDishName = itemView.findViewById(R.id.textDishName);
            textDescription = itemView.findViewById(R.id.textDescription);
            // ... Zainicjuj pozostałe pola widoku, jeśli to konieczne
        }
    }
}

