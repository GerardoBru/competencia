package com.ecommerce.models;

/**
 * Clase Administrador - Extiende Usuario
 * Demuestra HERENCIA en POO
 */
public class Administrador extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String departamento;
    private boolean activo;
    
    /**
     * Constructor de Administrador
     */
    public Administrador(int id, String nombre, String email, String contraseña, 
                        String telefono, String departamento) {
        super(id, nombre, email, contraseña, telefono, "Administrador");
        this.departamento = departamento;
        this.activo = true;
    }
    
    /**
     * Implementación del método abstracto
     * Demuestra POLIMORFISMO con @Override
     */
    @Override
    public void mostrarDatos() {
        System.out.println("=== ADMINISTRADOR ===");
        System.out.println("Nombre: " + getNombre());
        System.out.println("Email: " + getEmail());
        System.out.println("Teléfono: " + getTelefono());
        System.out.println("Departamento: " + departamento);
        System.out.println("Estado: " + (activo ? "Activo" : "Inactivo"));
    }
    
    // Encapsulación - Getters y Setters
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", departamento='" + departamento + '\'' +
                ", activo=" + activo +
                '}';
    }
}
