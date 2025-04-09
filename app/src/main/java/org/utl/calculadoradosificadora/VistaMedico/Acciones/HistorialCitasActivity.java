package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import org.utl.calculadoradosificadora.adapters.CitaAdapter;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Medico;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialCitasActivity extends AppCompatActivity implements CitaAdapter.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;
    private RecyclerView recyclerViewCitas;
    private CitaAdapter citaAdapter;
    private ProgressBar progressBar;
    private TextView tvEmptyView;
    private Spinner spinnerFiltroEstatus;
    private Button btnFiltrar;

    private List<Cita> citasList = new ArrayList<>();
    private List<Cita> citasFiltradas = new ArrayList<>();
    private String filtroEstatusSeleccionado = "Todas";
    private Medico medicoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_citas);

        medicoActual = obtenerMedicoActual();
        if (medicoActual == null) {
            Toast.makeText(this, "Error: No se pudo obtener información del médico", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setupToolbarAndDrawers();
        initializeViews();
        setupRecyclerView();
        setupSpinner();
        setupButtons();
        loadCitas();
    }

    private Medico obtenerMedicoActual() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        String medicoJson = preferences.getString("medico", "");
        if (!medicoJson.isEmpty()) {
            return new Gson().fromJson(medicoJson, Medico.class);
        }
        return null;
    }

    private void setupToolbarAndDrawers() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        findViewById(R.id.options_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END));

        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleLeftMenuSelection(id);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationViewRight.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleRightMenuSelection(id);
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void handleLeftMenuSelection(int id) {
        if (id == R.id.menu_inicio) {
            startActivity(new Intent(this, VistaMedico.class));
        } else if (id == R.id.menu_protocolos) {
            startActivity(new Intent(this, ProtocolosActivity.class));
        } else if (id == R.id.menu_sobre_nosotros) {
            startActivity(new Intent(this, SobreNosotrosActivity.class));
        } else if (id == R.id.menu_soporte) {
            startActivity(new Intent(this, SoporteActivity.class));
        }
    }

    private void handleRightMenuSelection(int id) {
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
    }

    private void initializeViews() {
        recyclerViewCitas = findViewById(R.id.rvHistorialCitas);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyView = findViewById(R.id.tvEmptyView);
        spinnerFiltroEstatus = findViewById(R.id.spinnerFiltroEstatus);
        btnFiltrar = findViewById(R.id.btnFiltrar);
    }

    private void setupRecyclerView() {
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(this));
        citaAdapter = new CitaAdapter(citasFiltradas, this);
        recyclerViewCitas.setAdapter(citaAdapter);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapterEstatus = ArrayAdapter.createFromResource(this,
                R.array.filtros_citas_historial, android.R.layout.simple_spinner_item);
        adapterEstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroEstatus.setAdapter(adapterEstatus);

        spinnerFiltroEstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtroEstatusSeleccionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filtroEstatusSeleccionado = "Todas";
            }
        });
    }

    private void setupButtons() {
        btnFiltrar.setOnClickListener(v -> aplicarFiltros());

        Button btnRegresar = findViewById(R.id.btnSalir);
        btnRegresar.setOnClickListener(v -> finish());
    }

    private void aplicarFiltros() {
        citasFiltradas.clear();

        if (filtroEstatusSeleccionado.equals("Todas")) {
            citasFiltradas.addAll(citasList);
        } else {
            for (Cita cita : citasList) {
                if (cita.getEstatus() != null &&
                        cita.getEstatus().equalsIgnoreCase(filtroEstatusSeleccionado)) {
                    citasFiltradas.add(cita);
                }
            }
        }

        citaAdapter.updateData(citasFiltradas);
        checkEmptyState();
    }

    private void loadCitas() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyView.setVisibility(View.GONE);
        recyclerViewCitas.setVisibility(View.GONE);

        CitaService service = ApiClient.getClient().create(CitaService.class);
        Call<ApiResponse<List<Cita>>> call = service.getAllCitas();

        call.enqueue(new Callback<ApiResponse<List<Cita>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Cita>>> call, Response<ApiResponse<List<Cita>>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Cita>> apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        citasList.clear();

                        // Filtrar citas del médico actual y que no estén programadas
                        for (Cita cita : apiResponse.getData()) {
                            if (cita.getMedico() != null &&
                                    cita.getMedico().getIdMedico() == medicoActual.getIdMedico() &&
                                    !"Programada".equalsIgnoreCase(cita.getEstatus())) {
                                citasList.add(cita);
                            }
                        }

                        citasFiltradas.clear();
                        citasFiltradas.addAll(citasList);
                        citaAdapter.updateData(citasFiltradas);
                        checkEmptyState();
                    } else {
                        showError(apiResponse.getMessage() != null ?
                                apiResponse.getMessage() : "Error al cargar citas");
                    }
                } else {
                    showError("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Cita>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void checkEmptyState() {
        if (citasFiltradas.isEmpty()) {
            tvEmptyView.setText("No hay citas en el historial");
            tvEmptyView.setVisibility(View.VISIBLE);
            recyclerViewCitas.setVisibility(View.GONE);
        } else {
            tvEmptyView.setVisibility(View.GONE);
            recyclerViewCitas.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String message) {
        tvEmptyView.setText(message);
        tvEmptyView.setVisibility(View.VISIBLE);
        recyclerViewCitas.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
    public void onItemClick(Cita cita) {
        Intent intent = new Intent(this, DetallesHistorialCitaActivity.class);
        intent.putExtra("cita", cita);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCitas();
    }
}