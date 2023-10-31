package com.majoissa.yummee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // Duración del SplashScreen en milisegundos (2 segundos)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;

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
