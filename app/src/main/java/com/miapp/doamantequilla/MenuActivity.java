package com.miapp.doamantequilla;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

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

        // Referenciamos el btn
        Button btnMap = findViewById(R.id.map);

        btnMap.setOnClickListener(v -> {
            // coordenadas
            double latitud = -33.35557468918811;
            double longitud = -70.53296987503704;

            // Crear el intent con la ubicación
            Uri ubicacion = Uri.parse("geo:" + latitud + "," + longitud + "?q=" + latitud + "," + longitud + "(Mi+Ubicación)");
            Intent intent = new Intent(Intent.ACTION_VIEW, ubicacion);
            intent.setPackage("com.google.android.apps.maps");

            // Verificar que haya una app que lo maneje
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }
}