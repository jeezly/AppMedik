package org.utl.calculadoradosificadora.VistaTitular;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class AgendarCitasDosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_citas_dos);

        // Obtener los datos del médico seleccionado desde el Intent
        Intent intent = getIntent();
        String nombreDoctor = intent.getStringExtra("nombreDoctor");
        String generoDoctor = intent.getStringExtra("generoDoctor");
        String cedulaDoctor = intent.getStringExtra("cedulaDoctor");
        int fotoDoctor = intent.getIntExtra("fotoDoctor", R.drawable.ic_user_doctor_male);

        // Configurar los datos del médico seleccionado
        ImageView ivFotoDoctor = findViewById(R.id.ivFotoDoctor);
        TextView tvNombreDoctor = findViewById(R.id.tvNombreDoctor);
        TextView tvGeneroDoctor = findViewById(R.id.tvGeneroDoctor);
        TextView tvCedulaDoctor = findViewById(R.id.tvCedulaDoctor);

        ivFotoDoctor.setImageResource(fotoDoctor);
        tvNombreDoctor.setText(nombreDoctor);
        tvGeneroDoctor.setText("Género: " + generoDoctor);
        tvCedulaDoctor.setText("Cédula: " + cedulaDoctor);

        // Configurar el botón de Siguiente
        Button btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la razón de la cita
                EditText etRazonCita = findViewById(R.id.etRazonCita);
                String razonCita = etRazonCita.getText().toString().trim();

                // Obtener la fecha seleccionada
                DatePicker datePicker = findViewById(R.id.datePicker);
                int dia = datePicker.getDayOfMonth();
                int mes = datePicker.getMonth() + 1;  // Los meses comienzan en 0
                int anio = datePicker.getYear();
                String fechaCita = dia + "/" + mes + "/" + anio;

                // Validar que la razón de la cita no esté vacía
                if (razonCita.isEmpty()) {
                    etRazonCita.setError("Por favor, escribe la razón de la cita");
                    return;
                }

                // Pasar los datos a la siguiente actividad (AgendarCitasTresActivity)
                Intent intent = new Intent(AgendarCitasDosActivity.this, AgendarCitasTresActivity.class);
                intent.putExtra("nombreDoctor", nombreDoctor);
                intent.putExtra("generoDoctor", generoDoctor);
                intent.putExtra("cedulaDoctor", cedulaDoctor);
                intent.putExtra("fotoDoctor", fotoDoctor);
                intent.putExtra("razonCita", razonCita);
                intent.putExtra("fechaCita", fechaCita);
                startActivity(intent);
            }
        });

        // Configurar el botón de Cancelar Cita
        Button btnCancelarCita = findViewById(R.id.btnCancelarCita);
        btnCancelarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresar a la actividad anterior
            }
        });
    }
}