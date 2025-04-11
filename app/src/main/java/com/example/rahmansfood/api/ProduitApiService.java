package com.example.rahmansfood.api;

import com.example.rahmansfood.models.ApiResponse;
import com.example.rahmansfood.models.Produit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProduitApiService {
    @GET("get_all_products")
    Call<List<Produit>> getAllProduits();

    @GET("get_all_burgers")
    Call<List<Produit>> getAllBurgers();

    @GET("get_all_pizzas")
    Call<List<Produit>> getAllPizzas();

    @GET("get_all_boissons")
    Call<List<Produit>> getAllBoissons();

    @GET("get_all_texmex")
    Call<List<Produit>> getAllTexMex();

    @GET("get_all_desserts")
    Call<List<Produit>> getAllDessert();

    @GET("get_all_sandwichs")
    Call<List<Produit>> getAllSandwiches();

    @GET("get_all_assiettes")
    Call<List<Produit>> getAllAssiettes();

    @GET("get_all_tacos")
    Call<List<Produit>> getAllTacos();







}
