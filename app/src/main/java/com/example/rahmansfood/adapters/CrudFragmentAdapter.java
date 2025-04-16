package com.example.rahmansfood.adapters;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.models.GratineEach;
import com.example.rahmansfood.models.IngredientEach;
import com.example.rahmansfood.models.SupplementEach;

import java.util.List;

public class CrudFragmentAdapter extends RecyclerView.Adapter<CrudFragmentAdapter.ViewHolder> {

    private List<Object> itemList;
    private String type;
    private int expandedPosition = -1;

    public CrudFragmentAdapter(List<Object> itemList, String type) {
        this.itemList = itemList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crud_view_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Object item = itemList.get(position);
        boolean isExpanded = position == expandedPosition;

        setupMarqueeEffect(holder.tvName);

        // Réinitialiser la visibilité par défaut
        holder.expandableLayoutButton.clearAnimation(); // <- important !
        holder.expandableLayoutButton.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.expandableLayoutButton.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if ("ingredient".equals(type) && item instanceof IngredientEach) {
            IngredientEach ingredient = (IngredientEach) item;
            holder.tvName.setText(ingredient.getNom());
            holder.tvInfo1.setText(ingredient.getMasse() + "g");
            holder.tvInfo2.setText(ingredient.getType_ingredient());
            holder.tvInfo1.setVisibility(View.VISIBLE);
            holder.tvInfo2.setVisibility(View.VISIBLE);

        } else if ("supplement".equals(type) && item instanceof SupplementEach) {
            SupplementEach supplement = (SupplementEach) item;
            holder.tvName.setText(supplement.getNom());
            holder.tvInfo1.setText(supplement.getPrix() + "€");
            holder.tvInfo1.setVisibility(View.VISIBLE);
            holder.tvInfo2.setVisibility(View.GONE);

        } else if ("gratine".equals(type) && item instanceof GratineEach) {
            GratineEach gratine = (GratineEach) item;
            holder.tvName.setText(gratine.getNom());
            holder.tvInfo1.setVisibility(View.GONE);
            holder.tvInfo2.setText(gratine.getPrix() + "€");
            holder.tvInfo2.setVisibility(View.VISIBLE);
        }

        // Appliquer ou non l'expansion (sans animation)
        if (isExpanded) {
            holder.expandableLayoutButton.setVisibility(View.VISIBLE);
            fadeInButtons(holder.expandableLayoutButton); // animation douce seulement ici
        } else {
            holder.expandableLayoutButton.setVisibility(View.GONE);
        }

        // Gérer clic pour toggle
        holder.cardView.setOnClickListener(v -> {
            int previousExpanded = expandedPosition;
            if (isExpanded) {
                expandedPosition = -1;
                notifyItemChanged(position);
            } else {
                expandedPosition = position;
                notifyItemChanged(position);
                if (previousExpanded != -1) {
                    notifyItemChanged(previousExpanded);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvInfo1, tvInfo2;
        Button btnEdit, btnDelete;
        View expandableLayoutButton;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvName = itemView.findViewById(R.id.tvName);
            tvInfo1 = itemView.findViewById(R.id.tvInfo1);
            tvInfo2 = itemView.findViewById(R.id.tvInfo2);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            expandableLayoutButton = itemView.findViewById(R.id.expandableLayoutButton);
        }
    }

    private void setupMarqueeEffect(TextView textView) {
        textView.setSelected(true);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(-1);
        textView.setHorizontallyScrolling(true);
    }

    // Animation d’apparition
    private void expand(View v) {
        v.setVisibility(View.VISIBLE);
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.requestLayout();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        fadeInButtons(v);
    }

    // Animation de disparition
    private void collapse(View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    // Animation douce des boutons
    private void fadeInButtons(View v) {
        for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
            View child = ((ViewGroup) v).getChildAt(i);
            AlphaAnimation anim = new AlphaAnimation(0f, 1f);
            anim.setDuration(300);
            child.startAnimation(anim);
            child.setVisibility(View.VISIBLE);
        }
    }
}
