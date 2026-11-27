package principal;

import javax.swing.SwingUtilities;
import misclases.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        // Swing debe iniciarse en el hilo de eventos
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}