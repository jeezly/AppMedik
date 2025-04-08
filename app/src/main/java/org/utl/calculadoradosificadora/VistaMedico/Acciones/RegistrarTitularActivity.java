package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import org.utl.calculadoradosificadora.MainActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.Menu.ProtocolosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SobreNosotrosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SoporteActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.NotificacionesActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.PerfilActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.SeguridadActivity;
import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.model.Persona;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.model.Usuario;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.TitularService;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarTitularActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    private TextView tvClaveTitular, tvUsuario;
    private EditText etCorreo, etTelefono, etNombre, etApellidos, etGenero;
    private Button btnCancelar, btnRegistrar;
    private int idTitular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_titular);

        // Inicializar vistas
        tvClaveTitular = findViewById(R.id.tvClaveTitular);
        tvUsuario = findViewById(R.id.tvUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etGenero = findViewById(R.id.etGenero);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Generar clave y usuario automáticos
        int claveTitular = new Random().nextInt(9000) + 1000; // Número entre 1000 y 9999
        String usuario = "usr" + claveTitular;

        tvClaveTitular.setText("Clave titular: " + claveTitular);
        tvUsuario.setText("Usuario: " + usuario);

        // Configurar botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancelar")
                    .setMessage("¿Estás seguro de que quieres cancelar el registro?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        Toast.makeText(RegistrarTitularActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Configurar botón Registrar
        btnRegistrar.setOnClickListener(v -> {
            if (validarCampos()) {
                // Crear nuevo titular
                Titular titular = new Titular(

                );

                insertTitular(fillTitularInsert());
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Registro de usuario")
                        .setMessage("Favor de llenar todos los campos para registrar el Titular")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        // Configurar el menú lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        // Abrir menú izquierdo
        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Abrir menú derecho
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        // Manejar las opciones del menú izquierdo
        navigationViewLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_inicio) {
                    startActivity(new Intent(RegistrarTitularActivity.this, VistaMedico.class));
                } else if (id == R.id.menu_protocolos) {
                    startActivity(new Intent(RegistrarTitularActivity.this, ProtocolosActivity.class));
                } else if (id == R.id.menu_sobre_nosotros) {
                    startActivity(new Intent(RegistrarTitularActivity.this, SobreNosotrosActivity.class));
                } else if (id == R.id.menu_soporte) {
                    startActivity(new Intent(RegistrarTitularActivity.this, SoporteActivity.class));
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Manejar las opciones del menú derecho
        navigationViewRight.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.opciones_perfil) {
                    startActivity(new Intent(RegistrarTitularActivity.this, PerfilActivity.class));
                } else if (id == R.id.opciones_configuracion) {
                    startActivity(new Intent(RegistrarTitularActivity.this, ConfiguracionActivity.class));
                } else if (id == R.id.opciones_seguridad) {
                    startActivity(new Intent(RegistrarTitularActivity.this, SeguridadActivity.class));
                } else if (id == R.id.opciones_notificaciones) {
                    startActivity(new Intent(RegistrarTitularActivity.this, NotificacionesActivity.class));
                } else if (id == R.id.opciones_cerrar_sesion) {
                    cerrarSesion();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }

    private boolean validarCampos() {
        return !etCorreo.getText().toString().isEmpty() &&
                !etTelefono.getText().toString().isEmpty() &&
                !etNombre.getText().toString().isEmpty() &&
                !etApellidos.getText().toString().isEmpty() &&
                !etGenero.getText().toString().isEmpty();
    }

    private void insertTitular(Titular titular){
        TitularService service = ApiClient.getClient().create(TitularService.class);
        Call<ApiResponse<Titular>> call = service.insertTitular(titular);

        call.enqueue(new Callback<ApiResponse<Titular>>() {
            @Override
            public void onResponse(Call<ApiResponse<Titular>> call, Response<ApiResponse<Titular>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Titular> apiResponse = response.body();
                    if(apiResponse.isSuccess()){
                        Toast.makeText(getBaseContext(), "Titular insertado!!", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(getBaseContext())
                                .setTitle("Registro de usuario")
                                .setMessage("Titular registrado con Éxito")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    finish();
                                })
                                .show();
                    }else{
                        showError(apiResponse.getMessage());
                        Log.e("API ERROR", "Error en la respuesta: " + apiResponse.getMessage());
                    }
                }else {
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

    private Titular fillTitularInsert(){
        idTitular = (int) getIntent().getSerializableExtra("idTitular");
        //System.out.println(idTitular);
        //Obtenemos los datos de persona
        Persona personaTitular = new Persona();
        //personaTitular.setIdPersona();
        personaTitular.setNombre(etNombre.getText().toString());
        personaTitular.setApellidos(etApellidos.getText().toString());
//        int generoSeleccionado = spGenero.getSelectedItemPosition(); // 0 -> Masculino, 1 -> Femenino
//        personaTitular.setGenero(generoSeleccionado + 1); // 1 -> Masculino, 2 -> Femenino
        personaTitular.setGenero(etGenero.getText().length());
        personaTitular.setEstado(1);
        //Obtenemos los datos de usuario
        Usuario usuarioTitular = new Usuario();
        //usuarioTitular.setIdUsuario(0);
        usuarioTitular.setUsuario(tvUsuario.getText().toString());
        usuarioTitular.setCorreo(etCorreo.getText().toString());
        usuarioTitular.setContrasenia("default");
        //usuarioTitular.setIdPersona();
        usuarioTitular.setToken(null);

        //Cargamos el modelo Titular
        Titular titularInsert = new Titular();
        titularInsert.setIdTitular(idTitular);
        titularInsert.setTelefono(etTelefono.getText().toString());
        titularInsert.setPersona(personaTitular);
        titularInsert.setUsuario(usuarioTitular);
        return titularInsert;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(RegistrarTitularActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}