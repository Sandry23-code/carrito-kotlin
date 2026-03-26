# 🛒 Carrito de Compras en Kotlin

Aplicación de consola desarrollada en **Kotlin** que simula el funcionamiento básico de un carrito de compras.

## 📌 Descripción

Este proyecto permite gestionar un carrito de compras desde la terminal mediante un menú interactivo. Está diseñado como práctica para reforzar conceptos de programación en Kotlin, manejo de estructuras de datos y organización del código.

## ⚙️ Funcionalidades

- 📦 Visualizar productos disponibles
- ➕ Agregar productos al carrito
- 🛒 Ver contenido del carrito
- ❌ Eliminar productos del carrito
- 🔄 Actualizar cantidad de productos
- 💳 Confirmar compra
- 🧹 Vaciar carrito

## 🏛️ Arquitectura

El proyecto sigue una arquitectura en capas que separa responsabilidades en tres paquetes principales:

```
carrito-kotlin/
└── src/
    └── main/
        └── kotlin/
            ├── Main.kt              # Punto de entrada y menú principal
            ├── models/              # Modelos de datos
            │   ├── Product.kt       # Representa un producto del inventario
            │   ├── CartItem.kt      # Producto dentro del carrito con cantidad
            │   └── Invoice.kt       # Factura generada al confirmar la compra
            ├── services/            # Lógica de negocio
            │   ├── CartService.kt   # Operaciones del carrito (agregar, eliminar, vaciar, etc.)
            │   ├── InvoiceService.kt# Generación de facturas al confirmar compra
            │   └── EmailService.kt  # Envío de confirmación por correo
            └── utils/               # Utilidades transversales
                ├── Inventory.kt     # Singleton con el catálogo de productos disponibles
                ├── InputValidator.kt# Validación de entradas del usuario
                └── AppLogger.kt     # Registro de eventos y errores de la aplicación
```

**Decisiones de diseño destacadas:**

- **`Inventory` como Singleton** — garantiza una única instancia del catálogo de productos compartida en toda la aplicación.
- **Separación models / services** — los modelos solo representan datos, mientras que los servicios contienen toda la lógica de negocio.
- **`AppLogger`** — centraliza el registro de eventos en la carpeta `logs/`, facilitando el seguimiento de errores sin mezclar lógica de logging con la lógica principal.

## 🗂️ Estructura del proyecto

```
carrito-kotlin/
├── src/                     # Código fuente
├── logs/                    # Archivos de log generados en ejecución
├── email.properties.example # Plantilla de configuración para el servicio de email
├── build.gradle.kts         # Configuración del build
└── gradlew / gradlew.bat    # Wrappers de Gradle
```

## 🛠️ Tecnologías utilizadas

- **Kotlin**
- **Gradle (Kotlin DSL)**
- **JDK 21**

## 🚀 Ejecución del proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Sandry23-code/carrito-kotlin.git
   cd carrito-kotlin
   ```

2. Copiar y configurar el archivo de propiedades de email:
   ```bash
   cp email.properties.example email.properties
   ```

3. Ejecutar con Gradle:
   ```bash
   ./gradlew run
   ```

   En Windows:
   ```bash
   .\gradlew.bat run
   ```
   