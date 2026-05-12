package com.ecommerce;

import com.ecommerce.gui.LoginWindow;
import com.ecommerce.util.EcommerceFacade;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase principal de la aplicación E-Commerce
 * Punto de entrada único de la aplicación
 */
public class Main {
    
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    /**
     * Método principal - Punto de entrada de la aplicación
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        // Inicializar la fachada de negocio (carga datos persisntentes)
        EcommerceFacade facade = EcommerceFacade.getInstance();
        
        // Configurar el Look and Feel
        configurarLookAndFeel();
        
        // Crear y mostrar la ventana principal en el EDT (Event Dispatch Thread)
        java.awt.EventQueue.invokeLater(() -> {
            LoginWindow ventana = new LoginWindow();
            ventana.setVisible(true);
        });
    }
    
    /**
     * Configura el Look and Feel de la aplicación
     */
    private static void configurarLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
