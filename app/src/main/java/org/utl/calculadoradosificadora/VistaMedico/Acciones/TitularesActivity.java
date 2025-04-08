package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import org.utl.calculadoradosificadora.VistaMedico.Menu.ProtocolosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SobreNosotrosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SoporteActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.NotificacionesActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.PerfilActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.SeguridadActivity;
import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.adapters.TitularesAdapter;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.TitularService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TitularesActivity extends AppCompatActivity implements TitularesAdapter.OnItemClickListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;
    private RecyclerView recyclerViewT;
    private TitularesAdapter adapter;
    private SearchView searchView;
    private ProgressBar progressBarTitular;
    private TextView tvEmptyViewT;
    private Button btnSalirT;

    private ImageView ivRegistrarTitular;
    private List<Titular> listaTitulares = new ArrayList<>();
    private Spinner spinnerFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulares);

        setupToolbarAndDrawers();
        initializeViews();
        setupSippner();
        loadTitulares();

        ivRegistrarTitular.setOnClickListener(v -> {
            Intent intent = new Intent(TitularesActivity.this, RegistrarTitularActivity.class);
            intent.putExtra("idTitular", listaTitulares.size() + 1);
            startActivity(intent);
        });
        btnSalirT.setOnClickListener(v -> {finish();});
    }

    private void initializeViews(){
        //Configurar carga titulares
        progressBarTitular = findViewById(R.id.progressBarTitular);
        tvEmptyViewT = findViewById(R.id.tvEmptyViewT);
        // Configurar el RecyclerView
        recyclerViewT = findViewById(R.id.rvTitulares);
        recyclerViewT.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TitularesAdapter(listaTitulares, this);
        recyclerViewT.setAdapter(adapter);
        // Configurar botón de registrar titular y salir
        ivRegistrarTitular = findViewById(R.id.ivRegistrarTitular);
        btnSalirT = findViewById(R.id.btnSalirT);

        // Configurar SearchView
        searchView = findViewById(R.id.searchViewTitulares);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                aplicarFiltros();
                return true;
            }
        });

    }

    private void setupSippner(){
        // Configurar Spinner de filtro
        spinnerFiltro = findViewById(R.id.spinnerFiltro);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.genero_array_titulares, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(spinnerAdapter);
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }



    private void setupToolbarAndDrawers(){
        // Configurar el menú lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        // Abrir menú izquierdo
        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Abrir menú derecho
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
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

    private void loadTitulares(){
        progressBarTitular.setVisibility(View.VISIBLE);
        tvEmptyViewT.setVisibility(View.GONE);
        recyclerViewT.setVisibility(View.GONE);

        TitularService service = ApiClient.getClient().create(TitularService.class);
        Call<ApiResponse<List<Titular>>> call = service.getAllTitulares();

        call.enqueue(new Callback<ApiResponse<List<Titular>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Titular>>> call, Response<ApiResponse<List<Titular>>> response) {
                progressBarTitular.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Titular>> apiResponse = response.body();
//                    System.out.println(apiResponse.getData());

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        listaTitulares.clear();
                        listaTitulares.addAll(apiResponse.getData());
                        adapter.updateData(listaTitulares);

                        if (listaTitulares.isEmpty()) {
                            tvEmptyViewT.setText(getString(R.string.sin_titulares));
                            tvEmptyViewT.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewT.setVisibility(View.VISIBLE);
                        }
                    } else {
                        showError(apiResponse.getMessage() != null ?
                                apiResponse.getMessage() : getString(R.string.error_cargando_titulares));
                    }
                } else {
                    showError(getString(R.string.error_cargando_titulares) + ": " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Titular>>> call, Throwable t) {
                progressBarTitular.setVisibility(View.GONE);
                showError(getString(R.string.error_cargando_titulares) + ": " + t.getMessage());
            }
        });
    }

    private void aplicarFiltros(){
        String texto = searchView.getQuery().toString();
        String generoSeleccionado = spinnerFiltro.getSelectedItem().toString();
        adapter.filtrar(texto, generoSeleccionado);
    }
    private void showError(String message) {
        tvEmptyViewT.setText(message);
        tvEmptyViewT.setVisibility(View.VISIBLE);
        recyclerViewT.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(TitularesActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public void onItemClick(Titular titular) {
        Intent intent = new Intent(TitularesActivity.this, DetallesTitularActivity.class);
        intent.putExtra("titular", titular);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTitulares();
    }
}