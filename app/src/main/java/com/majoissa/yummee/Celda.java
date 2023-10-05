package com.majoissa.yummee;
public class Celda {
    private String texto;
    private int imagenResId;
    private float imagenResId2;
    private String texto2;

    public Celda(String texto, int imagenResId, float imagenResId2, String texto2) {
        this.texto = texto;
        this.imagenResId = imagenResId;
        this.imagenResId2 = imagenResId2;
        this.texto2 = texto2;
    }

    public String getTexto() {
        return texto;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public float getImagenResId2() {
        return imagenResId2;
    }

    public String getTexto2() {
        return texto2;
    }
}