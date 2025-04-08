package org.utl.calculadoradosificadora.VistaTitular.Acciones;

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

import org.utl.calculadoradosificadora.MainActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaTitular.VistaTitular;
import org.utl.calculadoradosificadora.adapters.CitaAdapter;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialCitaActivity extends AppCompatActivity implements CitaAdapter.OnItemClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cita_titular);

        setupToolbarAndDrawers();
        initializeViews();
        setupRecyclerView();
        setupSpinner();
        setupButtons();
        loadCitas();
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
            if (id == R.id.menu_inicio) {
                startActivity(new Intent(this, VistaTitular.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationViewRight.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.opciones_cerrar_sesion) {
                cerrarSesion();
            }
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
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
            citasFiltradas.addAll(citasList.stream()
                    .filter(c -> filtroEstatusSeleccionado.equalsIgnoreCase(c.getEstatus()))
                    .collect(Collectors.toList()));
        }

        citaAdapter.updateData(citasFiltradas);

        if (citasFiltradas.isEmpty()) {
            tvEmptyView.setText(getString(R.string.sin_citas_historial));
            tvEmptyView.setVisibility(View.VISIBLE);
            recyclerViewCitas.setVisibility(View.GONE);
        } else {
            tvEmptyView.setVisibility(View.GONE);
            recyclerViewCitas.setVisibility(View.VISIBLE);
        }
    }

    private void loadCitas() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyView.setVisibility(View.GONE);
        recyclerViewCitas.setVisibility(View.GONE);

        // Obtener el ID del titular desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        int idTitular = preferences.getInt("idUsuario", 0);

        CitaService service = ApiClient.getClient().create(CitaService.class);
        Call<ApiResponse<List<Cita>>> call = service.getCitasByTitular(idTitular);

        call.enqueue(new Callback<ApiResponse<List<Cita>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Cita>>> call, Response<ApiResponse<List<Cita>>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Cita>> apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        citasList.clear();
                        // Filtrar solo citas atendidas o canceladas (no programadas)
                        citasList.addAll(apiResponse.getData().stream()
                                .filter(c -> !"Programada".equalsIgnoreCase(c.getEstatus()))
                                .collect(Collectors.toList()));

                        citasFiltradas.clear();
                        citasFiltradas.addAll(citasList);
                        citaAdapter.updateData(citasFiltradas);

                        if (citasFiltradas.isEmpty()) {
                            tvEmptyView.setText(getString(R.string.sin_citas_historial));
                            tvEmptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewCitas.setVisibility(View.VISIBLE);
                        }
                    } else {
                        showError(apiResponse.getMessage() != null ?
                                apiResponse.getMessage() : getString(R.string.error_cargando_citas));
                    }
                } else {
                    showError(getString(R.string.error_cargando_citas) + ": " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Cita>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError(getString(R.string.error_cargando_citas) + ": " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        tvEmptyView.setText(message);
        tvEmptyView.setVisibility(View.VISIBLE);
        recyclerViewCitas.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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