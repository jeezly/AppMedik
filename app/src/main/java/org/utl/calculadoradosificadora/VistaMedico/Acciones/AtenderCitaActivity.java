package org.utl.calculadoradosificadora.VistaMedico.Acciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class AtenderCitaActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft;
    private NavigationView navigationViewRight;

    // Views
    private Spinner spinnerMedicamentos;
    private Spinner spinnerEdad;
    private Spinner spinnerGenero;
    private EditText etPeso;
    private Button btnCalcular;
    private Button btnCancelar;
    private Button btnBuscarMedicamento;
    private Button btnFinalizarConsulta;
    private TextView tvResultado;
    private TextView tvNombrePaciente;
    private TextView tvEdadPaciente;
    private TextView tvPesoPaciente;

    // Datos
    private List<String> listaMedicamentos;
    private ArrayAdapter<String> adapterMedicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atender_cita);

        // Configurar Toolbar y menús
        setupToolbarAndMenus();

        // Inicializar vistas
        initViews();

        // Configurar spinners
        setupSpinners();

        // Configurar botones
        setupButtons();

        // Cargar datos del paciente
        cargarDatosPaciente();
    }

    private void setupToolbarAndMenus() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationViewLeft = findViewById(R.id.navigation_view_left);
        navigationViewRight = findViewById(R.id.navigation_view_right);

        // Configurar listeners para los menús
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        findViewById(R.id.options_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.END));

        navigationViewLeft.setNavigationItemSelectedListener(item -> {
            handleNavigationItemSelected(item);
            return true;
        });

        navigationViewRight.setNavigationItemSelectedListener(item -> {
            handleOptionsItemSelected(item);
            return true;
        });
    }

    private void handleNavigationItemSelected(MenuItem item) {
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
    }

    private void handleOptionsItemSelected(MenuItem item) {
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
    }

    private void initViews() {
        spinnerMedicamentos = findViewById(R.id.spinnerMedicamentos);
        spinnerEdad = findViewById(R.id.spinnerEdad);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        etPeso = findViewById(R.id.etPeso);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnBuscarMedicamento = findViewById(R.id.btnBuscarMedicamento);
        btnFinalizarConsulta = findViewById(R.id.btnFinalizarConsulta);
        tvResultado = findViewById(R.id.tvResultado);
        tvNombrePaciente = findViewById(R.id.tvNombrePaciente);
        tvEdadPaciente = findViewById(R.id.tvEdadPaciente);
        tvPesoPaciente = findViewById(R.id.tvPesoPaciente);
    }

    private void cargarDatosPaciente() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nombrePaciente")) {
            String nombre = intent.getStringExtra("nombrePaciente");
            String edad = intent.getStringExtra("edadPaciente");
            String peso = intent.getStringExtra("pesoPaciente");

            tvNombrePaciente.setText("Nombre: " + nombre);
            tvEdadPaciente.setText("Edad: " + edad);
            tvPesoPaciente.setText("Peso: " + peso + " kg");
            etPeso.setText(peso.replace(" kg", ""));
        } else {
            Toast.makeText(this, "No se recibieron datos del paciente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupSpinners() {
        // Medicamentos
        listaMedicamentos = new ArrayList<>();
        listaMedicamentos.add("Paracetamol (120mg/5ml)");
        listaMedicamentos.add("Ibuprofeno (100mg/5ml)");
        listaMedicamentos.add("Amoxicilina (250mg/5ml)");
        listaMedicamentos.add("Dexametasona (0.5mg/ml)");
        listaMedicamentos.add("Salbutamol (2mg/5ml)");

        adapterMedicamentos = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaMedicamentos);
        adapterMedicamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicamentos.setAdapter(adapterMedicamentos);

        // Edad
        ArrayAdapter<CharSequence> adapterEdad = ArrayAdapter.createFromResource(this,
                R.array.edad_array, android.R.layout.simple_spinner_item);
        adapterEdad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEdad.setAdapter(adapterEdad);

        // Género
        ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(this,
                R.array.genero_array, android.R.layout.simple_spinner_item);
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGenero);
    }

    private void setupButtons() {
        btnBuscarMedicamento.setOnClickListener(v -> {
            Toast.makeText(this, R.string.busqueda_medicamento, Toast.LENGTH_SHORT).show();
        });

        btnCancelar.setOnClickListener(v -> {
            resetCalculadora();
        });

        btnCalcular.setOnClickListener(v -> {
            calcularDosis();
        });

        btnFinalizarConsulta.setOnClickListener(v -> {
            finalizarConsulta();
        });
    }

    private void resetCalculadora() {
        spinnerMedicamentos.setSelection(0);
        spinnerEdad.setSelection(0);
        spinnerGenero.setSelection(0);
        etPeso.setText("");
        tvResultado.setVisibility(View.GONE);
    }

    private void calcularDosis() {
        if (etPeso.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.error_peso_vacio, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String medicamento = spinnerMedicamentos.getSelectedItem().toString();
            String edad = spinnerEdad.getSelectedItem().toString();
            double peso = Double.parseDouble(etPeso.getText().toString());
            String genero = spinnerGenero.getSelectedItem().toString();

            double dosis = calcularDosisSegunMedicamento(medicamento, peso);

            String resultado = getString(R.string.resultado_calculadora,
                    medicamento, dosis, genero, peso);

            tvResultado.setText(resultado);
            tvResultado.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_peso_invalido, Toast.LENGTH_SHORT).show();
        }
    }

    private double calcularDosisSegunMedicamento(String medicamento, double peso) {
        if (medicamento.contains("Paracetamol")) {
            return (peso * 15) / 24; // 120mg/5ml = 24mg/ml
        } else if (medicamento.contains("Ibuprofeno")) {
            return (peso * 10) / 20; // 100mg/5ml = 20mg/ml
        } else if (medicamento.contains("Amoxicilina")) {
            return (peso * 40 / 3) / 50; // 250mg/5ml = 50mg/ml
        } else if (medicamento.contains("Dexametasona")) {
            return peso * 0.3 / 0.5; // 0.5mg/ml
        } else if (medicamento.contains("Salbutamol")) {
            return peso * 0.15 / 0.4; // 2mg/5ml = 0.4mg/ml
        }
        return 0.0;
    }

    private void finalizarConsulta() {
        // Aquí iría la lógica para guardar los datos de la consulta
        Toast.makeText(this, "Consulta finalizada correctamente", Toast.LENGTH_SHORT).show();
        finish();
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