package com.majoissa.yummee;


import android.content.Context;
import android.graphics.Rect;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//Esta es la clase
public class Main extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private Button searchButton;
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
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        ImageView imageView4 = findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    // Realiza la búsqueda con la cadena 'query'
                }
            }
        });

        // Detectar el estado del teclado
        final View activityRootView = findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = activityRootView.getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // Teclado abierto, no hacer nada
                } else {
                    // Teclado cerrado, ocultar EditText y Button
                    searchEditText.setVisibility(View.GONE);
                    searchButton.setVisibility(View.GONE);
                }
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("2023recipesApp")
                .get()
                .addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Celda> celdas = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Celda celda = document.toObject(Celda.class);
                            celdas.add(celda);
                        }
                        CeldaAdapter adapter = new CeldaAdapter(celdas);
                        recyclerView.setAdapter(adapter);
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