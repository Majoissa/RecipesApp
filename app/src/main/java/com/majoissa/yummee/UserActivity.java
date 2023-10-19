package com.majoissa.yummee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    private Buttons buttons;
    private ImageButton home;
    private ImageButton logout;
    private ImageButton back;
    private TextView userName;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.textView5);
        logout = findViewById(R.id.imageButton2);
        home = findViewById(R.id.imageButton4);
        back = findViewById(R.id.imageButton);

        buttons = new Buttons(this);

        String email = mAuth.getCurrentUser().getEmail();
        userName.setText(email);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttons.HomeButton(view);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.setEnabled(false);
                onButtonShowPopup(view);
            }
        });
    }

    public void onButtonShowPopup(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_logout, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button btn1 = popupView.findViewById(R.id.button5);
        Button btn2 = popupView.findViewById(R.id.button6);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                buttons.LogoutButton(view);
                logout.setEnabled(true);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                logout.setEnabled(true);
            }
        });
    }
}