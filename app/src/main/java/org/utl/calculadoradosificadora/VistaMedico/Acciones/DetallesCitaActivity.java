package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
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

        initViews();
        loadCitaData();
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
        Cita cita = (Cita) getIntent().getSerializableExtra("cita");
        if (cita != null) {
            idCita = cita.getIdCita();
            tvFecha.setText("Fecha: " + cita.getFecha());
            tvHora.setText("Hora: " + cita.getHora());
            tvRazon.setText("Razón: " + cita.getRazonCita());

            if (cita.getTitular() != null) {
                tvNombreTitular.setText("Titular: " + cita.getTitular().getNombre() + " " + cita.getTitular().getApellidos());
                tvGenero.setText("Género: " + cita.getTitular().getGenero());
                tvCorreo.setText("Correo: " + cita.getTitular().getCorreo());
                tvTelefono.setText("Teléfono: " + cita.getTitular().getTelefono());
            }

            if (cita.getPaciente() != null) {
                tvNombrePaciente.setText("Paciente: " + cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos());
                tvEdadPaciente.setText("Edad: " + calcularEdad(cita.getPaciente().getFechaNacimiento()));
                tvPesoPaciente.setText("Peso: " + cita.getPaciente().getPeso() + " kg");
            }
        }
    }

    private String calcularEdad(String fechaNacimiento) {
        // Implementa tu lógica para calcular la edad
        return "5 años"; // Ejemplo simplificado
    }

    private void setupButtons() {
        btnReagendar.setOnClickListener(v -> {
            // Implementar lógica de reagendación
            Toast.makeText(this, "Reagendar cita", Toast.LENGTH_SHORT).show();
        });

        btnCancelar.setOnClickListener(v -> cancelarCita());

        btnAtender.setOnClickListener(v -> {
            Intent intent = new Intent(DetallesCitaActivity.this, AtenderCitaActivity.class);
            intent.putExtra("idCita", idCita);
            startActivity(intent);
        });

        btnRegresar.setOnClickListener(v -> finish());
    }

    private void cancelarCita() {
        CitaService service = ApiClient.getClient().create(CitaService.class);
        service.cancelarCita(idCita).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(DetallesCitaActivity.this, "Cita cancelada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorMsg = response.body() != null ?
                            response.body().getMessage() : "Error al cancelar cita";
                    Toast.makeText(DetallesCitaActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(DetallesCitaActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Error en cancelarCita", t);
            }
        });
    }
}