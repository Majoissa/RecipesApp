package com.majoissa.yummee;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Buttons buttons;
    private ImageView user;
    private ImageView favorites;
    private FirebaseFirestore db;
    private List<Celda> celdas;
    private Celda celda;
    private CeldaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        buttons = new Buttons(this);
        user = findViewById(R.id.imageView9);
        favorites = findViewById(R.id.imageView2);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Configuramos el espacio (gap) entre las tarjetas
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        loadCeldas();
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.UserButton(view);
            }
        });
    }

    private void loadCeldas() {
        // Realiza una consulta a Firestore para obtener los documentos de la colección "recetas"
        db.collection("2023recipesApp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null && !documentSnapshot.getDocuments().isEmpty()) {
                    celdas = new ArrayList<>();
                    List<DocumentSnapshot> documents = documentSnapshot.getDocuments();
                    for (DocumentSnapshot value : documents) {
                        String title = value.getString("recipe_name");
                        String image = value.getString("img_url");
                        String reviews = value.getString("totalReviews");
                        double ratings = value.getDouble("rating_recipes");
                        celdas.add(new Celda(title, image, ratings, reviews));
                    }
                    adapter = new CeldaAdapter(celdas);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }
}