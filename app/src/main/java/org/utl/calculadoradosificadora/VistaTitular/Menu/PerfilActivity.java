package org.utl.calculadoradosificadora.VistaTitular.Menu;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.R;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Button btnBack = findViewById(R.id.btnSalir);
        btnBack.setOnClickListener(v -> finish());
    }
}