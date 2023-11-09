package com.majoissa.yummee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Buttons {
    private static Context context; // Agrega un campo para el contexto

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

    public void LogoutButton(View v){
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }
    public static void RecipeDetails(View v){
        context.startActivity(new Intent(context, RecipesDetailsActivity.class));
    }
    public void CreateRecipe(View v){
        context.startActivity(new Intent(context, RecipeTemplate.class));
    }
    public void FavoriteButton(View v){
        context.startActivity(new Intent(context, FavoritesActivity.class));
    }
}
