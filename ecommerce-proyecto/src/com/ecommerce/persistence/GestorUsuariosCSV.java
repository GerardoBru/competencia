package com.ecommerce.persistence;

import com.ecommerce.models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase GestorUsuariosCSV - Persistencia de usuarios en CSV
 */
public class GestorUsuariosCSV {
    private static final String ARCHIVO_CSV = "datos/usuarios.csv";
    private List<Usuario> usuarios;
    
    /**
     * Constructor
     */
    public GestorUsuariosCSV() {
        this.usuarios = new ArrayList<>();
        crearDirectorio();
        cargarUsuarios();
        asegurarAdministradorPorDefecto();
    }
    
    private void crearDirectorio() {
        new File("datos").mkdirs();
    }
    
    /**
     * Carga usuarios desde CSV
     */
    private void cargarUsuarios() {
        File archivo = new File(ARCHIVO_CSV);
        if (!archivo.exists()) {
            inicializarUsuariosEjemplo();
            guardarUsuarios();
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // Saltar encabezado
                }
                
                String[] campos = linea.split(",", -1);
                if (campos.length >= 6) {
                    try {
                        int id = Integer.parseInt(campos[0]);
                        String nombre = campos[1];
                        String email = campos[2];
                        String rol = campos[3];
                        String telefono = campos[4];
                        
                        if ("Cliente".equals(rol)) {
                            String direccion = campos[5];
                            String ciudad = campos[6];
                            String codigoPostal = campos.length > 7 ? campos[7] : "0";
                            usuarios.add(new Cliente(id, nombre, email, "temp", telefono, direccion, ciudad, codigoPostal));
                        } else if ("Administrador".equals(rol)) {
                            String departamento = campos[5];
                            usuarios.add(new Administrador(id, nombre, email, "temp", telefono, departamento));
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error al parsear línea: " + linea);
                    }
                }
            }
            System.out.println("Usuarios cargados: " + usuarios.size());
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios CSV: " + e.getMessage());
        }
    }
    
    /**
     * Inicializa usuarios de ejemplo
     */
    private void inicializarUsuariosEjemplo() {
        usuarios.clear();
        Cliente cliente1 = new Cliente(1, "Juan Pérez", "juan@email.com", "temp", 
                                       "3105551234", "Calle 10 #20", "Bogotá", "110111");
        usuarios.add(cliente1);
        
        Cliente cliente2 = new Cliente(2, "María García", "maria@email.com", "temp", 
                                       "3105555678", "Carrera 5 #30", "Medellín", "050001");
        usuarios.add(cliente2);
        
        Administrador admin = new Administrador(3, "Admin User", "admin@email.com", "temp",
                                                "3015551111", "Sistemas");
        usuarios.add(admin);
    }
    
    /**
     * Guarda usuarios en CSV
     */
    public void guardarUsuarios() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_CSV))) {
            // Escribir encabezado
            writer.println("ID,Nombre,Email,Rol,Telefono,InfoAdicional1,InfoAdicional2,InfoAdicional3");
            
            // Escribir datos
            for (Usuario u : usuarios) {
                if (u instanceof Cliente) {
                    Cliente c = (Cliente) u;
                    writer.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
                        c.getId(),
                        c.getNombre(),
                        c.getEmail(),
                        c.getRol(),
                        c.getTelefono(),
                        c.getDireccion(),
                        c.getCiudad(),
                        c.getCodigoPostal());
                } else if (u instanceof Administrador) {
                    Administrador a = (Administrador) u;
                    writer.printf("%d,%s,%s,%s,%s,%s,,,%n",
                        a.getId(),
                        a.getNombre(),
                        a.getEmail(),
                        a.getRol(),
                        a.getTelefono(),
                        a.getDepartamento());
                }
            }
            System.out.println("Usuarios guardados: " + usuarios.size());
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios CSV: " + e.getMessage());
        }
    }
    
    /**
     * Autentica un usuario por email
     * Nota: Para esta versión simplificada, cualquier email registrado funciona
     */
    public Usuario autenticar(String email) throws Exception {
        String emailNormalizado = email == null ? "" : email.trim().toLowerCase();
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail() != null && usuario.getEmail().trim().toLowerCase().equals(emailNormalizado)) {
                return usuario;
            }
        }
        throw new Exception("Usuario no encontrado");
    }

    private void asegurarAdministradorPorDefecto() {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Administrador) {
                return;
            }
        }

        int nuevoId = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        Administrador admin = new Administrador(nuevoId, "Admin User", "admin@email.com", "temp",
                "3015551111", "Sistemas");
        usuarios.add(admin);
        guardarUsuarios();
    }
    
    /**
     * Registra un nuevo usuario cliente
     */
    public void registrarCliente(String nombre, String email, 
                                 String telefono, String direccion, String ciudad, String codigoPostal) 
            throws Exception {
        // Verificar si el email ya existe
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                throw new Exception("El email ya está registrado");
            }
        }
        
        int nuevoId = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        Cliente cliente = new Cliente(nuevoId, nombre, email, "temp", telefono, 
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
