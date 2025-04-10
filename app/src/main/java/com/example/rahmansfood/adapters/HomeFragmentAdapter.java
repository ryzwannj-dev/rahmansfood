package com.example.rahmansfood.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.models.Produit;

import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {

    private final List<Produit> produits;

    boolean isAnimating = false;

    private int expandedPosition = -1;


    public HomeFragmentAdapter(List<Produit> produits) {
        this.produits = produits;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPrice;
        TextView tvIngredientsTitle, tvIngredientsList;
        View expandableLayout;

        boolean isAnimating = false;

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

        boolean isExpanded = position == expandedPosition;
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.tvName.setText(produit.getNom());
        holder.tvName.setSelected(true);
        holder.tvType.setText(produit.getCategorie());
        holder.tvPrice.setText(produit.getPrix() + "€");

        // Vérification de la catégorie
        String categorie = produit.getCategorie();
        boolean showIngredients = !("Boissons".equalsIgnoreCase(categorie) || "Dessert".equalsIgnoreCase(categorie));

        // Masquer/afficher la section ingrédients selon la catégorie
        holder.tvIngredientsTitle.setVisibility(showIngredients ? View.VISIBLE : View.GONE);
        holder.tvIngredientsList.setVisibility(showIngredients ? View.VISIBLE : View.GONE);


        // Préparation de la liste des ingrédients seulement si nécessaire
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

        // Gestion du clic
        holder.itemView.setOnClickListener(v -> {
            if (!showIngredients || holder.isAnimating) {
                return;
            }

            int previousExpandedPosition = expandedPosition;
            expandedPosition = isExpanded ? -1 : position;

            holder.isAnimating = true;

            int initialHeight = holder.expandableLayout.getHeight();
            int targetHeight;

            if (previousExpandedPosition != -1) {
                notifyItemChanged(previousExpandedPosition);
            }

            if (expandedPosition != -1) {
                notifyItemChanged(expandedPosition);
            }

            if (holder.expandableLayout.getVisibility() == View.VISIBLE) {
                // Fermeture
                targetHeight = 0;
            } else {
                // Ouverture - mesure la hauteur réelle
                holder.expandableLayout.measure(
                        View.MeasureSpec.makeMeasureSpec(holder.itemView.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                targetHeight = holder.expandableLayout.getMeasuredHeight();
                holder.expandableLayout.getLayoutParams().height = 0;
                holder.expandableLayout.setVisibility(View.VISIBLE);
            }


            ValueAnimator animator = ValueAnimator.ofInt(initialHeight, targetHeight);
            animator.addUpdateListener(valueAnimator -> {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = holder.expandableLayout.getLayoutParams();
                layoutParams.height = val;
                holder.expandableLayout.setLayoutParams(layoutParams);
            });

            animator.setDuration(300);
            animator.setInterpolator(new FastOutSlowInInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    holder.isAnimating = false;
                    if (targetHeight == 0) {
                        holder.expandableLayout.setVisibility(View.GONE);
                    }
                }
            });
            animator.start();
        });
    }

    @Override
    public int getItemCount() {
        return produits.size();
    }
}
