package org.utl.calculadoradosificadora.VistaMedico.Opciones;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        RadioGroup rgTema = findViewById(R.id.rgTema);
        Spinner spinnerIdioma = findViewById(R.id.spinnerIdioma);
        Button btnGuardar = findViewById(R.id.btnGuardarConfig);

        // Configurar spinner de idiomas
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.idiomas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            // Guardar configuración
            int temaSeleccionado = rgTema.getCheckedRadioButtonId();
            String idioma = spinnerIdioma.getSelectedItem().toString();

            // Aquí iría la lógica para guardar las preferencias
            finish();
        });
    }
}