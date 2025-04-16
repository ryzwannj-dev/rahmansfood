package com.example.rahmansfood.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.api.ProduitApiService;
import com.example.rahmansfood.models.ApiClient;
import com.example.rahmansfood.models.GratineEach;
import com.example.rahmansfood.models.IngredientEach;
import com.example.rahmansfood.models.SupplementEach;
import com.example.rahmansfood.adapters.CrudFragmentAdapter;

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
    private TextView nb_element;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestion_crud, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewCrud);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nb_element = view.findViewById(R.id.tvElementCount);
        searchView = view.findViewById(R.id.searchViewCrud);

        if (getArguments() != null) {
            title = getArguments().getString("title");
            type = getArguments().getString("type");
        }

        // Ajout de l'écouteur de texte pour le SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Rien à faire ici si l'utilisateur soumet la recherche
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrer la liste chaque fois que le texte change
                filterList(newText);
                return true;
            }
        });

        if ("ingredient".equals(type)) {
            fetchIngredients();
        } else if ("supplement".equals(type)) {
            fetchSupplements();
        } else if ("gratine".equals(type)) {
            fetchGratines();
        }
    }

    private void fetchIngredients() {
        Retrofit retrofit = ApiClient.getClient(getContext());
        ProduitApiService service = retrofit.create(ProduitApiService.class);
        Call<List<IngredientEach>> call = service.getAllIngredients();
        call.enqueue(new Callback<List<IngredientEach>>() {
            @Override
            public void onResponse(Call<List<IngredientEach>> call, Response<List<IngredientEach>> response) {
                if (response.isSuccessful()) {
                    List<IngredientEach> ingredients = response.body();
                    nb_element.setText("Nombres d'éléments: " + String.valueOf(ingredients.size()));
                    Log.d("API", "Fetched " + ingredients.size() + " ingredients");
                    if (ingredients != null) {
                        itemList.clear();
                        itemList.addAll(ingredients);
                        filteredList.clear();
                        filteredList.addAll(itemList);  // Initialize filtered list
                        adapter = new CrudFragmentAdapter(filteredList, "ingredient");
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    nb_element.setText("Nombres d'éléments: N/A");
                }
            }

            @Override
            public void onFailure(Call<List<IngredientEach>> call, Throwable throwable) {
                Log.e("Error", "Failed to fetch ingredients", throwable);
                nb_element.setText("Nombres d'éléments: N/A");
            }
        });
    }

    private void fetchSupplements() {
        Retrofit retrofit = ApiClient.getClient(getContext());
        ProduitApiService service = retrofit.create(ProduitApiService.class);
        Call<List<SupplementEach>> call = service.getAllSupplements();

        call.enqueue(new Callback<List<SupplementEach>>() {
            @Override
            public void onResponse(Call<List<SupplementEach>> call, Response<List<SupplementEach>> response) {
                if (response.isSuccessful()) {
                    List<SupplementEach> supplements = response.body();
                    nb_element.setText("Nombres d'éléments: " + String.valueOf(supplements.size()));
                    if (supplements != null) {
                        itemList.clear();
                        itemList.addAll(supplements);
                        filteredList.clear();
                        filteredList.addAll(itemList);  // Initialize filtered list
                        adapter = new CrudFragmentAdapter(filteredList, "supplement");
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    nb_element.setText("Nombres d'éléments: N/A");
                }
            }

            @Override
            public void onFailure(Call<List<SupplementEach>> call, Throwable throwable) {
                Log.e("Error", "Failed to fetch supplements", throwable);
                nb_element.setText("Nombres d'éléments: N/A");
            }
        });
    }

    private void fetchGratines() {
        Retrofit retrofit = ApiClient.getClient(getContext());
        ProduitApiService service = retrofit.create(ProduitApiService.class);
        Call<List<GratineEach>> call = service.getAllGratine();

        call.enqueue(new Callback<List<GratineEach>>() {
            @Override
            public void onResponse(Call<List<GratineEach>> call, Response<List<GratineEach>> response) {
                if (response.isSuccessful()) {
                    List<GratineEach> gratines = response.body();
                    nb_element.setText("Nombres d'éléments: " + String.valueOf(gratines.size()));
                    if (gratines != null) {
                        itemList.clear();
                        itemList.addAll(gratines);
                        filteredList.clear();
                        filteredList.addAll(itemList);  // Initialize filtered list
                        adapter = new CrudFragmentAdapter(filteredList, "gratine");
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    nb_element.setText("Nombres d'éléments: N/A");
                }
            }

            @Override
            public void onFailure(Call<List<GratineEach>> call, Throwable throwable) {
                Log.e("Error", "Failed to fetch gratines", throwable);
                nb_element.setText("Nombres d'éléments: N/A");
            }
        });
    }

    // Méthode pour filtrer les éléments
    private void filterList(String query) {
        List<Object> filtered = new ArrayList<>();
        if (query.isEmpty()) {
            filtered.addAll(itemList);
        } else {
            for (Object item : itemList) {
                if (item instanceof IngredientEach) {
                    IngredientEach ingredient = (IngredientEach) item;
                    if (ingredient.getNom().toLowerCase().contains(query.toLowerCase())) {
                        filtered.add(ingredient);
                    }
                } else if (item instanceof SupplementEach) {
                    SupplementEach supplement = (SupplementEach) item;
                    if (supplement.getNom().toLowerCase().contains(query.toLowerCase())) {
                        filtered.add(supplement);
                    }
                } else if (item instanceof GratineEach) {
                    GratineEach gratine = (GratineEach) item;
                    if (gratine.getNom().toLowerCase().contains(query.toLowerCase())) {
                        filtered.add(gratine);
                    }
                }
            }
        }
        // Met à jour la liste filtrée et notifie l'adaptateur
        filteredList.clear();
        filteredList.addAll(filtered);
        adapter.notifyDataSetChanged();
        nb_element.setText("Nombres d'éléments: " + filtered.size());
    }
}
