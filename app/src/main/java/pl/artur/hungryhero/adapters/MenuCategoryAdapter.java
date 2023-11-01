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

public class MenuCategoryAdapter extends RecyclerView.Adapter<MenuCategoryAdapter.MenuCategoryViewHolder> {

    private Context context;
    private List<Menu> menuCategories;

    public MenuCategoryAdapter(Context context, List<Menu> menuCategories) {
        this.context = context;
        this.menuCategories = menuCategories;
    }

    public interface OnMenuCategoryClickListener {
        void onMenuCategoryClick(String menuId, String menuName);
    }

    private OnMenuCategoryClickListener listener;

    public void setOnMenuCategoryClickListener(OnMenuCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuCategoryAdapter.MenuCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_category, parent, false);
        return new MenuCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuCategoryAdapter.MenuCategoryViewHolder holder, int position) {
        Menu menuCategory = menuCategories.get(position);
        holder.textMenuCategoryName.setText(menuCategory.getMenuName());
    }

    @Override
    public int getItemCount() {
        return menuCategories.size();
    }

    public class MenuCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textMenuCategoryName;

        public MenuCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textMenuCategoryName = itemView.findViewById(R.id.textMenuCategoryName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onMenuCategoryClick(menuCategories.get(position).getMenuId(), menuCategories.get(position).getMenuName());
            }
        }
    }
}
