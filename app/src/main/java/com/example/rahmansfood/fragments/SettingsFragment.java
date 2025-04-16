package com.example.rahmansfood.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rahmansfood.R;

public class SettingsFragment extends Fragment {

    private EditText editTextServerIp;
    private Button buttonSaveIp;
    private TextView textViewStatus;

    public static final String PREFS_NAME = "app_settings";
    public static final String KEY_SERVER_IP = "server_ip";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lier les vues
        editTextServerIp = view.findViewById(R.id.editTextServerIp);
        buttonSaveIp = view.findViewById(R.id.buttonSaveIp);
        textViewStatus = view.findViewById(R.id.textViewStatus);

        // Charger l'IP déjà enregistrée
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedIp = prefs.getString(KEY_SERVER_IP, "");
        editTextServerIp.setText(savedIp);

        // Action bouton
        buttonSaveIp.setOnClickListener(v -> {
            String ip = editTextServerIp.getText().toString().trim();

            if (!ip.isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_SERVER_IP, ip);
                editor.apply();

                textViewStatus.setText("Adresse IP sauvegardée !");
                textViewStatus.setVisibility(View.VISIBLE);
            } else {
                textViewStatus.setText("Veuillez entrer une adresse IP.");
                textViewStatus.setVisibility(View.VISIBLE);
            }
        });
    }

    // Utilisable dans d'autres classes
    public static String getSavedServerIp(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_SERVER_IP, "http://192.168.1.156"); // par défaut
    }
}
