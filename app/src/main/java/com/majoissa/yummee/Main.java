package com.majoissa.yummee;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private Button searchButton;
    private Buttons buttons;
    private ImageView user;
    private ImageView favorites;
    private FirebaseFirestore db;
    private List<Celda> celdas;
    private CeldaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        initViews();
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideSearch();
            }
        });

        loadCeldas();
    }

    private void initViews() {
        buttons = new Buttons(this);
        user = findViewById(R.id.imageView9);
        favorites = findViewById(R.id.imageView2);
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        ImageView imageView4 = findViewById(R.id.imageView4);
        imageView4.setOnClickListener(v -> displaySearch());

        searchButton.setOnClickListener(v -> {
            searchInFirestore();
            hideKeyboard();     // Ocultar el teclado después de realizar la búsqueda
        });

        fetchDataFromFirestore();
    }

    private void searchInFirestore() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("2023recipesApp")
                    .orderBy("recipe_name")
                    .startAt(query)
                    .endAt(query + "\uf8ff")
                    .get()
                    .addOnSuccessListener(this::processFirestoreData)
                    .addOnFailureListener(e -> {
                        Log.e("Main", "Error al consultar Firestore", e);
                        Toast.makeText(Main.this, "Error al consultar Firestore", Toast.LENGTH_SHORT).show();
                    });
        } else {
            fetchDataFromFirestore();
        }
    }

    private void processFirestoreData(QuerySnapshot queryDocumentSnapshots) {
        if (!queryDocumentSnapshots.isEmpty()) {
            List<Celda> celdas = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Celda celda = document.toObject(Celda.class);
                celdas.add(celda);
            }
            CeldaAdapter adapter = new CeldaAdapter(celdas);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(Main.this, "No se encontraron recetas", Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(null);
        }
    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("2023recipesApp")
                .get()
                .addOnSuccessListener(this::processFirestoreData)
                .addOnFailureListener(e -> Log.e("Main", "Error al consultar Firestore", e));

        user.setOnClickListener(view -> buttons.UserButton(view));
    }

    private void displaySearch() {
        searchEditText.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideSearch() {
        searchEditText.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadCeldas() {
        db.collection("2023recipesApp").addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null && !documentSnapshot.getDocuments().isEmpty()) {
                celdas = new ArrayList<>();
                for (QueryDocumentSnapshot document : documentSnapshot) {
                    Celda celda = document.toObject(Celda.class);
                    celdas.add(celda);
                }
                adapter = new CeldaAdapter(celdas);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
