package org.utl.calculadoradosificadora.VistaMedico.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import org.utl.calculadoradosificadora.R;

public class ProtocolosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocolos);

        Button btnEntendido = findViewById(R.id.btnEntendido);
        btnEntendido.setOnClickListener(v -> finish());
    }
}