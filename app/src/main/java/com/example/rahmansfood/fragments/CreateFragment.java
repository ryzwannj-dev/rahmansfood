package com.example.rahmansfood.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rahmansfood.CrudSecondPartActivity;
import com.example.rahmansfood.R;

public class CreateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView createPizza;
    private CardView createBurger;
    private CardView createSandwich;
    private CardView createTexmex;
    private CardView createAssiette;
    private CardView createBoisson;
    private CardView createDessert;
    private CardView ingredientCrud;
    private CardView supplementCrud;
    private CardView supplementGratineCrud;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createPizza = view.findViewById(R.id.create_pizza);
        createBurger = view.findViewById(R.id.create_burger);
        createSandwich = view.findViewById(R.id.create_sandwich);
        createTexmex = view.findViewById(R.id.create_texmex);
        createAssiette = view.findViewById(R.id.create_assiette);
        createBoisson = view.findViewById(R.id.create_boisson);
        createDessert = view.findViewById(R.id.create_dessert);
        ingredientCrud = view.findViewById(R.id.ingredient_crud);
        supplementCrud = view.findViewById(R.id.supplement_crud);
        supplementGratineCrud = view.findViewById(R.id.supplement_gratine_crud);

        createPizza.setOnClickListener(v -> {
            // Action pour créer une pizza
            Toast.makeText(getContext(), "Créer une Pizza", Toast.LENGTH_SHORT).show();
            // openFragment(new CreatePizzaFragment()); // exemple
        });

        createBurger.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Créer un Burger", Toast.LENGTH_SHORT).show();
        });

        createSandwich.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Créer un Sandwich", Toast.LENGTH_SHORT).show();
        });

        createTexmex.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Créer un TexMex", Toast.LENGTH_SHORT).show();
        });

        createAssiette.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Créer une Assiette", Toast.LENGTH_SHORT).show();
        });

        createBoisson.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Ajouter une Boisson", Toast.LENGTH_SHORT).show();
        });

        createDessert.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Ajouter un Dessert", Toast.LENGTH_SHORT).show();
        });

        ingredientCrud.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CrudSecondPartActivity.class);
            intent.putExtra("title", "Gestion des Ingredients");
            intent.putExtra("type", "ingredient");
            startActivity(intent);
        });
        supplementCrud.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CrudSecondPartActivity.class);
            intent.putExtra("title", "Gestion des Suppléments");
            intent.putExtra("type", "supplement");
            startActivity(intent);
        });

        supplementGratineCrud.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CrudSecondPartActivity.class);
            intent.putExtra("title", "Gestion des Suppléments Gratiné");
            intent.putExtra("type", "gratine");
            startActivity(intent);
        });

    }


}