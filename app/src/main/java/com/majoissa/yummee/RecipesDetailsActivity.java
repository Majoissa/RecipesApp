package com.majoissa.yummee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RecipesDetailsActivity extends AppCompatActivity {

    private Buttons buttons;
    private ImageButton home;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_details);

        buttons = new Buttons(this);
        home = findViewById(R.id.imageButton16);
        back = findViewById(R.id.imageButton10);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CeldaMessage> celdas = new ArrayList<>();
        celdas.add(new CeldaMessage("mj@gmail.com", 4.5f, "Buenisima la receta, para repetirla muchas veces mas!!! Muy recomendado"));
        celdas.add(new CeldaMessage("juan@gmail.com", 3.0f, "Me gustó, pero podría mejorar."));
        celdas.add(new CeldaMessage("luis@gmail.com", 5.0f, "Excelente, la mejor receta que he probado."));
        // Añade más celdas según lo necesario

        CeldaAdapterMessage adapter = new CeldaAdapterMessage(celdas);
        recyclerView.setAdapter(adapter);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.HomeButton(view);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}