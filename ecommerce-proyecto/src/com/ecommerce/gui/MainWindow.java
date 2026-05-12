/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.ecommerce.gui;

import com.ecommerce.models.Administrador;
import com.ecommerce.models.Carrito;
import com.ecommerce.models.Cliente;
import com.ecommerce.models.DetallePedido;
import com.ecommerce.models.Pago;
import com.ecommerce.models.PagoEfectivo;
import com.ecommerce.models.PagoTransferencia;
import com.ecommerce.models.Pedido;
import com.ecommerce.models.Producto;
import com.ecommerce.models.Usuario;
import com.ecommerce.util.EcommerceFacade;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal de la aplicación.
 */
public class MainWindow extends JFrame {

    private final EcommerceFacade facade = EcommerceFacade.getInstance();
    private final Usuario usuario;
    private final boolean esCliente;
    private final boolean esAdministrador;
    private final Carrito carrito;
    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private JTabbedPane tabs;
    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloCarrito;
    private DefaultTableModel modeloPedidos;
    private DefaultTableModel modeloAdminProductos;

    private JTable tablaProductos;
    private JTable tablaCarrito;
    private JTable tablaPedidos;
    private JTable tablaAdminProductos;

    private JLabel lblUsuario;
    private JLabel lblSubtotalCarrito;
    private JLabel lblIvaCarrito;
    private JLabel lblTotalCarrito;
    private JLabel lblPagoSubtotal;
    private JLabel lblPagoIva;
    private JLabel lblPagoTotal;

    private JSpinner spinnerCantidad;
    private JComboBox<String> comboMetodoPago;
    private CardLayout cardPago;
    private JPanel panelDatosPago;
    private JTextField txtEfectivoRecibido;
    private JTextField txtBanco;
    private JTextField txtCuenta;

    private JTextField txtAdminNombre;
    private JTextField txtAdminDescripcion;
    private JTextField txtAdminPrecio;
    private JTextField txtAdminStock;
    private JTextField txtAdminCategoria;

    public MainWindow() {
        this((Usuario) null);
    }

    public MainWindow(Usuario usuario) {
        this.usuario = usuario;
        this.esCliente = usuario instanceof Cliente;
        this.esAdministrador = usuario instanceof Administrador;
        this.carrito = esCliente ? new Carrito((Cliente) usuario) : null;
        initComponents();
        cargarDatosIniciales();
    }

    private void initComponents() {
        setTitle("E-Commerce - " + (usuario != null ? usuario.getNombre() : "Invitado"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblUsuario = new JLabel(usuario != null ? "Usuario: " + usuario.getNombre() + " | Rol: " + usuario.getRol() : "Sin sesión activa");
        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        topBar.add(lblUsuario, BorderLayout.WEST);
        topBar.add(btnCerrarSesion, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.addTab("Productos", crearPanelProductos());
        if (esCliente) {
            tabs.addTab("Carrito", crearPanelCarrito());
            tabs.addTab("Pago", crearPanelPago());
            tabs.addTab("Pedidos", crearPanelPedidos());
        }
        if (esAdministrador) {
            tabs.addTab("Administración", crearPanelAdministracion());
            tabs.addTab("Pedidos", crearPanelPedidos());
        }
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloProductos = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloProductos);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        acciones.add(new JLabel("Cantidad:"), gbc);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        gbc.gridx = 1;
        acciones.add(spinnerCantidad, gbc);

        JButton btnAgregar = new JButton("Agregar al carrito");
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        gbc.gridx = 2;
        acciones.add(btnAgregar, gbc);

        JButton btnRefrescar = new JButton("Actualizar");
        btnRefrescar.addActionListener(e -> cargarProductos());
        gbc.gridx = 3;
        acciones.add(btnRefrescar, gbc);

        if (!esCliente) {
            btnAgregar.setEnabled(false);
            spinnerCantidad.setEnabled(false);
        }

        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloCarrito = new DefaultTableModel(new Object[]{"ID", "Producto", "Cantidad", "Precio Unitario", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloCarrito);
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel resumen = new JPanel(new GridLayout(3, 1, 4, 4));
        resumen.setBorder(BorderFactory.createTitledBorder("Totales"));
        lblSubtotalCarrito = new JLabel("Subtotal: " + formatoMoneda.format(0));
        lblIvaCarrito = new JLabel("IVA (19%): " + formatoMoneda.format(0));
        lblTotalCarrito = new JLabel("Total: " + formatoMoneda.format(0));
        resumen.add(lblSubtotalCarrito);
        resumen.add(lblIvaCarrito);
        resumen.add(lblTotalCarrito);

        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> eliminarDelCarrito());
        JButton btnVaciar = new JButton("Vaciar carrito");
        btnVaciar.addActionListener(e -> vaciarCarrito());
        JButton btnIrPago = new JButton("Ir a pago");
        btnIrPago.addActionListener(e -> seleccionarTab("Pago"));

        JPanel botones = new JPanel();
        botones.add(btnEliminar);
        botones.add(btnVaciar);
        botones.add(btnIrPago);

        JPanel sur = new JPanel(new BorderLayout(10, 10));
        sur.add(resumen, BorderLayout.CENTER);
        sur.add(botones, BorderLayout.SOUTH);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelPago() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel resumen = new JPanel(new GridLayout(3, 1, 4, 4));
        resumen.setBorder(BorderFactory.createTitledBorder("Resumen del pedido"));
        lblPagoSubtotal = new JLabel("Subtotal: " + formatoMoneda.format(0));
        lblPagoIva = new JLabel("IVA (19%): " + formatoMoneda.format(0));
        lblPagoTotal = new JLabel("Total: " + formatoMoneda.format(0));
        resumen.add(lblPagoSubtotal);
        resumen.add(lblPagoIva);
        resumen.add(lblPagoTotal);

        JPanel formularioPago = new JPanel(new BorderLayout(8, 8));
        formularioPago.setBorder(BorderFactory.createTitledBorder("Procesar pago"));

        comboMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Transferencia"});
        JPanel selector = new JPanel(new BorderLayout(5, 5));
        selector.add(new JLabel("Método de pago:"), BorderLayout.WEST);
        selector.add(comboMetodoPago, BorderLayout.CENTER);

        cardPago = new CardLayout();
        panelDatosPago = new JPanel(cardPago);
        panelDatosPago.add(crearFormularioEfectivo(), "Efectivo");
        panelDatosPago.add(crearFormularioTransferencia(), "Transferencia");
        comboMetodoPago.addActionListener(e -> cardPago.show(panelDatosPago, (String) comboMetodoPago.getSelectedItem()));
        cardPago.show(panelDatosPago, "Efectivo");

        JButton btnProcesar = new JButton("Procesar pedido");
        btnProcesar.addActionListener(e -> procesarPago());

        formularioPago.add(selector, BorderLayout.NORTH);
        formularioPago.add(panelDatosPago, BorderLayout.CENTER);
        formularioPago.add(btnProcesar, BorderLayout.SOUTH);

        panel.add(resumen, BorderLayout.WEST);
        panel.add(formularioPago, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearFormularioEfectivo() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Monto recibido:"));
        txtEfectivoRecibido = new JTextField();
        panel.add(txtEfectivoRecibido);
        panel.add(new JLabel("El sistema calcula el cambio automáticamente."));
        return panel;
    }

    private JPanel crearFormularioTransferencia() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Banco:"));
        txtBanco = new JTextField();
        panel.add(txtBanco);
        panel.add(new JLabel("Número de cuenta:"));
        txtCuenta = new JTextField();
        panel.add(txtCuenta);
        panel.add(new JLabel("No se solicitan datos de tarjeta."));
        return panel;
    }

    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloPedidos = new DefaultTableModel(new Object[]{"ID", "Fecha", "Subtotal", "IVA", "Total", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidos = new JTable(modeloPedidos);
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        JButton btnRefrescar = new JButton("Actualizar historial");
        btnRefrescar.addActionListener(e -> cargarPedidos());
        panel.add(btnRefrescar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelAdministracion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloAdminProductos = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaAdminProductos = new JTable(modeloAdminProductos);
        tablaAdminProductos.getSelectionModel().addListSelectionListener(e -> cargarProductoSeleccionadoEnFormulario());
        panel.add(new JScrollPane(tablaAdminProductos), BorderLayout.CENTER);

        JPanel formulario = new JPanel(new GridLayout(2, 5, 6, 6));
        txtAdminNombre = new JTextField();
        txtAdminDescripcion = new JTextField();
        txtAdminPrecio = new JTextField();
        txtAdminStock = new JTextField();
        txtAdminCategoria = new JTextField();

        formulario.add(new JLabel("Nombre"));
        formulario.add(new JLabel("Descripción"));
        formulario.add(new JLabel("Precio"));
        formulario.add(new JLabel("Stock"));
        formulario.add(new JLabel("Categoría"));
        formulario.add(txtAdminNombre);
        formulario.add(txtAdminDescripcion);
        formulario.add(txtAdminPrecio);
        formulario.add(txtAdminStock);
        formulario.add(txtAdminCategoria);

        JButton btnAgregar = new JButton("Agregar producto");
        btnAgregar.addActionListener(e -> agregarProductoAdmin());
        JButton btnActualizar = new JButton("Actualizar seleccionado");
        btnActualizar.addActionListener(e -> actualizarProductoAdmin());
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> eliminarProductoAdmin());
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarProductosAdmin());

        JPanel acciones = new JPanel();
        acciones.add(btnAgregar);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnRefrescar);

        JPanel sur = new JPanel(new BorderLayout(10, 10));
        sur.add(formulario, BorderLayout.CENTER);
        sur.add(acciones, BorderLayout.SOUTH);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarDatosIniciales() {
        cargarProductos();
        if (esCliente) {
            cargarCarrito();
            cargarPedidos();
            actualizarResumenPago();
        }
        if (esAdministrador) {
            cargarProductosAdmin();
        }
    }

    private void cargarProductos() {
        if (modeloProductos == null) {
            return;
        }
        modeloProductos.setRowCount(0);
        List<Producto> productos = facade.obtenerProductos();
        for (Producto producto : productos) {
            modeloProductos.addRow(new Object[]{
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                formatoMoneda.format(producto.getPrecio()),
                producto.getStock(),
                producto.getCategoria()
            });
        }
        if (esAdministrador) {
            cargarProductosAdmin();
        }
    }

    private void cargarCarrito() {
        if (modeloCarrito == null || carrito == null) {
            return;
        }
        modeloCarrito.setRowCount(0);
        for (DetallePedido detalle : carrito.getDetalles()) {
            modeloCarrito.addRow(new Object[]{
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                formatoMoneda.format(detalle.getPrecioUnitario()),
                formatoMoneda.format(detalle.getSubtotal())
            });
        }
        if (lblSubtotalCarrito != null) {
            lblSubtotalCarrito.setText("Subtotal: " + formatoMoneda.format(carrito.calcularSubtotal()));
            lblIvaCarrito.setText("IVA (19%): " + formatoMoneda.format(carrito.calcularIVA()));
            lblTotalCarrito.setText("Total: " + formatoMoneda.format(carrito.calcularTotal()));
        }
    }

    private void cargarPedidos() {
        if (modeloPedidos == null) {
            return;
        }
        modeloPedidos.setRowCount(0);
        List<Pedido> pedidos = esAdministrador ? facade.obtenerTodosPedidos() : facade.obtenerHistorialPedidos();
        for (Pedido pedido : pedidos) {
            modeloPedidos.addRow(new Object[]{
                pedido.getId(),
                pedido.getFecha().format(formatoFecha),
                formatoMoneda.format(pedido.getSubtotal()),
                formatoMoneda.format(pedido.getIva()),
                formatoMoneda.format(pedido.getTotal()),
                pedido.getEstado()
            });
        }
    }

    private void cargarProductosAdmin() {
        if (modeloAdminProductos == null) {
            return;
        }
        modeloAdminProductos.setRowCount(0);
        for (Producto producto : facade.obtenerProductos()) {
            modeloAdminProductos.addRow(new Object[]{
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria()
            });
        }
    }

    private void agregarAlCarrito() {
        if (!esCliente || carrito == null) {
            return;
        }
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }
        int id = Integer.parseInt(modeloProductos.getValueAt(fila, 0).toString());
        Producto producto = facade.obtenerProducto(id);
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            return;
        }
        int cantidad = (Integer) spinnerCantidad.getValue();
        if (!producto.hayStockDisponible(cantidad)) {
            JOptionPane.showMessageDialog(this, "No hay stock suficiente.", "Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }
        carrito.agregarProducto(producto, cantidad);
        cargarCarrito();
        actualizarResumenPago();
    }

    private void eliminarDelCarrito() {
        if (carrito == null) {
            return;
        }
        int fila = tablaCarrito.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un item del carrito.");
            return;
        }
        int idProducto = Integer.parseInt(modeloCarrito.getValueAt(fila, 0).toString());
        carrito.eliminarProducto(idProducto);
        cargarCarrito();
        actualizarResumenPago();
    }

    private void vaciarCarrito() {
        if (carrito != null) {
            carrito.limpiar();
            cargarCarrito();
            actualizarResumenPago();
        }
    }

    private void procesarPago() {
        if (!esCliente || carrito == null) {
            return;
        }
        if (carrito.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.");
            return;
        }

        Pago pago;
        String metodo = (String) comboMetodoPago.getSelectedItem();
        double total = carrito.calcularTotal();

        if ("Efectivo".equals(metodo)) {
            try {
                double recibido = parseMonto(txtEfectivoRecibido.getText());
                if (recibido < total) {
                    JOptionPane.showMessageDialog(this, "El monto recibido es insuficiente. El total es " + formatoMoneda.format(total), "Pago", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                pago = new PagoEfectivo(1, total, recibido);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite un monto válido.", "Pago", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            String banco = txtBanco.getText().trim();
            String cuenta = txtCuenta.getText().trim();
            if (banco.isEmpty() || cuenta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete banco y cuenta.", "Pago", JOptionPane.WARNING_MESSAGE);
                return;
            }
            pago = new PagoTransferencia(1, total, cuenta, banco);
        }

        if (facade.crearPedido(carrito, pago)) {
            JOptionPane.showMessageDialog(this, "Pedido procesado correctamente.");
            cargarCarrito();
            cargarPedidos();
            cargarProductos();
            actualizarResumenPago();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible procesar el pedido.", "Pago", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parseMonto(String texto) {
        String limpio = texto == null ? "" : texto.trim();
        limpio = limpio.replace("COP", "").replace("cop", "").replace("$", "").replace(" ", "");

        if (limpio.contains(",") && limpio.contains(".")) {
            limpio = limpio.replace(".", "").replace(",", ".");
        } else if (limpio.contains(",")) {
            limpio = limpio.replace(",", ".");
        }

        return Double.parseDouble(limpio);
    }

    private void agregarProductoAdmin() {
        if (!esAdministrador) {
            return;
        }
        try {
            List<Producto> productos = facade.obtenerProductos();
            int nuevoId = productos.stream().mapToInt(Producto::getId).max().orElse(0) + 1;
            Producto producto = new Producto(
                    nuevoId,
                    txtAdminNombre.getText().trim(),
                    txtAdminDescripcion.getText().trim(),
                    Double.parseDouble(txtAdminPrecio.getText().trim()),
                    Integer.parseInt(txtAdminStock.getText().trim()),
                    txtAdminCategoria.getText().trim());
            if (facade.agregarProducto(producto)) {
                cargarProductos();
                limpiarFormularioAdmin();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revise precio y stock.", "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarProductoAdmin() {
        if (!esAdministrador) {
            return;
        }
        int fila = tablaAdminProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }
        try {
            int id = Integer.parseInt(modeloAdminProductos.getValueAt(fila, 0).toString());
            Producto producto = new Producto(
                    id,
                    txtAdminNombre.getText().trim(),
                    txtAdminDescripcion.getText().trim(),
                    Double.parseDouble(txtAdminPrecio.getText().trim()),
                    Integer.parseInt(txtAdminStock.getText().trim()),
                    txtAdminCategoria.getText().trim());
            if (facade.actualizarProducto(producto)) {
                cargarProductos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revise precio y stock.", "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarProductoAdmin() {
        if (!esAdministrador) {
            return;
        }
        int fila = tablaAdminProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }
        int id = Integer.parseInt(modeloAdminProductos.getValueAt(fila, 0).toString());
        if (facade.eliminarProducto(id)) {
            cargarProductos();
            limpiarFormularioAdmin();
        }
    }

    private void limpiarFormularioAdmin() {
        txtAdminNombre.setText("");
        txtAdminDescripcion.setText("");
        txtAdminPrecio.setText("");
        txtAdminStock.setText("");
        txtAdminCategoria.setText("");
    }

    private void cargarProductoSeleccionadoEnFormulario() {
        if (tablaAdminProductos == null || txtAdminNombre == null) {
            return;
        }
        int fila = tablaAdminProductos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        txtAdminNombre.setText(modeloAdminProductos.getValueAt(fila, 1).toString());
        txtAdminDescripcion.setText(modeloAdminProductos.getValueAt(fila, 2).toString());
        txtAdminPrecio.setText(modeloAdminProductos.getValueAt(fila, 3).toString());
        txtAdminStock.setText(modeloAdminProductos.getValueAt(fila, 4).toString());
        txtAdminCategoria.setText(modeloAdminProductos.getValueAt(fila, 5).toString());
    }

    private void actualizarResumenPago() {
        if (carrito == null) {
            return;
        }
        if (lblSubtotalCarrito != null) {
            lblSubtotalCarrito.setText("Subtotal: " + formatoMoneda.format(carrito.calcularSubtotal()));
            lblIvaCarrito.setText("IVA (19%): " + formatoMoneda.format(carrito.calcularIVA()));
            lblTotalCarrito.setText("Total: " + formatoMoneda.format(carrito.calcularTotal()));
        }
        if (lblPagoSubtotal != null) {
            lblPagoSubtotal.setText("Subtotal: " + formatoMoneda.format(carrito.calcularSubtotal()));
            lblPagoIva.setText("IVA (19%): " + formatoMoneda.format(carrito.calcularIVA()));
            lblPagoTotal.setText("Total: " + formatoMoneda.format(carrito.calcularTotal()));
        }
    }

    private void cerrarSesion() {
        facade.cerrarSesion();
        dispose();
        new LoginWindow().setVisible(true);
    }

    private void seleccionarTab(String nombreTab) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            if (tabs.getTitleAt(i).equals(nombreTab)) {
                tabs.setSelectedIndex(i);
                return;
            }
        }
    }
}
