package com.example.rahmansfood;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.rahmansfood.fragments.GestionCreateFragment;
import com.example.rahmansfood.fragments.GestionCrudFragment;
import com.google.android.material.tabs.TabLayout;

public class CrudSecondPartActivity extends AppCompatActivity {

    private TextView title_text;
    private ImageButton back;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;

    private String title;
    public static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crud_second_part);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back = findViewById(R.id.back_button);
        back.setOnClickListener(v -> finish());

        title_text = findViewById(R.id.title);
        tabLayout = findViewById(R.id.tabLayout);
        frameLayout = findViewById(R.id.frameLayout);

        // Définir le texte du titre dans l'activité à partir de l'intent
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");

        setupMarqueeEffect(title_text);
        title_text.setText(title);

        // Créer un fragment par défaut (GestionCreateFragment) et lui passer les arguments
        GestionCreateFragment gestionCreateFragment = new GestionCreateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title); // Passer le titre
        bundle.putString("type", type);   // Passer le type
        gestionCreateFragment.setArguments(bundle); // Ajouter les arguments au fragment

        // Remplacer le fragment actuel dans le FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, gestionCreateFragment)
                .commit();

        // Configurer le TabLayout pour gérer les différents fragments si nécessaire
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;

                // Lorsque l'onglet est sélectionné, on peut envoyer les arguments également
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("type", type);

                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new GestionCreateFragment();
                        break;
                    case 1:
                        selectedFragment = new GestionCrudFragment();
                        break;
                }

                if (selectedFragment != null) {
                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, selectedFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // Configuration de l'effet défilant sur le texte
    private void setupMarqueeEffect(TextView textView) {
        textView.setSelected(true);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(-1);
        textView.setHorizontallyScrolling(true);
    }

    public static String getType(){
        return type;
    }
}
