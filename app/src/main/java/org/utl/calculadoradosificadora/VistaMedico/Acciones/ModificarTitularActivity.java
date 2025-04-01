package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Titular;

public class ModificarTitularActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    private EditText etCorreo, etTelefono, etNombre, etApellidos, etGenero;
    private Button btnCancelar, btnGuardar;
    private Titular titular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_titular);

        // Obtener el titular a modificar
        titular = (Titular) getIntent().getSerializableExtra("titular");

        // Inicializar vistas
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etGenero = findViewById(R.id.etGenero);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Mostrar datos actuales del titular
        if (titular != null) {
            etCorreo.setText(titular.getCorreo());
            etTelefono.setText(titular.getTelefono());
            etNombre.setText(titular.getNombre());
            etApellidos.setText(titular.getApellidos());
            etGenero.setText(titular.getGenero());
        }

        // Configurar botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancelar")
                    .setMessage("¿Deseas descartar los cambios?")
                    .setPositiveButton("Sí", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });

        // Configurar botón Guardar
        btnGuardar.setOnClickListener(v -> {
            if (validarCampos()) {
                // Actualizar datos del titular
                titular.setCorreo(etCorreo.getText().toString());
                titular.setTelefono(etTelefono.getText().toString());
                titular.setNombre(etNombre.getText().toString());
                titular.setApellidos(etApellidos.getText().toString());
                titular.setGenero(etGenero.getText().toString());

                // Aquí deberías actualizar el titular en tu base de datos
                // Por ahora solo mostramos un mensaje y regresamos

                Toast.makeText(this, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("titularActualizado", titular);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el menú lateral (igual que en tus otras actividades)
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // Manejar opciones del menú izquierdo
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationViewRight.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // Manejar opciones del menú derecho
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private boolean validarCampos() {
        return !etCorreo.getText().toString().isEmpty() &&
                !etTelefono.getText().toString().isEmpty() &&
                !etNombre.getText().toString().isEmpty() &&
                !etApellidos.getText().toString().isEmpty() &&
                !etGenero.getText().toString().isEmpty();
    }
}