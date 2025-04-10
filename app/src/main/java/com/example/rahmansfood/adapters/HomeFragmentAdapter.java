package com.example.rahmansfood.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.models.Produit;

import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {

    private final List<Produit> produits;

    public HomeFragmentAdapter(List<Produit> produits) {
        this.produits = produits;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPrice;
        TextView tvIngredientsTitle, tvIngredientsList;
        View expandableLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            tvIngredientsTitle = expandableLayout.findViewById(R.id.expandableLayout).findViewById(R.id.tvIngredientsTitle);
            tvIngredientsList = expandableLayout.findViewById(R.id.expandableLayout).findViewById(R.id.tvIngredientsList);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_food_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produit produit = produits.get(position);

        holder.tvName.setText(produit.getNom());
        holder.tvType.setText(produit.getCategorie());
        holder.tvPrice.setText(produit.getPrix() + "€");

        // Préparation de la liste des ingrédients
        StringBuilder sb = new StringBuilder();
        if (produit.getIngredients() != null) {
            for (int i = 0; i < produit.getIngredients().size(); i++) {
                sb.append("- ").append(produit.getIngredients().get(i).getNom());
                if (i < produit.getIngredients().size() - 1) sb.append("\n");
            }
        } else {
            sb.append("Aucun ingrédient renseigné.");
        }

        TextView tvIngredientsList = holder.itemView.findViewById(R.id.expandableLayout).findViewById(R.id.tvIngredientsList);
        tvIngredientsList.setText(sb.toString());

        // Toggle sur clic
        holder.itemView.setOnClickListener(v -> {
            if (holder.expandableLayout.getVisibility() == View.GONE) {
                holder.expandableLayout.setVisibility(View.VISIBLE);
            } else {
                holder.expandableLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return produits.size();
    }
}
