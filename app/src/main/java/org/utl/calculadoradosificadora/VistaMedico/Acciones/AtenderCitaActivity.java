package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Medicamento;
import org.utl.calculadoradosificadora.model.Paciente;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;
import org.utl.calculadoradosificadora.service.MedicamentoService;
import org.utl.calculadoradosificadora.service.PacienteService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtenderCitaActivity extends AppCompatActivity {

    // Views
    private TextView tvFecha, tvHora, tvRazon;
    private TextView tvTitularUsuario, tvTitularNombre, tvTitularGenero, tvTitularCorreo, tvTitularTelefono;
    private TextView tvPacienteNombre, tvPacienteEdad, tvPacienteGenero;
    private EditText etPesoPaciente, etNotas;
    private ImageView ivCalculadora;
    private Button btnFinalizar;

    // Calculadora
    private Spinner spinnerMedicamentos;
    private EditText etPesoCalculadora;
    private TextView tvEdadCalculadora, tvGeneroCalculadora;
    private TextView tvResultadoCalculadora;

    // Data
    private Cita cita;
    private List<Medicamento> medicamentos = new ArrayList<>();
    private Medicamento medicamentoSeleccionado;
    private String resultadoCalculadora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atender_cita);

        setupToolbar();
        initViews();
        loadCitaData();
        loadMedicamentos();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Atendiendo Cita");
        }
    }

    private void initViews() {
        // Datos de la cita
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvRazon = findViewById(R.id.tvRazon);

        // Datos del titular
        tvTitularUsuario = findViewById(R.id.tvTitularUsuario);
        tvTitularNombre = findViewById(R.id.tvTitularNombre);
        tvTitularGenero = findViewById(R.id.tvTitularGenero);
        tvTitularCorreo = findViewById(R.id.tvTitularCorreo);
        tvTitularTelefono = findViewById(R.id.tvTitularTelefono);

        // Datos del paciente
        tvPacienteNombre = findViewById(R.id.tvPacienteNombre);
        tvPacienteEdad = findViewById(R.id.tvPacienteEdad);
        tvPacienteGenero = findViewById(R.id.tvPacienteGenero);
        etPesoPaciente = findViewById(R.id.etPesoPaciente);

        // Notas y calculadora
        etNotas = findViewById(R.id.etNotas);
        ivCalculadora = findViewById(R.id.ivCalculadora);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        ivCalculadora.setOnClickListener(v -> showCalculadoraDialog());
        btnFinalizar.setOnClickListener(v -> finalizarConsulta());
    }

    private void loadCitaData() {
        cita = (Cita) getIntent().getSerializableExtra("cita");
        if (cita == null) {
            Toast.makeText(this, "Error al cargar la cita", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Datos de la cita
        tvFecha.setText("Fecha: " + formatFecha(cita.getFecha()));
        tvHora.setText("Hora: " + cita.getHora());
        tvRazon.setText("Razón: " + cita.getRazonCita());

        // Datos del titular
        if (cita.getTitular() != null) {
            tvTitularUsuario.setText("Usuario: " + cita.getTitular().getUsuarioNombre());
            tvTitularNombre.setText("Nombre: " + cita.getTitular().getNombre() + " " +
                    cita.getTitular().getApellidos());
            tvTitularGenero.setText("Género: " + cita.getTitular().getGenero());
            tvTitularCorreo.setText("Correo: " + cita.getTitular().getCorreo());
            tvTitularTelefono.setText("Teléfono: " + cita.getTitular().getTelefono());
        }

        // Datos del paciente
        if (cita.getPaciente() != null) {
            tvPacienteNombre.setText("Nombre: " + cita.getPaciente().getNombreCompleto());
            tvPacienteEdad.setText("Edad: " + cita.getPaciente().getEdad() + " años");
            tvPacienteGenero.setText("Género: " + (cita.getPaciente().getPersona().getGenero() == 1 ? "Masculino" : "Femenino"));
            etPesoPaciente.setText(String.format("%.2f", cita.getPaciente().getPeso()));
        }
    }

    private String formatFecha(String fecha) {
        try {
            String[] partes = fecha.split("-");
            if (partes.length == 3) {
                return partes[2] + "/" + partes[1] + "/" + partes[0];
            }
            return fecha;
        } catch (Exception e) {
            return fecha;
        }
    }

    private void loadMedicamentos() {
        MedicamentoService service = ApiClient.getClient().create(MedicamentoService.class);
        service.getAllMedicamento().enqueue(new Callback<ApiResponse<List<Medicamento>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Medicamento>>> call, Response<ApiResponse<List<Medicamento>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    medicamentos = response.body().getData();
                } else {
                    Toast.makeText(AtenderCitaActivity.this, "Error al cargar medicamentos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Medicamento>>> call, Throwable t) {
                Toast.makeText(AtenderCitaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCalculadoraDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_calculadora, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Inicializar views del diálogo
        spinnerMedicamentos = view.findViewById(R.id.spinnerMedicamentosDialog);
        etPesoCalculadora = view.findViewById(R.id.etPesoCalculadora);
        tvEdadCalculadora = view.findViewById(R.id.tvEdadCalculadora);
        tvGeneroCalculadora = view.findViewById(R.id.tvGeneroCalculadora);
        tvResultadoCalculadora = view.findViewById(R.id.tvResultadoCalculadora);
        Button btnCalcular = view.findViewById(R.id.btnCalcularDialog);
        Button btnCancelar = view.findViewById(R.id.btnCancelarDialog);
        Button btnOk = view.findViewById(R.id.btnOkDialog);
        Button btnAgregarANotas = view.findViewById(R.id.btnAgregarANotas);
        LinearLayout layoutBotonesCalculadora = view.findViewById(R.id.layoutBotonesCalculadora);
        LinearLayout layoutBotonesResultado = view.findViewById(R.id.layoutBotonesResultado);

        // Configurar spinner
        ArrayAdapter<Medicamento> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                medicamentos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicamentos.setAdapter(adapter);

        // Configurar datos del paciente
        tvEdadCalculadora.setText("Edad: " + cita.getPaciente().getEdad() + " años");
        tvGeneroCalculadora.setText("Género: " + (cita.getPaciente().getPersona().getGenero() == 1 ? "Masculino" : "Femenino"));
        etPesoCalculadora.setText(etPesoPaciente.getText().toString());

        btnCalcular.setOnClickListener(v -> {
            try {
                medicamentoSeleccionado = (Medicamento) spinnerMedicamentos.getSelectedItem();
                double peso = Double.parseDouble(etPesoCalculadora.getText().toString());

                if (peso <= 0) {
                    Toast.makeText(this, "El peso debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Actualizar peso en pantalla principal
                etPesoPaciente.setText(String.valueOf(peso));

                // Calcular dosis
                double dosis = calcularDosisMedicamento(medicamentoSeleccionado, peso);

                resultadoCalculadora = String.format(
                        "Dosis recomendada para %s %s:\n\n%.2f ml\n\nPara un paciente de %d años, género %s con peso de %.2f kg",
                        medicamentoSeleccionado.getNombre(),
                        medicamentoSeleccionado.getPresentacion(),
                        dosis,
                        cita.getPaciente().getEdad(),
                        (cita.getPaciente().getPersona().getGenero() == 1 ? "Masculino" : "Femenino"),
                        peso
                );

                tvResultadoCalculadora.setText(resultadoCalculadora);
                tvResultadoCalculadora.setVisibility(View.VISIBLE);
                layoutBotonesCalculadora.setVisibility(View.GONE);
                layoutBotonesResultado.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Toast.makeText(this, "Error en cálculo", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnOk.setOnClickListener(v -> dialog.dismiss());
        btnAgregarANotas.setOnClickListener(v -> {
            agregarResultadoANotas();
            dialog.dismiss();
        });

        dialog.show();
    }

    private double calcularDosisMedicamento(Medicamento medicamento, double peso) {
        // Lógica de cálculo específica para cada medicamento
        switch (medicamento.getIdMedicamentos()) {
            case 1: // Paracetamol
                return (peso * 15.0) / 500.0; // 15mg/kg/dosis (tabletas de 500mg)
            case 2: // Ibuprofeno (100mg/5ml)
                return (peso * 5.0) / 20.0; // 5-10mg/kg/dosis
            case 3: // Amoxicilina (250mg/5ml)
                return (peso * 20.0) / 50.0; // 20-40mg/kg/día dividido cada 8-12h
            case 4: // Dexametasona (0.5mg/ml)
                return (peso * 0.15) / 0.5; // 0.15mg/kg/dosis
            case 5: // Salbutamol (2mg/5ml)
                return (peso * 0.1) / 0.4; // 0.1mg/kg/dosis (máx 2.5mg)
            default:
                return 0.0;
        }
    }

    private void agregarResultadoANotas() {
        String notasActuales = etNotas.getText().toString();
        if (!notasActuales.isEmpty()) {
            notasActuales += "\n\n";
        }
        etNotas.setText(notasActuales + resultadoCalculadora);
    }

    private void finalizarConsulta() {
        // 1. Validar peso
        try {
            double nuevoPeso = Double.parseDouble(etPesoPaciente.getText().toString());
            if (nuevoPeso <= 0) {
                Toast.makeText(this, "El peso debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar el peso del paciente
            actualizarPesoPaciente(nuevoPeso);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un peso válido", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void actualizarPesoPaciente(double nuevoPeso) {
        PacienteService pacienteService = ApiClient.getClient().create(PacienteService.class);

        // Crear objeto Paciente con solo los campos necesarios
        Paciente pacienteActualizado = new Paciente();
        pacienteActualizado.setIdPaciente(cita.getPaciente().getIdPaciente());
        pacienteActualizado.setPeso(nuevoPeso);
        pacienteActualizado.setPersona(cita.getPaciente().getPersona()); // Mantener referencia a persona

        Call<ApiResponse<Paciente>> call = pacienteService.updatePaciente(
                pacienteActualizado.getIdPaciente(), pacienteActualizado);

        call.enqueue(new Callback<ApiResponse<Paciente>>() {
            @Override
            public void onResponse(Call<ApiResponse<Paciente>> call, Response<ApiResponse<Paciente>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Actualizar el peso localmente
                    cita.getPaciente().setPeso(nuevoPeso);
                    // Marcar cita como atendida
                    marcarCitaComoAtendida();
                } else {
                    Toast.makeText(AtenderCitaActivity.this,
                            "Error al actualizar peso: " + (response.body() != null ?
                                    response.body().getMessage() : "Error desconocido"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Paciente>> call, Throwable t) {
                Toast.makeText(AtenderCitaActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void marcarCitaComoAtendida() {
        CitaService citaService = ApiClient.getClient().create(CitaService.class);

        // Crear una copia mínima de la cita con solo los campos necesarios
        Cita citaActualizada = new Cita();
        citaActualizada.setIdCita(cita.getIdCita());
        citaActualizada.setEstatus("Atendida");
        citaActualizada.setNota(etNotas.getText().toString());

        Call<ApiResponse<Cita>> call = citaService.updateCita(cita.getIdCita(), citaActualizada);

        call.enqueue(new Callback<ApiResponse<Cita>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cita>> call, Response<ApiResponse<Cita>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(AtenderCitaActivity.this,
                            "Cita atendida correctamente",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AtenderCitaActivity.this,
                            "Error al actualizar cita: " + (response.body() != null ?
                                    response.body().getMessage() : "Error desconocido"),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cita>> call, Throwable t) {
                Toast.makeText(AtenderCitaActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}