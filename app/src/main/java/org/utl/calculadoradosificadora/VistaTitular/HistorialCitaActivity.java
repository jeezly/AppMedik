package org.utl.calculadoradosificadora.VistaTitular;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.utl.calculadoradosificadora.R;

public class HistorialCitaActivity extends AppCompatActivity {

    Button btnSalir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_cita_titular);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.historial_cita_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSalir = findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(view -> {
            Intent intent = new Intent(HistorialCitaActivity.this, DetallesHistorialCitaActivity.class);
            startActivity(intent);
//            Intent intent = new Intent(HistorialCitaActivity.this, VistaTitular.class);
//            startActivity(intent);
        });
    }
}