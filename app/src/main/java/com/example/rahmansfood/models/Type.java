package com.example.rahmansfood.models;

import com.google.gson.annotations.SerializedName;

public class Type {
    private String id;

    @SerializedName("type")
    private String nom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
