package com.majoissa.yummee;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeTemplate extends AppCompatActivity {

    private StorageReference mStorageRef;
    private Uri fileUri;

    private ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createrecipe_activity);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        fileUri = uri;
                    }
                });

        Button uploadButton = findViewById(R.id.add_image);
        uploadButton.setOnClickListener(v -> openFileChooser());

        Button acceptButton = findViewById(R.id.accept_image);
        acceptButton.setOnClickListener(v -> {
            if (fileUri != null) {
                uploadFile();
            } else {
                Log.e("FirebaseStorage", "No file selected");
            }
        });
    }

    private void openFileChooser() {
        mGetContent.launch("image/*");
    }

    private void uploadFile() {
        if (fileUri != null) {
            StorageReference riversRef = mStorageRef.child("ImagesRecipes/" + fileUri.getLastPathSegment());

            riversRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d("FirebaseStorage", "Image uploaded successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseStorage", "Error uploading image: " + e.getMessage());
                    });
        } else {
            Log.e("FirebaseStorage", "No file selected");
        }
    }
}
