package org.utl.calculadoradosificadora.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.Acciones.DetallesTitularActivity;
import org.utl.calculadoradosificadora.model.Titular;

import java.util.ArrayList;
import java.util.List;

public class TitularesAdapter extends RecyclerView.Adapter<TitularesAdapter.ViewHolder> {

    private List<Titular> listaTitulares;
    private List<Titular> listaTitularesFiltrada;

    public TitularesAdapter(List<Titular> listaTitulares) {
        this.listaTitulares = listaTitulares;
        this.listaTitularesFiltrada = new ArrayList<>(listaTitulares);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_titular, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Titular titular = listaTitularesFiltrada.get(position);
        holder.tvClaveNombre.setText("Clave: " + titular.getIdTitular() + " - Nombre: " + titular.getNombre() + " " + titular.getApellidos());

        // Manejar clic en el elemento
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetallesTitularActivity.class);
            intent.putExtra("titular", titular); // Pasar el titular seleccionado
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaTitularesFiltrada.size();
    }

    public void filtrar(String texto) {
        listaTitularesFiltrada.clear();
        if (texto.isEmpty()) {
            listaTitularesFiltrada.addAll(listaTitulares);
        } else {
            for (Titular titular : listaTitulares) {
                if (titular.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        String.valueOf(titular.getIdTitular()).contains(texto)) {
                    listaTitularesFiltrada.add(titular);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvClaveNombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClaveNombre = itemView.findViewById(R.id.tvClaveNombreTitular);
        }
    }
}