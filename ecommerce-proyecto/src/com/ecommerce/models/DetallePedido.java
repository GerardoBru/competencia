package com.ecommerce.models;

/**
 * Clase DetallePedido - Representa un item en el carrito/pedido
 * Demuestra ENCAPSULACIÓN
 */
public class DetallePedido {
    private int id;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    
    /**
     * Constructor
     */
    public DetallePedido(int id, Producto producto, int cantidad) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        calcularSubtotal();
    }
    
    /**
     * Calcula el subtotal del item
     */
    private void calcularSubtotal() {
        this.subtotal = precioUnitario * cantidad;
    }
    
    /**
     * Actualiza la cantidad y recalcula el subtotal
     */
    public void actualizarCantidad(int nuevaCantidad) {
        if (nuevaCantidad > 0) {
            this.cantidad = nuevaCantidad;
            calcularSubtotal();
        }
    }
    
    // Encapsulación - Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + id +
                ", producto=" + producto.getNombre() +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
}
