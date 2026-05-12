package com.ecommerce.models;

import java.time.LocalDateTime;

/**
 * Clase abstracta Pago - Demuestra ABSTRACCIÓN en POO
 * Base para diferentes métodos de pago
 */
public abstract class Pago {
    private int id;
    private double monto;
    private LocalDateTime fecha;
    private String estado; // "Pendiente", "Completado", "Cancelado"
    
    /**
     * Constructor de Pago
     */
    public Pago(int id, double monto) {
        this.id = id;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.estado = "Pendiente";
    }
    
    /**
     * Método abstracto que cada tipo de pago debe implementar
     * Demuestra POLIMORFISMO
     */
    public abstract boolean procesar();
    
    /**
     * Método abstracto para validar el pago
     */
    public abstract String obtenerDetalles();
    
    // Encapsulación - Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public double getMonto() {
        return monto;
    }
    
    public void setMonto(double monto) {
        this.monto = monto;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "Pago{" +
                "id=" + id +
                ", monto=" + monto +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                '}';
    }
}
