package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesCitaActivity extends AppCompatActivity {

    private TextView tvFecha, tvHora, tvRazon, tvNombreTitular, tvGenero, tvCorreo, tvTelefono;
    private TextView tvNombrePaciente, tvEdadPaciente, tvPesoPaciente;
    private Button btnReagendar, btnCancelar, btnAtender, btnRegresar;
    private Cita cita;
    private final Calendar calendar = Calendar.getInstance();

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cita);

        if (getIntent() == null || getIntent().getSerializableExtra("cita") == null) {
            Toast.makeText(this, "Error al cargar la cita", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cita = (Cita) getIntent().getSerializableExtra("cita");
        initViews();
        setupToolbar();
        setupNavigationDrawer();
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

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupNavigationDrawer() {
        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(navigationViewLeft));
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(navigationViewRight));

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

    private void loadCitaData() {
        // Formatear fecha
        String fechaFormateada = formatFecha(cita.getFecha());
        tvFecha.setText("Fecha: " + fechaFormateada);
        tvHora.setText("Hora: " + cita.getHora());
        tvRazon.setText("Razón: " + (cita.getRazonCita() != null ? cita.getRazonCita() : "No especificada"));

        // Datos del titular
        if (cita.getTitular() != null && cita.getTitular().getPersona() != null) {
            String nombreTitular = cita.getTitular().getPersona().getNombre() + " " + cita.getTitular().getPersona().getApellidos();
            tvNombreTitular.setText("Titular: " + nombreTitular);

            String genero = cita.getTitular().getPersona().getGenero() == 1 ? "Masculino" : "Femenino";
            tvGenero.setText("Género: " + genero);

            if (cita.getTitular().getUsuario() != null) {
                tvCorreo.setText("Correo: " + cita.getTitular().getUsuario().getCorreo());
            }
            tvTelefono.setText("Teléfono: " + cita.getTitular().getTelefono());
        }

        // Datos del paciente
        if (cita.getPaciente() != null && cita.getPaciente().getPersona() != null) {
            String nombrePaciente = cita.getPaciente().getPersona().getNombre() + " " + cita.getPaciente().getPersona().getApellidos();
            tvNombrePaciente.setText("Paciente: " + nombrePaciente);

            if (cita.getPaciente().getFechaNacimiento() != null) {
                tvEdadPaciente.setText("Edad: " + calcularEdad(cita.getPaciente().getFechaNacimiento()) + " años");
            }
            tvPesoPaciente.setText("Peso: " + cita.getPaciente().getPeso() + " kg");
        }

        // Configurar botones según estado
        if ("Cancelada".equals(cita.getEstatus())) {
            btnCancelar.setEnabled(false);
            btnAtender.setEnabled(false);
        } else if ("Atendida".equals(cita.getEstatus())) {
            btnAtender.setEnabled(false);
        }
    }

    private String formatFecha(String fechaOriginal) {
        try {
            String[] partes = fechaOriginal.split("-");
            if (partes.length == 3) {
                return partes[2] + "/" + partes[1] + "/" + partes[0];
            }
            return fechaOriginal;
        } catch (Exception e) {
            return fechaOriginal;
        }
    }

    private int calcularEdad(String fechaNacimiento) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long fechaNac = sdf.parse(fechaNacimiento).getTime();
            long ahora = System.currentTimeMillis();
            long diff = ahora - fechaNac;
            return (int) (diff / (1000L * 60 * 60 * 24 * 365));
        } catch (Exception e) {
            return 0;
        }
    }

    private void setupButtons() {
        btnReagendar.setOnClickListener(v -> showReagendarDialog());
        btnCancelar.setOnClickListener(v -> showCancelDialog());
        btnAtender.setOnClickListener(v -> atenderCita());
        btnRegresar.setOnClickListener(v -> finish());
    }

    private void showReagendarDialog() {
        new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    showTimePickerDialog();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePickerDialog() {
        new TimePickerDialog(
                this,
                (view, hour, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    confirmReagendar();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        ).show();
    }

    private void confirmReagendar() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String nuevaFecha = dateFormat.format(calendar.getTime());
        String nuevaHora = timeFormat.format(calendar.getTime());

        new AlertDialog.Builder(this)
                .setTitle("Confirmar Reagendación")
                .setMessage("Nueva fecha: " + formatFecha(nuevaFecha) + "\nNueva hora: " + nuevaHora)
                .setPositiveButton("Confirmar", (dialog, which) -> reagendarCita(nuevaFecha, nuevaHora))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void reagendarCita(String nuevaFecha, String nuevaHora) {
        Cita citaActualizada = new Cita();
        citaActualizada.setIdCita(cita.getIdCita());
        citaActualizada.setFecha(nuevaFecha);
        citaActualizada.setHora(nuevaHora);
        citaActualizada.setEstatus("Programada");

        // Mantener referencias necesarias
        citaActualizada.setMedico(cita.getMedico());
        citaActualizada.setPaciente(cita.getPaciente());
        citaActualizada.setTitular(cita.getTitular());

        CitaService service = ApiClient.getClient().create(CitaService.class);
        service.updateCita(cita.getIdCita(), citaActualizada).enqueue(new Callback<ApiResponse<Cita>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cita>> call, Response<ApiResponse<Cita>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(DetallesCitaActivity.this, "Cita reagendada", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(DetallesCitaActivity.this, "Error al reagendar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cita>> call, Throwable t) {
                Toast.makeText(DetallesCitaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Cita")
                .setMessage("¿Estás seguro de cancelar esta cita?")
                .setPositiveButton("Sí", (dialog, which) -> cancelarCita())
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelarCita() {
        CitaService service = ApiClient.getClient().create(CitaService.class);
        service.cancelarCita(cita.getIdCita()).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(DetallesCitaActivity.this, "Cita cancelada", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(DetallesCitaActivity.this, "Error al cancelar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(DetallesCitaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atenderCita() {
        Intent intent = new Intent(this, AtenderCitaActivity.class);
        intent.putExtra("cita", cita);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
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