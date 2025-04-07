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
        private TextView tvEstatus;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvPaciente = itemView.findViewById(R.id.tvPaciente);
            tvEstatus = itemView.findViewById(R.id.tvEstatus);
        }

        public void bind(final Cita cita, final OnItemClickListener listener) {
            // Formatear fecha si es necesario (ejemplo: de "2023-12-31" a "31/12/2023")
            String fechaFormateada = formatFecha(cita.getFecha());
            tvFecha.setText(fechaFormateada);

            tvHora.setText(cita.getHora());

            if (cita.getPaciente() != null && cita.getPaciente().getPersona() != null) {
                String nombrePaciente = cita.getPaciente().getPersona().getNombre() + " " +
                        cita.getPaciente().getPersona().getApellidos();
                tvPaciente.setText(nombrePaciente);
            } else {
                tvPaciente.setText("Paciente no disponible");
            }

            if (cita.getEstatus() != null) {
                tvEstatus.setText(cita.getEstatus());

                // Cambiar color según estatus
                switch (cita.getEstatus().toLowerCase()) {
                    case "programada":
                        tvEstatus.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        break;
                    case "cancelada":
                        tvEstatus.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                        break;
                    case "atendida":
                        tvEstatus.setTextColor(itemView.getContext().getResources().getColor(R.color.colorGreen));
                        break;
                    case "confirmada":
                        tvEstatus.setTextColor(itemView.getContext().getResources().getColor(R.color.colorBlue));
                        break;
                    default:
                        tvEstatus.setTextColor(itemView.getContext().getResources().getColor(R.color.colorBlack));
                }
            } else {
                tvEstatus.setText("Sin estatus");
                tvEstatus.setTextColor(itemView.getContext().getResources().getColor(R.color.colorBlack));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(cita);
                }
            });
        }

        private String formatFecha(String fechaOriginal) {
            try {
                // Si la fecha viene en formato "yyyy-MM-dd"
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