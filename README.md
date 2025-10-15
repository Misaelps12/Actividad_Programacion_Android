# Doña Mantequilla - App Android

Una aplicación móvil desarrollada en Android.
Desarrollada como parte del proyecto de evaluación de Programación Android 1.6.


 
📱 Características

Interfaz moderna con Material Design 3

Navegación fluida entre actividades

Sistema de autenticación con validaciones

Integración con mapas, cámara y QR

Diseño responsive para diferentes tamaños de pantalla



🛠️ Tecnologías Utilizadas

Lenguaje: Java

Plataforma: Android

API mínima: Android 8.0 (Oreo)

Arquitectura: MVC (Model-View-Controller)

Dependencias:

Material Components for Android

ConstraintLayout

CardView

CameraX y ML Kit para QR y cámara





📋 Estructura del Proyecto

app/
├── src/main/java/com/miapp/doamantequilla/
│   ├── MainActivity.java          # Pantalla de inicio
│   ├── LoginActivity.java         # Autenticación de usuarios
│   ├── RegistroActivity.java      # Registro de nuevos usuarios
│   └── MenuActivity.java          # Menú principal con funcionalidades
├── res/
│   ├── layout/                    # Archivos XML de diseño
│   ├── drawable/                  # Recursos gráficos
│   └── values/                    # Recursos de strings, colores, etc.


🎯 Intents Implementados

Intents Explícitos (3/3)

Origen       →   Destino	Descripción

MainActivity → LoginActivity - Navegación inicial

LoginActivity → RegistroActivity - Flujo de registro

LoginActivity → MenuActivity - Acceso al sistema



🌐 Intents Implícitos (5/5)

📧 Enviar correo - Recuperación de contraseña - (Implementado)

🗺️ Abrir mapa - Ubicación en Google Maps - (Implementado)

🌐 Abrir página web - (Implementado)

📷 Abrir Camara - (Implementado)

 Scanear qr - (Implementado)


🎮 Flujo de la Aplicación
Pantalla de bienvenida 👉 Presionar "Comenzar"

Inicio de sesión 👉 Validar credenciales

Menú principal 👉 Acceder a funcionalidades

📍 Ver ubicación en mapa

🌐 Navegar a sitio web

📷 Abrir Camara

 Escanear QR


🔐 Credenciales de Prueba
Email: usuario@doamantequilla.com

Contraseña: 123456

Email: test@test.com

Contraseña: password


📝 Validaciones Implementadas
✅ Formato de email válido

✅ Contraseña mínima 6 caracteres

✅ Campos obligatorios

✅ Persistencia de credenciales (Recordar contraseña)


🎨 Diseño y UX
Paleta de colores: Azul (#2196F3) con fondo azul claro (#90CAF9)

Tipografía: Roboto con tamaños responsivos

Componentes: TextInputLayout, CardView, Button con efectos

Iconografía: Íconos Material Design para email y contraseña


📊 Estado del Proyecto

✅ Completado:

Estructura base de actividades

Diseño de interfaces

Sistema de login funcional

3 intents explícitos

5 intents implícitos



🔄 En Progreso:

Mejoras en RegistroActivity

Mejorar UX y validaciones adicionales

Agregar pantalla de configuración


👥 Autores
Misael Enrique Oyarzún Martínez - Misaelps12 - misaeloyarzunm12@gmail.com



