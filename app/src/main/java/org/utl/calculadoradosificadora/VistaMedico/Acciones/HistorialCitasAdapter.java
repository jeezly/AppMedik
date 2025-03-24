package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Cita;
import java.util.ArrayList;
import java.util.List;

public class HistorialCitasAdapter extends RecyclerView.Adapter<HistorialCitasAdapter.ViewHolder> {

    private List<Cita> listaCitas;
    private List<Cita> listaCitasFiltrada;

    public HistorialCitasAdapter(List<Cita> listaCitas) {
        this.listaCitas = listaCitas;
        this.listaCitasFiltrada = new ArrayList<>(listaCitas);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial_cita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cita cita = listaCitasFiltrada.get(position);
        holder.tvClavePaciente.setText("Clave: " + cita.getPaciente().getIdPaciente());
        holder.tvNombrePaciente.setText("Nombre: " + cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos());
        holder.tvFecha.setText("Fecha: " + cita.getFecha());
        holder.tvEstatus.setText("Estatus: " + cita.getEstatus());

        // Manejar clic en el elemento
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetallesHistorialCitaActivity.class);
            intent.putExtra("cita", cita); // Pasar la cita seleccionada
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaCitasFiltrada.size();
    }

    public void filtrar(String texto) {
        listaCitasFiltrada.clear();
        if (texto.isEmpty()) {
            listaCitasFiltrada.addAll(listaCitas);
        } else {
            for (Cita cita : listaCitas) {
                if (cita.getPaciente().getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        cita.getFecha().toLowerCase().contains(texto.toLowerCase()) ||
                        cita.getEstatus().toLowerCase().contains(texto.toLowerCase())) {
                    listaCitasFiltrada.add(cita);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvClavePaciente, tvNombrePaciente, tvFecha, tvEstatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClavePaciente = itemView.findViewById(R.id.tvClavePaciente);
            tvNombrePaciente = itemView.findViewById(R.id.tvNombrePaciente);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEstatus = itemView.findViewById(R.id.tvEstatus);
        }
    }
}