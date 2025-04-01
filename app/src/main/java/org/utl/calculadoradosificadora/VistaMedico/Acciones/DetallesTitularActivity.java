package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import org.utl.calculadoradosificadora.model.Titular;

public class DetallesTitularActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MODIFICAR = 1;
    private TextView tvClaveTitular, tvUsuario, tvCorreo, tvTelefono, tvNombre, tvApellidos, tvGenero;
    private Button btnEliminar, btnModificar, btnOk;
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;
    private Titular titular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_titular);

        inicializarVistas();
        configurarTitular();
        configurarBotones();
        configurarMenus();
    }

    private void inicializarVistas() {
        tvClaveTitular = findViewById(R.id.tvClaveTitular);
        tvUsuario = findViewById(R.id.tvUsuario);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvNombre = findViewById(R.id.tvNombre);
        tvApellidos = findViewById(R.id.tvApellidos);
        tvGenero = findViewById(R.id.tvGenero);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnModificar = findViewById(R.id.btnModificar);
        btnOk = findViewById(R.id.btnOk);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);
    }

    private void configurarTitular() {
        titular = (Titular) getIntent().getSerializableExtra("titular");
        if (titular != null) {
            actualizarVistasConTitular();
        }
    }

    private void actualizarVistasConTitular() {
        tvClaveTitular.setText(getString(R.string.clave_titular, titular.getIdTitular()));
        tvUsuario.setText(getString(R.string.usuario, titular.getUsuario()));
        tvCorreo.setText(getString(R.string.correo, titular.getCorreo()));
        tvTelefono.setText(getString(R.string.telefono, titular.getTelefono()));
        tvNombre.setText(getString(R.string.nombres, titular.getNombre()));
        tvApellidos.setText(getString(R.string.apellidos, titular.getApellidos()));
        tvGenero.setText(getString(R.string.genero, titular.getGenero()));
    }

    private void configurarBotones() {
        btnEliminar.setOnClickListener(v -> mostrarDialogoConfirmacionEliminar());

        btnModificar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ModificarTitularActivity.class);
            intent.putExtra("titular", titular);
            startActivityForResult(intent, REQUEST_CODE_MODIFICAR);
        });

        btnOk.setOnClickListener(v -> finish());
    }

    private void mostrarDialogoConfirmacionEliminar() {
        new android.app.AlertDialog.Builder(this)
                .setTitle(R.string.eliminar)
                .setMessage(R.string.confirmar_eliminar)
                .setPositiveButton(R.string.si, (dialog, which) -> {
                    // Aquí iría la lógica para eliminar de la base de datos
                    Toast.makeText(this, R.string.titular_eliminado, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MODIFICAR && resultCode == RESULT_OK && data != null) {
            titular = (Titular) data.getSerializableExtra("titularActualizado");
            actualizarVistasConTitular();
            Toast.makeText(this, R.string.cambios_guardados, Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarMenus() {
        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            manejarMenuIzquierdo(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationViewRight.setNavigationItemSelectedListener(item -> {
            manejarMenuDerecho(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void manejarMenuIzquierdo(int id) {
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

    private void manejarMenuDerecho(int id) {
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

    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}