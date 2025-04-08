package org.utl.calculadoradosificadora.VistaTitular.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.adapters.HorarioAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeleccionarHorarioActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHorarios;
    private HorarioAdapter horarioAdapter;
    private Button btnCancelar, btnAgendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_citas_tres);

        // Recibir datos de la actividad anterior
        Intent intent = getIntent();
        String nombreDoctor = intent.getStringExtra("nombreDoctor");
        String generoDoctor = intent.getStringExtra("generoDoctor");
        String cedulaDoctor = intent.getStringExtra("cedulaDoctor");
        int fotoDoctor = intent.getIntExtra("fotoDoctor", R.drawable.ic_user_doctor_male);
        String razonCita = intent.getStringExtra("razonCita");
        String fechaCita = intent.getStringExtra("fechaCita");

        // Configurar RecyclerView

        recyclerViewHorarios.setLayoutManager(new LinearLayoutManager(this));

        // Lista de horarios disponibles
        List<String> horarios = new ArrayList<>();
        horarios.add("12:00 PM - 12:30 PM");
        horarios.add("01:00 PM - 01:30 PM");
        horarios.add("02:00 PM - 02:30 PM");
        horarios.add("03:00 PM - 03:30 PM");

        // Configurar el adaptador
        horarioAdapter = new HorarioAdapter(horarios);
        recyclerViewHorarios.setAdapter(horarioAdapter);



        // Acción para el botón Cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresa a la actividad anterior
            }
        });

        // Acción para el botón Agendar
        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horarioSeleccionado = horarioAdapter.getHorarioSeleccionado();
                if (horarioSeleccionado != null) {
                    // Crear el Intent y pasar todos los datos a la cuarta actividad
                    Intent confirmarIntent = new Intent(SeleccionarHorarioActivity.this, ConfirmarCitaActivity.class);
                    confirmarIntent.putExtra("nombreDoctor", nombreDoctor);
                    confirmarIntent.putExtra("generoDoctor", generoDoctor);
                    confirmarIntent.putExtra("cedulaDoctor", cedulaDoctor);
                    confirmarIntent.putExtra("fotoDoctor", fotoDoctor);
                    confirmarIntent.putExtra("razonCita", razonCita);
                    confirmarIntent.putExtra("fechaCita", fechaCita);
                    confirmarIntent.putExtra("horarioSeleccionado", horarioSeleccionado);
                    startActivity(confirmarIntent);
                } else {
                    // Mostrar un mensaje de que no se ha seleccionado un horario
                    Toast.makeText(SeleccionarHorarioActivity.this, "Selecciona un horario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}