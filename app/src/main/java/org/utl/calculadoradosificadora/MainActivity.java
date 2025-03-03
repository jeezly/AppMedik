package org.utl.calculadoradosificadora;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.VistaTitular.VistaTitular;
import org.utl.calculadoradosificadora.VistaTitular.CuentaNueva.CuentaNueva;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtén las referencias a los componentes
        EditText txtUsername = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
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

        // Configura el clic en el botón de inicio de sesión
        btnLogin.setOnClickListener(v -> {
            String username = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            // Verifica las credenciales
            if (username.equals("Medico") && password.equals("1234")) {
                // Credenciales correctas para médico, redirige a VistaMedico
                Intent intent = new Intent(MainActivity.this, VistaMedico.class);
                startActivity(intent);
                finish(); // Opcional: cierra la actividad actual
            } else if (username.equals("Titular") && password.equals("5678")) {
                // Credenciales correctas para titular, redirige a VistaTitular
                Intent intent = new Intent(MainActivity.this, VistaTitular.class);
                startActivity(intent);
                finish(); // Opcional: cierra la actividad actual
            } else {
                // Credenciales incorrectas, muestra un mensaje de error
                Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}