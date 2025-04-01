package org.utl.calculadoradosificadora.VistaMedico.Opciones;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class NotificacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        Switch switchNotificaciones = findViewById(R.id.switchNotificaciones);
        Switch switchNotifCitas = findViewById(R.id.switchNotifCitas);
        Switch switchRecordatorios = findViewById(R.id.switchRecordatorios);
        Switch switchSonido = findViewById(R.id.switchSonido);
        Switch switchVibracion = findViewById(R.id.switchVibracion);
        Button btnGuardar = findViewById(R.id.btnGuardarNotificaciones);

        btnGuardar.setOnClickListener(v -> {
            // Guardar preferencias de notificaciones
            boolean notificacionesActivas = switchNotificaciones.isChecked();
            boolean notifCitasActivas = switchNotifCitas.isChecked();
            boolean recordatoriosActivos = switchRecordatorios.isChecked();
            boolean sonidoActivo = switchSonido.isChecked();
            boolean vibracionActiva = switchVibracion.isChecked();

            // Aquí iría la lógica para guardar estas preferencias
            Toast.makeText(this, "Configuración de notificaciones guardada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}