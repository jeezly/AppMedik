package org.utl.calculadoradosificadora.VistaTitular.Menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.R;

public class SoporteAyudaTitularActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte_titular);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Configurar contacto 1
        TextView tvContact1 = findViewById(R.id.tvContact1);
        tvContact1.setOnClickListener(v -> callNumber("4771234567"));

        TextView tvEmail1 = findViewById(R.id.tvEmail1);
        tvEmail1.setOnClickListener(v -> sendEmail("a.casillas@utl.edu.mx"));

        // Configurar contacto 2
        TextView tvContact2 = findViewById(R.id.tvContact2);
        tvContact2.setOnClickListener(v -> callNumber("4772345678"));

        TextView tvEmail2 = findViewById(R.id.tvEmail2);
        tvEmail2.setOnClickListener(v -> sendEmail("j.garcia@utl.edu.mx"));

        // Configurar contacto 3
        TextView tvContact3 = findViewById(R.id.tvContact3);
        tvContact3.setOnClickListener(v -> callNumber("4773456789"));

        TextView tvEmail3 = findViewById(R.id.tvEmail3);
        tvEmail3.setOnClickListener(v -> sendEmail("h.gonzalez@utl.edu.mx"));

        // Configurar contacto 4
        TextView tvContact4 = findViewById(R.id.tvContact4);
        tvContact4.setOnClickListener(v -> callNumber("4774567890"));

        TextView tvEmail4 = findViewById(R.id.tvEmail4);
        tvEmail4.setOnClickListener(v -> sendEmail("j.velazquez@utl.edu.mx"));

        // Configurar contacto 5
        TextView tvContact5 = findViewById(R.id.tvContact5);
        tvContact5.setOnClickListener(v -> callNumber("4775678901"));

        TextView tvEmail5 = findViewById(R.id.tvEmail5);
        tvEmail5.setOnClickListener(v -> sendEmail("m.ramirez@utl.edu.mx"));
    }

    private void callNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void sendEmail(String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        startActivity(intent);
    }
}