package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;

public class AgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda); // Asegúrate de que el layout esté correctamente vinculado

        // Configurar el título de la actividad
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText("Agenda");

        // Botón para agregar una nueva cita
        Button btnAgregarCita = findViewById(R.id.btnAgregarCita);
        btnAgregarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgendaActivity.this, AgregarCitaActivity.class);
                startActivity(intent);
            }
        });

        // Botón para regresar a VistaMedico
        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgendaActivity.this, VistaMedico.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });

        // Lista de citas (puedes usar un adaptador personalizado)
        ListView listCitas = findViewById(R.id.listCitas);
        // Configura el adaptador para la lista de citas
    }
}