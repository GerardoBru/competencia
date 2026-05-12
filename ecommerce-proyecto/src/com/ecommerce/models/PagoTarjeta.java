package com.ecommerce.models;

/**
 * Clase PagoTarjeta - Implementa método abstracto de Pago
 * Demuestra POLIMORFISMO con @Override
 */
public class PagoTarjeta extends Pago {
    private static final long serialVersionUID = 1L;
    
    private String numeroTarjeta;
    private String nombreTitular;
    private String cvv;
    
    /**
     * Constructor de PagoTarjeta
     */
    public PagoTarjeta(int id, double monto, String numeroTarjeta, String nombreTitular, String cvv) {
        super(id, monto);
        this.numeroTarjeta = numeroTarjeta;
        this.nombreTitular = nombreTitular;
        this.cvv = cvv;
    }
    
    /**
     * Implementación del método abstracto
     */
    @Override
    public boolean procesar() {
        // Validar que el número de tarjeta tenga 16 dígitos
        if (numeroTarjeta != null && numeroTarjeta.length() == 16) {
            setEstado("Completado");
            System.out.println("Pago con tarjeta procesado correctamente");
            return true;
        }
        setEstado("Cancelado");
        return false;
    }
    
    /**
     * Retorna los detalles del pago
     */
    @Override
    public String obtenerDetalles() {
        return "Pago Tarjeta: " + numeroTarular() + " - Monto: $" + getMonto();
    }
    
    private String numeroTarular() {
        return numeroTarjeta.substring(numeroTarjeta.length() - 4);
    }
    
    // Encapsulación - Getters y Setters
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }
    
    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
    
    public String getNombreTitular() {
        return nombreTitular;
    }
    
    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
