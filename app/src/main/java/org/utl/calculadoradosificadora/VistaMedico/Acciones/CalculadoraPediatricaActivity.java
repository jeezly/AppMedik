package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.utl.calculadoradosificadora.MainActivity;
import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.NotificacionesActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.PerfilActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.SeguridadActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.ProtocolosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SobreNosotrosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SoporteActivity;
import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.model.Medicamento;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.MedicamentoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CalculadoraPediatricaActivity extends AppCompatActivity {
    private Spinner spinnerMedicamentos, spinnerEdad, spinnerGenero;
    private EditText etPeso;
    private Button btnCalcular, btnCancelar, btnBuscarMedicamento;
    private TextView tvResultado;
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft, navigationViewRight;
    private ArrayAdapter<Medicamento> adapterMedicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_pediatrica); // Asegúrate de tener este layout

        initViews();
        setupToolbarAndMenus();
        setupSpinners();
        setupButtons();
    }


    private void setupToolbarAndMenus() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        // Abrir menú izquierdo
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // Abrir menú derecho
        findViewById(R.id.options_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END));

        // Manejar opciones del menú izquierdo
        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_inicio) {
                startActivity(new Intent(this, VistaMedico.class));
            } else if (id == R.id.menu_protocolos) {
                startActivity(new Intent(this, ProtocolosActivity.class));
            } else if (id == R.id.menu_sobre_nosotros) {
                startActivity(new Intent(this, SobreNosotrosActivity.class));
            } else if (id == R.id.menu_soporte) {
                startActivity(new Intent(this, SoporteActivity.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Manejar opciones del menú derecho
        navigationViewRight.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.opciones_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
            } else if (id == R.id.opciones_configuracion) {
                startActivity(new Intent(this, ConfiguracionActivity.class));
            } else if (id == R.id.opciones_seguridad) {
                startActivity(new Intent(this, SeguridadActivity.class));
            } else if (id == R.id.opciones_notificaciones) {
                startActivity(new Intent(this, NotificacionesActivity.class));
            } else if (id == R.id.opciones_cerrar_sesion) {
                cerrarSesion();
            }
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void initViews() {
        spinnerMedicamentos = findViewById(R.id.spinnerMedicamentos);
        spinnerEdad = findViewById(R.id.spinnerEdad);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        etPeso = findViewById(R.id.etPeso);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnBuscarMedicamento = findViewById(R.id.btnBuscarMedicamento);
        tvResultado = findViewById(R.id.tvResultado);
    }

    private void setupSpinners() {
        //getAllMedicamentos();
//        listaMedicamentos = new ArrayList<>();
//        listaMedicamentos.add("Paracetamol (120mg/5ml)");
//        listaMedicamentos.add("Ibuprofeno (100mg/5ml)");
//        listaMedicamentos.add("Amoxicilina (250mg/5ml)");
//        listaMedicamentos.add("Dexametasona (0.5mg/ml)");
//        listaMedicamentos.add("Salbutamol (2mg/5ml)");
//
//        adapterMedicamentos = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, listaMedicamentos);
//        adapterMedicamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerMedicamentos.setAdapter(adapterMedicamentos);

        // Configurar spinner de edad
        ArrayAdapter<CharSequence> adapterEdad = ArrayAdapter.createFromResource(this,
                R.array.edad_array, android.R.layout.simple_spinner_item);
        adapterEdad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEdad.setAdapter(adapterEdad);

        // Configurar spinner de género
        ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(this,
                R.array.genero_array, android.R.layout.simple_spinner_item);
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGenero);
    }

    private void setupButtons() {
        // Botón de búsqueda de medicamento
        btnBuscarMedicamento.setOnClickListener(v -> {
            // Implementar lógica de búsqueda/filtrado
            Toast.makeText(this, "Funcionalidad de búsqueda en desarrollo", Toast.LENGTH_SHORT).show();
        });

        // Botón de cancelar
        btnCancelar.setOnClickListener(v -> {
            // Limpiar campos
            spinnerMedicamentos.setSelection(0);
            spinnerEdad.setSelection(0);
            spinnerGenero.setSelection(0);
            etPeso.setText("");
            tvResultado.setVisibility(View.GONE);
        });

        // Botón de calcular
        btnCalcular.setOnClickListener(v -> {
            calcularDosis();
        });
    }

//    private void getAllMedicamentos() {
//        System.out.println("Iniciando API getAll");
//        Retrofit retrofit = ApiClient.getClient();
//        MedicamentoService service = retrofit.create(MedicamentoService.class);
//        Call<List<Medicamento>> getAll = service.getAllMedicamento();
//
//        getAll.enqueue(new Callback<List<Medicamento>>() {
//            @Override
//            public void onResponse(Call<List<Medicamento>> call, Response<List<Medicamento>> response) {
//                //Para corroborar que funciona
//                Toast.makeText(CalculadoraPediatricaActivity.this, "Medicamentos cargados :D", Toast.LENGTH_SHORT).show();
//                System.out.println(response.code());
//                System.out.println(response.body());
//
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Medicamento> medicamentos = response.body();
//                    adapterMedicamentos = new ArrayAdapter<>(CalculadoraPediatricaActivity.this,
//                            android.R.layout.simple_spinner_item,
//                            medicamentos);
//                    adapterMedicamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinnerMedicamentos.setAdapter(adapterMedicamentos);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Medicamento>> call, Throwable t) {
//                Toast.makeText(CalculadoraPediatricaActivity.this, "Medicamentos NO cargados", Toast.LENGTH_SHORT).show();
//                t.printStackTrace();
//            }
//        });
//    }

    private void calcularDosis() {
        // Validar campos
        if (etPeso.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el peso del paciente", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Obtener valores seleccionados
            String medicamento = spinnerMedicamentos.getSelectedItem().toString();
            String edad = spinnerEdad.getSelectedItem().toString();
            double peso = Double.parseDouble(etPeso.getText().toString());
            String genero = spinnerGenero.getSelectedItem().toString();

            // Calcular dosis (esto es un ejemplo simplificado)
            double dosis = calcularDosisSegunMedicamento(medicamento, peso);

            // Mostrar resultado
            String resultado = String.format(
                    "Dosis recomendada para el medicamento %s es %.2f ml.\n" +
                            "Para un paciente de género %s con peso de: %.2f kg",
                    medicamento, dosis, genero, peso);

            tvResultado.setText(resultado);
            tvResultado.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingrese un peso válido", Toast.LENGTH_SHORT).show();
        }
    }

    private double calcularDosisSegunMedicamento(String medicamento, double peso) {
        // Esta es una implementación de ejemplo con cálculos simplificados
        // En una aplicación real, estos cálculos deberían basarse en protocolos médicos

        if (medicamento.contains("Paracetamol")) {
            // Dosis de paracetamol: 10-15 mg/kg/dosis
            return (peso * 15) / 24; // 120mg/5ml = 24mg/ml
        } else if (medicamento.contains("Ibuprofeno")) {
            // Dosis de ibuprofeno: 5-10 mg/kg/dosis
            return (peso * 10) / 20; // 100mg/5ml = 20mg/ml
        } else if (medicamento.contains("Amoxicilina")) {
            // Dosis de amoxicilina: 20-40 mg/kg/día dividido cada 8 horas
            return (peso * 40 / 3) / 50; // 250mg/5ml = 50mg/ml
        } else if (medicamento.contains("Dexametasona")) {
            // Dosis de dexametasona: 0.1-0.3 mg/kg/dosis
            return peso * 0.3 / 0.5; // 0.5mg/ml
        } else if (medicamento.contains("Salbutamol")) {
            // Dosis de salbutamol: 0.1-0.15 mg/kg/dosis
            return peso * 0.15 / 0.4; // 2mg/5ml = 0.4mg/ml
        }

        return 0.0;
    }

    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}