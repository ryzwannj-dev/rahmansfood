package com.example.rahmansfood.models;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
        String baseUrl = prefs.getString("server_ip", "http://192.168.1.156");

        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        return new Retrofit.Builder()
                .baseUrl(baseUrl + "rahmanfood_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
