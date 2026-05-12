package com.ecommerce.models;

/**
 * Clase Cliente - Extiende Usuario
 * Demuestra HERENCIA en POO
 */
public class Cliente extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    
    /**
     * Constructor de Cliente
     */
    public Cliente(int id, String nombre, String email, String contraseña, String telefono,
                   String direccion, String ciudad, String codigoPostal) {
        super(id, nombre, email, contraseña, telefono, "Cliente");
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }
    
    /**
     * Implementación del método abstracto
     * Demuestra POLIMORFISMO con @Override
     */
    @Override
    public void mostrarDatos() {
        System.out.println("=== CLIENTE ===");
        System.out.println("Nombre: " + getNombre());
        System.out.println("Email: " + getEmail());
        System.out.println("Teléfono: " + getTelefono());
        System.out.println("Dirección: " + direccion + ", " + ciudad + " (" + codigoPostal + ")");
    }
    
    // Encapsulación - Getters y Setters
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public String getCodigoPostal() {
        return codigoPostal;
    }
    
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
