package com.majoissa.yummee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Buttons {
    private Context context; // Agrega un campo para el contexto

    public Buttons(Context context) {
        this.context = context;
    }

    public void HomeButton(View v) {
        Intent i = new Intent(context, Main.class);
        context.startActivity(i);
    }

    public void UserButton(View v){
        Intent i = new Intent(context, UserActivity.class);
        context.startActivity(i);
    }
}
