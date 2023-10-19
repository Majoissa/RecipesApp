package com.majoissa.yummee;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUtilities {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static void loginUser(String email, String password, FirebaseLoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public static void registerUser(String email, String password, FirebaseRegisterCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public interface FirebaseLoginCallback {
        void onSuccess();
        void onError(Exception exception);
    }

    public interface FirebaseRegisterCallback {
        void onSuccess();
        void onError(Exception exception);
    }
}
