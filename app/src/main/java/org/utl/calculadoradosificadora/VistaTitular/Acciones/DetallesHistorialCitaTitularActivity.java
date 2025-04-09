package org.utl.calculadoradosificadora.VistaTitular.Acciones;

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
import org.utl.calculadoradosificadora.VistaTitular.Menu.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaTitular.Opciones.InformacionUtilActivity;
import org.utl.calculadoradosificadora.VistaTitular.Menu.PerfilActivity;
import org.utl.calculadoradosificadora.VistaTitular.Menu.SoporteAyudaTitularActivity;
import org.utl.calculadoradosificadora.VistaTitular.VistaTitular;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Titular;

public class DetallesHistorialCitaTitularActivity extends AppCompatActivity {

    private TextView tvFecha, tvHora, tvClaveNombreTitular, tvClaveNombrePaciente, tvRazonCita, tvNotas;
    private Button btnOk;
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;
    private Cita cita;
    private Titular titularActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_historial_cita_titular);

        titularActual = obtenerTitularActual();
        if (titularActual == null) {
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

        cita = (Cita) getIntent().getSerializableExtra("cita");

        if (cita != null) {
            mostrarDatosCita();
        } else {
            finish();
        }

        btnOk.setOnClickListener(v -> finish());
    }

    private Titular obtenerTitularActual() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        String titularJson = preferences.getString("titular", "");
        if (!titularJson.isEmpty()) {
            return new Gson().fromJson(titularJson, Titular.class);
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
                startActivity(new Intent(this, VistaTitular.class));
            } else if (id == R.id.menu_informacion_util) {
                startActivity(new Intent(this, InformacionUtilActivity.class));
            } else if (id == R.id.menu_soporte) {
                startActivity(new Intent(this, SoporteAyudaTitularActivity.class));
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

        if (cita.getTitular() != null) {
            tvClaveNombreTitular.setText(String.format("Titular: %s %s",
                    cita.getTitular().getNombre() != null ? cita.getTitular().getNombre() : "",
                    cita.getTitular().getApellidos() != null ? cita.getTitular().getApellidos() : ""));
        } else {
            tvClaveNombreTitular.setText("Titular: No disponible");
        }

        if (cita.getPaciente() != null) {
            tvClaveNombrePaciente.setText(String.format("Paciente: %s %s - Edad: %d años - Peso: %.2f kg",
                    cita.getPaciente().getNombre() != null ? cita.getPaciente().getNombre() : "",
                    cita.getPaciente().getApellidos() != null ? cita.getPaciente().getApellidos() : "",
                    cita.getPaciente().getEdad(),
                    cita.getPaciente().getPeso()));
        } else {
            tvClaveNombrePaciente.setText("Paciente: No disponible");
        }

        tvRazonCita.setText("Razón: " + (cita.getRazonCita() != null ? cita.getRazonCita() : "N/A"));

        if (cita.getNota() != null && !cita.getNota().isEmpty()) {
            tvNotas.setText(cita.getNota());
        } else {
            tvNotas.setText("No hay notas registradas para esta cita");
        }
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