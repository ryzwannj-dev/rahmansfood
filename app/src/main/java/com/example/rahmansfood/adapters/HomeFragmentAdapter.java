package com.example.rahmansfood.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.models.Produit;

import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {

    private final List<Produit> produits;
    private int expandedPosition = -1;
    private static final String TAG = "HomeFragmentAdapter";
    private static final long DEBOUNCE_INTERVAL = 500; // 500ms délai anti-rebond

    public HomeFragmentAdapter(List<Produit> produits) {
        this.produits = produits;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPrice, tvIngredientsTitle, tvIngredientsList;
        View expandableLayout;
        long lastClickTime = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            tvIngredientsTitle = expandableLayout.findViewById(R.id.tvIngredientsTitle);
            tvIngredientsList = expandableLayout.findViewById(R.id.tvIngredientsList);
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

        // Configurer les données de base
        holder.tvName.setText(produit.getNom());
        holder.tvType.setText(produit.getCategorie());
        holder.tvPrice.setText(String.format("%s€", produit.getPrix()));

        // Gestion des ingrédients
        setupIngredients(holder, produit);

        // Gestion de l'état d'expansion
        manageExpansionState(holder, position, isExpanded);

        // Configurer le clic
        setupItemClick(holder, position, isExpanded);
    }

    private void setupIngredients(ViewHolder holder, Produit produit) {
        String categorie = produit.getCategorie();
        boolean showIngredients = !("Boissons".equalsIgnoreCase(categorie) || "Dessert".equalsIgnoreCase(categorie));

        holder.tvIngredientsTitle.setVisibility(showIngredients ? View.VISIBLE : View.GONE);
        holder.tvIngredientsList.setVisibility(showIngredients ? View.VISIBLE : View.GONE);

        if (showIngredients) {
            StringBuilder sb = new StringBuilder();
            if (produit.getIngredients() != null && !produit.getIngredients().isEmpty()) {
                for (int i = 0; i < produit.getIngredients().size(); i++) {
                    sb.append("- ").append(produit.getIngredients().get(i).getNom());
                    if (i < produit.getIngredients().size() - 1) sb.append("\n");
                }
            } else {
                sb.append("Aucun ingrédient renseigné.");
            }
            holder.tvIngredientsList.setText(sb.toString());
        }
    }

    private void manageExpansionState(ViewHolder holder, int position, boolean isExpanded) {
        // Mesurer la hauteur nécessaire avant toute interaction
        holder.expandableLayout.measure(
                View.MeasureSpec.makeMeasureSpec(holder.itemView.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        if (isExpanded) {
            holder.expandableLayout.setVisibility(View.VISIBLE);
            holder.expandableLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            holder.expandableLayout.setVisibility(View.GONE);
            holder.expandableLayout.getLayoutParams().height = 0;
        }
    }

    private void setupItemClick(ViewHolder holder, int position, boolean isExpanded) {
        holder.itemView.setOnClickListener(v -> {
            // Anti-rebond
            long currentTime = System.currentTimeMillis();
            if (currentTime - holder.lastClickTime < DEBOUNCE_INTERVAL) {
                return;
            }
            holder.lastClickTime = currentTime;

            // Gestion de l'état d'expansion
            int previousExpandedPosition = expandedPosition;
            expandedPosition = isExpanded ? -1 : position;

            // Fermer l'élément précédemment étendu
            if (previousExpandedPosition != -1 && previousExpandedPosition != position) {
                notifyItemChanged(previousExpandedPosition);
            }

            // Animer le changement
            if (isExpanded) {
                collapseView(holder.expandableLayout);
            } else {
                expandView(holder.expandableLayout);
            }
        });
    }

    private void expandView(final View view) {
        view.setVisibility(View.VISIBLE);
        view.measure(
                View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        final int targetHeight = view.getMeasuredHeight();

        ValueAnimator animator = ValueAnimator.ofInt(0, targetHeight);
        animator.addUpdateListener(animation -> {
            view.getLayoutParams().height = (int) animation.getAnimatedValue();
            view.requestLayout();
        });
        animator.setDuration(200);
        animator.start();
    }

    private void collapseView(final View view) {
        int initialHeight = view.getHeight();
        ValueAnimator animator = ValueAnimator.ofInt(initialHeight, 0);
        animator.addUpdateListener(animation -> {
            view.getLayoutParams().height = (int) animation.getAnimatedValue();
            view.requestLayout();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    @Override
    public int getItemCount() {
        return produits != null ? produits.size() : 0;
    }

    public void updateData(List<Produit> newProduits) {
        this.produits.clear();
        this.produits.addAll(newProduits);
        this.expandedPosition = -1; // Réinitialiser l'état d'expansion
        notifyDataSetChanged();
    }
}