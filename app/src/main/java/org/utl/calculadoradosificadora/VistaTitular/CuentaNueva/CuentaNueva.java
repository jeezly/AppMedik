package org.utl.calculadoradosificadora.VistaTitular.CuentaNueva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.utl.calculadoradosificadora.MainActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.CuentaNueva.CrearCuentaMedico;

public class CuentaNueva extends AppCompatActivity {

    private Button btnRegresar;
    private CardView cardTitular, cardMedico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta_nueva);

        btnRegresar = findViewById(R.id.btnRegresar);
        cardTitular = findViewById(R.id.cardTitular);
        cardMedico = findViewById(R.id.cardMedico);

        // Listener para el botón de regresar
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CuentaNueva.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });

        // Listener para la opción de Titular
        cardTitular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CuentaNueva.this, CrearCuentaTitular.class);
                startActivity(intent);
            }
        });

        // Listener para la opción de Médico
        cardMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CuentaNueva.this, CrearCuentaMedico.class);
                startActivity(intent);
            }
        });
    }
}