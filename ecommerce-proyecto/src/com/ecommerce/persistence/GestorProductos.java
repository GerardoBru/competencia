package com.ecommerce.persistence;

import com.ecommerce.models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase GestorProductos - Maneja la persistencia de productos
 * Usa archivos serializados para guardar/cargar datos
 */
public class GestorProductos {
    private static final String ARCHIVO_PRODUCTOS = "productos.dat";
    private List<Producto> productos;
    
    /**
     * Constructor
     */
    public GestorProductos() {
        this.productos = new ArrayList<>();
        cargarProductos();
    }
    
    /**
     * Carga productos desde archivo
     */
    @SuppressWarnings("unchecked")
    private void cargarProductos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_PRODUCTOS))) {
            this.productos = (List<Producto>) ois.readObject();
            System.out.println("Productos cargados desde archivo: " + productos.size());
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de productos no encontrado. Inicializando con datos de ejemplo.");
            inicializarProductosEjemplo();
            guardarProductos();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
            inicializarProductosEjemplo();
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
     * Guarda productos en archivo
     */
    public void guardarProductos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PRODUCTOS))) {
            oos.writeObject(productos);
            System.out.println("Productos guardados: " + productos.size());
        } catch (IOException e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
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
