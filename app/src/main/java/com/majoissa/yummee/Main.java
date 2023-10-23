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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

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

        searchButton.setOnClickListener(v -> searchInFirestore());

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
    }
}
