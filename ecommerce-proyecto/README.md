# Proyecto E-Commerce con Java Swing

## Descripción
Aplicación de E-Commerce desarrollada con Java y Swing como interfaz gráfica.

## Estructura del Proyecto

```
ecommerce-proyecto/
├── src/
│   └── com/ecommerce/
│       ├── Main.java                 # Punto de entrada
│       ├── gui/
│       │   └── MainWindow.java       # Ventana principal
│       ├── models/
│       │   └── Producto.java         # Modelo de producto
│       └── util/                     # Utilidades (a completar)
├── build/                            # Directorio de compilación
├── dist/                             # Directorio de distribución
├── nbproject/
│   ├── project.properties            # Propiedades de NetBeans
│   └── project.xml                   # Configuración de NetBeans
├── build.xml                         # Script de construcción
└── README.md                         # Este archivo
```

## Requisitos
- Java 11 o superior
- NetBeans IDE (o cualquier IDE Java compatible)
- Apache Ant (para compilación desde línea de comandos)

## Cómo Abrir en NetBeans

1. Abre NetBeans
2. Ve a `File > Open Project`
3. Navega a la carpeta `ecommerce-proyecto`
4. Selecciona la carpeta y haz clic en `Open Project`

## Cómo Compilar

### Desde NetBeans
1. Si el proyecto está abierto, presiona `F11` o ve a `Build > Build Project`

### Desde línea de comandos
```bash
cd ecommerce-proyecto
ant clean jar
```

## Cómo Ejecutar

### Desde NetBeans
1. Presiona `F6` o ve a `Run > Run Project`

### Desde línea de comandos
```bash
cd ecommerce-proyecto
java -jar dist/ecommerce.jar
```

## Estructura de Paquetes

- **com.ecommerce**: Paquete principal
- **com.ecommerce.gui**: Componentes gráficos
- **com.ecommerce.models**: Modelos de datos
- **com.ecommerce.util**: Clases utilitarias

## Próximos Pasos

1. Expandir las interfaces gráficas
2. Implementar funcionalidades de productos
3. Agregar gestión de carritos de compra
4. Implementar persistencia de datos

## Autor
[Tu Nombre]

## Licencia
GPL 2.0
