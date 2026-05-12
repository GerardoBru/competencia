package com.ecommerce.models;

/**
 * Clase PagoTransferencia - Implementa método abstracto de Pago
 * Demuestra POLIMORFISMO con @Override
 */
public class PagoTransferencia extends Pago {
    private static final long serialVersionUID = 1L;

    private String numeroCuenta;
    private String banco;
    private String codigoTransferencia;

    /**
     * Constructor de PagoTransferencia
     */
    public PagoTransferencia(int id, double monto, String numeroCuenta, String banco) {
        super(id, monto);
        this.numeroCuenta = numeroCuenta;
        this.banco = banco;
        this.codigoTransferencia = "";
    }

    /**
     * Implementación del método abstracto
     */
    @Override
    public boolean procesar() {
        if (numeroCuenta != null && !numeroCuenta.isBlank()) {
            this.codigoTransferencia = "TRF-" + System.currentTimeMillis();
            setEstado("Completado");
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
        return "Pago Transferencia: " + banco + " - Monto: $" + getMonto() + " - Código: " + codigoTransferencia;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getCodigoTransferencia() {
        return codigoTransferencia;
    }

    public void setCodigoTransferencia(String codigoTransferencia) {
        this.codigoTransferencia = codigoTransferencia;
    }
}
