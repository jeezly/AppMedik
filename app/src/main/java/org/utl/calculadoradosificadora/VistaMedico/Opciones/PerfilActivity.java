package org.utl.calculadoradosificadora.VistaMedico.Opciones;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class PerfilActivity extends AppCompatActivity {

    private ImageView ivFotoPerfil;
    private Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar vistas
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
        btnSalir = findViewById(R.id.btnSalir);  // Asegúrate de que el ID coincida con el XML

        // Configurar el botón para regresar
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Cierra esta actividad y regresa a VistaMedico
            }
        });
    }
}