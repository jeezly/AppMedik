package org.utl.calculadoradosificadora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.model.Titular;

public class LoginTitular extends AppCompatActivity {
    private EditText etUsuarioT, etContraseniaT;
    private Button btnLoginT;
    private Titular titular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_titular);

        etUsuarioT = findViewById(R.id.etUsuarioT);
        etContraseniaT = findViewById(R.id.etContraseniaT);
        btnLoginT = findViewById(R.id.btnLoginT);

        btnLoginT.setOnClickListener(v -> {
            String usuario = etUsuarioT.getText().toString();
            String contrasenia = etContraseniaT.getText().toString();

            if(usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                //API LOG ING
                Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginTitular.this, VistaMedico.class);
                intent.putExtra("titular", titular);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginTitular), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}