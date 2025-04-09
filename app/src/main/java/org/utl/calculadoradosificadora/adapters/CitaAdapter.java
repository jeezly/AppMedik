package org.utl.calculadoradosificadora.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Cita;

import java.util.List;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {

    private List<Cita> citas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cita cita);
    }

    public CitaAdapter(List<Cita> citas, OnItemClickListener listener) {
        this.citas = citas;
        this.listener = listener;
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
        holder.bind(cita, listener);
    }

    @Override
    public int getItemCount() {
        return citas != null ? citas.size() : 0;
    }

    public void updateData(List<Cita> newCitas) {
        citas = newCitas;
        notifyDataSetChanged();
    }

    static class CitaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFecha;
        private TextView tvHora;
        private TextView tvPaciente;
        private TextView tvTitular;
        private TextView tvEstatus;
        private TextView tvNota;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvPaciente = itemView.findViewById(R.id.tvPaciente);
            tvTitular = itemView.findViewById(R.id.tvTitular);
            tvEstatus = itemView.findViewById(R.id.tvEstatus);
            tvNota = itemView.findViewById(R.id.tvNota);
        }

        public void bind(final Cita cita, final OnItemClickListener listener) {
            tvFecha.setText("Fecha: " + formatFecha(cita.getFecha()));
            tvHora.setText("Hora: " + cita.getHora());

            // Mostrar información del paciente
            if (cita.getPaciente() != null && cita.getPaciente().getPersona() != null) {
                tvPaciente.setText("Paciente: " +
                        cita.getPaciente().getPersona().getNombre() + " " +
                        cita.getPaciente().getPersona().getApellidos());
            } else {
                tvPaciente.setText("Paciente: No disponible");
            }

            // Mostrar información del titular
            if (cita.getTitular() != null && cita.getTitular().getPersona() != null) {
                tvTitular.setText("Titular: " +
                        cita.getTitular().getPersona().getNombre() + " " +
                        cita.getTitular().getPersona().getApellidos());
            } else {
                tvTitular.setText("Titular: No disponible");
            }

            // Mostrar estatus y nota si está atendida
            if (cita.getEstatus() != null) {
                String estatusText = "Estatus: " + cita.getEstatus();
                if ("Atendida".equalsIgnoreCase(cita.getEstatus())) {
                    estatusText += "\nNota: " + (cita.getNota() != null ?
                            (cita.getNota().length() > 20 ?
                                    cita.getNota().substring(0, 20) + "..." :
                                    cita.getNota()) : "Sin notas");
                }
                tvEstatus.setText(estatusText);
            } else {
                tvEstatus.setText("Sin estatus");
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(cita);
                }
            });
        }

        private String formatFecha(String fechaOriginal) {
            try {
                String[] partes = fechaOriginal.split("-");
                if (partes.length == 3) {
                    return partes[2] + "/" + partes[1] + "/" + partes[0];
                }
                return fechaOriginal;
            } catch (Exception e) {
                return fechaOriginal;
            }
        }
    }
}