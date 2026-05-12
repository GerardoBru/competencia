package com.ecommerce.util;

/**
 * Clase Validador - Utilidades para validar datos
 * Demuestra ENCAPSULACIÓN en métodos utilitarios
 */
public class Validador {
    
    /**
     * Valida un email
     */
    public static boolean esEmailValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Valida un teléfono
     */
    public static boolean esTelefonoValido(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }
        return telefono.matches("^[0-9]+$") && telefono.length() >= 7;
    }
    
    /**
     * Valida que una cadena no sea vacía
     */
    public static boolean noEstaVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Valida una contraseña (mínimo 6 caracteres)
     */
    public static boolean esContraseñaValida(String contraseña) {
        return contraseña != null && contraseña.length() >= 6;
    }
    
    /**
     * Valida un número de tarjeta (16 dígitos)
     */
    public static boolean esNumeroTarjetaValido(String numeroTarjeta) {
        if (numeroTarjeta == null || numeroTarjeta.isEmpty()) {
            return false;
        }
        return numeroTarjeta.matches("^[0-9]{16}$");
    }
    
    /**
     * Valida un CVV (3 dígitos)
     */
    public static boolean esCVVValido(String cvv) {
        if (cvv == null || cvv.isEmpty()) {
            return false;
        }
        return cvv.matches("^[0-9]{3}$");
    }
}
