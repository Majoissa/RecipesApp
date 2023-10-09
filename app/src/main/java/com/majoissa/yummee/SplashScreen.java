package com.majoissa.yummee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // Duración del SplashScreen en milisegundos (2 segundos)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);
        // Crear una intención para la LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        Toast.makeText(this, "Esto funciona", Toast.LENGTH_LONG).show();
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar LoginActivity y mostrar activity_login.xml
                startActivity(intent);
                //finish(); // Cierra el SplashScreen
            }
        }, SPLASH_DURATION);
    }
}
