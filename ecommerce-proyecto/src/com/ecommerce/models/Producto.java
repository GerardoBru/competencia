package com.ecommerce.models;

/**
 * Clase Producto - Modelo de dominio
 * Demuestra ENCAPSULACIÓN con atributos privados
 */
public class Producto {
    
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String categoria;
    
    /**
     * Constructor vacío
     */
    public Producto() {
    }
    
    /**
     * Constructor con parámetros
     */
    public Producto(int id, String nombre, String descripcion, double precio, int stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }
    
    /**
     * Valida la disponibilidad de stock
     * Demuestra manejo de lógica de negocio
     */
    public boolean hayStockDisponible(int cantidad) {
        return stock >= cantidad;
    }
    
    /**
     * Reduce el stock tras una compra
     * @param cantidad cantidad a descontar
     * @return true si se descontó exitosamente
     */
    public boolean descontarStock(int cantidad) {
        if (hayStockDisponible(cantidad)) {
            stock -= cantidad;
            return true;
        }
        return false;
    }
    
    /**
     * Aumenta el stock
     */
    public void aumentarStock(int cantidad) {
        stock += cantidad;
    }
    
    // Encapsulación - Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
