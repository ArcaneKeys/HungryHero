package pl.artur.hungryhero.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Menu;

public class MenuSectionAdapter extends RecyclerView.Adapter<MenuSectionAdapter.SectionViewHolder> {

    private Context context;
    private List<String> sectionNames;
    private Map<String, List<Menu>> menuSections;

    public MenuSectionAdapter(Context context, Map<String, List<Menu>> menuSections) {
        this.context = context;
        this.menuSections = menuSections;
        this.sectionNames = new ArrayList<>(menuSections.keySet());
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        String sectionName = sectionNames.get(position);
        List<Menu> menuItemsInSection = menuSections.get(sectionName);

        holder.textSectionName.setText(sectionName);

        // Ustaw adapter RecyclerView dla sekcji, aby wyświetlić elementy z tego samego menuName
        MenuAdapter menuAdapter = new MenuAdapter(context, menuItemsInSection);
        holder.recyclerViewMenuItems.setAdapter(menuAdapter);
    }

    @Override
    public int getItemCount() {
        return menuSections.size();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView textSectionName;
        RecyclerView recyclerViewMenuItems;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            textSectionName = itemView.findViewById(R.id.textSectionName);
            recyclerViewMenuItems = itemView.findViewById(R.id.recyclerViewMenuItems);
            recyclerViewMenuItems.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}



