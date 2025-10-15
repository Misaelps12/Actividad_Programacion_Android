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



    // Layouts de los campos de entrada (Material Design)
    private TextInputLayout emailLayout, passwordLayout;

    // Campos editables donde el usuario ingresa su email y contraseña
    private TextInputEditText etEmail, etPassword;

    // Botón principal de inicio de sesión
    private Button btnLogin;

    // CheckBox para recordar las credenciales
    private CheckBox cbRemember;

    // TextViews clicables: registrar cuenta nueva y recuperar contraseña
    private TextView tvRegister, tvForgotPassword;

    // SharedPreferences: almacena datos de usuario localmente (correo y contraseña)
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita el diseño Edge-to-Edge (ocupa toda la pantalla, más moderno)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Ajusta márgenes según las barras del sistema (status bar, navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa todos los componentes visuales
        initViews();

        // Configura SharedPreferences y carga credenciales guardadas (si existen)
        setupSharedPreferences();

        // Define las acciones que ocurren al presionar botones o textos
        setupClickListeners();
    }




    // Enlaza las variables Java con los elementos XML
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

    // Configura SharedPreferences para guardar datos del usuario
    private void setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        loadSavedCredentials(); // Si el usuario eligió “Recordar”, se completan los campos
    }

    // Define qué sucede cuando se hace clic en los elementos interactivos
    private void setupClickListeners() {
        // Al presionar el botón “Login” se intenta iniciar sesión
        btnLogin.setOnClickListener(v -> attemptLogin());

        // -------- Intent explícito 1 --------
        // Abre la actividad de registro (RegistroActivity)
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // -------- Intent implícito 1 --------

        // Llama al metodo que abre la app de correo para recuperar contraseña
        tvForgotPassword.setOnClickListener(v -> recoverPassword());
    }


    // ---------- LÓGICA DE LOGIN ----------

    // Verifica que los datos sean válidos y simula el inicio de sesión
    private void attemptLogin() {
        if (validateFields()) { // Primero valida campos
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Comprueba credenciales (solo ejemplo, sin base de datos real)
            if (authenticateUser(email, password)) {
                handleSuccessfulLogin(email, password);
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Valida los campos de email y contraseña
    private boolean validateFields() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean isValid = true;

        // Validación del email
        if (email.isEmpty()) {
            emailLayout.setError("El correo no puede estar vacío");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Ingresa un correo válido");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        // Validación de la contraseña
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

    // Simula la autenticación de usuario con credenciales predefinidas
    private boolean authenticateUser(String email, String password) {
        // Credenciales de prueba (sin base de datos)
        return (email.equals("usuario@doamantequilla.com") && password.equals("123456")) ||
                (email.equals("test@test.com") && password.equals("password"));
    }




    private void handleSuccessfulLogin(String email, String password) {
        // Guarda o limpia credenciales según el CheckBox “Recordar”
        if (cbRemember.isChecked()) {
            saveCredentials(email, password);
        } else {
            clearCredentials();
        }

        // Muestra mensaje emergente de éxito
        Toast.makeText(this, "¡Login exitoso! 🎉", Toast.LENGTH_SHORT).show();

        // -------- Intent explícito 2 --------
        // Abre la pantalla principal del menú
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

        // Envía el email del usuario a la siguiente actividad
        intent.putExtra("user_email", email);

        // Inicia MenuActivity
        startActivity(intent);

        // Cierra LoginActivity (no se puede volver con el botón “Atrás”)
        finish();
    }




    // Guarda credenciales si el usuario seleccionó “Recordar”
    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_email", email);
        editor.putString("saved_password", password);
        editor.putBoolean("remember_me", true);
        editor.apply(); // Guarda los cambios
    }

    // Carga credenciales guardadas al abrir la app
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

    // Elimina credenciales guardadas si el usuario desmarca “Recordar”
    private void clearCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("saved_email");
        editor.remove("saved_password");
        editor.remove("remember_me");
        editor.apply();
    }



    private void recoverPassword() {
        // -------- Intent implícito 2 --------

        // Crea un intent implícito para enviar correo a soporte
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        // Indica que se trata de un correo electrónico
        intent.setData(android.net.Uri.parse("mailto:soporte@donamantequilla.com"));

        // Agrega asunto y cuerpo del mensaje
        intent.putExtra(Intent.EXTRA_SUBJECT, "Recuperación de contraseña - App DonaMantequilla");
        intent.putExtra(Intent.EXTRA_TEXT,
                "Hola, necesito recuperar mi contraseña para la aplicación DoaMantequilla.\n\n" +
                        "Mi email es: " + etEmail.getText().toString());

        // Verifica que exista una app de correo instalada antes de abrir el intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent); // Abre la app de correo
        } else {
            Toast.makeText(this, "No hay aplicación de email instalada", Toast.LENGTH_SHORT).show();
        }
    }
}
