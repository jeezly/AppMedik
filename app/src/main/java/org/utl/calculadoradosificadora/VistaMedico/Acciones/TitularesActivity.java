package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import org.utl.calculadoradosificadora.model.Titular;

import java.util.ArrayList;
import java.util.List;

public class TitularesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;
    private RecyclerView recyclerView;
    private TitularesAdapter adapter;
    private List<Titular> listaTitulares;
    private ImageView ivRegistrarTitular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulares);

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.rvTitulares);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de titulares (esto debería venir de tu base de datos)
        listaTitulares = new ArrayList<>();
        listaTitulares.add(new Titular(1, "Juan", "Pérez", "juan@example.com", "5551234567", "juan123", "Hombre"));
        listaTitulares.add(new Titular(2, "María", "Gómez", "maria@example.com", "5557654321", "maria456", "Mujer"));

        adapter = new TitularesAdapter(listaTitulares);
        recyclerView.setAdapter(adapter);

        // Configurar SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filtrar(newText);
                return false;
            }
        });

        // Configurar Spinner de filtro
        Spinner spinnerFiltro = findViewById(R.id.spinnerFiltro);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.genero_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(spinnerAdapter);

        // Configurar botón de registrar titular
        ivRegistrarTitular = findViewById(R.id.ivRegistrarTitular);
        ivRegistrarTitular.setOnClickListener(v -> {
            Intent intent = new Intent(TitularesActivity.this, RegistrarTitularActivity.class);
            startActivity(intent);
        });

        // Configurar el menú lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        // Abrir menú izquierdo
        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Abrir menú derecho
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        // Manejar las opciones del menú izquierdo
        navigationViewLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_inicio) {
                    startActivity(new Intent(TitularesActivity.this, VistaMedico.class));
                } else if (id == R.id.menu_protocolos) {
                    startActivity(new Intent(TitularesActivity.this, ProtocolosActivity.class));
                } else if (id == R.id.menu_sobre_nosotros) {
                    startActivity(new Intent(TitularesActivity.this, SobreNosotrosActivity.class));
                } else if (id == R.id.menu_soporte) {
                    startActivity(new Intent(TitularesActivity.this, SoporteActivity.class));
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
                    startActivity(new Intent(TitularesActivity.this, PerfilActivity.class));
                } else if (id == R.id.opciones_configuracion) {
                    startActivity(new Intent(TitularesActivity.this, ConfiguracionActivity.class));
                } else if (id == R.id.opciones_seguridad) {
                    startActivity(new Intent(TitularesActivity.this, SeguridadActivity.class));
                } else if (id == R.id.opciones_notificaciones) {
                    startActivity(new Intent(TitularesActivity.this, NotificacionesActivity.class));
                } else if (id == R.id.opciones_cerrar_sesion) {
                    cerrarSesion();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
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
}