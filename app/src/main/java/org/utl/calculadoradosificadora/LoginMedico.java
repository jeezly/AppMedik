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
import org.utl.calculadoradosificadora.model.Medico;

public class LoginMedico extends AppCompatActivity {
    private EditText etContraseniaM, etUsuarioM;
    private Button btnLoginM;
    private Medico medico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_medico);

        etUsuarioM = findViewById(R.id.etUsuarioM);
        etContraseniaM = findViewById(R.id.etContraseniaM);
        btnLoginM = findViewById(R.id.btnLoginM);

        btnLoginM.setOnClickListener(v -> {
            String usuario = etUsuarioM.getText().toString();
            String contrasenia = etContraseniaM.getText().toString();

            if(usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                //API LOG ING
                Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginMedico.this, VistaMedico.class);
                intent.putExtra("medico", medico);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginMedico), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}