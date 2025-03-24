package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

import java.util.Calendar;

public class AgregarCitaActivity extends AppCompatActivity {

    private EditText etBuscarTitular, etRazon, etFecha;
    private Spinner spHorarios;
    private Button btnAgendar, btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cita);

        // Configurar el título de la actividad
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText("Agendar Cita");

        // Inicializar vistas
        etBuscarTitular = findViewById(R.id.etBuscarTitular);
        etRazon = findViewById(R.id.etRazon);
        etFecha = findViewById(R.id.etFecha);
        spHorarios = findViewById(R.id.spHorarios);
        btnAgendar = findViewById(R.id.btnAgendar);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Selector de fecha
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AgregarCitaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Botón Agendar
        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para guardar la cita
                String titular = etBuscarTitular.getText().toString();
                String razon = etRazon.getText().toString();
                String fecha = etFecha.getText().toString();
                String horario = spHorarios.getSelectedItem().toString();

                // Guardar la cita en tu backend o base de datos
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