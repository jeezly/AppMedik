package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.model.Persona;
import org.utl.calculadoradosificadora.model.Titular;
import org.utl.calculadoradosificadora.model.Usuario;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.TitularService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarTitularActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    private EditText etCorreo, etTelefono, etNombre, etApellidos;
    private Button btnCancelar, btnGuardar;
    private Spinner spGeneroM;
    private Titular titular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_titular);

        // Obtener el titular a modificar
        titular = (Titular) getIntent().getSerializableExtra("titular");

        // Inicializar vistas
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        spGeneroM = findViewById(R.id.spGeneroM);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.generoInsert_array_titulares, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGeneroM.setAdapter(spinnerAdapter);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Mostrar datos actuales del titular
        if (titular != null) {
            etCorreo.setText(titular.getCorreo());
            etTelefono.setText(titular.getTelefono());
            etNombre.setText(titular.getNombre());
            etApellidos.setText(titular.getApellidos());
                String genero = titular.getGenero();
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spGeneroM.getAdapter();
                int posicionGenero = adapter.getPosition(genero);
            spGeneroM.setSelection(posicionGenero);
        }

        // Configurar botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancelar")
                    .setMessage("¿Deseas descartar los cambios?")
                    .setPositiveButton("Sí", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });

        // Configurar botón Guardar
        btnGuardar.setOnClickListener(v -> {
            if (validarCampos()) {

                updateTitular(fillTitular());

                Intent resultIntent = new Intent(ModificarTitularActivity.this, TitularesActivity.class);
                startActivity(resultIntent);
//                resultIntent.putExtra("titularActualizado", titular);
//                setResult(RESULT_OK, resultIntent);
//                finish();
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el menú lateral (igual que en tus otras actividades)
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        findViewById(R.id.options_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // Manejar opciones del menú izquierdo
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationViewRight.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // Manejar opciones del menú derecho
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }
    private Titular fillTitular(){
        Persona personaT = new Persona();
        personaT.setNombre(etNombre.getText().toString());
        personaT.setApellidos(etApellidos.getText().toString());
        int generoSeleccionado = spGeneroM.getSelectedItemPosition(); // 0 -> Masculino, 1 -> Femenino
        personaT.setGenero(generoSeleccionado + 1);
        personaT.setEstado(1);

        Usuario usuarioT = new Usuario();
        usuarioT.setCorreo(etCorreo.getText().toString());

        Titular titularUpdate = new Titular();
        titularUpdate.setIdTitular(titular.getIdTitular());
        titularUpdate.setTelefono(etTelefono.getText().toString());
        titularUpdate.setPersona(personaT);
        titularUpdate.setUsuario(usuarioT);

        return titularUpdate;
    }

    private void updateTitular(Titular titular){
        TitularService service = ApiClient.getClient().create(TitularService.class);
        Call<ApiResponse<Titular>> call = service.updateTitular(titular.getIdTitular(), titular);

        call.enqueue(new Callback<ApiResponse<Titular>>() {
            @Override
            public void onResponse(Call<ApiResponse<Titular>> call, Response<ApiResponse<Titular>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Titular> apiResponse = response.body();
                    if(apiResponse.isSuccess()){
                        Toast.makeText(getBaseContext(), "Titular actualizado!!", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(getBaseContext())
                                .setTitle("Actualización")
                                .setMessage("Titular actualizado con Éxito")
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

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean validarCampos() {
        return !etCorreo.getText().toString().isEmpty() &&
                !etTelefono.getText().toString().isEmpty() &&
                !etNombre.getText().toString().isEmpty() &&
                !etApellidos.getText().toString().isEmpty();
    }
}