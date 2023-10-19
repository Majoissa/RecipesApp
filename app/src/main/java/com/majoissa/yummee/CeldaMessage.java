package com.majoissa.yummee;

import android.widget.RatingBar;

import java.util.Date;

public class CeldaMessage {
    //public int imageId;
    public String user;
    public float rating;
    public String message;
    public String date;

    //private int imagenResId;

        public CeldaMessage(String user, float rating, String message, String date) {
            //this.imageId = imageId;
            this.user = user;
            this.rating = rating;
            this.message = message;
            this.date = date;
            // this.imagenResId = imagenResId;
    }
}