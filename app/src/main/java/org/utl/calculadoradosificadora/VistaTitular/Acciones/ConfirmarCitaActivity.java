package org.utl.calculadoradosificadora.VistaTitular.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaTitular.VistaTitular;

public class ConfirmarCitaActivity extends AppCompatActivity {

    private TextView tvFecha, tvHora, tvNombreDoctor, tvCedulaDoctor, tvGeneroDoctor, tvRazonCita;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_cita);

        // Obtener referencias de las vistas
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvNombreDoctor = findViewById(R.id.tvNombreDoctor);
        tvCedulaDoctor = findViewById(R.id.tvCedulaDoctor);
        tvGeneroDoctor = findViewById(R.id.tvGeneroDoctor);

        btnOk = findViewById(R.id.btnOk);

        // Recibir datos de la actividad anterior
        Intent intent = getIntent();
        String fechaCita = intent.getStringExtra("fechaCita");
        String horarioSeleccionado = intent.getStringExtra("horarioSeleccionado");
        String nombreDoctor = intent.getStringExtra("nombreDoctor");
        String cedulaDoctor = intent.getStringExtra("cedulaDoctor");
        String generoDoctor = intent.getStringExtra("generoDoctor");
        String razonCita = intent.getStringExtra("razonCita");

        // Mostrar datos en la vista
        tvFecha.setText(fechaCita);
        tvHora.setText(horarioSeleccionado);
        tvNombreDoctor.setText("Nombre: " + nombreDoctor);
        tvCedulaDoctor.setText("Cédula: " + cedulaDoctor);
        tvGeneroDoctor.setText("Género: " + generoDoctor);
        tvRazonCita.setText("Razón de la cita: " + razonCita);

        // Acción para el botón OK
        btnOk.setOnClickListener(v -> {
            Intent mainIntent = new Intent(ConfirmarCitaActivity.this, VistaTitular.class);
            startActivity(mainIntent);
            finish(); // Cierra esta actividad
        });
    }
}