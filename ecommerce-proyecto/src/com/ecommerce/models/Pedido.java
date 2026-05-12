package com.ecommerce.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Pedido - Representa una orden de compra
 * Demuestra uso de COLECCIONES (ArrayList)
 */
public class Pedido {
    private static final double IVA_COLOMBIA = 0.19;

    private int id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private List<DetallePedido> detalles; // ArrayList de items
    private double subtotal;
    private double iva;
    private double total;
    private String estado; // "Pendiente", "Procesando", "Enviado", "Entregado", "Cancelado"
    private Pago pago;
    
    /**
     * Constructor
     */
    public Pedido(int id, Cliente cliente, List<DetallePedido> detalles) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = LocalDateTime.now();
        this.detalles = new ArrayList<>(detalles); // COLECCIÓN - ArrayList
        this.estado = "Pendiente";
        calcularTotales();
    }

    /**
     * Constructor para restaurar pedidos desde persistencia.
     */
    public Pedido(int id, Cliente cliente, LocalDateTime fecha, double subtotal, double iva, double total, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.detalles = new ArrayList<>();
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.estado = estado;
    }
    
    /**
     * Calcula subtotal, IVA y total del pedido
     */
    private void calcularTotales() {
        subtotal = detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
        iva = subtotal * IVA_COLOMBIA;
        total = subtotal + iva;
    }
    
    /**
     * Cambia el estado del pedido
     */
    public void cambiarEstado(String nuevoEstado) {
        String[] estadosValidos = {"Pendiente", "Procesando", "Enviado", "Entregado", "Cancelado"};
        for (String estado : estadosValidos) {
            if (estado.equals(nuevoEstado)) {
                this.estado = nuevoEstado;
                return;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
    }
    
    /**
     * Asigna un método de pago al pedido
     */
    public void asignarPago(Pago pago) {
        this.pago = pago;
    }
    
    /**
     * Procesa el pago y actualiza el estado
     */
    public boolean procesarPago() {
        if (pago != null && pago.procesar()) {
            cambiarEstado("Procesando");
            // Descontar stock de los productos
            for (DetallePedido detalle : detalles) {
                detalle.getProducto().descontarStock(detalle.getCantidad());
            }
            return true;
        }
        return false;
    }
    
    // Encapsulación - Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public List<DetallePedido> getDetalles() {
        return new ArrayList<>(detalles); // Retorna copia para encapsulación
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIva() {
        return iva;
    }
    
    public double getTotal() {
        return total;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTotales(double subtotal, double iva, double total) {
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }
    
    public Pago getPago() {
        return pago;
    }
    
    public void setPago(Pago pago) {
        this.pago = pago;
    }
    
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + cliente.getNombre() +
                ", fecha=" + fecha +
            ", subtotal=$" + subtotal +
            ", iva=$" + iva +
                ", total=$" + total +
                ", estado='" + estado + '\'' +
                ", items=" + detalles.size() +
                '}';
    }
}
