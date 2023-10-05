package com.majoissa.yummee;

import android.widget.RatingBar;

public class CeldaMessage {
    //public int imageId;
    public String user;
    public float rating;
    public String message;

    //private int imagenResId;

    public CeldaMessage(String user, float rating, String message) {
        //this.imageId = imageId;
        this.user = user;
        this.rating = rating;
        this.message = message;
        // this.imagenResId = imagenResId;
    }
}
