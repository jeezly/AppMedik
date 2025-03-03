package org.utl.calculadoradosificadora.VistaMedico.CuentaNueva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.BienvenidaActivity;
import org.utl.calculadoradosificadora.R;

public class CrearMedicoDos extends AppCompatActivity {

    private EditText etUsuario, etCorreo, etContrasenia, etCedula;
    private Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_medico_dos);

        // Referencias a los componentes
        etUsuario = findViewById(R.id.etUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasenia = findViewById(R.id.etContrasenia);
        etCedula = findViewById(R.id.etCedula);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString().trim();
                String correo = etCorreo.getText().toString().trim();
                String contrasenia = etContrasenia.getText().toString().trim();
                String cedulaStr = etCedula.getText().toString().trim();

                // Validar que ningún campo esté vacío
                if (usuario.isEmpty() || correo.isEmpty() || contrasenia.isEmpty() || cedulaStr.isEmpty()) {
                    Toast.makeText(CrearMedicoDos.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar que la cédula sea un número válido
                int cedula;
                try {
                    cedula = Integer.parseInt(cedulaStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(CrearMedicoDos.this, "La cédula debe ser un número válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Si todo está correcto, redirigir a BienvenidaActivity
                Intent intent = new Intent(CrearMedicoDos.this, BienvenidaActivity.class);
                intent.putExtra("tipoUsuario", "Medico"); // Tipo de usuario
                intent.putExtra("nombreUsuario", usuario); // Nombre del usuario
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });
    }
}