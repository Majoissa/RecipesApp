package com.majoissa.yummee;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.majoissa.yummee.LoginActivity;
import com.majoissa.yummee.R;

public class UserActivity extends AppCompatActivity {


    //Logout function
    private Button logoutButton; // Agrega esta línea

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        logoutButton = findViewById(R.id.logoutButton); // Asocia el botón de cierre de sesión

        // Agrega un listener al botón de cierre de sesión
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión en Firebase
                FirebaseAuth.getInstance().signOut();

                // Redirigir al usuario a LoginActivity
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
