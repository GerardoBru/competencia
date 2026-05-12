package com.ecommerce.persistence;

import com.ecommerce.models.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase GestorPedidosCSV - Persistencia de pedidos en CSV
 */
public class GestorPedidosCSV {
    private static final String ARCHIVO_CSV = "datos/pedidos.csv";
    private List<Pedido> pedidos;
    private GestorUsuariosCSV gestorUsuarios;
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Constructor
     */
    public GestorPedidosCSV() {
        this.pedidos = new ArrayList<>();
        this.gestorUsuarios = new GestorUsuariosCSV();
        crearDirectorio();
        cargarPedidos();
    }
    
    private void crearDirectorio() {
        new File("datos").mkdirs();
    }
    
    /**
     * Carga pedidos desde CSV (solo información básica)
     */
    private void cargarPedidos() {
        File archivo = new File(ARCHIVO_CSV);
        if (!archivo.exists()) {
            // Inicializar vacío
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                
                String[] campos = linea.split(",", -1);
                if (campos.length >= 6) {
                    try {
                        int id = Integer.parseInt(campos[0]);
                        int clienteId = Integer.parseInt(campos[1]);
                        LocalDateTime fecha = LocalDateTime.parse(campos[2], formato);
                        double total = Double.parseDouble(campos[3]);
                        String estado = campos[4];

                        double subtotal = total / 1.19;
                        double iva = total - subtotal;

                        Usuario usuario = gestorUsuarios.obtenerPorId(clienteId);
                        Cliente cliente;
                        if (usuario instanceof Cliente) {
                            cliente = (Cliente) usuario;
                        } else {
                            cliente = new Cliente(clienteId, "Cliente " + clienteId, "cliente@demo.com", "temp", "", "", "", "");
                        }

                        Pedido pedido = new Pedido(id, cliente, fecha, subtotal, iva, total, estado);
                        pedidos.add(pedido);
                    } catch (Exception e) {
                        System.err.println("Error al parsear pedido: " + linea);
                    }
                }
            }
            System.out.println("Pedidos cargados: " + pedidos.size());
        } catch (IOException e) {
            System.err.println("Error al cargar pedidos CSV: " + e.getMessage());
        }
    }
    
    /**
     * Guarda pedidos en CSV
     */
    public void guardarPedidos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_CSV))) {
            // Escribir encabezado
            writer.println("ID,ClienteID,Fecha,Total,Estado,MetodoPago");
            
            // Escribir datos
            for (Pedido p : pedidos) {
                writer.printf("%d,%d,%s,%.2f,%s,%s%n",
                    p.getId(),
                    p.getCliente().getId(),
                    p.getFecha().format(formato),
                    p.getTotal(),
                    p.getEstado(),
                    p.getPago() != null ? p.getPago().getClass().getSimpleName() : "Sin pago");
            }
            System.out.println("Pedidos guardados: " + pedidos.size());
        } catch (IOException e) {
            System.err.println("Error al guardar pedidos CSV: " + e.getMessage());
        }
    }
    
    /**
     * Agrega un nuevo pedido
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
