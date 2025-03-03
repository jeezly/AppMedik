package org.utl.calculadoradosificadora.VistaTitular.CuentaNueva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.BienvenidaActivity;
import org.utl.calculadoradosificadora.R;

public class CrearTitularDos extends AppCompatActivity {

    private EditText etUsuario, etCorreo, etContrasenia, etTelefono;
    private Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_titular_dos);

        // Referencias a los componentes
        etUsuario = findViewById(R.id.etUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasenia = findViewById(R.id.etContrasenia);
        etTelefono = findViewById(R.id.etTelefono);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        // Botón Crear Cuenta
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar campos
                if (etUsuario.getText().toString().isEmpty() ||
                        etCorreo.getText().toString().isEmpty() ||
                        etContrasenia.getText().toString().isEmpty() ||
                        etTelefono.getText().toString().isEmpty()) {
                    Toast.makeText(CrearTitularDos.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Redirigir a BienvenidaActivity
                    Intent intent = new Intent(CrearTitularDos.this, BienvenidaActivity.class);
                    intent.putExtra("tipoUsuario", "Titular"); // Tipo de usuario
                    intent.putExtra("nombreUsuario", etUsuario.getText().toString()); // Nombre del usuario
                    startActivity(intent);
                    finish(); // Cierra la actividad actual
                }
            }
        });
    }
}