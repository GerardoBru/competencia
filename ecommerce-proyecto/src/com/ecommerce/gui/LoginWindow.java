package com.ecommerce.gui;

import com.ecommerce.util.EcommerceFacade;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Pantalla de ingreso simple por correo electrónico.
 */
public class LoginWindow extends JFrame {

    private final EcommerceFacade facade = EcommerceFacade.getInstance();
    private JTextField txtEmail;

    public LoginWindow() {
        initComponents();
    }

    private void initComponents() {
        setTitle("E-Commerce - Ingreso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel content = new JPanel(new GridLayout(4, 1, 8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Ingreso al sistema");
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);

        JPanel panelEmail = new JPanel(new BorderLayout(6, 6));
        JLabel lblEmail = new JLabel("Correo electrónico:");
        txtEmail = new JTextField();
        panelEmail.add(lblEmail, BorderLayout.NORTH);
        panelEmail.add(txtEmail, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 8, 8));
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnRegistrar = new JButton("Registrar cliente");
        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrar);

        JLabel lblAyuda = new JLabel("Admin: admin@email.com | Ingreso solo con correo");
        lblAyuda.setHorizontalAlignment(JLabel.CENTER);

        content.add(lblTitulo);
        content.add(panelEmail);
        content.add(panelBotones);
        content.add(lblAyuda);

        btnIngresar.addActionListener(e -> ingresar());
        btnRegistrar.addActionListener(e -> registrarCliente());

        add(content, BorderLayout.CENTER);
    }

    private void ingresar() {
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite su correo electrónico.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (facade.autenticar(email)) {
            dispose();
            new MainWindow(facade.getUsuarioActual()).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un usuario con ese correo.", "Ingreso fallido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarCliente() {
        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtCiudad = new JTextField();
        JTextField txtPostal = new JTextField();

        JPanel formulario = new JPanel(new GridLayout(0, 1, 6, 6));
        formulario.add(new JLabel("Nombre completo:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Correo electrónico:"));
        formulario.add(txtCorreo);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(txtTelefono);
        formulario.add(new JLabel("Dirección:"));
        formulario.add(txtDireccion);
        formulario.add(new JLabel("Ciudad:"));
        formulario.add(txtCiudad);
        formulario.add(new JLabel("Código postal:"));
        formulario.add(txtPostal);

        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Registrar cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        boolean registrado = facade.registrarCliente(
                txtNombre.getText().trim(),
                txtCorreo.getText().trim(),
                txtTelefono.getText().trim(),
                txtDireccion.getText().trim(),
                txtCiudad.getText().trim(),
                txtPostal.getText().trim());

        if (registrado) {
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
            txtEmail.setText(txtCorreo.getText().trim());
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
