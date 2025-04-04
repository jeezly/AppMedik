package org.utl.calculadoradosificadora.VistaTitular;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaTitular.DialogFragment.MoficiarCita_DialogFragment;

public class DetallesHistorialCitaActivity extends AppCompatActivity {
    ImageView menu_icon;
    ImageView options_icon;
    TextView tcUsuario;
    TextView tcFecha;
    TextView tcHora;
    TextView dcNombreMedico;
    TextView dcCedulaMedico;
    TextView dcGeneroMedico;
    Button btnCancelarCitaT;
    Button btnModificarCitaT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles_historial_cita_titular);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnModificarCitaT = findViewById(R.id.btnModificarCitaT);
        btnCancelarCitaT = findViewById(R.id.btnModificarCitaT);
        btnCancelarCitaT.setOnClickListener(view ->{
            mostrarDialogoCancelar();
        });
        btnModificarCitaT.setOnClickListener(view -> {
            MoficiarCita_DialogFragment dialog = new MoficiarCita_DialogFragment();
            dialog.show(getSupportFragmentManager(), "customDialog");
        });
    }

    private void mostrarDialogoCancelar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancelar Cita");
        builder.setMessage("¿Estás seguro de querer cancelar la cita?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción al presionar "Sí"
                mostrarDialogoCancelarConfirmacion();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción al presionar "No"
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void mostrarDialogoCancelarConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancelar Cita");
        builder.setMessage("La cita a sido cancelada con éxito");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(DetallesHistorialCitaActivity.this, HistorialCitaActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}