package pl.artur.hungryhero.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import pl.artur.hungryhero.R;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private final List<String> ingredients;

    public IngredientsAdapter(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.ingredientEditText.setText(ingredients.get(position));
        holder.ingredientEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int updatedPosition = holder.getAdapterPosition();
                if(updatedPosition != RecyclerView.NO_POSITION) {
                    ingredients.set(updatedPosition, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        holder.buttonRemove.setOnClickListener(v -> {
            int updatedPosition = holder.getAdapterPosition();
            if(updatedPosition != RecyclerView.NO_POSITION) {
                ingredients.remove(updatedPosition);
                notifyItemRemoved(updatedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        final TextInputEditText ingredientEditText;
        final Button buttonRemove;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientEditText = itemView.findViewById(R.id.ingredientEditText);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}

