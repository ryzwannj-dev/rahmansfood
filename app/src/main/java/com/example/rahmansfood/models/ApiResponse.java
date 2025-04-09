package com.example.rahmansfood.models;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Produit> data;

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Produit> getData() { return data; }
}