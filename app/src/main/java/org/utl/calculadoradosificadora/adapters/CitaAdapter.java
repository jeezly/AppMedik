package org.utl.calculadoradosificadora.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.R;
import java.util.List;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {

    private List<Cita> citas;

    public CitaAdapter(List<Cita> citas) {
        this.citas = citas;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cita, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = citas.get(position);
        holder.tvFecha.setText(cita.getFecha());
        holder.tvHora.setText(cita.getHora());
        holder.tvPaciente.setText(cita.getPaciente().getNombre());
        holder.tvEstatus.setText(cita.getEstatus());
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    public static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvHora, tvPaciente, tvEstatus;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvPaciente = itemView.findViewById(R.id.tvPaciente);
            tvEstatus = itemView.findViewById(R.id.tvEstatus);
        }
    }
}