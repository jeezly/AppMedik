package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Medico;
import org.utl.calculadoradosificadora.model.Paciente;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;
import org.utl.calculadoradosificadora.service.MedicoService;
import org.utl.calculadoradosificadora.service.TitularService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarCitaActivity extends AppCompatActivity {

    private EditText etFecha, etRazon;
    private Spinner spHorarios, spMedicos, spTitulares, spPacientes;
    private Button btnAgendar;
    private TextView tvUsuario, tvNombre, tvApellidos, tvGenero, tvCorreo, tvTelefono;

    private List<Titular> titulares;
    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private Titular titularSeleccionado;
    private Medico medicoSeleccionado;
    private Paciente pacienteSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cita);

        bindViews();
        setupSpinners();
        loadData();
        setupListeners();
    }

    private void bindViews() {
        etFecha = findViewById(R.id.etFecha);
        etRazon = findViewById(R.id.etRazon);
        spHorarios = findViewById(R.id.spHorarios);
        spMedicos = findViewById(R.id.spMedicos);
        spTitulares = findViewById(R.id.spTitulares);
        spPacientes = findViewById(R.id.spPacientes);
        btnAgendar = findViewById(R.id.btnAgendar);
        tvUsuario = findViewById(R.id.tvUsuario);
        tvNombre = findViewById(R.id.tvNombre);
        tvApellidos = findViewById(R.id.tvApellidos);
        tvGenero = findViewById(R.id.tvGenero);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvTelefono = findViewById(R.id.tvTelefono);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> horariosAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.horarios_array,
                android.R.layout.simple_spinner_item
        );
        horariosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHorarios.setAdapter(horariosAdapter);

        spMedicos.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>()));
        spTitulares.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>()));
        spPacientes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>()));
    }

    private void loadData() {
        loadMedicos();
        loadTitulares();
    }

    private void loadMedicos() {
        MedicoService service = ApiClient.getClient().create(MedicoService.class);
        service.getAllMedicos().enqueue(new Callback<ApiResponse<List<Medico>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Medico>>> call, Response<ApiResponse<List<Medico>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    medicos = response.body().getData();
                    updateMedicosSpinner();
                } else {
                    String errorMsg = response.body() != null ?
                            response.body().getMessage() : "Error al cargar médicos";
                    showError(errorMsg);
                    Log.e("API_ERROR", "Error en getAllMedicos: " + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Medico>>> call, Throwable t) {
                showError("Error de red: " + t.getMessage());
                Log.e("API_ERROR", "Error en getAllMedicos", t);
            }
        });
    }

    private void loadTitulares() {
        TitularService service = ApiClient.getClient().create(TitularService.class);
        service.getAllTitulares().enqueue(new Callback<ApiResponse<List<Titular>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Titular>>> call, Response<ApiResponse<List<Titular>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    titulares = response.body().getData();
                    updateTitularesSpinner();
                } else {
                    String errorMsg = response.body() != null ?
                            response.body().getMessage() : "Error al cargar titulares";
                    showError(errorMsg);
                    Log.e("API_ERROR", "Error en getAllTitulares: " + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Titular>>> call, Throwable t) {
                showError("Error de red: " + t.getMessage());
                Log.e("API_ERROR", "Error en getAllTitulares", t);
            }
        });
    }

    private void loadPacientesPorTitular(int idTitular) {
        // Implementación simulada - en producción debería llamar al endpoint correspondiente
        pacientes = new ArrayList<>();
        pacientes.add(new Paciente(1, "Juan", "Pérez", true, 1, "2018-05-15", 18.5, "Seguro Popular"));
        pacientes.add(new Paciente(2, "María", "Gómez", false, 2, "2016-02-20", 22.0, "IMSS"));
        updatePacientesSpinner();
    }

    private void updateMedicosSpinner() {
        ArrayAdapter<Medico> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                medicos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMedicos.setAdapter(adapter);

        spMedicos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medicoSeleccionado = medicos.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                medicoSeleccionado = null;
            }
        });
    }

    private void updateTitularesSpinner() {
        ArrayAdapter<Titular> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                titulares
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTitulares.setAdapter(adapter);

        spTitulares.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                titularSeleccionado = titulares.get(position);
                updateTitularUI(titularSeleccionado);
                if (titularSeleccionado != null) {
                    loadPacientesPorTitular(titularSeleccionado.getIdTitular());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                titularSeleccionado = null;
            }
        });
    }

    private void updatePacientesSpinner() {
        ArrayAdapter<Paciente> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                pacientes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPacientes.setAdapter(adapter);

        spPacientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pacienteSeleccionado = pacientes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pacienteSeleccionado = null;
            }
        });
    }

    private void updateTitularUI(Titular titular) {
        tvUsuario.setText("Usuario: " + titular.getUsuario());
        tvNombre.setText("Nombre: " + titular.getNombre());
        tvApellidos.setText("Apellidos: " + titular.getApellidos());
        tvGenero.setText("Género: " + titular.getGenero());
        tvCorreo.setText("Correo: " + titular.getCorreo());
        tvTelefono.setText("Teléfono: " + titular.getTelefono());
    }

    private void setupListeners() {
        etFecha.setOnClickListener(v -> showDatePicker());
        btnAgendar.setOnClickListener(v -> scheduleAppointment());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, year, month, day) -> etFecha.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, month+1, day)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void scheduleAppointment() {
        if (validateFields()) {
            Cita cita = createAppointment();
            sendAppointmentToBackend(cita);
        }
    }

    private boolean validateFields() {
        if (etFecha.getText().toString().isEmpty()) {
            showError("Seleccione una fecha");
            return false;
        }
        if (etRazon.getText().toString().isEmpty()) {
            showError("Ingrese la razón de la cita");
            return false;
        }
        if (titularSeleccionado == null) {
            showError("Seleccione un titular");
            return false;
        }
        if (pacienteSeleccionado == null) {
            showError("Seleccione un paciente");
            return false;
        }
        if (medicoSeleccionado == null) {
            showError("Seleccione un médico");
            return false;
        }
        return true;
    }

    private Cita createAppointment() {
        Cita cita = new Cita();
        cita.setFecha(etFecha.getText().toString());
        cita.setHora(spHorarios.getSelectedItem().toString());
        cita.setRazonCita(etRazon.getText().toString());
        cita.setEstatus("Programada");
        cita.setMedico(medicoSeleccionado);
        cita.setPaciente(pacienteSeleccionado);
        cita.setTitular(titularSeleccionado);
        return cita;
    }

    private void sendAppointmentToBackend(Cita cita) {
        CitaService service = ApiClient.getClient().create(CitaService.class);
        service.agendarCita(cita).enqueue(new Callback<ApiResponse<Cita>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cita>> call, Response<ApiResponse<Cita>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    showConfirmationDialog(response.body().getData());
                } else {
                    String errorMsg = response.body() != null ?
                            response.body().getMessage() : "Error al agendar cita";
                    showError(errorMsg);
                    Log.e("API_ERROR", "Error en agendarCita: " + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cita>> call, Throwable t) {
                showError("Error de red: " + t.getMessage());
                Log.e("API_ERROR", "Error en agendarCita", t);
            }
        });
    }

    private void showConfirmationDialog(Cita cita) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirmacion_cita, null);

        TextView tvFecha = dialogView.findViewById(R.id.tvFecha);
        TextView tvHora = dialogView.findViewById(R.id.tvHora);
        TextView tvNombreDoctor = dialogView.findViewById(R.id.tvNombreDoctor);
        TextView tvCedulaDoctor = dialogView.findViewById(R.id.tvCedulaDoctor);
        TextView tvNombrePaciente = dialogView.findViewById(R.id.tvNombrePaciente);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        tvFecha.setText("Fecha: " + cita.getFecha());
        tvHora.setText("Hora: " + cita.getHora());

        if (cita.getMedico() != null) {
            tvNombreDoctor.setText("Dr. " + cita.getMedico().getNombre() + " " + cita.getMedico().getApellidos());
            tvCedulaDoctor.setText("Cédula: " + cita.getMedico().getNumCedula());
        }

        if (cita.getPaciente() != null) {
            tvNombrePaciente.setText("Paciente: " + cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos());
        }

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}