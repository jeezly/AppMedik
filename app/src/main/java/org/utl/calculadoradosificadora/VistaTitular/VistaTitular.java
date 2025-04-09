package org.utl.calculadoradosificadora.VistaTitular;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.utl.calculadoradosificadora.MainActivity;
import org.utl.calculadoradosificadora.R;


import org.utl.calculadoradosificadora.VistaTitular.Acciones.AgendarCitasActivity;
import org.utl.calculadoradosificadora.VistaTitular.Acciones.HistorialCitaActivity;
import org.utl.calculadoradosificadora.VistaTitular.Menu.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaTitular.Menu.PerfilActivity;
import org.utl.calculadoradosificadora.VistaTitular.Menu.SoporteAyudaTitularActivity;
import org.utl.calculadoradosificadora.VistaTitular.Opciones.InformacionUtilActivity;
import org.utl.calculadoradosificadora.model.Persona;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.model.Usuario;

public class VistaTitular extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_titular);

        // Configura el Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);


        guardarTitular(crearTitularTEMPORAL());

        // Abre el menú izquierdo al hacer clic en el ícono de 3 líneas
        findViewById(R.id.menu_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Abre el menú derecho al hacer clic en el ícono de 3 puntos
        findViewById(R.id.options_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // Maneja las opciones del menú izquierdo
        navigationViewLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_inicio) {
                    startActivity(new Intent(VistaTitular.this, VistaTitular.class));
                } else if (id == R.id.menu_informacion_util) {
                    startActivity(new Intent(VistaTitular.this, InformacionUtilActivity.class));
                } else if (id == R.id.menu_soporte) {
                    startActivity(new Intent(VistaTitular.this, SoporteAyudaTitularActivity.class));
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Maneja las opciones del menú derecho
        navigationViewRight.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.opciones_perfil) {
                    startActivity(new Intent(VistaTitular.this, PerfilActivity.class));
                } else if (id == R.id.opciones_configuracion) {
                    startActivity(new Intent(VistaTitular.this, ConfiguracionActivity.class));
                } else if (id == R.id.opciones_cerrar_sesion) {
                    cerrarSesion();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        // Navegación a Agendar Cita
        CardView cardAgendarCita = findViewById(R.id.cardAgendarCita);
        cardAgendarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VistaTitular.this, AgendarCitasActivity.class);
                startActivity(intent);
            }
        });

        // Navegación a Historial de Citas
        CardView cardHistorialCitas = findViewById(R.id.cardHistorialCitas);
        cardHistorialCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VistaTitular.this, HistorialCitaActivity.class);
                startActivity(intent);
            }
        });
    }

    private Titular crearTitularTEMPORAL(){
        // Primero creamos la Persona
        Persona persona = new Persona(
                1,                      // idPersona
                "Juan",                 // nombre
                "Pérez López",          // apellidos
                1,                      // genero (1: masculino)
                1                       // estado (1: activo)
        );

// Luego creamos el Usuario asociado
        Usuario usuario = new Usuario(
                1,                    // idUsuario
                "juanperez",           // usuario
                "juan@example.com", // correo
                "password123",   // contrasenia
                1,                      // idPersona (debe coincidir con idPersona anterior)
                ""   // token
        );

// Finalmente creamos el Titular
        Titular titular = new Titular(
                1,                   // idTitular
                "5512345678",           // telefono
                persona,                // objeto Persona
                usuario                 // objeto Usuario
        );
        return titular;
    }
    private void guardarTitular(Titular titular) {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convertir el objeto Titular a JSON
        String titularJson = new Gson().toJson(titular);

        // Guardar el JSON en SharedPreferences
        editor.putString("titular", titularJson);
        editor.apply(); // o editor.commit() para guardado sincrónico
    }

    private void cerrarSesion() {
        // Limpiar preferencias de sesión
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirigir a la pantalla de inicio
        Intent intent = new Intent(VistaTitular.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}