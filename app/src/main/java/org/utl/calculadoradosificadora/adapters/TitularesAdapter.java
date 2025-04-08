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
    private TitularesAdapter.OnItemClickListener listener;
    private List<Titular> listaTitularesFiltrada;

    public interface  OnItemClickListener {
        void onItemClick(Titular titular);
    }

    public TitularesAdapter(List<Titular> listaTitulares, TitularesAdapter.OnItemClickListener listener) {
        this.listaTitulares = listaTitulares;
//        this.listaTitularesFiltrada = new ArrayList<>(listaTitulares);
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
        Titular titular = listaTitulares.get(position);
        holder.bind(titular, listener);
        // Manejar clic en el elemento
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), DetallesTitularActivity.class);
//            intent.putExtra("titular", titular); // Pasar el titular seleccionado
//            v.getContext().startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return listaTitulares.size();
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
        listaTitulares = newTitulares;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itvClaveTitular;
        TextView itvCNombreTitular;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itvClaveTitular = itemView.findViewById(R.id.itvClaveTitular);
            itvCNombreTitular = itemView.findViewById(R.id.itvCNombreTitular);
        }

        public void bind(final Titular titular, final OnItemClickListener listener){
            if(titular != null){
                itvClaveTitular.setText("Clave: " + titular.getIdTitular());
                itvCNombreTitular.setText(titular.getNombre() + " " + titular.getApellidos());
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