package com.majoissa.yummee;

public class Recipes {
    private String directions;
    private String img_url;
    private String ingredients;
    private int rating_recipes;
    private String recipe_name;
    private String video_url;

    public Recipes() {
        // Constructor vacío necesario para Firebase
    }

    public Recipes(String directions, String img_url, String ingredients, int rating_recipes, String recipe_name, String video_url) {
        this.directions = directions;
        this.img_url = img_url;
        this.ingredients = ingredients;
        this.rating_recipes = rating_recipes;
        this.recipe_name = recipe_name;
        this.video_url = video_url;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getRating_recipes() {
        return rating_recipes;
    }

    public void setRating_recipes(int rating_recipes) {
        this.rating_recipes = rating_recipes;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}

