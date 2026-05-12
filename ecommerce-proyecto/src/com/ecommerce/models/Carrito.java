package com.ecommerce.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Carrito - Maneja los productos del carrito de compra
 * Demuestra uso de COLECCIONES (ArrayList)
 * IVA Colombia: 19%
 */
public class Carrito implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double IVA_COLOMBIA = 0.19; // 19% IVA
    
    private Cliente cliente;
    private List<DetallePedido> detalles; // ArrayList de items
    private int proximoId;
    
    /**
     * Constructor
     */
    public Carrito(Cliente cliente) {
        this.cliente = cliente;
        this.detalles = new ArrayList<>(); // COLECCIÓN - ArrayList
        this.proximoId = 1;
    }
    
    /**
     * Agrega un producto al carrito
     * @param producto producto a agregar
     * @param cantidad cantidad
     */
    public void agregarProducto(Producto producto, int cantidad) {
        // Verificar si el producto ya está en el carrito
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().getId() == producto.getId()) {
                detalle.actualizarCantidad(detalle.getCantidad() + cantidad);
                return;
            }
        }
        // Si no existe, agregar nuevo item
        detalles.add(new DetallePedido(proximoId++, producto, cantidad));
    }
    
    /**
     * Elimina un producto del carrito
     * @param idProducto id del producto a eliminar
     */
    public void eliminarProducto(int idProducto) {
        detalles.removeIf(detalle -> detalle.getProducto().getId() == idProducto);
    }
    
    /**
     * Calcula el subtotal del carrito (sin IVA)
     */
    public double calcularSubtotal() {
        return detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
    }
    
    /**
     * Calcula el IVA a aplicar (19% en Colombia)
     */
    public double calcularIVA() {
        return calcularSubtotal() * IVA_COLOMBIA;
    }
    
    /**
     * Calcula el total del carrito (con IVA incluido)
     */
    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA();
    }
    
    /**
     * Obtiene la cantidad de items
     */
    public int obtenerCantidadItems() {
        return detalles.size();
    }
    
    /**
     * Limpia el carrito
     */
    public void limpiar() {
        detalles.clear();
    }
    
    /**
     * Valida que hay stock disponible para todos los items
     */
    public boolean validarStock() {
        for (DetallePedido detalle : detalles) {
            if (!detalle.getProducto().hayStockDisponible(detalle.getCantidad())) {
                return false;
            }
        }
        return true;
    }
    
    // Encapsulación - Getters
    public Cliente getCliente() {
        return cliente;
    }
    
    public List<DetallePedido> getDetalles() {
        return new ArrayList<>(detalles); // Retorna copia para encapsulación
    }
    
    @Override
    public String toString() {
        return "Carrito{" +
                "cliente=" + cliente.getNombre() +
                ", items=" + detalles.size() +
                ", subtotal=$" + calcularSubtotal() +
                ", iva=$" + calcularIVA() +
                ", total=$" + calcularTotal() +
                '}';
    }
}
