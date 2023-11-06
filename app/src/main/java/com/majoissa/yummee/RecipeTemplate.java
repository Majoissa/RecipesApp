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
import android.widget.VideoView;

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
    private Uri fileUri, fileUri2, downloadUri, downloadUri2;
    private ImageButton back;
    private ImageButton addImageButton;
    private ActivityResultLauncher<String> mGetContent;
    private ActivityResultLauncher<String> mGetContent2;
    private VideoView video;
    private FirebaseFirestore db;
    private EditText direc, ingr, time, title;
    private Button uploadButton, nutriButton;
    private String imageUrl, videoUrl;
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
        video = findViewById(R.id.videoView2);
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

        mGetContent2 = registerForActivityResult(new ActivityResultContracts.GetContent(),
                videoUri -> {
                    if (videoUri != null) {
                        fileUri2 = videoUri;
                        video.setVideoURI(videoUri);
                        video.setForeground(null);
                        video.seekTo(1);
                    }
                });

        addImageButton = findViewById(R.id.imageView10);
        addImageButton.setOnClickListener(v -> openImageFileChooser());
        video.setOnClickListener(x -> openVideoFileChooser());
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

    private void openVideoFileChooser() {
        mGetContent2.launch("video/*");
    }

    private void uploadFile() {
        final boolean[] isImageUploaded = {false};
        final boolean[] isVideoUploaded = {false};

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
                                if (isImageUploaded[0] && isVideoUploaded[0]) {
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

        if (fileUri2 != null) {
            StorageReference riversRef2 = mStorageRef.child("VideosRecipes/" + fileUri2.getLastPathSegment());

            riversRef2.putFile(fileUri2)
                    .addOnSuccessListener(taskSnapshot -> {
                        riversRef2.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                downloadUri2 = task.getResult();
                                videoUrl = downloadUri2.toString();
                                isVideoUploaded[0] = true;

                                if (isImageUploaded[0] && isVideoUploaded[0]) {
                                    uploadRecipe();
                                }
                            }
                        });
                    })
                    .addOnFailureListener(i -> {
                        Log.e("FirebaseStorage", "Error uploading video: " + i.getMessage());
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
        data.put("",0);
        data.put("video_url", videoUrl);
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
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
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
