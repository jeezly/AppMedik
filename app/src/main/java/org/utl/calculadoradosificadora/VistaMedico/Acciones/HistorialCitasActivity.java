package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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
import org.utl.calculadoradosificadora.model.Cita;
import org.utl.calculadoradosificadora.model.Paciente;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.CitaService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistorialCitasActivity extends AppCompatActivity {

    private RecyclerView rvHistorialCitas;
    private SearchView searchView;
    private Button btnSalir;
    private HistorialCitasAdapter adapter;
    private List<Cita> listaCitas;

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_citas);

        // Inicializar vistas
        rvHistorialCitas = findViewById(R.id.rvHistorialCitas);
        searchView = findViewById(R.id.searchView);
        btnSalir = findViewById(R.id.btnSalir);

        listaCitas = new ArrayList<>();

        // Configura el RecyclerView desde ya con el adapter vacío
        adapter = new HistorialCitasAdapter(listaCitas);
        // Configurar RecyclerView
        rvHistorialCitas.setLayoutManager(new LinearLayoutManager(this));
        rvHistorialCitas.setAdapter(adapter);

        // Obtener datos de prueba
        //listaCitas = obtenerCitasDePrueba();
        obtenerCitasAPI();

        // Configurar SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filtrar(newText);
                return true;
            }
        });

        // Configurar botón de salir //NO JALA :')
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresar a VistaMedico
            }
        });

        // Configurar el menú lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        // Abrir menú izquierdo
        findViewById(R.id.menu_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Abrir menú derecho
        findViewById(R.id.options_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // Manejar las opciones del menú izquierdo
        navigationViewLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_inicio) {
                    startActivity(new Intent(HistorialCitasActivity.this, VistaMedico.class));
                } else if (id == R.id.menu_protocolos) {
                    startActivity(new Intent(HistorialCitasActivity.this, ProtocolosActivity.class));
                } else if (id == R.id.menu_sobre_nosotros) {
                    startActivity(new Intent(HistorialCitasActivity.this, SobreNosotrosActivity.class));
                } else if (id == R.id.menu_soporte) {
                    startActivity(new Intent(HistorialCitasActivity.this, SoporteActivity.class));
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Manejar las opciones del menú derecho
        navigationViewRight.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.opciones_perfil) {
                    startActivity(new Intent(HistorialCitasActivity.this, PerfilActivity.class));
                } else if (id == R.id.opciones_configuracion) {
                    startActivity(new Intent(HistorialCitasActivity.this, ConfiguracionActivity.class));
                } else if (id == R.id.opciones_seguridad) {
                    startActivity(new Intent(HistorialCitasActivity.this, SeguridadActivity.class));
                } else if (id == R.id.opciones_notificaciones) {
                    startActivity(new Intent(HistorialCitasActivity.this, NotificacionesActivity.class));
                } else if (id == R.id.opciones_cerrar_sesion) {
                    cerrarSesion();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }

    private void cerrarSesion() {
        // Limpiar preferencias de sesión
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirigir a la pantalla de inicio
        Intent intent = new Intent(HistorialCitasActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Elimina el historial de actividades
        startActivity(intent);
        finish();
    }

    private void obtenerCitasAPI(){
        Retrofit retrofit = ApiClient.getClient();
        CitaService servie = retrofit.create(CitaService.class);

        Call<List<Cita>> getAll = servie.getAllCita();
        getAll.enqueue(new Callback<List<Cita>>() {
            @Override
            public void onResponse(Call<List<Cita>> call, Response<List<Cita>> response) {
                Toast.makeText(HistorialCitasActivity.this, "GetAll realizado", Toast.LENGTH_SHORT).show();
                System.out.println(response.code());
                System.out.println(response.body());

                if (response.isSuccessful() && response.body() != null) {
                    listaCitas.clear(); // Limpia lista vieja
                    listaCitas.addAll(response.body()); // Agrega nuevas
                    adapter.notifyDataSetChanged(); // Notifica cambio
                    Toast.makeText(HistorialCitasActivity.this, "Citas cargadas", Toast.LENGTH_SHORT).show();
                }
                if (listaCitas == null) {
                    listaCitas = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<Cita>> call, Throwable t) {
                System.out.println("onFailure API");
                t.printStackTrace();
            }
        });
    }
    // Método para obtener datos de prueba
    private List<Cita> obtenerCitasDePrueba() {
        List<Cita> citas = new ArrayList<>();
        citas.add(new Cita(1, "2023-10-01", "10:00", "Control", "Atendida", new Paciente(1, "Juan", "Pérez", true, 1, "2000-01-01", 30.5, "Seguro Popular")));
        citas.add(new Cita(2, "2023-10-02", "11:00", "Revisión", "Cancelada", new Paciente(2, "Ana", "Gómez", false, 2, "2005-05-05", 25.0, "IMSS")));
        return citas;
    }
}