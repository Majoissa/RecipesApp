package com.majoissa.yummee;
public class Celda {
    private String documentId;
    private String recipe_name;
    private String img_url;
    private double rating_recipes;
    private String totalReviews;
    private String directions;
    private String ingredients;
    private String video_url;

    public Celda() {
        // Constructor sin argumentos necesario para Firestore
    }
    public Celda(String directions, String ingredients,String recipe_name, String img_url, double rating_recipes, String totalReviews,String video_url) {
        this.directions = directions;
        this.recipe_name = recipe_name;
        this.img_url = img_url;
        this.rating_recipes = rating_recipes;
        this.totalReviews = totalReviews;
        this.ingredients = ingredients;
        this.video_url = video_url;
    }
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getRecipe_name() {
        return recipe_name;
    }

    public String getImg_url() {
        return img_url;
    }
    public String getDirections() {
        return directions;
    }
    public String getIngredients() {
        return ingredients;
    }
    public String getVideo_url() {
        return video_url;
    }
    public double getRating_recipes() {
        return rating_recipes;
    }

    public String getTotalReviews() {
        return totalReviews;
    }
}
