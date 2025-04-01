package org.utl.calculadoradosificadora.VistaMedico.Opciones;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Medico;

public class PerfilActivity extends AppCompatActivity {

    private EditText etCedula, etNombre, etApellidos, etCorreo, etTelefono;
    private Button btnSalir, btnCancelar, btnModificar;
    private ImageView ivFotoPerfil;
    private boolean modoEdicion = false;
    private Medico medico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Obtener datos del médico (esto debería venir de tu base de datos/sesión)
        medico = new Medico(1, "Juan", "Pérez", true, 1, "", 12345678);

        inicializarVistas();
        cargarDatosMedico();
        configurarBotones();
    }

    private void inicializarVistas() {
        etCedula = findViewById(R.id.etCedula);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        btnSalir = findViewById(R.id.btnSalir);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnModificar = findViewById(R.id.btnModificar);
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
    }

    private void cargarDatosMedico() {
        etCedula.setText(String.valueOf(medico.getNumCedula()));
        etNombre.setText(medico.getNombre());
        etApellidos.setText(medico.getApellidos());
        // Estos campos deberían venir de tu objeto Usuario asociado al Médico
        etCorreo.setText("juan.perez@example.com");
        etTelefono.setText("4771234567");
    }

    private void configurarBotones() {
        btnSalir.setOnClickListener(v -> finish());

        btnModificar.setOnClickListener(v -> {
            if (modoEdicion) {
                // Guardar cambios
                medico.setNombre(etNombre.getText().toString());
                medico.setApellidos(etApellidos.getText().toString());
                // Actualizar otros campos según sea necesario

                Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
                btnModificar.setText("Modificar");
                modoEdicion = false;
                deshabilitarEdicion();
            } else {
                modoEdicion = true;
                btnModificar.setText("Guardar");
                habilitarEdicion();
            }
        });

        btnCancelar.setOnClickListener(v -> {
            if (modoEdicion) {
                cargarDatosMedico(); // Restaurar valores originales
                btnModificar.setText("Modificar");
                modoEdicion = false;
                deshabilitarEdicion();
            }
        });
    }

    private void habilitarEdicion() {
        etNombre.setEnabled(true);
        etApellidos.setEnabled(true);
        etCorreo.setEnabled(true);
        etTelefono.setEnabled(true);
        btnCancelar.setVisibility(View.VISIBLE);
    }

    private void deshabilitarEdicion() {
        etNombre.setEnabled(false);
        etApellidos.setEnabled(false);
        etCorreo.setEnabled(false);
        etTelefono.setEnabled(false);
        btnCancelar.setVisibility(View.GONE);
    }
}