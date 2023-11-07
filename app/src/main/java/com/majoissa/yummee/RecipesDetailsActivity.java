package com.majoissa.yummee;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesDetailsActivity extends AppCompatActivity {

    private Buttons buttons;
    private Button post;
    private ImageButton home;
    private ImageButton back;
    private ImageButton goToLikes;
    private EditText addComment;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<CeldaMessage> celdas;
    private CeldaAdapterMessage adapter;
    private RatingBar stars;
    private String recipeId;//aqui se almacena el id de cada receta
    private TextView recipeName;
    private ImageView recipeImg;
    private RatingBar recipeRating;
    private VideoView recipeVideo;
    private TextView recipeIngredients;
    private TextView recipeDirections;
    private TextView recipeDuration;
    private ImageButton toTop;
    private ScrollView scrollView;
    private Button nutriButton;
    private ImageButton favButton;
    private boolean favorited = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_details);
        recipeId = getIntent().getStringExtra("recipeId");
        if (recipeId == null) {
            Toast.makeText(this, "Recipe ID is missing!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadRecipeDetails();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        buttons = new Buttons(this);
        stars = findViewById(R.id.ratingRecipes);
        post = findViewById(R.id.button4);
        home = findViewById(R.id.imageButton16);
        back = findViewById(R.id.imageButton10);
        addComment = findViewById(R.id.editTextText);
        toTop = findViewById(R.id.imageButton8);
        scrollView = findViewById(R.id.scrollView2);
        nutriButton = findViewById(R.id.button3);
        favButton = findViewById(R.id.imageButton9);
        goToLikes = findViewById(R.id.imageButton12);
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
        toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0, 0);
            }
        });
        nutriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nutriButton.setEnabled(false);
                onButtonShowPopup(view);
            }
        });
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDislike();
            }
        });
        goToLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.FavoriteButton(view);
            }
        });
        checkIfLiked();
    }

    private void loadRecipeDetails() {
        // Obtiene los datos del Intent
        String recipeNameString = getIntent().getStringExtra("recipeName");
        String recipeImgString = getIntent().getStringExtra("recipeImg");
        String recipeVideoString = getIntent().getStringExtra("recipeVideo");
        String recipeDirectionsString = getIntent().getStringExtra("recipeDirections");
        String recipeIngredientsString = getIntent().getStringExtra("recipeIngredients");
        float ratingRecipeFloat = getIntent().getFloatExtra("ratingRecipe", 0.0f);
        int recipeTimeInt = getIntent().getIntExtra("recipeDuration", 0);

        // Inicializa tus Views si aún no lo has hecho
        recipeName = findViewById(R.id.textView);
        recipeImg = findViewById(R.id.imageView);
        recipeVideo = findViewById(R.id.videoView);
        recipeIngredients = findViewById(R.id.ingredientes);
        recipeDirections = findViewById(R.id.textView13);
        recipeRating = findViewById(R.id.ratingRecipes);
        recipeDuration = findViewById(R.id.textView7);

        // Asigna los valores a tus Views
        recipeName.setText(recipeNameString);
        Picasso.get().load(recipeImgString).into(recipeImg);
        // Asumiendo que tu VideoView usa un Uri, podrías hacer:
        Uri u = Uri.parse(recipeVideoString);
        recipeVideo.setVideoURI(u);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(recipeVideo);
        mediaController.setMediaPlayer(recipeVideo);
        recipeVideo.setMediaController(mediaController);
        recipeVideo.start();
        // TODO: Considera usar alguna librería o método para cargar el video en el VideoView si no es un Uri directo
        recipeIngredients.setText(recipeIngredientsString);
        recipeDirections.setText(recipeDirectionsString);
        recipeRating.setRating(ratingRecipeFloat);
        recipeDuration.setText(recipeTimeInt + " min");
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
    public void onButtonShowPopup(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_nutritional, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        String recipeNut = getIntent().getStringExtra("recipeNutrition");
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
        Button btn = (Button) popupView.findViewById(R.id.button7);
        btn.setText("Back");
        EditText text = (EditText) popupView.findViewById(R.id.editTextTextMultiLine);
        text.setText(recipeNut);
        text.requestFocus();
        text.setClickable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        text.setCursorVisible(false);
        popupWindow.update();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                nutriButton.setEnabled(true);
            }
        });
    }
    private void likeDislike() {
        Map<String, Object> addLike = new HashMap<>();
        addLike.put("likedBy", mAuth.getCurrentUser().getEmail());
        CollectionReference likesCollection = db.collection("2023recipesApp").document(recipeId).collection("Likes");

        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (favorited) {
            favButton.setColorFilter(Color.argb(255, 131, 121, 121));
            favorited = false;
            editor.putBoolean("liked_" + recipeId, false);
            editor.apply();
            likesCollection.whereEqualTo("likedBy", mAuth.getCurrentUser().getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                Toast.makeText(this, "Removed like!", Toast.LENGTH_SHORT).show();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Obtiene la referencia del documento y lo borra.
                    DocumentReference docRef = document.getReference();
                    docRef.delete();
                }
            });
        }
        else {
            favButton.setColorFilter(Color.argb(255, 121, 155, 126));
            favorited = true;
            editor.putBoolean("liked_" + recipeId, true);
            editor.apply();
            // Agrega un nuevo documento a la colección "Likes".
            likesCollection.add(addLike).addOnSuccessListener(documentReference -> {
                Toast.makeText(RecipesDetailsActivity.this, "Liked!", Toast.LENGTH_SHORT).show();
            });
        }
    }
    private void checkIfLiked() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        // Obtén el valor de "liked_recipeId" (reemplaza recipeId por el ID real de la receta).
        boolean liked = sharedPreferences.getBoolean("liked_" + recipeId, false);

        if (liked) {
            // El usuario ya dio "like" a esta receta.
            favButton.setColorFilter(Color.argb(255, 121, 155, 126));
            favorited = true;
        } else {
            // El usuario no ha dado "like" a esta receta.
            favButton.setColorFilter(Color.argb(255, 131, 121, 121));
            favorited = false;
        }
    }

}