package org.utl.calculadoradosificadora.VistaTitular.DialogFragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaTitular.DetallesHistorialCitaActivity;
import org.utl.calculadoradosificadora.VistaTitular.HistorialCitaActivity;

public class MoficiarCita_DialogFragment extends DialogFragment{

    EditText txtDate;
    Spinner spinnerHorarioM;
    Button btnCancelarReagendar;
    Button btnCancelarCitaT;
    Button btnModificarCitaT;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflar el layout personalizado
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.reagendar_cita_titular, null);

//        // Obtener referencia al EditText
//        EditText etInput = view.findViewById(R.id.etInput);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view)
                .setTitle("Reagendar Cita")
                .setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo lo de la api
                        mensajeConfirmación();
                    }
                })
                .setNegativeButton("Cancelar", null);

        return builder.create();
    }

    private void mensajeConfirmación() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cita Reagendada");
        builder.setMessage("La cita a sido reagendada con éxito");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), HistorialCitaActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

