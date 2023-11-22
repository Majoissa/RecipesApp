package com.majoissa.yummee;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private Buttons buttons;
    private ImageButton home;
    private ImageButton logout;
    private ImageButton back;
    private ImageButton favorite;
    private TextView userName;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<Celda> celdas;
    private CeldaAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.userRecipes);
        emptyRecipes = findViewById(R.id.textView14);
        userName = findViewById(R.id.textView5);
        logout = findViewById(R.id.imageButton2);
        home = findViewById(R.id.imageButton4);
        back = findViewById(R.id.imageButton);
        favorite = findViewById(R.id.imageButton5);

        buttons = new Buttons(this);

        String email = mAuth.getCurrentUser().getEmail();
        userName.setText(email);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.HomeButton(view);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.setEnabled(false);
                onButtonShowPopup(view);
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.FavoriteButton(view);
            }
        });
        loadCeldas();
    }

    public void onButtonShowPopup(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_logout, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button btn1 = popupView.findViewById(R.id.button5);
        Button btn2 = popupView.findViewById(R.id.button6);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                buttons.LogoutButton(view);
                logout.setEnabled(true);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                logout.setEnabled(true);
            }
        });
    }
    private void loadCeldas() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));
        
        db.collection("2023recipesApp")
                .whereEqualTo("creator", mAuth.getCurrentUser().getEmail())
                .addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null && !documentSnapshot.getDocuments().isEmpty()) {
                celdas = new ArrayList<>();
                for (QueryDocumentSnapshot document : documentSnapshot) {
                    Celda celda = document.toObject(Celda.class);
                    celda.setDocumentId(document.getId());
                    celdas.add(celda);
                }
                adapter = new CeldaAdapter(celdas);
                recyclerView.setAdapter(adapter);
            }
            else {
                recyclerView.setAdapter(null);
                emptyRecipes.setVisibility(View.VISIBLE);
            }
        });
    }
}
