package com.example.rahmansfood.models;

import com.google.gson.annotations.SerializedName;

public class IngredientEach {

    @SerializedName("id")
    private String id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("masse")
    private float masse;

    @SerializedName("id_type")
    private String id_type;

    @SerializedName("type_ingredient")
    private String type_ingredient;

    // Getters

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public float getMasse() {
        return masse;
    }

    public String getId_type() {
        return id_type;
    }

    public String getType_ingredient() {
        return type_ingredient;
    }

    @Override
    public String toString() {
        return "IngredientEach{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", masse='" + masse + '\'' +
                ", id_type='" + id_type + '\'' +
                ", type_ingredient='" + type_ingredient + '\'' +
                '}';
    }
}

