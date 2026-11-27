package principal;

import javax.swing.SwingUtilities;
import misclases.VentanaCalculadora;

public class MainCalculadora {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaCalculadora().setVisible(true));
    }
}