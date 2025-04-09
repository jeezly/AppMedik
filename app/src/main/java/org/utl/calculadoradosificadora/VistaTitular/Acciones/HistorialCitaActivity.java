package org.utl.calculadoradosificadora.VistaTitular.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaTitular.VistaTitular;

public class HistorialCitaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cita_titular);

        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarAVistaTitular();
            }
        });
    }

    private void regresarAVistaTitular() {
        Intent intent = new Intent(this, VistaTitular.class);
        startActivity(intent);
        finish(); // Opcional: cierra esta actividad para que no quede en la pila
    }
}