package org.utl.calculadoradosificadora.VistaTitular;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class AgendarCitasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_citas);

        // Configurar el botón de Cancelar Cita
        Button btnCancelarCita = findViewById(R.id.btnCancelarCita);
        btnCancelarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresar a la vista anterior (VistaTitular)
            }
        });

        // Obtener el contenedor de la lista de doctores
        LinearLayout listaDoctores = findViewById(R.id.listaDoctores);

        // Agregar doctores de ejemplo
        agregarDoctor(listaDoctores, "Dr. Juan Pérez", "Masculino", "12345678", R.drawable.ic_user_doctor_male);
        agregarDoctor(listaDoctores, "Dra. María López", "Femenino", "87654321", R.drawable.ic_user_doctor_female);
    }

    private void agregarDoctor(LinearLayout listaDoctores, String nombre, String genero, String cedula, int foto) {
        // Inflar el layout de cada doctor
        View itemDoctor = LayoutInflater.from(this).inflate(R.layout.item_doctor, listaDoctores, false);

        // Configurar los datos del doctor
        TextView tvNombreDoctor = itemDoctor.findViewById(R.id.tvNombreDoctor);
        TextView tvGeneroDoctor = itemDoctor.findViewById(R.id.tvGeneroDoctor);
        TextView tvCedulaDoctor = itemDoctor.findViewById(R.id.tvCedulaDoctor);
        Button btnAgendarCita = itemDoctor.findViewById(R.id.btnAgendarCita);

        tvNombreDoctor.setText(nombre);
        tvGeneroDoctor.setText("Género: " + genero);
        tvCedulaDoctor.setText("Cédula: " + cedula);
        btnAgendarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para agendar cita
            }
        });
        btnAgendarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pasar los datos del médico seleccionado a la siguiente actividad
                Intent intent = new Intent(AgendarCitasActivity.this, AgendarCitasDosActivity.class);
                intent.putExtra("nombreDoctor", nombre);
                intent.putExtra("generoDoctor", genero);
                intent.putExtra("cedulaDoctor", cedula);
                intent.putExtra("fotoDoctor", foto);
                startActivity(intent);
            }
        });

        // Agregar el doctor a la lista
        listaDoctores.addView(itemDoctor);
    }

}