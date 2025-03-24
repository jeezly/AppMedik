package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class DetallesCitaActivity extends AppCompatActivity {

    private TextView tvFecha, tvHora, tvRazon, tvNombreTitular, tvGenero, tvCorreo, tvTelefono;
    private Button btnReagendar, btnCancelar, btnAtender, btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cita);

        // Configurar el título de la actividad
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText("Detalle de la Cita");

        // Inicializar vistas
        tvFecha = findViewById(R.id.tvFecha);
        tvHora = findViewById(R.id.tvHora);
        tvRazon = findViewById(R.id.tvRazon);
        tvNombreTitular = findViewById(R.id.tvNombreTitular);
        tvGenero = findViewById(R.id.tvGenero);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvTelefono = findViewById(R.id.tvTelefono);
        btnReagendar = findViewById(R.id.btnReagendar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnAtender = findViewById(R.id.btnAtender);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Obtener datos de la cita desde el Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvFecha.setText("Fecha: " + extras.getString("fecha"));
            tvHora.setText("Hora: " + extras.getString("hora"));
            tvRazon.setText("Razón: " + extras.getString("razon"));
            tvNombreTitular.setText("Nombre: " + extras.getString("nombreTitular"));
            tvGenero.setText("Género: " + extras.getString("genero"));
            tvCorreo.setText("Correo: " + extras.getString("correo"));
            tvTelefono.setText("Teléfono: " + extras.getString("telefono"));
        }

        // Botón Reagendar
        btnReagendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesCitaActivity.this, AgregarCitaActivity.class);
                startActivity(intent);
            }
        });

        // Botón Cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad y regresa a la agenda
            }
        });

        // Botón Atender
        btnAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para atender la cita
            }
        });

        // Botón Regresar
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresa a la agenda
            }
        });
    }
}