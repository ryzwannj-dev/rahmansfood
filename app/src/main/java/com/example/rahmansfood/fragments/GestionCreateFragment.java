package com.example.rahmansfood.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rahmansfood.CrudSecondPartActivity;
import com.example.rahmansfood.R;
import com.example.rahmansfood.api.ProduitApiService;
import com.example.rahmansfood.models.Type;
import com.example.rahmansfood.models.TypeResponse;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GestionCreateFragment extends Fragment {

    private String title;
    private String type;

    private TextView title2;

    private EditText editNom, editMasse, editPrix;
    private Spinner spinnerType;
    private Button btnReset, btnAdd;
    private LinearLayout buttonLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestion_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.155/rahmanfood_api/") // Remplace par ton IP/URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProduitApiService service = retrofit.create(ProduitApiService.class);

        title2 = view.findViewById(R.id.titleText);

        // Récupérer les arguments
        if (getArguments() != null) {
            title = getArguments().getString("title");
            type = getArguments().getString("type");
        }

        editNom = view.findViewById(R.id.editNom);
        editMasse = view.findViewById(R.id.editMasse);
        editPrix = view.findViewById(R.id.editPrix);
        spinnerType = view.findViewById(R.id.spinnerType);
        btnReset = view.findViewById(R.id.btn_reset);
        btnAdd = view.findViewById(R.id.btnAdd);
        buttonLayout = view.findViewById(R.id.buttonLayout);

        if (title != null) {
            switch (type) {
                case "ingredient":
                    title2.setText("Ajouter un nouvel ingrédient à la liste");
                    spinnerType.setVisibility(View.VISIBLE);
                    editMasse.setVisibility(View.VISIBLE);

                    Call<TypeResponse> call = service.getTypes();
                    call.enqueue(new Callback<TypeResponse>() {
                        @Override
                        public void onResponse(Call<TypeResponse> call, Response<TypeResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Type> types = response.body().getTypes();

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                        requireContext(),
                                        android.R.layout.simple_spinner_item,
                                        convertTypeListToStringList(types)
                                );
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerType.setAdapter(adapter);
                            } else {
                                Toast.makeText(requireContext(), "Erreur lors de la récupération des types", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TypeResponse> call, Throwable t) {
                            Toast.makeText(requireContext(), "Erreur API : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case "supplement":
                    title2.setText("Ajouter un nouveau supplément à la liste");
                    editPrix.setVisibility(View.VISIBLE);
                    break;

                case "gratine":
                    title2.setText("Ajouter un nouveau supplément gratiné à la liste");
                    editPrix.setVisibility(View.VISIBLE);
                    break;

                default:
                    title2.setText("Ajouter un élément");
                    break;
            }
        } else {
            title2.setText("Ajouter un élément");
        }
    }

    private List<String> convertTypeListToStringList(List<Type> typeList) {
        List<String> names = new java.util.ArrayList<>();
        for (Type t : typeList) {
            names.add(t.getNom());
        }
        return names;
    }


}
