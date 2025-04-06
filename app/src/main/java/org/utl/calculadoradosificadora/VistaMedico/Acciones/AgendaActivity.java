package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.adapters.CitaAdapter;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaActivity extends AppCompatActivity {

    private ListView listCitas;
    private Button btnAgregarCita, btnRegresar;
    private CitaAdapter citaAdapter;
    private List<Cita> citasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        listCitas = findViewById(R.id.listCitas);
        btnAgregarCita = findViewById(R.id.btnAgregarCita);
        btnRegresar = findViewById(R.id.btnRegresar);

        cargarCitas();

        btnAgregarCita.setOnClickListener(v -> {
            startActivity(new Intent(this, AgregarCitaActivity.class));
        });

        btnRegresar.setOnClickListener(v -> finish());

        listCitas.setOnItemClickListener((parent, view, position, id) -> {
            Cita citaSeleccionada = citasList.get(position);
            abrirDetallesCita(citaSeleccionada);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCitas();
    }

    private void cargarCitas() {
        CitaService service = ApiClient.getClient().create(CitaService.class);
        service.getAllCitas().enqueue(new Callback<ApiResponse<List<Cita>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Cita>>> call, Response<ApiResponse<List<Cita>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    citasList = response.body().getData();
                    mostrarCitas();
                } else {
                    String errorMsg = response.body() != null ?
                            response.body().getMessage() : "Error al cargar citas";
                    Toast.makeText(AgendaActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Error: " + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Cita>>> call, Throwable t) {
                Toast.makeText(AgendaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Error en getAllCitas", t);
            }
        });
    }

    private void mostrarCitas() {
        if (citasList != null && !citasList.isEmpty()) {
            citaAdapter = new CitaAdapter(citasList);
            listCitas.setAdapter((ListAdapter) citaAdapter);
        } else {
            Toast.makeText(this, "No hay citas programadas", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirDetallesCita(Cita cita) {
        Intent intent = new Intent(this, DetallesCitaActivity.class);
        intent.putExtra("idCita", cita.getIdCita());
        intent.putExtra("fecha", cita.getFecha());
        intent.putExtra("hora", cita.getHora());
        intent.putExtra("razon", cita.getRazonCita());
        intent.putExtra("estatus", cita.getEstatus());

        if (cita.getTitular() != null) {
            intent.putExtra("nombreTitular", cita.getTitular().getNombre() + " " + cita.getTitular().getApellidos());
            intent.putExtra("genero", cita.getTitular().getGenero());
            intent.putExtra("correo", cita.getTitular().getCorreo());
            intent.putExtra("telefono", cita.getTitular().getTelefono());
        }

        if (cita.getPaciente() != null) {
            intent.putExtra("nombrePaciente", cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos());
            intent.putExtra("edadPaciente", calcularEdad(cita.getPaciente().getFechaNacimiento()));
            intent.putExtra("pesoPaciente", String.valueOf(cita.getPaciente().getPeso()));
        }

        startActivity(intent);
    }

    private String calcularEdad(String fechaNacimiento) {
        // Implementación simplificada
        return "5 años";
    }
}