package org.utl.calculadoradosificadora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.model.InicioSesion;
import org.utl.calculadoradosificadora.model.Medico;
import org.utl.calculadoradosificadora.model.Persona;
import org.utl.calculadoradosificadora.model.Usuario;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.MedicoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                validarInicioS();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginMedico), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void validarInicioS(){
        InicioSesion request = new InicioSesion();
        request.setUsuario(etUsuarioM.getText().toString());
        request.setContrasenia(etContraseniaM.getText().toString());


        MedicoService service = ApiClient.getClient().create(MedicoService.class);
        Call<ApiResponse<Medico>> call = service.loginMedico(request);
        call.enqueue(new Callback<ApiResponse<Medico>>() {
            @Override
            public void onResponse(Call<ApiResponse<Medico>> call, Response<ApiResponse<Medico>> response) {
                if(response.isSuccessful() && response.body() != null){
                    ApiResponse<Medico> apiResponse = response.body();
                    if(apiResponse.isSuccess()){
                        guardarMedico(apiResponse.getData());
                        navegarAPantallaPrincipal();
                    }else {
                        showError("Usuario y/o contraseña incorrectos");

                        showError(apiResponse.getMessage());
                        Log.e("API ERROR", "Error en la respuesta: " + apiResponse.getMessage());
                    }
                }else{
                    try {
                        showError("Usuario y/o contraseña incorrectos");
                        etUsuarioM.setText("");
                        etContraseniaM.setText("");
//                        String errorBody = response.errorBody() != null ?
//                                response.errorBody().string() : "empty error body";
//                        showError("Error del servidor: " + errorBody);
//                        Log.e("API_ERROR", "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error al leer errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Medico>> call, Throwable t) {
                showError("Error de red: " + t.getMessage());
                Log.e("API_ERROR", "Error de red", t);
            }
        });
    }

    private void guardarMedico(Medico medico) {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convertir el objeto Titular a JSON
        String medicoJson = new Gson().toJson(medico);

        // Guardar el JSON en SharedPreferences
        editor.putString("medico", medicoJson);
        //editor.putInt("id_medico", medico.getIdMedico());
        editor.putString("nombre", medico.getNombreCompleto());
        editor.putString("especialidad", medico.getEspecialidad());
        editor.putString("correo", medico.getUsuario().getCorreo());
        editor.apply(); // o editor.commit() para guardado sincrónico
    }

    private void navegarAPantallaPrincipal(){
        //Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginMedico.this, VistaMedico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("medico", medico);
        startActivity(intent);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Medico crearMedicoTEMPORAL(){
        // Primero creamos la Persona
        Persona persona = new Persona(
                3,                      // idPersona
                "Carlos",                 // nombre
                "Gómez",          // apellidos
                1,                      // genero (1: masculino)
                1                       // estado (1: activo)
        );

// Luego creamos el Usuario asociado
        Usuario usuario = new Usuario(
                2,                    // idUsuario
                "carlosgomez",           // usuario
                "carlos@example.com", // correo
                "password123",   // contrasenia
                3,                      // idPersona (debe coincidir con idPersona anterior)
                ""   // token
        );

// Finalmente creamos el Titular
        Medico medico = new Medico(
                1,                   // idTitular
                "foto_medico.jpg",           // telefono
                "123456",
                persona,                // objeto Persona
                usuario                 // objeto Usuario
        );
        return medico;
    }
}