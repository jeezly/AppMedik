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
import org.utl.calculadoradosificadora.VistaTitular.VistaTitular;
import org.utl.calculadoradosificadora.model.InicioSesion;
import org.utl.calculadoradosificadora.model.Medico;
import org.utl.calculadoradosificadora.model.Persona;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.model.Usuario;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.MedicoService;
import org.utl.calculadoradosificadora.service.TitularService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                validarInicioS();
                navegarAPantallaPrincipal();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginTitular), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void validarInicioS(){
        InicioSesion request = new InicioSesion();
        request.setUsuario(etUsuarioT.getText().toString());
        request.setContrasenia(etContraseniaT.getText().toString());


        TitularService service = ApiClient.getClient().create(TitularService.class);
        Call<ApiResponse<Titular>> call = service.loginTitular(request);
        call.enqueue(new Callback<ApiResponse<Titular>>() {
            @Override
            public void onResponse(Call<ApiResponse<Titular>> call, Response<ApiResponse<Titular>> response) {
                if(response.isSuccessful() && response.body() != null){
                    ApiResponse<Titular> apiResponse = response.body();
                    if(apiResponse.isSuccess()){
                        guardarTitular(apiResponse.getData());
                    }else {
                        showError(apiResponse.getMessage());
                        Log.e("API ERROR", "Error en la respuesta: " + apiResponse.getMessage());
                    }
                }else{
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "empty error body";
                        showError("Error del servidor: " + errorBody);
                        Log.e("API_ERROR", "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error al leer errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Titular>> call, Throwable t) {
                showError("Error de red: " + t.getMessage());
                Log.e("API_ERROR", "Error de red", t);
            }
        });
    }
    private void guardarTitular(Titular titular) {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convertir el objeto Titular a JSON
        String titularJson = new Gson().toJson(titular);

        // Guardar el JSON en SharedPreferences
        editor.putString("titular", titularJson);
        //editor.putInt("id_medico", medico.getIdMedico());
        editor.putString("nombre", titular.getNombre());
        editor.putString("telefono", titular.getTelefono());
        editor.putString("correo", titular.getUsuario().getCorreo());
        editor.apply(); // o editor.commit() para guardado sincrónico
    }

    private void navegarAPantallaPrincipal(){
        //Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginTitular.this, VistaTitular.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("titular", titular);
        startActivity(intent);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private Titular crearTitularTEMPORAL(){
        // Primero creamos la Persona
        Persona persona = new Persona(
                1,                      // idPersona
                "Juan",                 // nombre
                "Pérez López",          // apellidos
                1,                      // genero (1: masculino)
                1                       // estado (1: activo)
        );

// Luego creamos el Usuario asociado
        Usuario usuario = new Usuario(
                1,                    // idUsuario
                "juanperez",           // usuario
                "juan@example.com", // correo
                "password123",   // contrasenia
                1,                      // idPersona (debe coincidir con idPersona anterior)
                ""   // token
        );

// Finalmente creamos el Titular
        Titular titular = new Titular(
                1,                   // idTitular
                "5512345678",           // telefono
                persona,                // objeto Persona
                usuario                 // objeto Usuario
        );
        return titular;
    }
}