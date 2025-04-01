package org.utl.calculadoradosificadora.VistaMedico.Opciones;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class SeguridadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguridad);

        EditText etContraseniaActual = findViewById(R.id.etContraseniaActual);
        EditText etNuevaContrasenia = findViewById(R.id.etNuevaContrasenia);
        EditText etConfirmarContrasenia = findViewById(R.id.etConfirmarContrasenia);
        Button btnCambiarContrasenia = findViewById(R.id.btnCambiarContrasenia);
        Switch switchAutenticacion = findViewById(R.id.switchAutenticacionDosFactores);
        Button btnVerSesiones = findViewById(R.id.btnVerSesiones);

        btnCambiarContrasenia.setOnClickListener(v -> {
            String nueva = etNuevaContrasenia.getText().toString();
            String confirmacion = etConfirmarContrasenia.getText().toString();

            if (!nueva.equals(confirmacion)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lógica para cambiar contraseña
            Toast.makeText(this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
        });

        switchAutenticacion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "Autenticación de dos factores activada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Autenticación de dos factores desactivada", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerSesiones.setOnClickListener(v -> {
            // Lógica para mostrar historial de sesiones
            Toast.makeText(this, "Mostrando historial de sesiones", Toast.LENGTH_SHORT).show();
        });
    }
}