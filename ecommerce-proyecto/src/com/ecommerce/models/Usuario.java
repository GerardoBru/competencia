package com.ecommerce.models;

import java.io.Serializable;

/**
 * Clase abstracta Usuario - Base para Cliente y Administrador
 * Demuestra HERENCIA en POO
 */
public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private String email;
    private String contraseña;
    private String telefono;
    private String rol;
    
    /**
     * Constructor de Usuario
     */
    public Usuario(int id, String nombre, String email, String contraseña, String telefono, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.rol = rol;
    }
    
    /**
     * Método abstracto que cada subclase debe implementar
     * Demuestra POLIMORFISMO
     */
    public abstract void mostrarDatos();
    
    // Encapsulación - Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getContraseña() {
        return contraseña;
    }
    
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
