package com.example.rahmansfood.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Produit {
    @SerializedName("id")
    private String id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prix")
    private String prix;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @SerializedName("categorie")
    private String categorie;

    // Getters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getPrix() { return prix; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public String getCategorie() { return categorie; }

    // Méthode utilitaire pour obtenir le prix comme double
    public double getPrixNumerique() {
        try {
            return Double.parseDouble(prix);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
