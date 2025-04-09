package org.utl.calculadoradosificadora.VistaTitular.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.adapters.CitaAdapter;
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialCitaActivity extends AppCompatActivity implements CitaAdapter.OnItemClickListener {

    private static final String TAG = "HistorialCitaTitular";
    private RecyclerView recyclerViewCitas;
    private CitaAdapter citaAdapter;
    private ProgressBar progressBar;
    private TextView tvEmptyView;
    private Spinner spinnerFiltroEstatus;
    private Button btnFiltrar;

    private List<Cita> citasList = new ArrayList<>();
    private List<Cita> citasFiltradas = new ArrayList<>();
    private String filtroEstatusSeleccionado = "Todas";
    private Titular titularActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cita_titular);

        titularActual = obtenerTitularActual();
        if (titularActual == null) {
            Toast.makeText(this, "Error: No se pudo obtener información del titular", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Log.d(TAG, "Titular actual ID: " + titularActual.getIdTitular());

        initializeViews();
        setupRecyclerView();
        setupSpinner();
        setupButtons();
        loadCitas();
    }

    private Titular obtenerTitularActual() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        String titularJson = preferences.getString("titular", "");
        if (!titularJson.isEmpty()) {
            return new Gson().fromJson(titularJson, Titular.class);
        }
        return null;
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

                        // Filtrar citas del titular actual
                        for (Cita cita : apiResponse.getData()) {
                            if (cita.getTitular() != null &&
                                    cita.getTitular().getIdTitular() == titularActual.getIdTitular()) {
                                citasList.add(cita);
                            }
                        }

                        citasFiltradas.clear();
                        citasFiltradas.addAll(citasList);
                        citaAdapter.updateData(citasFiltradas);
                        checkEmptyState();

                        Log.d(TAG, "Citas cargadas: " + citasList.size());
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
                Log.e(TAG, "Error al cargar citas", t);
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

    @Override
    public void onItemClick(Cita cita) {
        Intent intent = new Intent(this, DetallesHistorialCitaTitularActivity.class);
        intent.putExtra("cita", cita);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCitas();
    }
}