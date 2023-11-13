package com.majoissa.yummee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RecipeTemplate extends AppCompatActivity {

    private StorageReference mStorageRef;
    private Uri fileUri, downloadUri;
    private ImageButton back;
    private ImageButton addImageButton;
    private ActivityResultLauncher<String> mGetContent;
    private FirebaseFirestore db;
    private EditText direc, ingr, time, title, videoUrl;
    private Button uploadButton, nutriButton;
    private String imageUrl;
    private FirebaseAuth mAuth;
    private static final int BACK = 1000;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createrecipe_activity);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = new Intent(this, Main.class);
        pref = getPreferences(MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("info", "");
        editor.commit();
        db = FirebaseFirestore.getInstance();
        uploadButton = findViewById(R.id.button4);
        nutriButton = findViewById(R.id.button3);
        direc = findViewById(R.id.editTextTextMultiLine3);
        ingr = findViewById(R.id.editTextTextMultiLine2);
        videoUrl = findViewById(R.id.videoView2);
        back = findViewById(R.id.imageView9);
        time = findViewById(R.id.editTextTime4);
        title = findViewById(R.id.editTextText);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        nutriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopup(view);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileUri == null) {
                    Toast.makeText(RecipeTemplate.this, "Please select an image.", Toast.LENGTH_SHORT).show();
                }
                else if (videoUrl.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RecipeTemplate.this, "Please provide a YouTube link.", Toast.LENGTH_SHORT).show();
                }
                else if (ingr.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RecipeTemplate.this, "You should write the ingredients!", Toast.LENGTH_SHORT).show();
                }
                else if (direc.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RecipeTemplate.this, "Don't forget to write the directions!", Toast.LENGTH_SHORT).show();
                }
                else if (title.getText().toString().trim().isEmpty() || title.getText().toString().equals("Recipe Title")) {
                    Toast.makeText(RecipeTemplate.this, "Please write the name of your recipe.", Toast.LENGTH_SHORT).show();
                }
                else if (time.getText().toString().isEmpty() || !time.getText().toString().matches("\\d+")) {
                    Toast.makeText(RecipeTemplate.this, "Please provide numbers to the time field.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String youtubePattern = "^(?:https?:\\/\\/)?(?:(?:www\\.)?youtube\\.com\\/(?:(?:v\\/)|(?:embed\\/|watch(?:\\/|\\?)){1,2}(?:.*v=)?|.*v=)?|(?:www\\.)?youtu\\.be\\/)([A-Za-z0-9_\\-]+)&?.*$";
                    if (videoUrl.getText().toString().trim().matches(youtubePattern)) {
                        uploadFile();
                        Toast.makeText(RecipeTemplate.this, "Uploading recipe...", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        }, BACK);
                    }
                    else {
                        Toast.makeText(RecipeTemplate.this, "Please provide a valid YouTube link.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                imageUri -> {
                    if (imageUri != null) {
                        fileUri = imageUri;
                        getIMGSize(imageUri);
                        addImageButton.setForeground(null);
                        addImageButton.setImageURI(imageUri);
                    }
                });
        addImageButton = findViewById(R.id.imageView10);
        addImageButton.setOnClickListener(v -> openImageFileChooser());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void openImageFileChooser() {
        mGetContent.launch("image/*");
    }
    private void uploadFile() {
        final boolean[] isImageUploaded = {false};
        if (fileUri != null) {
            StorageReference riversRef = mStorageRef.child("ImagesRecipes/" + fileUri.getLastPathSegment());

            riversRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        riversRef.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                downloadUri = task.getResult();
                                imageUrl = downloadUri.toString();
                                isImageUploaded[0] = true;

                                // Llama a uploadRecipe si ambos archivos se han cargado
                                if (isImageUploaded[0]) {
                                    uploadRecipe();
                                }
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseStorage", "Error uploading image: " + e.getMessage());
                    });
        } else {
            Log.e("FirebaseStorage", "No file selected");
        }
    }

    private void uploadRecipe() {
        String nutritionalInfo = pref.getString("info", "");
        String name = title.getText().toString().trim();
        String recipeTitle = name.substring(0, 1).toUpperCase() + name.substring(1);
        Map<String, Object> data = new HashMap<>();
        data.put("directions", direc.getText().toString());
        data.put("img_url", imageUrl);
        data.put("ingredients", ingr.getText().toString());
        data.put("rating_recipes", 0.0);
        data.put("recipe_name", recipeTitle);
        data.put("video_url", videoUrl.getText().toString());
        data.put("time", Integer.parseInt(time.getText().toString()));
        data.put("nutritional_info", nutritionalInfo);
        data.put("creator", mAuth.getCurrentUser().getEmail());

        db.collection("2023recipesApp").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RecipeTemplate.this, "Recipe added!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getIMGSize(Uri uri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
    }
    public void onButtonShowPopup(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_nutritional, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
        Button btn = (Button) popupView.findViewById(R.id.button7);
        EditText text = (EditText) popupView.findViewById(R.id.editTextTextMultiLine);
        text.setText(pref.getString("info", ""));
        text.requestFocus();
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.update();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = pref.edit();
                editor.putString("info", text.getText().toString());
                editor.commit();
                popupWindow.dismiss();
            }
        });
    }
}
