package com.majoissa.yummee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//test

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // Duración del SplashScreen en milisegundos (2 segundos)
    private FirebaseFirestore db;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        // Check if the deep link is not null and has the expected path
                        if (deepLink != null && deepLink.getPathSegments().contains("recipe")) {
                            // Extract the recipeId from the deep link
                            String linkId = extractRecipeIdFromDeepLink(deepLink);
                            // Use the recipeId as needed
                            if (linkId != null && currentUser != null) {

                                intent = new Intent(SplashScreen.this, RecipesDetailsActivity.class);
                                db.collection("2023recipesApp")
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                    if (document.getId().equals(linkId)) {
                                                        intent.putExtra("recipeId", linkId);
                                                        intent.putExtra("recipeName", document.getString("recipe_name"));
                                                        intent.putExtra("recipeImg", document.getString("img_url"));
                                                        intent.putExtra("recipeVideo", document.getString("video_url"));
                                                        intent.putExtra("recipeDirections", document.getString("directions"));
                                                        intent.putExtra("recipeIngredients", document.getString("ingredients"));
                                                        intent.putExtra("ratingRecipe", document.getDouble("rating_recipes"));
                                                        intent.putExtra("recipeDuration", document.getDouble("time").intValue());
                                                        intent.putExtra("recipeNutrition", document.getString("nutritional_info"));
                                                        startActivity(intent);
                                                        finish();
                                                        return;
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                redirectToAppropriateActivity(currentUser);
                            }
                        }
                        else {
                            redirectToAppropriateActivity(currentUser);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SplashScreen.this, "Failure!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String extractRecipeIdFromDeepLink(Uri deepLink) {
        if (deepLink != null) {
            String linkId = String.valueOf(deepLink).substring(String.valueOf(deepLink).lastIndexOf('/')+1);
            return linkId;
        }
        else {
            return null;
        }
    }
    private void redirectToAppropriateActivity(FirebaseUser currentUser) {
        if (currentUser != null) {
            // Si el usuario ya está autenticado, redirige a la actividad principal (Main).
            intent = new Intent(this, Main.class);
        } else {
            // Si el usuario no está autenticado, redirige a LoginActivity.
            intent = new Intent(this, LoginActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar la actividad correspondiente y finalizar SplashScreen.
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
