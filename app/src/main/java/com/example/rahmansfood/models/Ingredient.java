package com.example.rahmansfood.models;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("id")
    private String id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("quantite")
    private int quantite;

    // Getters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public int getQuantite() { return quantite; }
}
