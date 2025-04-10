package com.example.rahmansfood.api;

import com.example.rahmansfood.models.ApiResponse;
import com.example.rahmansfood.models.Produit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProduitApiService {
    @GET("get_all_products")
    Call<ApiResponse> getAllProduits();
}
