package com.miapp.doamantequilla;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MenuActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;

    // Launcher para escaneo QR
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    String scannedContent = result.getContents();
                    Toast.makeText(this, "Código QR leído: " + scannedContent, Toast.LENGTH_LONG).show();

                    // Manejar el contenido escaneado
                    handleScannedContent(scannedContent);
                } else {
                    Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // Launcher para permisos de cámara
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permiso concedido, iniciar escaneo
                    launchQRScanner();
                } else {
                    Toast.makeText(this, "Se necesita permiso de cámara para escanear QR", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a botones
        Button btnMap = findViewById(R.id.map);
        Button btnWeb = findViewById(R.id.web);
        Button btnSugerencias = findViewById(R.id.sugerencias);
        Button btnCamera = findViewById(R.id.camara);
        Button btnQr = findViewById(R.id.qr);

        // Asignar eventos
        setupMapButton(btnMap);
        setupWebButton(btnWeb);
        setupSuggestionButton(btnSugerencias);
        setupCameraButton(btnCamera);

        // Botón QR
        btnQr.setOnClickListener(v -> checkCameraPermissionForQR());
    }

    private void checkCameraPermissionForQR() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permiso ya concedido, iniciar escaneo
            launchQRScanner();
        } else {
            // Solicitar permiso
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchQRScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Coloca el código QR dentro del marco");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setCameraId(0); // Usar cámara trasera
        options.setBarcodeImageEnabled(true);

        barcodeLauncher.launch(options);
    }

    private void handleScannedContent(String content) {
        // Aquí manejas lo que se escaneó
        try {
            // Si es una URL, abrir en navegador
            if (content.startsWith("http://") || content.startsWith("https://")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(content));
                startActivity(browserIntent);
            }
            // Si es un teléfono
            else if (content.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(content));
                startActivity(intent);
            }
            // Si es una ubicación geo
            else if (content.startsWith("geo:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(content));
                startActivity(intent);
            }
            // Si es texto plano, mostrar en Toast o diálogo
            else {
                showScannedContentDialog(content);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al procesar QR: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showScannedContentDialog(String content) {
        // Puedes crear un diálogo personalizado aquí
        Toast.makeText(this, "Contenido escaneado: " + content, Toast.LENGTH_LONG).show();

        // O usar un AlertDialog
        /*
        new AlertDialog.Builder(this)
                .setTitle("Código QR Escaneado")
                .setMessage(content)
                .setPositiveButton("Aceptar", null)
                .show();
        */
    }

    // Resto de tus métodos existentes...
    private void setupMapButton(Button btnMap) {
        btnMap.setOnClickListener(v -> {
            double latitud = -33.35557468918811;
            double longitud = -70.53296987503704;
            Uri ubicacion = Uri.parse("geo:" + latitud + "," + longitud + "?q=" + latitud + "," + longitud + "(Doña+Mantequilla)");
            Intent intent = new Intent(Intent.ACTION_VIEW, ubicacion);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Uri webUri = Uri.parse("https://www.google.com/maps?q=" + latitud + "," + longitud);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                startActivity(webIntent);
            }
        });
    }

    private void setupWebButton(Button btnWeb) {
        btnWeb.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://donamantequilla.proshops.cl/");
            Intent viewWeb = new Intent(Intent.ACTION_VIEW, uri);
            if (viewWeb.resolveActivity(getPackageManager()) != null) {
                startActivity(viewWeb);
            } else {
                Toast.makeText(this, "No hay navegador disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSuggestionButton(Button btnSugerencias) {
        btnSugerencias.setOnClickListener(v -> sendSuggestionEmail());
    }

    private void sendSuggestionEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:sugerencias@doamantequilla.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sugerencia - App DoaMantequilla");
        intent.putExtra(Intent.EXTRA_TEXT,
                "Hola equipo de DoaMantequilla,\n\n" +
                        "Tengo la siguiente sugerencia para mejorar la aplicación:\n\n" +
                        "[Escribe tu sugerencia aquí]\n\n" +
                        "Saludos cordiales,\n" +
                        "Usuario de la app"
        );
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No hay aplicación de email instalada", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCameraButton(Button btnCamera) {
        btnCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No se encontró app de cámara", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
