package com.example.rahmansfood.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.rahmansfood.R;
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        afficherMessageToast();
    }

    private void afficherMessageToast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.155/rahmanfood_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProduitApiService service = retrofit.create(ProduitApiService.class);

        Call<List<Produit>> call = service.getAllProduits();
        call.enqueue(new Callback<List<Produit>>() {
            @Override
            public void onResponse(Call<List<Produit>> call, Response<List<Produit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Produit> produits = response.body();
                    if (!produits.isEmpty()) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String jsonProduits = gson.toJson(produits);
                        Log.i("json_response", jsonProduits);
                    } else {
                        Toast.makeText(getContext(), "Aucun produit trouvé", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Produit>> call, Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Erreur : ", t);
            }
        });

    }
}
