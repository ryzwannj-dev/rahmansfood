package com.example.rahmansfood.models;

import com.google.gson.annotations.SerializedName;

public class GratineEach {

    @SerializedName("id")
    private String id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prix")
    private float prix;

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public float getPrix() {
        return prix;
    }

    @Override
    public String toString() {
        return "GratineEach{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prix='" + prix + '\'' +
                '}';
    }



}
