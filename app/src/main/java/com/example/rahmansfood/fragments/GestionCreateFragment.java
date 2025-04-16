package com.example.rahmansfood.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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
import com.example.rahmansfood.models.ApiClient;
import com.example.rahmansfood.models.Type;

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

    private List<Type> typeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gestion_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title2 = view.findViewById(R.id.titleText);
        editNom = view.findViewById(R.id.editNom);
        editMasse = view.findViewById(R.id.editMasse);
        editPrix = view.findViewById(R.id.editPrix);
        spinnerType = view.findViewById(R.id.spinnerType);
        btnReset = view.findViewById(R.id.btn_reset);
        btnAdd = view.findViewById(R.id.btnAdd);
        buttonLayout = view.findViewById(R.id.buttonLayout);

        if (getArguments() != null) {
            title = getArguments().getString("title");
            type = getArguments().getString("type");
        }

        btnReset.setOnClickListener(v -> {
            resetInputs();
        });

        if (title != null && "ingredient".equals(CrudSecondPartActivity.getType())) {
            title2.setText("Ajouter un nouvel ingrédient à la liste");
            editMasse.setVisibility(View.VISIBLE);
            spinnerType.setVisibility(View.VISIBLE);
            loadTypesFromApi();

            btnAdd.setOnClickListener(v -> {
                String nom = editNom.getText().toString().trim();
                String masse = editMasse.getText().toString().trim();
                Type selectedType = (Type) spinnerType.getSelectedItem();

                if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(masse) || selectedType == null || "default".equals(selectedType.getId())) {
                    Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                String message = "Voulez-vous vraiment ajouter cet ingrédient ?\n\n" +
                        "Nom : " + nom + "\n" +
                        "Masse : " + masse + "\n" +
                        "Type : " + selectedType.getNom();

                new android.app.AlertDialog.Builder(requireContext())
                        .setTitle("Confirmation")
                        .setMessage(message)
                        .setPositiveButton("Oui", (dialog, which) -> {
                            Retrofit retrofit = ApiClient.getClient(getContext());
                            ProduitApiService service = retrofit.create(ProduitApiService.class);
                            Call<Void> call = service.addIngredient(nom, masse, selectedType.getId());

                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        showAlert("Succès", "Ingrédient ajouté avec succès");
                                        resetInputs(); // Réinitialiser les champs
                                    } else {
                                        showAlert("Échec", "Erreur lors de l'ajout de l'ingrédient");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    showAlert("Erreur de connexion", "Erreur de connexion : " + t.getMessage());
                                }
                            });
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            });

        } else if ("supplement".equals(CrudSecondPartActivity.getType())) {
            title2.setText("Ajouter un nouveau supplément à la liste");
            editPrix.setVisibility(View.VISIBLE);

            btnAdd.setOnClickListener(v -> {
                String nom = editNom.getText().toString().trim();
                String prixStr = editPrix.getText().toString().trim();

                if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(prixStr)) {
                    showAlert("Champs manquants", "Veuillez remplir tous les champs");
                    return;
                }

                float prix;
                try {
                    prix = Float.parseFloat(prixStr);
                } catch (NumberFormatException e) {
                    showAlert("Format invalide", "Veuillez entrer un prix valide");
                    return;
                }

                String message = "Voulez-vous vraiment ajouter ce supplément ?\n\n" +
                        "Nom : " + nom + "\n" +
                        "Prix : " + prix + " €";

                new android.app.AlertDialog.Builder(requireContext())
                        .setTitle("Confirmation")
                        .setMessage(message)
                        .setPositiveButton("Oui", (dialog, which) -> {
                            Retrofit retrofit = ApiClient.getClient(getContext());
                            ProduitApiService service = retrofit.create(ProduitApiService.class);
                            Call<Void> call = service.addIngredient(nom, prix);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        showAlert("Succès", "Supplément ajouté avec succès");
                                        resetInputs();
                                    } else {
                                        showAlert("Erreur", "Échec de l'ajout du supplément");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    showAlert("Erreur réseau", "Impossible de se connecter : " + t.getMessage());
                                }
                            });
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            });



        } else if ("gratine".equals(CrudSecondPartActivity.getType())) {
            title2.setText("Ajouter un nouveau supplément gratiné à la liste");
            editPrix.setVisibility(View.VISIBLE);

            btnAdd.setOnClickListener(v -> {
                String nom = editNom.getText().toString().trim();
                String prixStr = editPrix.getText().toString().trim();

                if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(prixStr)) {
                    showAlert("Champs manquants", "Veuillez remplir tous les champs");
                    return;
                }

                float prix;
                try {
                    prix = Float.parseFloat(prixStr);
                } catch (NumberFormatException e) {
                    showAlert("Format invalide", "Veuillez entrer un prix valide");
                    return;
                }

                String message = "Voulez-vous vraiment ajouter ce supplément gratiné ?\n\n" +
                        "Nom : " + nom + "\n" +
                        "Prix : " + prix + " €";

                new android.app.AlertDialog.Builder(requireContext())
                        .setTitle("Confirmation")
                        .setMessage(message)
                        .setPositiveButton("Oui", (dialog, which) -> {
                            Retrofit retrofit = ApiClient.getClient(getContext());
                            ProduitApiService service = retrofit.create(ProduitApiService.class);
                            Call<Void> call = service.addSupplementGratine(nom, prix); // Ajoute cette méthode dans ton interface

                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        showAlert("Succès", "Supplément gratiné ajouté avec succès");
                                        resetInputs();
                                    } else {
                                        showAlert("Erreur", "Échec de l'ajout du supplément gratiné");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    showAlert("Erreur réseau", "Impossible de se connecter : " + t.getMessage());
                                }
                            });
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            });

        } else {
            title2.setText("Ajouter un élément");
        }
    }

    private void loadTypesFromApi() {
        Retrofit retrofit = ApiClient.getClient(getContext());
        ProduitApiService service = retrofit.create(ProduitApiService.class);
        Call<List<Type>> call = service.getTypes();

        call.enqueue(new Callback<List<Type>>() {
            @Override
            public void onResponse(Call<List<Type>> call, Response<List<Type>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    typeList = response.body();

                    Type defaultType = new Type();
                    defaultType.setId("default");
                    defaultType.setNom("Sélectionner un type");
                    typeList.add(0, defaultType);

                    ArrayAdapter<Type> adapter = new ArrayAdapter<Type>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            typeList
                    ) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            TextView view = (TextView) super.getView(position, convertView, parent);
                            view.setText(typeList.get(position).getNom());
                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                            view.setText(typeList.get(position).getNom());
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapter);

                } else {
                    Toast.makeText(requireContext(), "Erreur de chargement des types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Type>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur API : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlert(String title, String message) {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void resetInputs() {
        editPrix.setText("");
        editNom.setText("");
        editMasse.setText("");
        spinnerType.setSelection(0);
    }
}
