package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Medicamento;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.CitaService;
import org.utl.calculadoradosificadora.service.MedicamentoService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtenderCitaActivity extends AppCompatActivity {

    private Spinner spinnerMedicamentos;
    private EditText etPeso;
    private Button btnCalcular, btnCancelar, btnFinalizarConsulta;
    private TextView tvResultado, tvNombrePaciente, tvEdadPaciente, tvPesoPaciente;
    private List<Medicamento> listaMedicamentos;
    private int idCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atender_cita);

        // Inicializar vistas
        initViews();

        // Cargar datos del paciente
        cargarDatosPaciente();

        // Cargar medicamentos
        cargarMedicamentos();
    }

    private void initViews() {
        spinnerMedicamentos = findViewById(R.id.spinnerMedicamentos);
        etPeso = findViewById(R.id.etPeso);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnFinalizarConsulta = findViewById(R.id.btnFinalizarConsulta);
        tvResultado = findViewById(R.id.tvResultado);
        tvNombrePaciente = findViewById(R.id.tvNombrePaciente);
        tvEdadPaciente = findViewById(R.id.tvEdadPaciente);
        tvPesoPaciente = findViewById(R.id.tvPesoPaciente);
    }

    private void cargarDatosPaciente() {
        Intent intent = getIntent();
        if (intent != null) {
            idCita = intent.getIntExtra("idCita", -1);
            tvNombrePaciente.setText("Nombre: " + intent.getStringExtra("nombrePaciente"));
            tvEdadPaciente.setText("Edad: " + intent.getStringExtra("edadPaciente"));
            tvPesoPaciente.setText("Peso: " + intent.getStringExtra("pesoPaciente") + " kg");
            etPeso.setText(intent.getStringExtra("pesoPaciente"));
        } else {
            Toast.makeText(this, "No se recibieron datos del paciente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarMedicamentos() {
        MedicamentoService service = ApiClient.getClient().create(MedicamentoService.class);
        service.getAllMedicamento().enqueue(new Callback<List<Medicamento>>() {
            @Override
            public void onResponse(Call<List<Medicamento>> call, Response<List<Medicamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaMedicamentos = response.body();
                    setupSpinners();
                } else {
                    Toast.makeText(AtenderCitaActivity.this, "Error al cargar medicamentos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Medicamento>> call, Throwable t) {
                Toast.makeText(AtenderCitaActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<Medicamento> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaMedicamentos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicamentos.setAdapter(adapter);

        // Configurar botones
        btnCalcular.setOnClickListener(v -> calcularDosis());
        btnCancelar.setOnClickListener(v -> resetCalculadora());
        btnFinalizarConsulta.setOnClickListener(v -> finalizarConsulta());
    }

    private void calcularDosis() {
        if (etPeso.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese el peso del paciente", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Medicamento medicamento = (Medicamento) spinnerMedicamentos.getSelectedItem();
            double peso = Double.parseDouble(etPeso.getText().toString());

            double dosis = (peso * medicamento.getDosisPorKg()) / medicamento.getConcentracion();

            String resultado = String.format(
                    "Medicamento: %s\nDosis calculada: %.2f ml\nPeso: %.1f kg",
                    medicamento.getNombre(),
                    dosis,
                    peso
            );

            tvResultado.setText(resultado);
            tvResultado.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un peso válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetCalculadora() {
        spinnerMedicamentos.setSelection(0);
        etPeso.setText("");
        tvResultado.setVisibility(View.GONE);
    }

    private void finalizarConsulta() {

    }
}