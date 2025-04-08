package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.NotificacionesActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.PerfilActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.SeguridadActivity;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Medico;
import org.utl.calculadoradosificadora.model.Paciente;
import org.utl.calculadoradosificadora.model.Persona;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;
import org.utl.calculadoradosificadora.service.MedicoService;
import org.utl.calculadoradosificadora.service.PacienteService;
import org.utl.calculadoradosificadora.service.TitularService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarCitaActivity extends AppCompatActivity {

    private EditText etFecha, etRazon;
    private Spinner spHorarios, spMedicos, spTitulares, spPacientes;
    private Button btnAgendar, btnRegresar;
    private TextView tvUsuario, tvNombre, tvApellidos, tvGenero, tvCorreo, tvTelefono;
    private TextView tvNombrePaciente, tvEdadPaciente, tvPesoPaciente;

    private List<Titular> titulares = new ArrayList<>();
    private List<Medico> medicos = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private Titular titularSeleccionado;
    private Medico medicoSeleccionado;
    private Paciente pacienteSeleccionado;

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cita);

        bindViews();
        setupToolbar();
        setupNavigationDrawer();
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
        btnRegresar = findViewById(R.id.btnRegresar);

        tvUsuario = findViewById(R.id.tvUsuario);
        tvNombre = findViewById(R.id.tvNombre);
        tvApellidos = findViewById(R.id.tvApellidos);
        tvGenero = findViewById(R.id.tvGenero);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvTelefono = findViewById(R.id.tvTelefono);

        tvNombrePaciente = findViewById(R.id.tvNombrePaciente);
        tvEdadPaciente = findViewById(R.id.tvEdadPaciente);
        tvPesoPaciente = findViewById(R.id.tvPesoPaciente);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(navigationViewLeft));
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(navigationViewRight));

        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_inicio) {
                startActivity(new Intent(this, org.utl.calculadoradosificadora.VistaMedico.VistaMedico.class));
            } else if (id == R.id.menu_protocolos) {
                startActivity(new Intent(this, org.utl.calculadoradosificadora.VistaMedico.Menu.ProtocolosActivity.class));
            } else if (id == R.id.menu_sobre_nosotros) {
                startActivity(new Intent(this, org.utl.calculadoradosificadora.VistaMedico.Menu.SobreNosotrosActivity.class));
            } else if (id == R.id.menu_soporte) {
                startActivity(new Intent(this, org.utl.calculadoradosificadora.VistaMedico.Menu.SoporteActivity.class));
            }
            drawerLayout.closeDrawer(navigationViewLeft);
            return true;
        });

        navigationViewRight.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.opciones_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
            } else if (id == R.id.opciones_configuracion) {
                startActivity(new Intent(this, ConfiguracionActivity.class));
            } else if (id == R.id.opciones_seguridad) {
                startActivity(new Intent(this, SeguridadActivity.class));
            } else if (id == R.id.opciones_notificaciones) {
                startActivity(new Intent(this, NotificacionesActivity.class));
            }
            drawerLayout.closeDrawer(navigationViewRight);
            return true;
        });
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
        Call<ApiResponse<List<Medico>>> call = service.getAllMedicos();

        call.enqueue(new Callback<ApiResponse<List<Medico>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Medico>>> call, Response<ApiResponse<List<Medico>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Medico>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        medicos = apiResponse.getData();
                        updateMedicosSpinner();
                    } else {
                        showError(apiResponse.getMessage());
                    }
                } else {
                    showError("Error al cargar médicos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Medico>>> call, Throwable t) {
                showError("Error de conexión: " + t.getMessage());
                Log.e("API_ERROR", "Error al cargar médicos", t);
            }
        });
    }

    private void loadTitulares() {
        TitularService service = ApiClient.getClient().create(TitularService.class);
        Call<ApiResponse<List<Titular>>> call = service.getAllTitulares();

        call.enqueue(new Callback<ApiResponse<List<Titular>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Titular>>> call, Response<ApiResponse<List<Titular>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Titular>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        titulares = apiResponse.getData();
                        updateTitularesSpinner();
                    } else {
                        showError(apiResponse.getMessage());
                    }
                } else {
                    showError("Error al cargar titulares: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Titular>>> call, Throwable t) {
                showError("Error de conexión: " + t.getMessage());
                Log.e("API_ERROR", "Error al cargar titulares", t);
            }
        });
    }

    private void loadPacientesPorTitular(int idTitular) {
        PacienteService service = ApiClient.getClient().create(PacienteService.class);
        Call<ApiResponse<List<Paciente>>> call = service.getPacientesByTitular(idTitular);

        call.enqueue(new Callback<ApiResponse<List<Paciente>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Paciente>>> call, Response<ApiResponse<List<Paciente>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Paciente>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        pacientes = apiResponse.getData();
                        updatePacientesSpinner();
                        updatePacienteUI();
                    } else {
                        showError(apiResponse.getMessage());
                    }
                } else {
                    showError("Error al cargar pacientes: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Paciente>>> call, Throwable t) {
                showError("Error de conexión: " + t.getMessage());
                Log.e("API_ERROR", "Error al cargar pacientes", t);
            }
        });
    }

    private void updateMedicosSpinner() {
        ArrayAdapter<Medico> adapter = new ArrayAdapter<Medico>(this, android.R.layout.simple_spinner_item, medicos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(medicos.get(position).toString());
                return view;
            }
        };
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
        ArrayAdapter<Titular> adapter = new ArrayAdapter<Titular>(this, android.R.layout.simple_spinner_item, titulares) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(titulares.get(position).toString());
                return view;
            }
        };
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
                clearTitularUI();
                clearPacientesSpinner();
            }
        });
    }

    private void updatePacientesSpinner() {
        List<Paciente> pacientesConDefault = new ArrayList<>();
        Paciente placeholder = new Paciente();
        placeholder.setPersona(new Persona());
        placeholder.getPersona().setNombre("Seleccione un paciente");
        pacientesConDefault.add(placeholder);
        pacientesConDefault.addAll(pacientes);

        ArrayAdapter<Paciente> adapter = new ArrayAdapter<Paciente>(
                this,
                android.R.layout.simple_spinner_item,
                pacientesConDefault
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                if (position == 0) {
                    textView.setText(getItem(position).getPersona().getNombre());
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    Paciente paciente = getItem(position);
                    textView.setText(paciente.getNombreCompleto());
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                if (position == 0) {
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPacientes.setAdapter(adapter);

        spPacientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    pacienteSeleccionado = pacientes.get(position - 1);
                    updatePacienteUI();
                } else {
                    pacienteSeleccionado = null;
                    clearPacienteUI();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pacienteSeleccionado = null;
                clearPacienteUI();
            }
        });
    }

    private void updateTitularUI(Titular titular) {
        if (titular != null) {
            tvUsuario.setText("Usuario: " + titular.getUsuarioNombre());
            tvNombre.setText("Nombre: " + titular.getNombre());
            tvApellidos.setText("Apellidos: " + titular.getApellidos());
            tvGenero.setText("Género: " + titular.getGenero());
            tvCorreo.setText("Correo: " + titular.getCorreo());
            tvTelefono.setText("Teléfono: " + titular.getTelefono());
        }
    }

    private void clearTitularUI() {
        tvUsuario.setText("Usuario: ");
        tvNombre.setText("Nombre: ");
        tvApellidos.setText("Apellidos: ");
        tvGenero.setText("Género: ");
        tvCorreo.setText("Correo: ");
        tvTelefono.setText("Teléfono: ");
    }

    private void updatePacienteUI() {
        if (pacienteSeleccionado != null) {
            tvNombrePaciente.setText("Paciente: " + pacienteSeleccionado.getNombreCompleto());
            tvEdadPaciente.setText("Edad: " + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
            tvPesoPaciente.setText("Peso: " + pacienteSeleccionado.getPeso() + " kg");
        }
    }

    private void clearPacienteUI() {
        tvNombrePaciente.setText("Paciente: ");
        tvEdadPaciente.setText("Edad: ");
        tvPesoPaciente.setText("Peso: ");
    }

    private void clearPacientesSpinner() {
        pacientes.clear();
        spPacientes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>()));
        clearPacienteUI();
    }

    private String calcularEdad(String fechaNacimiento) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fechaNac = sdf.parse(fechaNacimiento);

            Calendar dob = Calendar.getInstance();
            dob.setTime(fechaNac);
            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age + " años";
        } catch (ParseException e) {
            return "N/A";
        }
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

    private int generarIdUnicoCita() {
        // Genera un ID único basado en timestamp + random
        long timestamp = System.currentTimeMillis() / 1000;
        int random = (int) (Math.random() * 1000);
        return (int) (timestamp % 1000000) * 1000 + random;
    }

    private void scheduleAppointment() {
        if (validateFields()) {
            // Crear la cita con objetos completos temporalmente
            Cita cita = new Cita();
            cita.setIdCita(generarIdUnicoCita());
            cita.setFecha(etFecha.getText().toString());
            cita.setHora(spHorarios.getSelectedItem().toString());
            cita.setRazonCita(etRazon.getText().toString());
            cita.setEstatus("Programada");

            // Asignar objetos completos (necesarios para el backend)
            cita.setMedico(medicoSeleccionado);
            cita.setPaciente(pacienteSeleccionado);
            cita.setTitular(titularSeleccionado);

            // Crear una versión simplificada para enviar
            Cita citaParaEnviar = new Cita();
            citaParaEnviar.setIdCita(cita.getIdCita());
            citaParaEnviar.setFecha(cita.getFecha());
            citaParaEnviar.setHora(cita.getHora());
            citaParaEnviar.setRazonCita(cita.getRazonCita());
            citaParaEnviar.setEstatus(cita.getEstatus());

            // Solo incluir IDs para las relaciones
            if (medicoSeleccionado != null) {
                Medico medicoMin = new Medico();
                medicoMin.setIdMedico(medicoSeleccionado.getIdMedico());
                citaParaEnviar.setMedico(medicoMin);
            }

            if (pacienteSeleccionado != null) {
                Paciente pacienteMin = new Paciente();
                pacienteMin.setIdPaciente(pacienteSeleccionado.getIdPaciente());
                citaParaEnviar.setPaciente(pacienteMin);
            }

            if (titularSeleccionado != null) {
                Titular titularMin = new Titular();
                titularMin.setIdTitular(titularSeleccionado.getIdTitular());
                citaParaEnviar.setTitular(titularMin);
            }

            sendAppointmentToBackend(citaParaEnviar);
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

        if (pacienteSeleccionado == null || pacienteSeleccionado.getIdPaciente() == 0) {
            showError("Seleccione un paciente válido");
            return false;
        }

        if (medicoSeleccionado == null || medicoSeleccionado.getIdMedico() == 0) {
            showError("Seleccione un médico válido");
            return false;
        }

        if (spHorarios.getSelectedItem() == null || spHorarios.getSelectedItem().toString().isEmpty()) {
            showError("Seleccione un horario válido");
            return false;
        }

        return true;
    }

    private Cita createAppointment() {
        Cita cita = new Cita();
        cita.setIdCita(generarIdUnicoCita());
        cita.setFecha(etFecha.getText().toString());
        cita.setHora(spHorarios.getSelectedItem().toString());
        cita.setRazonCita(etRazon.getText().toString());
        cita.setEstatus("Programada");

        if (medicoSeleccionado != null) {
            Medico medico = new Medico();
            medico.setIdMedico(medicoSeleccionado.getIdMedico());
            cita.setMedico(medico);
        }

        if (pacienteSeleccionado != null) {
            Paciente paciente = new Paciente();
            paciente.setIdPaciente(pacienteSeleccionado.getIdPaciente());
            cita.setPaciente(paciente);
        }

        if (titularSeleccionado != null) {
            Titular titular = new Titular();
            titular.setIdTitular(titularSeleccionado.getIdTitular());
            cita.setTitular(titular);
        }

        // Log para depuración
        Gson gson = new Gson();
        String citaJson = gson.toJson(cita);
        Log.d("CITA_JSON", "JSON a enviar: " + citaJson);

        return cita;
    }

    private void sendAppointmentToBackend(Cita cita) {
        // Crear una versión completa temporalmente para el backend
        Cita citaCompleta = new Cita();
        citaCompleta.setIdCita(cita.getIdCita());
        citaCompleta.setFecha(cita.getFecha());
        citaCompleta.setHora(cita.getHora());
        citaCompleta.setRazonCita(cita.getRazonCita());
        citaCompleta.setEstatus(cita.getEstatus());

        // Asignar objetos completos (necesarios para el backend)
        if (medicoSeleccionado != null) {
            citaCompleta.setMedico(medicoSeleccionado);
        }
        if (pacienteSeleccionado != null) {
            citaCompleta.setPaciente(pacienteSeleccionado);
        }
        if (titularSeleccionado != null) {
            citaCompleta.setTitular(titularSeleccionado);
        }

        CitaService service = ApiClient.getClient().create(CitaService.class);
        Call<ApiResponse<Cita>> call = service.insertCita(citaCompleta);

        call.enqueue(new Callback<ApiResponse<Cita>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cita>> call, Response<ApiResponse<Cita>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Cita> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        showConfirmationDialog(apiResponse.getData());
                    } else {
                        showError(apiResponse.getMessage());
                        Log.e("API_ERROR", "Error en la respuesta: " + apiResponse.getMessage());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "empty error body";
                        showError("Error del servidor: " + errorBody);
                        Log.e("API_ERROR", "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error al leer errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cita>> call, Throwable t) {
                showError("Error de red: " + t.getMessage());
                Log.e("API_ERROR", "Error de red", t);
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
        TextView tvEdadPaciente = dialogView.findViewById(R.id.tvEdadPaciente);
        TextView tvPesoPaciente = dialogView.findViewById(R.id.tvPesoPaciente);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        tvFecha.setText("Fecha: " + cita.getFecha());
        tvHora.setText("Hora: " + cita.getHora());

        if (medicoSeleccionado != null) {
            tvNombreDoctor.setText("Dr. " + medicoSeleccionado.getPersona().getNombre() + " " + medicoSeleccionado.getPersona().getApellidos());
            tvCedulaDoctor.setText("Cédula: " + medicoSeleccionado.getNumCedula());
        }

        if (pacienteSeleccionado != null) {
            tvNombrePaciente.setText("Paciente: " + pacienteSeleccionado.getNombreCompleto());
            tvEdadPaciente.setText("Edad: " + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
            tvPesoPaciente.setText("Peso: " + pacienteSeleccionado.getPeso() + " kg");
        }

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void setupListeners() {
        etFecha.setOnClickListener(v -> showDatePicker());
        btnAgendar.setOnClickListener(v -> scheduleAppointment());

        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(AgregarCitaActivity.this, AgendaActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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