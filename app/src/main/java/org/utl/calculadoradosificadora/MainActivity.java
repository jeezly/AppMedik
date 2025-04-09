package org.utl.calculadoradosificadora;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.VistaTitular.CuentaNueva.CuentaNueva;
import org.utl.calculadoradosificadora.VistaTitular.VistaTitular;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtén las referencias a los componentes
        Button btnMedico = findViewById(R.id.btnMedico);
        Button btnTitular = findViewById(R.id.btnTitular);
        TextView tvCreateAccount = findViewById(R.id.textView2);

        // Configura el texto subrayado para "Crear una cuenta"
        SpannableString content = new SpannableString("Crear una cuenta");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvCreateAccount.setText(content);
        tvCreateAccount.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

        // Configura el clic en "Crear una cuenta"
        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CuentaNueva.class);
            startActivity(intent);
        });

        // Configura los botones para navegar directamente a las vistas
        btnMedico.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginMedico.class);
            startActivity(intent);
        });

        btnTitular.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginTitular.class);
            startActivity(intent);
        });
    }
}
