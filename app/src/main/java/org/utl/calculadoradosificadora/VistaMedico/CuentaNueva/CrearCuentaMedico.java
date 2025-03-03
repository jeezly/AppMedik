package org.utl.calculadoradosificadora.VistaMedico.CuentaNueva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.R;

public class CrearCuentaMedico extends AppCompatActivity {
    private EditText etNombre, etApellido;
    private Spinner spGenero;
    private Button btnSiguiente;
    private boolean generoSeleccionado; // true = Hombre, false = Mujer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_medico);

        // Referencias a los componentes
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        spGenero = findViewById(R.id.spGenero);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        // Configurar Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genero_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero.setAdapter(adapter);

        spGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                generoSeleccionado = position == 0; // 0 = Hombre, 1 = Mujer
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Botón Siguiente
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().isEmpty() ||
                        etApellido.getText().toString().isEmpty()) {
                    Toast.makeText(CrearCuentaMedico.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CrearCuentaMedico.this, CrearMedicoDos.class);
                    startActivity(intent);
                }
            }
        });
    }
}
