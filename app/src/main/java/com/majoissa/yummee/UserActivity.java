package com.majoissa.yummee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class UserActivity extends AppCompatActivity {

    private Buttons buttons;
    private ImageButton home;
    private ImageButton logout;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        logout = findViewById(R.id.imageButton2);
        home = findViewById(R.id.imageButton4);
        back = findViewById(R.id.imageButton);

        buttons = new Buttons(this);

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
    }
    public void LogOut(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}