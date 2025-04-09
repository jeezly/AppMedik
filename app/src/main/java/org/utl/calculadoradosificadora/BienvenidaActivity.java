package org.utl.calculadoradosificadora;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.VistaTitular.VistaTitular;

public class BienvenidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        // Obtén el tipo de usuario (médico o titular) desde el Intent
        String tipoUsuario = getIntent().getStringExtra("tipoUsuario");
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");

        // Configura el mensaje de bienvenida
        TextView tvBienvenida = findViewById(R.id.tvBienvenida);
        if (tipoUsuario.equals("Medico")) {
            tvBienvenida.setText("Bienvenido seas Médico " + nombreUsuario);
        } else if (tipoUsuario.equals("Titular")) {
            tvBienvenida.setText("Bienvenido seas Titular " + nombreUsuario);
        }

        // Redirige a la vista correspondiente después de 3 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (tipoUsuario.equals("Medico")) {
                    intent = new Intent(BienvenidaActivity.this, VistaMedico.class);
                } else {
                    intent = new Intent(BienvenidaActivity.this, VistaTitular.class);
                }
                startActivity(intent);
                finish(); // Cierra la actividad de bienvenida
            }
        }, 3000); // 3 segundos de delay
    }
}