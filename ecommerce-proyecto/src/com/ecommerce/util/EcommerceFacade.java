package com.ecommerce.util;

import com.ecommerce.models.*;
import com.ecommerce.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase EcommerceFacade - Fachada que centraliza la lógica de negocio
 * Proporciona una interfaz unificada para toda la aplicación
 */
public class EcommerceFacade {
    
    private static EcommerceFacade instancia;
    private final GestorProductosCSV gestorProductos;
    private final GestorUsuariosCSV gestorUsuarios;
    private final GestorPedidosCSV gestorPedidos;
    private Usuario usuarioActual;
    
    /**
     * Constructor privado para implementar Singleton
     */
    private EcommerceFacade() {
        gestorProductos = new GestorProductosCSV();
        gestorUsuarios = new GestorUsuariosCSV();
        gestorPedidos = new GestorPedidosCSV();
        usuarioActual = null;
    }
    
    /**
     * Obtiene la instancia única de la fachada
     * Patrón SINGLETON
     */
    public static EcommerceFacade getInstance() {
        if (instancia == null) {
            instancia = new EcommerceFacade();
        }
        return instancia;
    }
    
    // =============== MÉTODOS DE AUTENTICACIÓN ===============
    
    /**
     * Autentica un usuario solo por correo electrónico.
     */
    public boolean autenticar(String email) {
        try {
            usuarioActual = gestorUsuarios.autenticar(email);
            System.out.println("Usuario autenticado: " + usuarioActual.getNombre());
            return true;
        } catch (Exception e) {
            System.err.println("Error de autenticación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Compatibilidad con el flujo anterior.
     */
    public boolean autenticar(String email, String contraseña) {
        return autenticar(email);
    }
    
    /**
     * Registra un nuevo cliente
     */
    public boolean registrarCliente(String nombre, String email, String telefono, String direccion, String ciudad, String codigoPostal) {
        try {
            gestorUsuarios.registrarCliente(nombre, email, telefono, direccion, ciudad, codigoPostal);
            System.out.println("Cliente registrado exitosamente");
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Compatibilidad con el flujo anterior.
     */
    public boolean registrarCliente(String nombre, String email, String contraseña,
                                   String telefono, String direccion, String ciudad, String codigoPostal) {
        return registrarCliente(nombre, email, telefono, direccion, ciudad, codigoPostal);
    }
    
    /**
     * Cierra la sesión del usuario actual
     */
    public void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Sesión cerrada: " + usuarioActual.getNombre());
            usuarioActual = null;
        }
    }
    
    // =============== MÉTODOS DE PRODUCTOS ===============
    
    /**
     * Obtiene todos los productos
     */
    public List<Producto> obtenerProductos() {
        return gestorProductos.obtenerTodos();
    }
    
    /**
     * Obtiene un producto por ID
     */
    public Producto obtenerProducto(int id) {
        return gestorProductos.obtenerPorId(id);
    }
    
    /**
     * Agrega un nuevo producto (solo para administradores)
     */
    public boolean agregarProducto(Producto producto) {
        if (usuarioActual instanceof Administrador) {
            gestorProductos.agregar(producto);
            return true;
        }
        System.err.println("No tienes permisos para agregar productos");
        return false;
    }
    
    /**
     * Obtiene productos por categoría
     */
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        return gestorProductos.obtenerPorCategoria(categoria);
    }

    public boolean actualizarProducto(Producto producto) {
        if (usuarioActual instanceof Administrador) {
            gestorProductos.actualizar(producto);
            return true;
        }
        System.err.println("No tienes permisos para editar productos");
        return false;
    }

    public boolean eliminarProducto(int id) {
        if (usuarioActual instanceof Administrador) {
            gestorProductos.eliminar(id);
            return true;
        }
        System.err.println("No tienes permisos para eliminar productos");
        return false;
    }
    
    // =============== MÉTODOS DE PEDIDOS ===============
    
    /**
     * Crea un pedido a partir del carrito
     */
    public boolean crearPedido(Carrito carrito, Pago pago) {
        try {
            if (!carrito.validarStock()) {
                throw new Exception("Stock insuficiente para algunos productos");
            }
            
            int nuevoId = gestorPedidos.obtenerTodos().size() + 1;
            Pedido pedido = new Pedido(nuevoId, carrito.getCliente(), carrito.getDetalles());
            pedido.asignarPago(pago);
            
            if (pedido.procesarPago()) {
                gestorPedidos.agregar(pedido);
                carrito.limpiar();
                System.out.println("Pedido creado exitosamente: " + pedido.getId());
                return true;
            } else {
                throw new Exception("El pago no pudo ser procesado");
            }
        } catch (Exception e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el historial de pedidos del cliente actual
     */
    public List<Pedido> obtenerHistorialPedidos() {
        if (usuarioActual instanceof Cliente) {
            return gestorPedidos.obtenerPorCliente((Cliente) usuarioActual);
        }
        return new ArrayList<>();
    }
    
    /**
     * Obtiene todos los pedidos (solo para administradores)
     */
    public List<Pedido> obtenerTodosPedidos() {
        if (usuarioActual instanceof Administrador) {
            return gestorPedidos.obtenerTodos();
        }
        return new ArrayList<>();
    }
    
    // =============== GETTERS ===============
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public GestorProductosCSV getGestorProductos() {
        return gestorProductos;
    }
    
    public GestorUsuariosCSV getGestorUsuarios() {
        return gestorUsuarios;
    }
    
    public GestorPedidosCSV getGestorPedidos() {
        return gestorPedidos;
    }

    public boolean esCliente() {
        return usuarioActual instanceof Cliente;
    }

    public boolean esAdministrador() {
        return usuarioActual instanceof Administrador;
    }

    public void guardarTodo() {
        gestorProductos.guardarProductos();
        gestorUsuarios.guardarUsuarios();
        gestorPedidos.guardarPedidos();
    }
}
