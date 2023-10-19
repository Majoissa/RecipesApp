package com.majoissa.yummee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editText);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.button);
        TextView textViewCreateAccount = findViewById(R.id.button2);

        buttonLogin.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            // Llama al método loginUser de FirebaseUtilities
            FirebaseUtilities.loginUser(email, password, new FirebaseUtilities.FirebaseLoginCallback() {
                @Override
                public void onSuccess() {
                    // Inicio de sesión exitoso, redirigir a la actividad principal o realizar otras acciones
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    // Ejemplo de redirección a una actividad principal
                    Intent intent = new Intent(LoginActivity.this, Main.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Exception exception) {
                    // Error en el inicio de sesión
                    Toast.makeText(LoginActivity.this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
                }
            });
        });

        textViewCreateAccount.setOnClickListener(view -> {
            // Redirigir a la actividad de registro
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
