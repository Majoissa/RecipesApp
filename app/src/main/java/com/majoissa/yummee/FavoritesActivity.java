package com.majoissa.yummee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private ImageButton back;
    private Buttons b;
    private ImageButton homeButton;
    private ImageButton searchButton;
    private ImageButton userButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<Celda> celdas;
    private CeldaAdapter adapter;
    private TextView empty;
    private String recipeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        b = new Buttons(this);
        homeButton = findViewById(R.id.imageButton14);
        searchButton = findViewById(R.id.imageButton15);
        userButton = findViewById(R.id.imageButton17);
        recipeId = getIntent().getStringExtra("recipeId");
        recyclerView = findViewById(R.id.recyclerView2);
        empty = findViewById(R.id.textView19);
        back = findViewById(R.id.imageButton13);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.HomeButton(view);
            }
        });
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.UserButton(view);
            }
        });
        loadCeldas();
    }
    private void loadCeldas() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        db.collection("2023recipesApp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    celdas = new ArrayList<>();
                    for (QueryDocumentSnapshot recipeDocument : queryDocumentSnapshots) {
                        // Obtén el ID de la receta actual
                        String recipeId = recipeDocument.getId();

                        // Accede a la colección "Likes" dentro del documento de la receta
                        CollectionReference likesCollection = db.collection("2023recipesApp")
                                .document(recipeId)
                                .collection("Likes");

                        likesCollection.whereEqualTo("likedBy", mAuth.getCurrentUser().getEmail())
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshotsLikes -> {
                                    if (!queryDocumentSnapshotsLikes.isEmpty()) {
                                        Celda celda = recipeDocument.toObject(Celda.class);
                                        celda.setDocumentId(recipeDocument.getId());
                                        celdas.add(celda);
                                    }
                                    adapter = new CeldaAdapter(celdas);
                                    recyclerView.setAdapter(adapter);
                                    if (celdas.isEmpty()) {
                                        empty.setVisibility(View.VISIBLE);
                                    } else {
                                        empty.setVisibility(View.INVISIBLE);
                                    }
                                });
                    }
                });
    }
}