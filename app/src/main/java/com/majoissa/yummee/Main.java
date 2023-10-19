package com.majoissa.yummee;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

        // Configuramos el espacio (gap) entre las tarjetas
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        ImageView imageView4 = findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambia la visibilidad de searchEditText y searchButton a VISIBLE
                searchEditText.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);

                // Solicita el foco para searchEditText
                searchEditText.requestFocus();

                // Abre el teclado soft
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes colocar el código para hacer algo cuando el usuario hace clic en el botón de búsqueda
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    // Realiza la búsqueda con la cadena 'query'
                }
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Realiza una consulta a Firestore para obtener los documentos de la colección "recetas"
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
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores en caso de fallo en la consulta a Firestore
                });
    }
}
