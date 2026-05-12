package com.ecommerce.models;

/**
 * Clase PagoEfectivo - Implementa método abstracto de Pago
 * Demuestra POLIMORFISMO con @Override
 */
public class PagoEfectivo extends Pago {
    private double montoRecibido;
    private double cambio;
    
    /**
     * Constructor de PagoEfectivo
     */
    public PagoEfectivo(int id, double monto, double montoRecibido) {
        super(id, monto);
        this.montoRecibido = montoRecibido;
        this.cambio = 0;
    }
    
    /**
     * Implementación del método abstracto
     */
    @Override
    public boolean procesar() {
        // Validar que el monto recibido sea suficiente
        if (montoRecibido >= getMonto()) {
            this.cambio = montoRecibido - getMonto();
            setEstado("Completado");
            System.out.println("Pago en efectivo procesado. Cambio: $" + cambio);
            return true;
        }
        setEstado("Cancelado");
        System.out.println("Monto insuficiente");
        return false;
    }
    
    /**
     * Retorna los detalles del pago
     */
    @Override
    public String obtenerDetalles() {
        return "Pago Efectivo: Monto: $" + getMonto() + " - Cambio: $" + cambio;
    }
    
    // Encapsulación - Getters y Setters
    public double getMontoRecibido() {
        return montoRecibido;
    }
    
    public void setMontoRecibido(double montoRecibido) {
        this.montoRecibido = montoRecibido;
    }
    
    public double getCambio() {
        return cambio;
    }
    
    public void setCambio(double cambio) {
        this.cambio = cambio;
    }
}
