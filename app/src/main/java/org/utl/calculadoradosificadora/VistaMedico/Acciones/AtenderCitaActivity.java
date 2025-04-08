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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.Menu.ProtocolosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SobreNosotrosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SoporteActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.NotificacionesActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.PerfilActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.SeguridadActivity;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Medicamento;
import org.utl.calculadoradosificadora.model.Nota;
import org.utl.calculadoradosificadora.model.Paciente;
import org.utl.calculadoradosificadora.model.Persona;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;
import org.utl.calculadoradosificadora.service.MedicamentoService;
import org.utl.calculadoradosificadora.service.NotaService;
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

    // Navigation
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

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
        setupNavigationDrawer();
        initViews();
        loadCitaData();
        loadMedicamentos();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView tvTitulo = findViewById(R.id.tvTituloToolbar);
        tvTitulo.setText("Atendiendo Cita");
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        findViewById(R.id.options_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END));

        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_inicio) {
                startActivity(new Intent(this, org.utl.calculadoradosificadora.VistaMedico.VistaMedico.class));
            } else if (id == R.id.menu_protocolos) {
                startActivity(new Intent(this, ProtocolosActivity.class));
            } else if (id == R.id.menu_sobre_nosotros) {
                startActivity(new Intent(this, SobreNosotrosActivity.class));
            } else if (id == R.id.menu_soporte) {
                startActivity(new Intent(this, SoporteActivity.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
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
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
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

        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(v -> {
            // Simplemente finaliza la actividad para regresar a la anterior
            finish();
        });
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
            etPesoPaciente.setText(String.format("%.5f", cita.getPaciente().getPeso()));
        }
    }

    private String formatFecha(String fecha) {
        try {
            String[] partes = fecha.split("-");
            return partes[2] + "/" + partes[1] + "/" + partes[0];
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

        // Inicializar views
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

        // Configurar spinner y datos
        ArrayAdapter<Medicamento> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                medicamentos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicamentos.setAdapter(adapter);

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

                etPesoPaciente.setText(String.valueOf(peso));
                double dosis = calcularDosisMedicamento(medicamentoSeleccionado, peso);

                resultadoCalculadora = String.format(
                        "Dosis recomendada para %s %s:\n\n%.5f ml\n\nPara un paciente de %d años, género %s con peso de %.1f kg",
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

    private void agregarResultadoANotas() {
        String notasActuales = etNotas.getText().toString();
        if (!notasActuales.isEmpty()) {
            notasActuales += "\n\n";
        }
        etNotas.setText(notasActuales + resultadoCalculadora);
    }

    private void calcularDosis() {
        try {
            medicamentoSeleccionado = (Medicamento) spinnerMedicamentos.getSelectedItem();
            double peso = Double.parseDouble(etPesoCalculadora.getText().toString());

            // Validar peso
            if (peso <= 0) {
                Toast.makeText(this, "El peso debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar peso en pantalla principal
            etPesoPaciente.setText(String.format("%.5f", peso));

            // Calcular dosis según medicamento
            double dosis = calcularDosisMedicamento(medicamentoSeleccionado, peso);

            // Formatear resultado con 5 decimales
            resultadoCalculadora = String.format(
                    "Dosis recomendada para %s %s:\n\n%.5f ml\n\nPara un paciente de %d años, género %s con peso de %.5f kg",
                    medicamentoSeleccionado.getNombre(),
                    medicamentoSeleccionado.getPresentacion(),
                    dosis,
                    cita.getPaciente().getEdad(),
                    (cita.getPaciente().getPersona().getGenero() == 1 ? "Masculino" : "Femenino"),
                    peso
            );

            // Mostrar resultado en el diálogo
            tvResultadoCalculadora.setText(resultadoCalculadora);
            tvResultadoCalculadora.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un peso válido", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error en cálculo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private double calcularDosisMedicamento(Medicamento medicamento, double peso) {
        // Lógica de cálculo específica para cada medicamento con precisión de 5 decimales
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

    private void finalizarConsulta() {
        // 1. Validar y actualizar peso del paciente
        try {
            double nuevoPeso = Double.parseDouble(etPesoPaciente.getText().toString());
            if (nuevoPeso <= 0) {
                Toast.makeText(this, "El peso debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar el objeto paciente localmente
            cita.getPaciente().setPeso(nuevoPeso);

            // Aquí deberías llamar al servicio para actualizar el peso en el backend
            actualizarPesoPaciente(cita.getPaciente().getIdPaciente(), nuevoPeso);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un peso válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Marcar cita como atendida
        marcarCitaComoAtendida();
    }

    private void actualizarPesoPaciente(int idPaciente, double nuevoPeso) {
        PacienteService pacienteService = ApiClient.getClient().create(PacienteService.class);

        // Crear objeto Paciente con solo los campos necesarios
        Paciente pacienteActualizado = new Paciente();
        pacienteActualizado.setIdPaciente(idPaciente);
        pacienteActualizado.setPeso(nuevoPeso);

        // Mantener referencia al paciente original
        pacienteActualizado.setPersona(cita.getPaciente().getPersona());

        Call<ApiResponse<Paciente>> call = pacienteService.actualizarPesoPaciente(idPaciente, pacienteActualizado);
        call.enqueue(new Callback<ApiResponse<Paciente>>() {
            @Override
            public void onResponse(Call<ApiResponse<Paciente>> call, Response<ApiResponse<Paciente>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Actualización exitosa
                    runOnUiThread(() -> {
                        Toast.makeText(AtenderCitaActivity.this,
                                "Peso actualizado correctamente",
                                Toast.LENGTH_SHORT).show();
                        marcarCitaComoAtendida();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(AtenderCitaActivity.this,
                                    "Error al actualizar peso: " + (response.body() != null ?
                                            response.body().getMessage() : "Respuesta inválida"),
                                    Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Paciente>> call, Throwable t) {
                runOnUiThread(() ->
                        Toast.makeText(AtenderCitaActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void marcarCitaComoAtendida() {
        CitaService citaService = ApiClient.getClient().create(CitaService.class);

        // Crear objeto Cita con solo los campos necesarios
        Cita citaActualizada = new Cita();
        citaActualizada.setIdCita(cita.getIdCita());
        citaActualizada.setEstatus("Atendida");
        citaActualizada.setPaciente(cita.getPaciente());
        citaActualizada.setMedico(cita.getMedico());

        Call<ApiResponse<Cita>> call = citaService.marcarCitaComoAtendida(cita.getIdCita(), citaActualizada);
        call.enqueue(new Callback<ApiResponse<Cita>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cita>> call, Response<ApiResponse<Cita>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    runOnUiThread(() -> {
                        Toast.makeText(AtenderCitaActivity.this,
                                "Cita marcada como atendida",
                                Toast.LENGTH_SHORT).show();
                        guardarNotas();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(AtenderCitaActivity.this,
                                    "Error al actualizar cita: " + (response.body() != null ?
                                            response.body().getMessage() : "Respuesta inválida"),
                                    Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cita>> call, Throwable t) {
                runOnUiThread(() ->
                        Toast.makeText(AtenderCitaActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }
        });
    }
    private void guardarNotas() {
        String notasCompletas = etNotas.getText().toString();

        if (notasCompletas.trim().isEmpty()) {
            finishWithSuccess();
            return;
        }

        // Crear una versión mínima de la cita para la nota
        Cita citaMinima = new Cita();
        citaMinima.setIdCita(cita.getIdCita());

        Nota nota = new Nota();
        nota.setDato(notasCompletas);
        nota.setCita(citaMinima);

        NotaService service = ApiClient.getClient().create(NotaService.class);
        service.insertNota(nota).enqueue(new Callback<ApiResponse<Nota>>() {
            @Override
            public void onResponse(Call<ApiResponse<Nota>> call, Response<ApiResponse<Nota>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(AtenderCitaActivity.this, "Notas guardadas correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMsg = "Error al guardar notas";
                    if (response.body() != null) {
                        errorMsg += ": " + response.body().getMessage();
                    }
                    Toast.makeText(AtenderCitaActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
                finishWithSuccess();
            }

            @Override
            public void onFailure(Call<ApiResponse<Nota>> call, Throwable t) {
                Toast.makeText(AtenderCitaActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finishWithSuccess();
            }
        });
    }

    private void finishWithSuccess() {
        setResult(RESULT_OK);
        finish();
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