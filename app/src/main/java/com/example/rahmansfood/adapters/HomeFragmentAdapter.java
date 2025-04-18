package com.example.rahmansfood.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.models.Produit;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> implements Filterable {

    private final List<Produit> produits;       // liste filtrée
    private final List<Produit> allProduits;    // liste complète
    private int expandedPosition = -1;
    private static final long DEBOUNCE_INTERVAL = 500;

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HomeFragmentAdapter(List<Produit> produits) {
        this.produits = new ArrayList<>(produits);
        this.allProduits = new ArrayList<>(produits);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPrice, tvIngredientsTitle, tvIngredientsList;
        View expandableLayout, expandableLayoutButton;
        Button btnAdd, btnEdit, btnDelete;
        long lastClickTime = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            expandableLayoutButton = itemView.findViewById(R.id.expandableLayoutButton);
            tvIngredientsTitle = expandableLayout.findViewById(R.id.tvIngredientsTitle);
            tvIngredientsList = expandableLayout.findViewById(R.id.tvIngredientsList);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_food_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produit produit = produits.get(position);
        boolean isExpanded = position == expandedPosition;

        holder.tvName.setText(produit.getNom());
        setupMarqueeEffect(holder.tvName);
        holder.tvType.setText(produit.getCategorie());
        holder.tvPrice.setText(String.format("%s€", produit.getPrix()));

        setupIngredients(holder, produit);
        manageExpansionState(holder, position, isExpanded);
        setupItemClick(holder, position, isExpanded);
        setupButtons(holder, position);
    }

    private void setupMarqueeEffect(TextView textView) {
        textView.setSelected(true);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(-1);
        textView.setHorizontallyScrolling(true);
    }

    private void setupIngredients(ViewHolder holder, Produit produit) {
        String categorie = produit.getCategorie();
        boolean showIngredients = !("Boissons".equalsIgnoreCase(categorie) || "Dessert".equalsIgnoreCase(categorie));

        holder.tvIngredientsTitle.setVisibility(showIngredients ? View.VISIBLE : View.GONE);
        holder.tvIngredientsList.setVisibility(showIngredients ? View.VISIBLE : View.GONE);
        holder.btnEdit.setVisibility(showIngredients ? View.VISIBLE : View.GONE);

        if (showIngredients) {
            if ("Tacos".equalsIgnoreCase(categorie)) {
                holder.btnEdit.setText("Composer");
                holder.btnEdit.setBackgroundResource(R.drawable.button_compose_item);
            } else {
                holder.btnEdit.setText("Modifier");
            }

            StringBuilder sb = new StringBuilder();
            if (produit.getIngredients() != null && !produit.getIngredients().isEmpty()) {
                for (int i = 0; i < produit.getIngredients().size(); i++) {
                    sb.append("- ").append(produit.getIngredients().get(i).getNom());
                    if (i < produit.getIngredients().size() - 1) sb.append("\n");
                }
            } else {
                if ("Tacos".equalsIgnoreCase(categorie)) {
                    sb.append("Composition selon les attentes du client.");
                } else {
                    sb.append("Aucun ingrédient renseigné.");
                    holder.btnEdit.setVisibility(View.GONE);
                }
            }
            holder.tvIngredientsList.setText(sb.toString());
        }
    }

    private void manageExpansionState(ViewHolder holder, int position, boolean isExpanded) {
        holder.expandableLayout.measure(
                View.MeasureSpec.makeMeasureSpec(holder.itemView.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        if (isExpanded) {
            holder.expandableLayout.setVisibility(View.VISIBLE);
            holder.expandableLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.expandableLayoutButton.setVisibility(View.VISIBLE);
            holder.expandableLayoutButton.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            holder.expandableLayout.setVisibility(View.GONE);
            holder.expandableLayout.getLayoutParams().height = 0;
            holder.expandableLayoutButton.setVisibility(View.GONE);
            holder.expandableLayoutButton.getLayoutParams().height = 0;
        }
    }

    private void setupItemClick(ViewHolder holder, int position, boolean isExpanded) {
        holder.itemView.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - holder.lastClickTime < DEBOUNCE_INTERVAL) return;
            holder.lastClickTime = currentTime;

            int previousExpandedPosition = expandedPosition;

            if (isExpanded) {
                expandedPosition = -1;
                notifyItemChanged(position);
            } else {
                expandedPosition = position;
                notifyItemChanged(position);
                if (previousExpandedPosition != -1) {
                    notifyItemChanged(previousExpandedPosition);
                }
            }
        });
    }

    private void setupButtons(ViewHolder holder, int position) {
        holder.btnAdd.setOnClickListener(v -> {
            if (listener != null) listener.onAddClick(position);
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(position);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return produits != null ? produits.size() : 0;
    }

    public void updateData(List<Produit> newProduits) {
        this.produits.clear();
        this.produits.addAll(newProduits);
        this.allProduits.clear();
        this.allProduits.addAll(newProduits);
        this.expandedPosition = -1;
        notifyDataSetChanged();
    }

    public void filter(String query) {
        expandedPosition = -1;
        produits.clear();

        if (TextUtils.isEmpty(query)) {
            produits.addAll(allProduits);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (Produit produit : allProduits) {
                if (produit.getNom().toLowerCase().contains(lowerQuery)
                        || produit.getCategorie().toLowerCase().contains(lowerQuery)) {
                    produits.add(produit);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Produit> filteredList = new ArrayList<>();
                if (TextUtils.isEmpty(constraint)) {
                    filteredList.addAll(allProduits);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Produit produit : allProduits) {
                        if (produit.getNom().toLowerCase().contains(filterPattern)
                                || produit.getCategorie().toLowerCase().contains(filterPattern)) {
                            filteredList.add(produit);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                produits.clear();
                produits.addAll((List<Produit>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
