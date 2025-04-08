package org.utl.calculadoradosificadora.VistaMedico.Opciones;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class SeguridadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguridad);

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> {
            // Simplemente finaliza esta actividad para volver a la anterior
            finish();
        });
    }
}