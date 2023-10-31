package com.majoissa.yummee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.utils.DateUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RecipesDetailsActivity extends AppCompatActivity {

    private Buttons buttons;
    private Button post;
    private ImageButton home;
    private ImageButton back;
    private EditText addComment;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<CeldaMessage> celdas;
    private CeldaAdapterMessage adapter;
    private RatingBar stars;
    private String recipeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeId = getIntent().getStringExtra("recipeId");
        if (recipeId == null) {
            Toast.makeText(this, "Recipe ID is missing!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }else {
            Toast.makeText(this, "no  funciona", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_recipes_details);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        buttons = new Buttons(this);
        stars = findViewById(R.id.ratingRecipes);
        post = findViewById(R.id.button4);
        home = findViewById(R.id.imageButton16);
        back = findViewById(R.id.imageButton10);
        addComment = findViewById(R.id.editTextText);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        celdas = new ArrayList<>();
        loadComments();

        adapter = new CeldaAdapterMessage(celdas);
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
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putComment();
            }
        });
    }
    private void putComment() {
        String userEmail = mAuth.getCurrentUser().getEmail();
        float userRating = stars.getRating();
        String userComment = addComment.getText().toString();
        Date day = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(day);

        Map<String, Object> commentData = new HashMap<>();
        commentData.put("userEmail", userEmail);
        commentData.put("userRating", userRating);
        commentData.put("userComment", userComment);
        commentData.put("day", formattedDate);

        // Añade el comentario como un documento en una subcolección
        db.collection("2023recipesApp").document(recipeId)
                .collection("Comments")  // Crea una subcolección llamada "Comments"
                .add(commentData)       // Agrega un nuevo comentario
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RecipesDetailsActivity.this, "Comment added!", Toast.LENGTH_SHORT).show();
                        celdas.add(new CeldaMessage(userEmail,userRating,userComment, formattedDate));

                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void loadComments() {
        // Accede a la subcolección "Comments" de la receta
        //String recipeId = getIntent().getStringExtra("recipeId");
        db.collection("2023recipesApp").document(recipeId)
                .collection("Comments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Obtiene los datos del comentario y agrega a la lista local
                            String userEmail = document.getString("userEmail");
                            float userRating = document.getDouble("userRating").floatValue();
                            String userComment = document.getString("userComment");
                            String day = document.getString("day");
                            celdas.add(new CeldaMessage(userEmail, userRating, userComment, day));
                        }
                        // Notifica al adaptador de que se realizaron cambios en la lista
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}