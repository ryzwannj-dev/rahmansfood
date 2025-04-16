package com.example.rahmansfood.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.SearchView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.adapters.HomeFragmentAdapter;
import com.example.rahmansfood.api.ProduitApiService;
import com.example.rahmansfood.models.ApiClient;
import com.example.rahmansfood.models.ApiResponse;
import com.example.rahmansfood.models.Produit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private ProgressBar loadingProgressBar;
    private RecyclerView recyclerViewProduits;
    private SearchView searchView;
    private HomeFragmentAdapter adapter;

    private List<Produit> allProduits = new ArrayList<>();

    private CardView pizza_card, burger_card, tex_mex_card, dessert_card, boisson_card, tacos_card, sandwich_card, all_card, assiette_card;

    private final AtomicBoolean pizza_card_selected = new AtomicBoolean(false);
    private final AtomicBoolean burger_card_selected = new AtomicBoolean(false);
    private final AtomicBoolean tex_mex_card_selected = new AtomicBoolean(false);
    private final AtomicBoolean dessert_card_selected = new AtomicBoolean(false);
    private final AtomicBoolean boisson_card_selected = new AtomicBoolean(false);
    private final AtomicBoolean tacos_card_selected = new AtomicBoolean(false);
    private final AtomicBoolean sandwich_card_selected = new AtomicBoolean(false);
    private final AtomicBoolean all_card_selected = new AtomicBoolean(false);
    private final AtomicBoolean assiette_card_selected = new AtomicBoolean(false);

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        recyclerViewProduits = view.findViewById(R.id.recyclerViewProduits);
        searchView = view.findViewById(R.id.searchViewHome);

        recyclerViewProduits.setLayoutManager(new LinearLayoutManager(getContext()));

        pizza_card = view.findViewById(R.id.pizza_card);
        burger_card = view.findViewById(R.id.burger_card);
        tex_mex_card = view.findViewById(R.id.tex_mex_card);
        dessert_card = view.findViewById(R.id.dessert_card);
        boisson_card = view.findViewById(R.id.boisson_card);
        tacos_card = view.findViewById(R.id.tacos_card);
        sandwich_card = view.findViewById(R.id.sandwiches_card);
        all_card = view.findViewById(R.id.all_card);
        assiette_card = view.findViewById(R.id.assiette_card);

        pizza_card.setOnClickListener(v -> selectCategoryAndReload(pizza_card, pizza_card_selected, "pizza"));
        burger_card.setOnClickListener(v -> selectCategoryAndReload(burger_card, burger_card_selected, "burger"));
        tex_mex_card.setOnClickListener(v -> selectCategoryAndReload(tex_mex_card, tex_mex_card_selected, "texmex"));
        dessert_card.setOnClickListener(v -> selectCategoryAndReload(dessert_card, dessert_card_selected, "dessert"));
        boisson_card.setOnClickListener(v -> selectCategoryAndReload(boisson_card, boisson_card_selected, "boisson"));
        tacos_card.setOnClickListener(v -> selectCategoryAndReload(tacos_card, tacos_card_selected, "tacos"));
        sandwich_card.setOnClickListener(v -> selectCategoryAndReload(sandwich_card, sandwich_card_selected, "sandwich"));
        all_card.setOnClickListener(v -> selectCategoryAndReload(all_card, all_card_selected, "all"));
        assiette_card.setOnClickListener(v -> selectCategoryAndReload(assiette_card, assiette_card_selected, "assiette"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) adapter.getFilter().filter(newText);
                return true;
            }
        });

        // Load "all" products by default
        selectCategoryAndReload(all_card, all_card_selected, "all");
    }

    private void selectCategoryAndReload(CardView selectedCard, AtomicBoolean selectedFlag, String category) {
        pizza_card_selected.set(false);
        burger_card_selected.set(false);
        tex_mex_card_selected.set(false);
        dessert_card_selected.set(false);
        boisson_card_selected.set(false);
        tacos_card_selected.set(false);
        sandwich_card_selected.set(false);
        all_card_selected.set(false);
        assiette_card_selected.set(false);

        pizza_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));
        burger_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));
        tex_mex_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));
        dessert_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));
        boisson_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));
        tacos_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));
        sandwich_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));
        all_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));
        assiette_card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background));

        selectedCard.setCardBackgroundColor(Color.RED);
        selectedFlag.set(true);

        reloadData(category);
    }

    private void reloadData(String category) {
        if (!isAdded()) return;

        loadingProgressBar.setVisibility(View.VISIBLE);

        try {
            Retrofit retrofit = ApiClient.getClient(getContext());
            ProduitApiService service = retrofit.create(ProduitApiService.class);
            Call<Object> call = (Call<Object>) getCallBasedOnCategory(service, category);

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (!isAdded()) return;
                    loadingProgressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        handleResponse(response);
                    } else {
                        String errorMsg = "Erreur serveur";
                        if (response.errorBody() != null) {
                            try {
                                errorMsg += ": " + response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    if (!isAdded()) return;
                    loadingProgressBar.setVisibility(View.GONE);
                    showErrorDialog(t.getMessage(), category);
                }
            });
        } catch (Exception e) {
            loadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Erreur de configuration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private Call<?> getCallBasedOnCategory(ProduitApiService service, String category) {
        switch (category) {
            case "tacos": return service.getAllTacos();
            case "pizza": return service.getAllPizzas();
            case "burger": return service.getAllBurgers();
            case "texmex": return service.getAllTexMex();
            case "dessert": return service.getAllDessert();
            case "boisson": return service.getAllBoissons();
            case "assiette": return service.getAllAssiettes();
            case "sandwich": return service.getAllSandwiches();
            default: return service.getAllProduits();
        }
    }

    private void handleResponse(Response<Object> response) {
        try {
            if (response.body() instanceof ApiResponse) {
                ApiResponse apiResponse = (ApiResponse) response.body();
                if (apiResponse != null && apiResponse.getData() != null) {
                    displayProducts(apiResponse.getData());
                }
            } else if (response.body() instanceof List) {
                @SuppressWarnings("unchecked")
                List<Produit> produits = (List<Produit>) response.body();
                displayProducts(produits);
            } else {
                Toast.makeText(getContext(), "Format de réponse inattendu", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erreur de traitement des données", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void displayProducts(List<Produit> produits) {
        if (!isAdded()) return;

        requireActivity().runOnUiThread(() -> {
            if (produits != null && !produits.isEmpty()) {
                allProduits = produits;
                adapter = new HomeFragmentAdapter(allProduits);
                recyclerViewProduits.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "Aucun produit trouvé", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorDialog(String message, String category) {
        requireActivity().runOnUiThread(() -> {
            if (!isAdded()) return;

            new AlertDialog.Builder(requireContext())
                    .setTitle("Erreur de connexion")
                    .setMessage("Impossible de se connecter au serveur.\n\n" + message)
                    .setCancelable(false)
                    .setPositiveButton("Recharger", (dialog, which) -> reloadData(category))
                    .setNeutralButton("Configurer", (dialog, which) -> navigateToSettings())
                    .setNegativeButton("Quitter", (dialog, which) -> requireActivity().finish())
                    .show();
        });
    }

    private void navigateToSettings() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }
}
