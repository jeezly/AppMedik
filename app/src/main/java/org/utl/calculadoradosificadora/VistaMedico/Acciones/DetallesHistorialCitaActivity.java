package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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

public class DetallesHistorialCitaActivity extends AppCompatActivity {

    private TextView tvFecha, tvHora, tvClaveNombreTitular, tvClaveNombrePaciente, tvRazonCita, tvNotas;
    private Button btnOk;

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_historial_cita);

        // Inicializar vistas
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvClaveNombreTitular = findViewById(R.id.tvClaveNombreTitular);
        tvClaveNombrePaciente = findViewById(R.id.tvClaveNombrePaciente);
        tvRazonCita = findViewById(R.id.tvRazonCita);
        tvNotas = findViewById(R.id.tvNotas);
        btnOk = findViewById(R.id.btnOk);

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
                    startActivity(new Intent(DetallesHistorialCitaActivity.this, VistaMedico.class));
                } else if (id == R.id.menu_protocolos) {
                    startActivity(new Intent(DetallesHistorialCitaActivity.this, ProtocolosActivity.class));
                } else if (id == R.id.menu_sobre_nosotros) {
                    startActivity(new Intent(DetallesHistorialCitaActivity.this, SobreNosotrosActivity.class));
                } else if (id == R.id.menu_soporte) {
                    startActivity(new Intent(DetallesHistorialCitaActivity.this, SoporteActivity.class));
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
                    startActivity(new Intent(DetallesHistorialCitaActivity.this, PerfilActivity.class));
                } else if (id == R.id.opciones_configuracion) {
                    startActivity(new Intent(DetallesHistorialCitaActivity.this, ConfiguracionActivity.class));
                } else if (id == R.id.opciones_seguridad) {
                    startActivity(new Intent(DetallesHistorialCitaActivity.this, SeguridadActivity.class));
                } else if (id == R.id.opciones_notificaciones) {
                    startActivity(new Intent(DetallesHistorialCitaActivity.this, NotificacionesActivity.class));
                } else if (id == R.id.opciones_cerrar_sesion) {
                    cerrarSesion();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        // Obtener la cita seleccionada
        Cita cita = (Cita) getIntent().getSerializableExtra("cita");

        // Mostrar los datos de la cita
        if (cita != null) {
            tvFecha.setText("Fecha: " + cita.getFecha());
            tvHora.setText("Hora: " + cita.getHora());
            tvClaveNombreTitular.setText("Clave: " + cita.getPaciente().getIdPaciente() + " - Nombre: " + cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos());
            tvClaveNombrePaciente.setText("Clave: " + cita.getPaciente().getIdPaciente() + " - Nombre: " + cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos() + " - Fecha Nac: " + cita.getPaciente().getFechaNacimiento() + " - Peso: " + cita.getPaciente().getPeso() + " kg");
            tvRazonCita.setText("Razón: " + cita.getRazonCita());
            tvNotas.setText("Notas: " + cita.getRazonCita()); // Aquí puedes cambiar "Notas" por otro campo si es necesario
        }

        // Configurar botón "OK"
        btnOk.setOnClickListener(v -> finish()); // Regresar al historial de citas
    }

    private void cerrarSesion() {
        // Limpiar preferencias de sesión
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirigir a la pantalla de inicio
        Intent intent = new Intent(DetallesHistorialCitaActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Elimina el historial de actividades
        startActivity(intent);
        finish();
    }
}