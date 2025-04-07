package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

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
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.CitaService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaActivity extends AppCompatActivity implements CitaAdapter.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;
    private RecyclerView recyclerViewCitas;
    private CitaAdapter citaAdapter;
    private ProgressBar progressBar;
    private TextView tvEmptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Cita> citasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        // Configurar Toolbar y Drawers
        setupToolbarAndDrawers();

        // Inicializar vistas
        initializeViews();

        // Configurar RecyclerView
        setupRecyclerView();

        // Configurar SwipeRefresh
        setupSwipeRefresh();

        // Cargar citas
        loadCitas();

        // Configurar botones
        setupButtons();
    }

    private void setupToolbarAndDrawers() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        // Configurar menú izquierdo
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // Configurar menú derecho
        findViewById(R.id.options_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END));

        // Manejar opciones del menú izquierdo
        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleLeftMenuSelection(id);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Manejar opciones del menú derecho
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
            // Implementar lógica de cierre de sesión
            finish();
        }
    }

    private void initializeViews() {
        recyclerViewCitas = findViewById(R.id.recyclerViewCitas);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyView = findViewById(R.id.tvEmptyView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    private void setupRecyclerView() {
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(this));
        citaAdapter = new CitaAdapter(citasList, this);
        recyclerViewCitas.setAdapter(citaAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadCitas();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupButtons() {
        // Botón verde para agregar cita
        Button btnAgregarCita = findViewById(R.id.btnAgregarCita);
        btnAgregarCita.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        btnAgregarCita.setOnClickListener(v ->
                startActivity(new Intent(this, AgregarCitaActivity.class)));

        // Botón rojo para regresar
        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setBackgroundColor(getResources().getColor(R.color.colorRed));
        btnRegresar.setOnClickListener(v -> {
            startActivity(new Intent(this, VistaMedico.class));
            finish();
        });
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
                Log.d("API_RESPONSE", "Código: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Cita>> apiResponse = response.body();

                    if (apiResponse.getData() != null) {
                        Log.d("API_RESPONSE", "Citas recibidas: " + apiResponse.getData().size());
                    } else {
                        Log.d("API_RESPONSE", "Data es null");
                    }

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        citasList.clear();
                        citasList.addAll(apiResponse.getData());
                        citaAdapter.notifyDataSetChanged();

                        if (citasList.isEmpty()) {
                            tvEmptyView.setText("No hay citas programadas");
                            tvEmptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewCitas.setVisibility(View.VISIBLE);
                        }
                    } else {
                        String errorMsg = apiResponse.getMessage() != null ?
                                apiResponse.getMessage() : "Error en la respuesta del servidor";
                        showError(errorMsg);
                    }
                } else {
                    String errorMsg = "Error al obtener citas: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error al leer errorBody", e);
                    }
                    showError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Cita>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                String errorMsg = "Error de conexión: " + t.getMessage();
                showError(errorMsg);
                Log.e("API_ERROR", "Error en loadCitas", t);
            }
        });
    }

    private void showError(String message) {
        tvEmptyView.setText(message);
        tvEmptyView.setVisibility(View.VISIBLE);
        recyclerViewCitas.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e("AgendaActivity", message);
    }

    @Override
    public void onItemClick(Cita cita) {
        Intent intent = new Intent(this, DetallesCitaActivity.class);
        intent.putExtra("cita", cita);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCitas();
    }
}