package com.ecommerce.persistence;

import com.ecommerce.models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase GestorUsuarios - Maneja la persistencia de usuarios
 * Usa archivos serializados para guardar/cargar datos
 */
public class GestorUsuarios {
    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    private List<Usuario> usuarios;
    
    /**
     * Constructor
     */
    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
        cargarUsuarios();
    }
    
    /**
     * Carga usuarios desde archivo
     */
    @SuppressWarnings("unchecked")
    private void cargarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_USUARIOS))) {
            this.usuarios = (List<Usuario>) ois.readObject();
            System.out.println("Usuarios cargados desde archivo: " + usuarios.size());
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de usuarios no encontrado. Inicializando con usuarios de ejemplo.");
            inicializarUsuariosEjemplo();
            guardarUsuarios();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            inicializarUsuariosEjemplo();
        }
    }
    
    /**
     * Inicializa usuarios de ejemplo
     */
    private void inicializarUsuariosEjemplo() {
        usuarios.clear();
        
        // Agregar cliente de ejemplo
        Cliente cliente1 = new Cliente(1, "Juan Pérez", "juan@email.com", "password123", 
                                       "3105551234", "Calle 10 #20", "Bogotá", "110111");
        usuarios.add(cliente1);
        
        Cliente cliente2 = new Cliente(2, "María García", "maria@email.com", "password456", 
                                       "3105555678", "Carrera 5 #30", "Medellín", "050001");
        usuarios.add(cliente2);
        
        // Agregar administrador de ejemplo
        Administrador admin = new Administrador(3, "Admin User", "admin@email.com", "admin123",
                                                "3015551111", "Sistemas");
        usuarios.add(admin);
    }
    
    /**
     * Guarda usuarios en archivo
     */
    public void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(usuarios);
            System.out.println("Usuarios guardados: " + usuarios.size());
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }
    
    /**
     * Autentica un usuario
     * Demuestra MANEJO DE EXCEPCIONES
     */
    public Usuario autenticar(String email, String contraseña) throws Exception {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email) && usuario.getContraseña().equals(contraseña)) {
                return usuario;
            }
        }
        throw new Exception("Credenciales inválidas");
    }
    
    /**
     * Registra un nuevo usuario cliente
     */
    public void registrarCliente(String nombre, String email, String contraseña, 
                                 String telefono, String direccion, String ciudad, String codigoPostal) 
            throws Exception {
        // Verificar si el email ya existe
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                throw new Exception("El email ya está registrado");
            }
        }
        
        int nuevoId = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        Cliente cliente = new Cliente(nuevoId, nombre, email, contraseña, telefono, 
                                     direccion, ciudad, codigoPostal);
        usuarios.add(cliente);
        guardarUsuarios();
    }
    
    /**
     * Obtiene todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }
    
    /**
     * Obtiene un usuario por ID
     */
    public Usuario obtenerPorId(int id) {
        return usuarios.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
    
    /**
     * Obtiene un usuario por email
     */
    public Usuario obtenerPorEmail(String email) {
        return usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }
}
