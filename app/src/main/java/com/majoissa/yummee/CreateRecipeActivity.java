package com.majoissa.yummee;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRecipeActivity extends AppCompatActivity {

    EditText directionsEditText, imgUrlEditText, ingredientsEditText, ratingEditText, recipeNameEditText, videoUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createrecipe_activity);

        // Inicializar los EditText para cada campo de la receta
        directionsEditText = findViewById(R.id.editTextTextMultiLine3);
        //imgUrlEditText = findViewById(R.id.imageView10);
        ingredientsEditText = findViewById(R.id.editTextTextMultiLine2);
        //ratingEditText = findViewById(R.id.textView9);
        recipeNameEditText = findViewById(R.id.editTextText);
        //videoUrlEditText = findViewById(R.id.textView9);

        Button uploadButton = findViewById(R.id.button4);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadRecipeData();
            }
        });
    }

    private void uploadRecipeData() {
        // Obtén una instancia de la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("recipes");

        // Crea un ID único para la receta
        String recipeId = reference.push().getKey();

        // Obtén los datos ingresados por el usuario de los EditText
        String directions = directionsEditText.getText().toString();
        String imgUrl = imgUrlEditText.getText().toString();
        String ingredients = ingredientsEditText.getText().toString();
        int rating = Integer.parseInt(ratingEditText.getText().toString());
        String recipeName = recipeNameEditText.getText().toString();
        String videoUrl = videoUrlEditText.getText().toString();

        // Crea un objeto Recipe con los datos de la receta
        Recipes recipe = new Recipes(directions, imgUrl, ingredients, rating, recipeName, videoUrl);

        // Sube los datos de la receta a la base de datos de Firebase
        reference.child(recipeId).setValue(recipe);
    }
}