package org.utl.calculadoradosificadora.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.utl.calculadoradosificadora.R;

import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder> {

    private List<String> horarios;
    private int selectedPosition = -1;

    public HorarioAdapter(List<String> horarios) {
        this.horarios = horarios;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder holder, int position) {
        String horario = horarios.get(position);
        holder.tvHorario.setText(horario);

        // Cambiar el fondo si está seleccionado
        holder.itemView.setBackgroundResource(selectedPosition == position ? R.color.colorGreen : android.R.color.white);
        holder.tvHorario.setTextColor(selectedPosition == position ? android.R.color.white : android.R.color.black);

        // Manejar la selección de un horario
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public String getHorarioSeleccionado() {
        if (selectedPosition != -1) {
            return horarios.get(selectedPosition);
        }
        return null;
    }

    public static class HorarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvHorario;

        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHorario = itemView.findViewById(R.id.tvHorario);
        }
    }
}