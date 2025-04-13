package com.example.rahmansfood.api;

import com.example.rahmansfood.models.ApiResponse;
import com.example.rahmansfood.models.Produit;
import com.example.rahmansfood.models.Type;
import com.example.rahmansfood.models.TypeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

    @GET("get_all_types")
    Call<List<Type>> getTypes();

    @FormUrlEncoded
    @POST("add_ingredient")
    Call<Void> addIngredient(
            @Field("nom") String nom,
            @Field("masse") String masse,
            @Field("id_type_ingredient") String idTypeIngredient
    );

    @FormUrlEncoded
    @POST("add_supplement")
    Call<Void> addIngredient(
            @Field("nom") String nom,
            @Field("prix") float prix
    );

    @FormUrlEncoded
    @POST("add_supplement_gratine")
    Call<Void> addSupplementGratine(
            @Field("nom") String nom,
            @Field("prix") float prix
    );


}
