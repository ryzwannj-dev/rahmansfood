package com.example.rahmansfood.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rahmansfood.R;
import com.example.rahmansfood.adapters.HomeFragmentAdapter;
import com.example.rahmansfood.api.ProduitApiService;
import com.example.rahmansfood.models.ApiResponse;
import com.example.rahmansfood.models.Produit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressBar loadingProgressBar;
    private RecyclerView recyclerViewProduits;

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        recyclerViewProduits = view.findViewById(R.id.recyclerViewProduits);
        recyclerViewProduits.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }

    private void reloadData() {

        loadingProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.155/rahmanfood_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProduitApiService service = retrofit.create(ProduitApiService.class);

        Call<ApiResponse> call = service.getAllProduits();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                loadingProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();

                    // Utilisez getData() pour obtenir la List<Produit>
                    List<Produit> produits = apiResponse.getData();

                    if (produits != null && !produits.isEmpty()) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String jsonProduits = gson.toJson(produits);
                        Log.i("json_response", jsonProduits);

                        recyclerViewProduits.setAdapter(new HomeFragmentAdapter(produits));
                    } else {
                        Toast.makeText(getContext(), "Aucun produit trouvé", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Gestion des erreurs HTTP (404, 500, etc.)
                    Toast.makeText(getContext(), "Erreur de serveur: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
                if (!isAdded()) {
                    Log.w("API_ERROR", "Fragment détaché, on annule le traitement.");
                    return;
                }

                requireActivity().runOnUiThread(() -> {
                    if (!isAdded()) return; // double sécurité

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Erreur")
                            .setMessage("Une erreur est survenue :\n" + t.getMessage())
                            .setCancelable(false)
                            .setPositiveButton("Recharger", (dialog, which) -> {
                                reloadData(); // relance l'appel
                            })
                            .setNegativeButton("Quitter", (dialog, which) -> {
                                requireActivity().finish(); // ferme l’app
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                });
            }
        });
    }
}
