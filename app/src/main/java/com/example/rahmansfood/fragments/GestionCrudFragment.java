package com.example.rahmansfood.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.adapters.CrudFragmentAdapter;
import com.example.rahmansfood.api.ProduitApiService;
import com.example.rahmansfood.models.ApiClient;
import com.example.rahmansfood.models.GratineEach;
import com.example.rahmansfood.models.IngredientEach;
import com.example.rahmansfood.models.SupplementEach;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GestionCrudFragment extends Fragment {

    private String title;
    private String type;
    private RecyclerView recyclerView;
    private CrudFragmentAdapter adapter;
    private List<Object> itemList = new ArrayList<>();
    private List<Object> filteredList = new ArrayList<>();
    private TextView nbElement;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gestion_crud, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewCrud);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        nbElement = view.findViewById(R.id.tvElementCount);
        searchView = view.findViewById(R.id.searchViewCrud);

        if (getArguments() != null) {
            title = getArguments().getString("title");
            type = getArguments().getString("type");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }
            @Override public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        switch (type) {
            case "ingredient":
                fetchData(ApiClient.getClient(getContext()).create(ProduitApiService.class).getAllIngredients(), "ingredient");
                break;
            case "supplement":
                fetchData(ApiClient.getClient(getContext()).create(ProduitApiService.class).getAllSupplements(), "supplement");
                break;
            case "gratine":
                fetchData(ApiClient.getClient(getContext()).create(ProduitApiService.class).getAllGratine(), "gratine");
                break;
        }
    }

    private <T> void fetchData(Call<List<T>> call, String itemType) {
        call.enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(@NonNull Call<List<T>> call, @NonNull Response<List<T>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemList.clear();
                    itemList.addAll(response.body());
                    filteredList.clear();
                    filteredList.addAll(itemList);
                    adapter = new CrudFragmentAdapter(getContext(), filteredList, itemType);

                    adapter.setOnItemDeletedListener(newSize -> {
                        nbElement.setText("Nombres d'éléments: " + newSize);
                    });

                    recyclerView.setAdapter(adapter);
                    nbElement.setText("Nombres d'éléments: " + filteredList.size());
                } else {
                    nbElement.setText("Nombres d'éléments: N/A");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<T>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Failed to fetch data: " + itemType, t);
                nbElement.setText("Nombres d'éléments: N/A");
            }
        });
    }

    private void filterList(String query) {
        List<Object> filtered = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Object item : itemList) {
            String name = "";

            if (item instanceof IngredientEach) {
                name = ((IngredientEach) item).getNom();
            } else if (item instanceof SupplementEach) {
                name = ((SupplementEach) item).getNom();
            } else if (item instanceof GratineEach) {
                name = ((GratineEach) item).getNom();
            }

            if (name.toLowerCase().contains(lowerQuery)) {
                filtered.add(item);
            }
        }

        filteredList.clear();
        filteredList.addAll(filtered);
        adapter.notifyDataSetChanged();
        nbElement.setText("Nombres d'éléments: " + filtered.size());
    }
}
