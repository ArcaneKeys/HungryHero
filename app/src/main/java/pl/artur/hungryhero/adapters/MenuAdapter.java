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
    private List<Menu> menus;

    public MenuAdapter(Context context, List<Menu> menus) {
        this.context = context;
        this.menus = menus;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);

        holder.textMenuName.setText(menu.getMenuName());
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView textMenuName;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            textMenuName = itemView.findViewById(R.id.textMenuName);
        }
    }
}

