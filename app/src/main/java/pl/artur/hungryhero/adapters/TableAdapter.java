package pl.artur.hungryhero.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.artur.hungryhero.EditTableActivity;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Table;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    private List<Table> tableList;
    private Context context;

    public TableAdapter(Context context, List<Table> tableList) {
        this.context = context;
        this.tableList = tableList;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = tableList.get(position);
        holder.tableNumber.setText("Stolik nr: " + table.getNumber());
        holder.tableCapacity.setText("Capacity: " + table.getCapacity());
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    public void updateTableList(List<Table> newTableList) {
        this.tableList.clear();
        this.tableList.addAll(newTableList);
        notifyDataSetChanged();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tableNumber, tableCapacity;
        ImageView tableImage;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.table_number);
            tableCapacity = itemView.findViewById(R.id.table_capacity);
            tableImage = itemView.findViewById(R.id.table_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String tableId = tableList.get(position).getTableId();
                Intent intent = new Intent(context, EditTableActivity.class);
                intent.putExtra("tableId", tableId);
                context.startActivity(intent);
            }
        }
    }
}
