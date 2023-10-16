package com.majoissa.yummee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextTextEmailAddress;
    private EditText editTextTextPassword;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Intent intent = new Intent(this, LoginActivity.class);

        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            String email = editTextTextEmailAddress.getText().toString();
            String password = editTextTextPassword.getText().toString();

            // Llama al método registerUser de FirebaseUtilities
            FirebaseUtilities.registerUser(email, password, new FirebaseUtilities.FirebaseRegisterCallback() {
                @Override
                public void onSuccess() {
                    // Registro exitoso, el usuario se ha creado
                    // Puedes realizar acciones adicionales aquí si es necesario
                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }

                @Override
                public void onError(Exception exception) {
                    // Fallo en el registro
                    Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
