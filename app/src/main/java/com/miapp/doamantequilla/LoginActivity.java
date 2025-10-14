package com.miapp.doamantequilla;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    // Layouts de los campos de email y contraseña
    private TextInputLayout emailLayout, passwordLayout;
    // Campos de texto editables para ingresar email y contraseña
    private TextInputEditText etEmail, etPassword;
    // Botón de login
    private Button btnLogin;
    // CheckBox para "Recordar usuario"
    private CheckBox cbRemember;
    // TextViews para registro y recuperación de contraseña
    private TextView tvRegister, tvForgotPassword;

    // SharedPreferences para guardar datos locales como credenciales
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();              // Inicializa los elementos de la UI
        setupSharedPreferences(); // Configura SharedPreferences para guardar/cargar credenciales
        setupClickListeners();    // Configura los listeners para botones y textos clicables
    }

    //Inicialización de vistas
    private void initViews() {
        emailLayout = findViewById(R.id.email);
        passwordLayout = findViewById(R.id.password);
        etEmail = (TextInputEditText) emailLayout.getEditText();
        etPassword = (TextInputEditText) passwordLayout.getEditText();
        btnLogin = findViewById(R.id.btnLogin);
        cbRemember = findViewById(R.id.cbRemember);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        loadSavedCredentials(); // Si el usuario marcó "Recordar", carga email y contraseña
    }

    private void setupClickListeners() {
        // Botón de login
        btnLogin.setOnClickListener(v -> attemptLogin());

        // TextView registro → Intent explícito 1
        tvRegister.setOnClickListener(v -> {
            // Intent explícito 1: indica directamente abrir RegistroActivity
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // TextView recuperar contraseña → Intent implícito 1
        tvForgotPassword.setOnClickListener(v -> recoverPassword());
    }

    private void attemptLogin() {
        if (validateFields()) { // Primero validar campos
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Simular validación de credenciales
            if (authenticateUser(email, password)) {
                handleSuccessfulLogin(email, password);
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateFields() {

        // Validar que el email y contraseña no estén vacíos
        // Validar formato de email y longitud mínima de contraseña
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean isValid = true;

        // Validamos el email
        if (email.isEmpty()) {
            emailLayout.setError("El correo no puede estar vacío");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Ingresa un correo válido");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        // Validamos la  contraseña
        if (password.isEmpty()) {
            passwordLayout.setError("La contraseña no puede estar vacía");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Mínimo 6 caracteres");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        return isValid;
    }

    private boolean authenticateUser(String email, String password) {
        // Credenciales de prueba
        return (email.equals("usuario@doamantequilla.com") && password.equals("123456")) ||
                (email.equals("test@test.com") && password.equals("password"));
    }

    private void handleSuccessfulLogin(String email, String password) {
        // Guardar o borrar credenciales según CheckBox "Recordar"
        if (cbRemember.isChecked()) {
            saveCredentials(email, password);
        } else {
            clearCredentials();
        }

        // Mostrar toast de éxito
        Toast.makeText(this, "¡Login exitoso! 🎉", Toast.LENGTH_SHORT).show();

        // Intent explícito 2: abrir MenuActivity y pasar email
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra("user_email", email);
        startActivity(intent);

        // Cerrar LoginActivity para no volver con el botón atrás
        finish();
    }


    //Guardar, cargar y borrar credenciales
    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_email", email);
        editor.putString("saved_password", password);
        editor.putBoolean("remember_me", true);
        editor.apply();
    }

    private void loadSavedCredentials() {
        boolean rememberMe = sharedPreferences.getBoolean("remember_me", false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("saved_email", "");
            String savedPassword = sharedPreferences.getString("saved_password", "");
            etEmail.setText(savedEmail);
            etPassword.setText(savedPassword);
            cbRemember.setChecked(true);
        }
    }

    private void clearCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("saved_email");
        editor.remove("saved_password");
        editor.remove("remember_me");
        editor.apply();
    }

    private void recoverPassword() {
        // Intent implícito: abrir app de correo
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(android.net.Uri.parse("mailto:soporte@donamantequilla.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Recuperación de contraseña - App DonaMantequilla");
        intent.putExtra(Intent.EXTRA_TEXT, "Hola, necesito recuperar mi contraseña para la aplicación DoaMantequilla.\n\nMi email es: " + etEmail.getText().toString());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent); // Abre app de correo
        } else {
            Toast.makeText(this, "No hay aplicación de email instalada", Toast.LENGTH_SHORT).show();
        }
    }
}