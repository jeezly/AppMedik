package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.utl.calculadoradosificadora.R;
import org.utl.calculadoradosificadora.VistaMedico.VistaMedico;
import org.utl.calculadoradosificadora.VistaMedico.Menu.ProtocolosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SobreNosotrosActivity;
import org.utl.calculadoradosificadora.VistaMedico.Menu.SoporteActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.ConfiguracionActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.NotificacionesActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.PerfilActivity;
import org.utl.calculadoradosificadora.VistaMedico.Opciones.SeguridadActivity;
import org.utl.calculadoradosificadora.model.Medicamento;
import org.utl.calculadoradosificadora.service.ApiClient;
import org.utl.calculadoradosificadora.service.ApiResponse;
import org.utl.calculadoradosificadora.service.MedicamentoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculadoraPediatricaActivity extends AppCompatActivity {

    // Views
    private Spinner spinnerMedicamentos;
    private RadioGroup rgGenero;
    private EditText etEdad, etPeso;
    private Button btnCalcular, btnOk, btnRegresar;
    private TextView tvResultado;

    // Navigation
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    // Data
    private List<Medicamento> medicamentos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_pediatrica);

        setupToolbar();
        setupNavigationDrawer();
        initViews();
        loadMedicamentos();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        // Configurar íconos del toolbar
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        findViewById(R.id.options_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END));

        // Menú izquierdo
        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_inicio) {
                finish();
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

        // Menú derecho
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
            }
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void initViews() {
        spinnerMedicamentos = findViewById(R.id.spinnerMedicamentos);
        rgGenero = findViewById(R.id.rgGenero);
        etEdad = findViewById(R.id.etEdad);
        etPeso = findViewById(R.id.etPeso);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnOk = findViewById(R.id.btnOk);
        btnRegresar = findViewById(R.id.btnRegresar);
        tvResultado = findViewById(R.id.tvResultado);

        btnCalcular.setOnClickListener(v -> calcularDosis());
        btnOk.setOnClickListener(v -> {
            tvResultado.setVisibility(View.GONE);
            btnOk.setVisibility(View.GONE);
            btnCalcular.setVisibility(View.VISIBLE);
        });

        btnRegresar.setOnClickListener(v -> {
            startActivity(new Intent(this, VistaMedico.class));
            finish();
        });
    }

    private void loadMedicamentos() {
        MedicamentoService service = ApiClient.getClient().create(MedicamentoService.class);
        service.getAllMedicamento().enqueue(new Callback<ApiResponse<List<Medicamento>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Medicamento>>> call, Response<ApiResponse<List<Medicamento>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    medicamentos = response.body().getData();
                    setupSpinner();
                } else {
                    Toast.makeText(CalculadoraPediatricaActivity.this,
                            "Error al cargar medicamentos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Medicamento>>> call, Throwable t) {
                Toast.makeText(CalculadoraPediatricaActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter<Medicamento> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                medicamentos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicamentos.setAdapter(adapter);
    }

    private void calcularDosis() {
        try {
            // Validar campos
            if (etEdad.getText().toString().isEmpty() || etPeso.getText().toString().isEmpty()) {
                Toast.makeText(this, "Ingrese edad y peso del paciente", Toast.LENGTH_SHORT).show();
                return;
            }

            int edad = Integer.parseInt(etEdad.getText().toString());
            double peso = Double.parseDouble(etPeso.getText().toString());
            int genero = ((RadioButton)findViewById(rgGenero.getCheckedRadioButtonId()) == findViewById(R.id.rbMasculino)) ? 1 : 0;

            Medicamento medicamento = (Medicamento) spinnerMedicamentos.getSelectedItem();

            if (peso <= 0) {
                Toast.makeText(this, "El peso debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                return;
            }

            if (edad <= 0) {
                Toast.makeText(this, "La edad debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calcular dosis
            double dosis = calcularDosisMedicamento(medicamento, peso);

            // Mostrar resultado
            String resultado = String.format(
                    "Dosis recomendada para %s %s:\n\n%.5f ml\n\nPara un paciente de %d años, género %s con peso de %.1f kg",
                    medicamento.getNombre(),
                    medicamento.getPresentacion(),
                    dosis,
                    edad,
                    (genero == 1 ? "Masculino" : "Femenino"),
                    peso
            );

            tvResultado.setText(resultado);
            tvResultado.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);
            btnCalcular.setVisibility(View.GONE);

        } catch (Exception e) {
            Toast.makeText(this, "Error en cálculo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private double calcularDosisMedicamento(Medicamento medicamento, double peso) {
        // Lógica de cálculo específica para cada medicamento
        switch (medicamento.getIdMedicamentos()) {
            case 1: // Paracetamol
                return (peso * 15.0) / 500.0; // 15mg/kg/dosis (tabletas de 500mg)
            case 2: // Ibuprofeno (100mg/5ml)
                return (peso * 5.0) / 20.0; // 5-10mg/kg/dosis
            case 3: // Amoxicilina (250mg/5ml)
                return (peso * 20.0) / 50.0; // 20-40mg/kg/día dividido cada 8-12h
            case 4: // Dexametasona (0.5mg/ml)
                return (peso * 0.15) / 0.5; // 0.15mg/kg/dosis
            case 5: // Salbutamol (2mg/5ml)
                return (peso * 0.1) / 0.4; // 0.1mg/kg/dosis (máx 2.5mg)
            default:
                return 0.0;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}