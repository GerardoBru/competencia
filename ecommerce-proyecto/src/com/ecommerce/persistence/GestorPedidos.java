package com.ecommerce.persistence;

import com.ecommerce.models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase GestorPedidos - Maneja la persistencia de pedidos
 * Usa archivos serializados para guardar/cargar datos
 */
public class GestorPedidos {
    private static final String ARCHIVO_PEDIDOS = "pedidos.dat";
    private List<Pedido> pedidos;
    
    /**
     * Constructor
     */
    public GestorPedidos() {
        this.pedidos = new ArrayList<>();
        cargarPedidos();
    }
    
    /**
     * Carga pedidos desde archivo
     */
    @SuppressWarnings("unchecked")
    private void cargarPedidos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_PEDIDOS))) {
            this.pedidos = (List<Pedido>) ois.readObject();
            System.out.println("Pedidos cargados desde archivo: " + pedidos.size());
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de pedidos no encontrado. Iniciando vacío.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar pedidos: " + e.getMessage());
        }
    }
    
    /**
     * Guarda pedidos en archivo
     */
    public void guardarPedidos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PEDIDOS))) {
            oos.writeObject(pedidos);
            System.out.println("Pedidos guardados: " + pedidos.size());
        } catch (IOException e) {
            System.err.println("Error al guardar pedidos: " + e.getMessage());
        }
    }
    
    /**
     * Agrrega un nuevo pedido
     * Demuestra MANEJO DE EXCEPCIONES
     */
    public void agregar(Pedido pedido) throws Exception {
        if (pedido == null) {
            throw new Exception("Pedido nulo");
        }
        pedidos.add(pedido);
        guardarPedidos();
    }
    
    /**
     * Obtiene todos los pedidos
     */
    public List<Pedido> obtenerTodos() {
        return new ArrayList<>(pedidos);
    }
    
    /**
     * Obtiene un pedido por ID
     */
    public Pedido obtenerPorId(int id) throws Exception {
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        throw new Exception("Pedido no encontrado");
    }
    
    /**
     * Obtiene los pedidos de un cliente
     */
    public List<Pedido> obtenerPorCliente(Cliente cliente) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente().getId() == cliente.getId()) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }
    
    /**
     * Actualiza el estado de un pedido
     */
    public void actualizarEstado(int idPedido, String nuevoEstado) throws Exception {
        Pedido pedido = obtenerPorId(idPedido);
        pedido.cambiarEstado(nuevoEstado);
        guardarPedidos();
    }
    
    /**
     * Calcula el total de ventas
     */
    public double calcularTotalVentas() {
        return pedidos.stream()
                .filter(p -> !p.getEstado().equals("Cancelado"))
                .mapToDouble(Pedido::getTotal)
                .sum();
    }
}
