package com.majoissa.yummee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class Main extends AppCompatActivity {

    private Buttons buttons;
    private ImageView user;
    private ImageView favorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons = new Buttons(this);
        user = findViewById(R.id.imageView9);
        favorites = findViewById(R.id.imageView2);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Celda> celdas = new ArrayList<>();
        celdas.add(new Celda("KFC Inspo Food", R.drawable.ic_launcher_foreground, 3.4f,"1.600+ reviews"));
        celdas.add(new Celda("KFC Inspo Food", R.drawable.ic_launcher_foreground, 3.5f,"1.600+ reviews"));
        celdas.add(new Celda("KFC Inspo Food", R.drawable.ic_launcher_foreground, 3.5f,"1.600+ reviews"));
        celdas.add(new Celda("KFC Inspo Food", R.drawable.ic_launcher_foreground, 3.5f,"1.600+ reviews"));
        celdas.add(new Celda("KFC Inspo Food", R.drawable.ic_launcher_foreground, 3.5f,"1.600+ reviews"));
        celdas.add(new Celda("KFC Inspo Food", R.drawable.ic_launcher_foreground, 3.5f,"1.600+ reviews"));

        // Añade más celdas según lo necesario

        CeldaAdapter adapter = new CeldaAdapter(celdas);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.UserButton(view);
            }
        });
    }
}