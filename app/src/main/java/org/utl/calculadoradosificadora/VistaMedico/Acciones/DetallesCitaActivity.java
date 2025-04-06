package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.CitaService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesCitaActivity extends AppCompatActivity {

    private TextView tvFecha, tvHora, tvRazon, tvNombreTitular, tvGenero, tvCorreo, tvTelefono;
    private TextView tvNombrePaciente, tvEdadPaciente, tvPesoPaciente;
    private Button btnReagendar, btnCancelar, btnAtender, btnRegresar;
    private int idCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cita);

        // Inicializar vistas
        initViews();

        // Obtener datos de la cita desde el Intent
        loadCitaData();

        // Configurar botones
        setupButtons();
    }

    private void initViews() {
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvRazon = findViewById(R.id.tvRazon);
        tvNombreTitular = findViewById(R.id.tvNombreTitular);
        tvGenero = findViewById(R.id.tvGenero);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvNombrePaciente = findViewById(R.id.tvNombrePaciente);
        tvEdadPaciente = findViewById(R.id.tvEdadPaciente);
        tvPesoPaciente = findViewById(R.id.tvPesoPaciente);
        btnReagendar = findViewById(R.id.btnReagendar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnAtender = findViewById(R.id.btnAtender);
        btnRegresar = findViewById(R.id.btnRegresar);
    }

    private void loadCitaData() {
        Intent intent = getIntent();
        if (intent != null) {
            idCita = intent.getIntExtra("idCita", -1);
            tvFecha.setText("Fecha: " + intent.getStringExtra("fecha"));
            tvHora.setText("Hora: " + intent.getStringExtra("hora"));
            tvRazon.setText("Razón: " + intent.getStringExtra("razon"));

            if (intent.hasExtra("nombreTitular")) {
                tvNombreTitular.setText("Titular: " + intent.getStringExtra("nombreTitular"));
                tvGenero.setText("Género: " + intent.getStringExtra("genero"));
                tvCorreo.setText("Correo: " + intent.getStringExtra("correo"));
                tvTelefono.setText("Teléfono: " + intent.getStringExtra("telefono"));
            }

            if (intent.hasExtra("nombrePaciente")) {
                tvNombrePaciente.setText("Paciente: " + intent.getStringExtra("nombrePaciente"));
                tvEdadPaciente.setText("Edad: " + intent.getStringExtra("edadPaciente"));
                tvPesoPaciente.setText("Peso: " + intent.getStringExtra("pesoPaciente") + " kg");
            }
        }
    }

    private void setupButtons() {
        btnReagendar.setOnClickListener(v -> {
            // Implementar lógica de reagendación
            Toast.makeText(this, "Reagendar cita", Toast.LENGTH_SHORT).show();
        });

        btnCancelar.setOnClickListener(v -> cancelarCita());

        btnAtender.setOnClickListener(v -> {
            Intent intent = new Intent(DetallesCitaActivity.this, AtenderCitaActivity.class);

            // Pasar datos del paciente
            intent.putExtra("nombrePaciente", tvNombrePaciente.getText().toString().replace("Paciente: ", ""));
            intent.putExtra("edadPaciente", tvEdadPaciente.getText().toString().replace("Edad: ", ""));
            intent.putExtra("pesoPaciente", tvPesoPaciente.getText().toString().replace("Peso: ", "").replace(" kg", ""));

            startActivity(intent);
        });

        btnRegresar.setOnClickListener(v -> finish());
    }

    private void cancelarCita() {
        CitaService service = ApiClient.getClient().create(CitaService.class);
        service.cancelarCita(idCita).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetallesCitaActivity.this, "Cita cancelada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetallesCitaActivity.this, "Error al cancelar cita", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetallesCitaActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}