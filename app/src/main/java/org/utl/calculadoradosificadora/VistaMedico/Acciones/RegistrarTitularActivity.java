package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import org.utl.calculadoradosificadora.model.Titular;

import java.util.Random;

public class RegistrarTitularActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    private TextView tvClaveTitular, tvUsuario;
    private EditText etCorreo, etTelefono, etNombre, etApellidos, etGenero;
    private Button btnCancelar, btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_titular);

        // Inicializar vistas
        tvClaveTitular = findViewById(R.id.tvClaveTitular);
        tvUsuario = findViewById(R.id.tvUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etGenero = findViewById(R.id.etGenero);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Generar clave y usuario automáticos
        int claveTitular = new Random().nextInt(9000) + 1000; // Número entre 1000 y 9999
        String usuario = "usr" + claveTitular;

        tvClaveTitular.setText("Clave titular: " + claveTitular);
        tvUsuario.setText("Usuario: " + usuario);

        // Configurar botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancelar")
                    .setMessage("¿Estás seguro de que quieres cancelar el registro?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        Toast.makeText(RegistrarTitularActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Configurar botón Registrar
        btnRegistrar.setOnClickListener(v -> {
            if (validarCampos()) {
                // Crear nuevo titular
                Titular titular = new Titular(
                        claveTitular,
                        etNombre.getText().toString(),
                        etApellidos.getText().toString(),
                        etCorreo.getText().toString(),
                        etTelefono.getText().toString(),
                        usuario,
                        etGenero.getText().toString()
                );

                // Aquí deberías guardar el titular en tu base de datos
                // Por ahora solo mostramos un mensaje

                new AlertDialog.Builder(this)
                        .setTitle("Registro de usuario")
                        .setMessage("Titular registrado con Éxito su usuario es: " + usuario)
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Regresar a la actividad de titulares
                            Intent intent = new Intent(RegistrarTitularActivity.this, TitularesActivity.class);
                            intent.putExtra("nuevoTitular", titular);
                            startActivity(intent);
                            finish();
                        })
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Registro de usuario")
                        .setMessage("Favor de llenar todos los campos para registrar el Titular")
                        .setPositiveButton("OK", null)
                        .show();
            }
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
                    startActivity(new Intent(RegistrarTitularActivity.this, VistaMedico.class));
                } else if (id == R.id.menu_protocolos) {
                    startActivity(new Intent(RegistrarTitularActivity.this, ProtocolosActivity.class));
                } else if (id == R.id.menu_sobre_nosotros) {
                    startActivity(new Intent(RegistrarTitularActivity.this, SobreNosotrosActivity.class));
                } else if (id == R.id.menu_soporte) {
                    startActivity(new Intent(RegistrarTitularActivity.this, SoporteActivity.class));
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
                    startActivity(new Intent(RegistrarTitularActivity.this, PerfilActivity.class));
                } else if (id == R.id.opciones_configuracion) {
                    startActivity(new Intent(RegistrarTitularActivity.this, ConfiguracionActivity.class));
                } else if (id == R.id.opciones_seguridad) {
                    startActivity(new Intent(RegistrarTitularActivity.this, SeguridadActivity.class));
                } else if (id == R.id.opciones_notificaciones) {
                    startActivity(new Intent(RegistrarTitularActivity.this, NotificacionesActivity.class));
                } else if (id == R.id.opciones_cerrar_sesion) {
                    cerrarSesion();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }

    private boolean validarCampos() {
        return !etCorreo.getText().toString().isEmpty() &&
                !etTelefono.getText().toString().isEmpty() &&
                !etNombre.getText().toString().isEmpty() &&
                !etApellidos.getText().toString().isEmpty() &&
                !etGenero.getText().toString().isEmpty();
    }

    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(RegistrarTitularActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}