package com.example.rahmansfood.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.api.ProduitApiService;
import com.example.rahmansfood.models.ApiClient;
import com.example.rahmansfood.models.GratineEach;
import com.example.rahmansfood.models.IngredientEach;
import com.example.rahmansfood.models.SupplementEach;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrudFragmentAdapter extends RecyclerView.Adapter<CrudFragmentAdapter.ViewHolder> {

    private final List<Object> itemList;
    private final String type;
    private int expandedPosition = -1;
    private Context context;
    private OnItemDeletedListener itemDeletedListener;

    public CrudFragmentAdapter(Context context, List<Object> itemList, String type) {
        this.context = context;
        this.itemList = itemList;
        this.type = type;
    }

    public void setOnItemDeletedListener(OnItemDeletedListener listener) {
        this.itemDeletedListener = listener;
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

        holder.expandableLayoutButton.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.expandableLayoutButton.clearAnimation();
        holder.expandableLayoutButton.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

        setupMarquee(holder.tvName);

        if (type.equals("ingredient") && item instanceof IngredientEach) {
            IngredientEach ing = (IngredientEach) item;
            holder.tvName.setText(ing.getNom());
            holder.tvInfo1.setText(ing.getMasse() + "g");
            holder.tvInfo2.setText(ing.getType_ingredient());
            holder.tvInfo1.setVisibility(View.VISIBLE);
            holder.tvInfo2.setVisibility(View.VISIBLE);
        } else if (type.equals("supplement") && item instanceof SupplementEach) {
            SupplementEach sup = (SupplementEach) item;
            holder.tvName.setText(sup.getNom());
            holder.tvInfo1.setText(sup.getPrix() + "€");
            holder.tvInfo1.setVisibility(View.VISIBLE);
            holder.tvInfo2.setVisibility(View.GONE);
        } else if (type.equals("gratine") && item instanceof GratineEach) {
            GratineEach grat = (GratineEach) item;
            holder.tvName.setText(grat.getNom());
            holder.tvInfo1.setVisibility(View.GONE);
            holder.tvInfo2.setText(grat.getPrix() + "€");
            holder.tvInfo2.setVisibility(View.VISIBLE);
        }

        if (isExpanded) {
            fadeInButtons(holder.expandableLayoutButton);
        }

        holder.cardView.setOnClickListener(v -> {
            int previous = expandedPosition;
            expandedPosition = isExpanded ? -1 : position;
            notifyItemChanged(position);
            if (previous != -1) notifyItemChanged(previous);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (type.equals("ingredient") && item instanceof IngredientEach) {
                deleteIngredientFromApi(((IngredientEach) item).getId(), position);
            } else if (type.equals("supplement") && item instanceof SupplementEach) {
                deleteSupplementFromApi(((SupplementEach) item).getId(), position);
            } else if (type.equals("gratine") && item instanceof GratineEach) {
                deleteGratineFromApi(((GratineEach) item).getId(), position);
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

    private void setupMarquee(TextView tv) {
        tv.setSelected(true);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setMarqueeRepeatLimit(-1);
        tv.setHorizontallyScrolling(true);
    }

    private void fadeInButtons(View layout) {
        for (int i = 0; i < ((ViewGroup) layout).getChildCount(); i++) {
            View child = ((ViewGroup) layout).getChildAt(i);
            AlphaAnimation anim = new AlphaAnimation(0f, 1f);
            anim.setDuration(300);
            child.startAnimation(anim);
            child.setVisibility(View.VISIBLE);
        }
    }

    private void notifyItemDeleted(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
        if (itemDeletedListener != null) {
            itemDeletedListener.onItemDeleted(itemList.size());
        }
    }

    private void deleteIngredientFromApi(String id, int position) {
        Retrofit retrofit = ApiClient.getClient(context);
        ProduitApiService service = retrofit.create(ProduitApiService.class);

        service.deleteIngredient(id).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notifyItemDeleted(position);
                }
            }

            @Override public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API", "Échec suppression ingrédient", t);
            }
        });
    }

    private void deleteSupplementFromApi(String id, int position) {
        Retrofit retrofit = ApiClient.getClient(context);
        ProduitApiService service = retrofit.create(ProduitApiService.class);

        service.deleteSupplement(id).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notifyItemDeleted(position);
                }
            }

            @Override public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API", "Échec suppression supplément", t);
            }
        });
    }

    private void deleteGratineFromApi(String id, int position) {
        Retrofit retrofit = ApiClient.getClient(context);
        ProduitApiService service = retrofit.create(ProduitApiService.class);

        service.deleteGratine(id).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notifyItemDeleted(position);
                }
            }

            @Override public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API", "Échec suppression gratiné", t);
            }
        });
    }
}
