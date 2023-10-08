package com.majoissa.yummee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majoissa.yummee.CeldaMessage;

import java.util.List;

public class CeldaAdapterMessage extends RecyclerView.Adapter<CeldaAdapterMessage.ViewHolder> {

    private final List<CeldaMessage> celdas;

    public CeldaAdapterMessage(List<CeldaMessage> celdas) {
        this.celdas = celdas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_comment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CeldaMessage celda = celdas.get(position);

        // Actualiza la vista de acuerdo a la clase CeldaMessage
        //holder.imageId.setImageDrawable(celda.imageId.getDrawable());
        holder.user.setText(celda.user);
        holder.ratingBar.setRating(celda.rating);
        holder.message.setText(celda.message);
    }

    @Override
    public int getItemCount() {
        return celdas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageId;
        TextView user;
        RatingBar ratingBar;
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageId = itemView.findViewById(R.id.profileimg);
            user = itemView.findViewById(R.id.user);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            message = itemView.findViewById(R.id.coment);
        }
    }
}
