package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.utl.calculadoradosificadora.MainActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.Menu.ProtocolosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SobreNosotrosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SoporteActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.NotificacionesActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.PerfilActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.SeguridadActivity;
import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Medico;
import org.utl.calculadoradosificadora.model.Nota;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.NotaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesHistorialCitaActivity extends AppCompatActivity {

    private TextView tvFecha, tvHora, tvClaveNombreTitular, tvClaveNombrePaciente, tvRazonCita, tvNotas;
    private Button btnOk;
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;
    private Cita cita;
    private Medico medicoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_historial_cita);

        medicoActual = obtenerMedicoActual();
        cita = (Cita) getIntent().getSerializableExtra("cita");

        if (cita == null) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalles de Cita");
        }

        initViews();
        setupNavigationDrawer();
        mostrarDatosCita();

        // Cargar notas directamente de la cita primero
        if (cita.getNota() != null && !cita.getNota().isEmpty()) {
            tvNotas.setText(cita.getNota());
        } else {
            // Si no hay nota en la cita, intentar cargar desde el servicio
            loadNotas();
        }

        btnOk.setOnClickListener(v -> finish());
    }

    private Medico obtenerMedicoActual() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        String medicoJson = preferences.getString("medico", "");
        if (!medicoJson.isEmpty()) {
            return new Gson().fromJson(medicoJson, Medico.class);
        }
        return null;
    }

    private void initViews() {
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvClaveNombreTitular = findViewById(R.id.tvClaveNombreTitular);
        tvClaveNombrePaciente = findViewById(R.id.tvClaveNombrePaciente);
        tvRazonCita = findViewById(R.id.tvRazonCita);
        tvNotas = findViewById(R.id.tvNotas);
        btnOk = findViewById(R.id.btnOk);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);
    }

    private void setupNavigationDrawer() {
        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_inicio) {
                startActivity(new Intent(this, VistaMedico.class));
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
            } else if (id == R.id.opciones_cerrar_sesion) {
                cerrarSesion();
            }

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void mostrarDatosCita() {
        tvFecha.setText("Fecha: " + (cita.getFecha() != null ? cita.getFecha() : "N/A"));
        tvHora.setText("Hora: " + (cita.getHora() != null ? cita.getHora() : "N/A"));

        // Datos del titular
        if (cita.getTitular() != null && cita.getTitular().getPersona() != null) {
            tvClaveNombreTitular.setText(String.format("Titular: %s %s\nTeléfono: %s\nCorreo: %s",
                    cita.getTitular().getPersona().getNombre(),
                    cita.getTitular().getPersona().getApellidos(),
                    cita.getTitular().getTelefono() != null ? cita.getTitular().getTelefono() : "N/A",
                    cita.getTitular().getUsuario() != null ?
                            cita.getTitular().getUsuario().getCorreo() : "N/A"));
        } else {
            tvClaveNombreTitular.setText("Titular: No disponible");
        }

        // Datos del paciente
        if (cita.getPaciente() != null && cita.getPaciente().getPersona() != null) {
            tvClaveNombrePaciente.setText(String.format("Paciente: %s %s\nEdad: %d años\nPeso: %.2f kg\nSeguro: %s",
                    cita.getPaciente().getPersona().getNombre(),
                    cita.getPaciente().getPersona().getApellidos(),
                    cita.getPaciente().getEdad(),
                    cita.getPaciente().getPeso(),
                    cita.getPaciente().getSeguro() != null ?
                            cita.getPaciente().getSeguro() : "N/A"));
        } else {
            tvClaveNombrePaciente.setText("Paciente: No disponible");
        }

        tvRazonCita.setText("Razón: " + (cita.getRazonCita() != null ? cita.getRazonCita() : "N/A"));
    }

    private void loadNotas() {
        NotaService service = ApiClient.getClient().create(NotaService.class);
        Call<ApiResponse<List<Nota>>> call = service.getNotasByCita(cita.getIdCita());

        call.enqueue(new Callback<ApiResponse<List<Nota>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Nota>>> call, Response<ApiResponse<List<Nota>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Nota> notas = response.body().getData();
                    if (notas != null && !notas.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (Nota nota : notas) {
                            sb.append(nota.getDato()).append("\n\n");
                        }
                        tvNotas.setText(sb.toString().trim());
                    } else {
                        tvNotas.setText("No hay notas registradas para esta cita");
                    }
                } else {
                    String errorMsg = "Error al cargar notas";
                    if (response.body() != null) {
                        errorMsg += ": " + response.body().getMessage();
                    }
                    tvNotas.setText(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Nota>>> call, Throwable t) {
                tvNotas.setText("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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