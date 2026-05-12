package com.ecommerce.persistence;

import com.ecommerce.models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase GestorProductosCSV - Persistencia de productos en CSV
 */
public class GestorProductosCSV {
    private static final String ARCHIVO_CSV = "datos/productos.csv";
    private List<Producto> productos;
    
    /**
     * Constructor
     */
    public GestorProductosCSV() {
        this.productos = new ArrayList<>();
        crearDirectorio();
        cargarProductos();
    }
    
    private void crearDirectorio() {
        new File("datos").mkdirs();
    }
    
    /**
     * Carga productos desde CSV
     */
    private void cargarProductos() {
        File archivo = new File(ARCHIVO_CSV);
        if (!archivo.exists()) {
            inicializarProductosEjemplo();
            guardarProductos();
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // Saltar encabezado
                }
                
                String[] campos = linea.split(",", -1);
                if (campos.length >= 6) {
                    try {
                        int id = Integer.parseInt(campos[0]);
                        String nombre = campos[1];
                        String descripcion = campos[2];
                        double precio = Double.parseDouble(campos[3]);
                        int stock = Integer.parseInt(campos[4]);
                        String categoria = campos[5];
                        
                        productos.add(new Producto(id, nombre, descripcion, precio, stock, categoria));
                    } catch (NumberFormatException e) {
                        System.err.println("Error al parsear línea: " + linea);
                    }
                }
            }
            System.out.println("Productos cargados: " + productos.size());
        } catch (IOException e) {
            System.err.println("Error al cargar CSV: " + e.getMessage());
        }
    }
    
    /**
     * Inicializa productos de ejemplo
     */
    private void inicializarProductosEjemplo() {
        productos.clear();
        productos.add(new Producto(1, "Laptop", "Laptop HP 15 pulgadas", 3500000, 10, "Electrónica"));
        productos.add(new Producto(2, "Mouse", "Mouse inalámbrico Logitech", 85000, 50, "Accesorios"));
        productos.add(new Producto(3, "Teclado", "Teclado mecánico RGB", 220000, 25, "Accesorios"));
        productos.add(new Producto(4, "Monitor", "Monitor LG 24 pulgadas", 780000, 15, "Electrónica"));
        productos.add(new Producto(5, "Headphones", "Audífonos Sony WH-1000", 980000, 8, "Accesorios"));
    }
    
    /**
     * Guarda productos en CSV
     */
    public void guardarProductos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_CSV))) {
            // Escribir encabezado
            writer.println("ID,Nombre,Descripcion,Precio,Stock,Categoria");
            
            // Escribir datos
            for (Producto p : productos) {
                writer.printf("%d,%s,%s,%.2f,%d,%s%n",
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getPrecio(),
                    p.getStock(),
                    p.getCategoria());
            }
            System.out.println("Productos guardados: " + productos.size());
        } catch (IOException e) {
            System.err.println("Error al guardar CSV: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todos los productos
     */
    public List<Producto> obtenerTodos() {
        return new ArrayList<>(productos);
    }
    
    /**
     * Obtiene un producto por ID
     */
    public Producto obtenerPorId(int id) {
        return productos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
    
    /**
     * Agrega un nuevo producto
     */
    public void agregar(Producto producto) {
        productos.add(producto);
        guardarProductos();
    }
    
    /**
     * Actualiza un producto
     */
    public void actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == producto.getId()) {
                productos.set(i, producto);
                guardarProductos();
                return;
            }
        }
    }
    
    /**
     * Elimina un producto
     */
    public void eliminar(int id) {
        productos.removeIf(p -> p.getId() == id);
        guardarProductos();
    }
    
    /**
     * Obtiene productos por categoría
     */
    public List<Producto> obtenerPorCategoria(String categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
