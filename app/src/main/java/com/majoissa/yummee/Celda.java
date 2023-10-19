package com.majoissa.yummee;
public class Celda {
    private String recipe_name;
    private String img_url;
    private double rating_recipes;
    private String totalReviews;

    public Celda() {
<<<<<<< Updated upstream
        // Constructor sin argumentos necesario para Firestore
    }

    public Celda(String recipe_name, String img_url, double rating_recipes, String totalReviews) {
        this.recipe_name = recipe_name;
        this.img_url = img_url;
        this.rating_recipes = rating_recipes;
        this.totalReviews = totalReviews;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public double getRating_recipes() {
        return rating_recipes;
=======

    }
    public Celda(String recipe_name, String img_url, double rating_recipes, String totalReviews) {
        this.recipe_name = recipe_name;
        this.img_url = img_url;
        this.rating_recipes = rating_recipes;
        this.totalReviews = totalReviews;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public double getRating_recipes() {
        return rating_recipes;
    }

    public String getTotalReviews() {
        return totalReviews;
>>>>>>> Stashed changes
    }

    public String getTotalReviews() {
        return totalReviews;
    }
}
