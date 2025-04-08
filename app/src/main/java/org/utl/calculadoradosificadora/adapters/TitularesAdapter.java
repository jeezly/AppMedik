package org.utl.calculadoradosificadora.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Titular;

import java.util.ArrayList;
import java.util.List;

public class TitularesAdapter extends RecyclerView.Adapter<TitularesAdapter.ViewHolder> {

    private List<Titular> listaTitulares;
    private List<Titular> listaTitularesFiltrada;
    private TitularesAdapter.OnItemClickListener listener;

    public interface  OnItemClickListener {
        void onItemClick(Titular titular);
    }

    public TitularesAdapter(List<Titular> listaTitulares, TitularesAdapter.OnItemClickListener listener) {
        this.listaTitulares = new ArrayList<>(listaTitulares);
        this.listaTitularesFiltrada = new ArrayList<>(listaTitulares);
        this.listener = listener;
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
        holder.bind(titular, listener);
    }

    @Override
    public int getItemCount() {
        return listaTitularesFiltrada.size();
    }

//    public void filtrar(String texto) {
//        listaTitularesFiltrada.clear();
//        if (texto.isEmpty()) {
//            listaTitularesFiltrada.addAll(listaTitulares);
//        } else {
//            for (Titular titular : listaTitulares) {
//                if (titular.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
//                        String.valueOf(titular.getIdTitular()).contains(texto)) {
//                    listaTitularesFiltrada.add(titular);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
    public void updateData(List<Titular> newTitulares) {
        this.listaTitulares = new ArrayList<>(newTitulares);
        this.listaTitularesFiltrada = new ArrayList<>(newTitulares);
        notifyDataSetChanged();
    }

    public void filtrar(String texto, String generoSeleccionado) {
        listaTitularesFiltrada.clear();

        for (Titular titular : listaTitulares) {
            // Filtro por nombre (insensible a mayúsculas/minúsculas)
            boolean coincideNombre = texto == null || texto.isEmpty() ||
                    titular.getNombre().toLowerCase().contains(texto.toLowerCase());

            // Filtro por género usando el método del modelo
            String generoTitularTexto = titular.getGenero();
            boolean coincideGenero = generoSeleccionado == null || generoSeleccionado.equalsIgnoreCase("Todos")
                    || generoTitularTexto.equalsIgnoreCase(generoSeleccionado);

            if (coincideNombre && coincideGenero) {
                listaTitularesFiltrada.add(titular);
            }
        }
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itvClaveTitular;
        TextView itvCNombreTitular;
        TextView itvCorreoTitular;
        TextView itvTelefonoTitular;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itvClaveTitular = itemView.findViewById(R.id.itvClaveTitular);
            itvCNombreTitular = itemView.findViewById(R.id.itvCNombreTitular);
            itvCorreoTitular = itemView.findViewById(R.id.itvCorreoTitular);
            itvTelefonoTitular = itemView.findViewById(R.id.itvTelefonoTitular);
        }

        public void bind(final Titular titular, final OnItemClickListener listener){
            if(titular != null){
                itvClaveTitular.setText("Clave: " + titular.getIdTitular());
                itvCNombreTitular.setText(titular.getNombre() + " " + titular.getApellidos());
                itvCorreoTitular.setText("Correo electrónico: " + titular.getUsuario().getCorreo());
                itvTelefonoTitular.setText("Teléfono: " + titular.getTelefono());
            }else{
                itvCNombreTitular.setText("Clave: XXXXXX");
                itvCNombreTitular.setText("Titular no disponible");
            }

            itemView.setOnClickListener(view ->{
                if(listener != null){
                    listener.onItemClick(titular);
                }
            });
        }
    }
}