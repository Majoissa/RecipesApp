package com.majoissa.yummee;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Importa Glide
import java.util.List;

public class CeldaAdapter extends RecyclerView.Adapter<CeldaAdapter.ViewHolder> {

    private final List<Celda> celdas;

    public CeldaAdapter(List<Celda> celdas) {
        this.celdas = celdas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Celda celda = celdas.get(position);
        holder.titul.setText(celda.getRecipe_name());
        holder.valoracio.setRating(celda.getRating_recipes());
        holder.totalreviews.setText(celda.getTotalReviews());

        // Carga la imagen desde la URL utilizando Glide
        Glide.with(holder.imatge.getContext())
                .load(celda.getImg_url()) // La URL de la imagen
                .into(holder.imatge);
    }

    @Override
    public int getItemCount() {
        return celdas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imatge;
        TextView titul;
        RatingBar valoracio;
        TextView totalreviews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imatge = itemView.findViewById(R.id.imatge);
            titul = itemView.findViewById(R.id.titul);
            valoracio = itemView.findViewById(R.id.valoracio);
            totalreviews = itemView.findViewById(R.id.totalreviews);
        }
    }
}
