package misclases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Interfaz gráfica de la calculadora básica (estilo oscuro con acentos amarillos).
 */
public class VentanaCalculadora extends JFrame {

    // Paleta de colores
    private static final Color COLOR_FONDO = new Color(18, 18, 18);
    private static final Color COLOR_PANTALLA = new Color(30, 30, 30);
    private static final Color COLOR_AMARILLO = new Color(255, 210, 0);
    private static final Color COLOR_TECLA_NUMERO = new Color(50, 50, 50);
    private static final Color COLOR_TECLA_FUNCION = new Color(120, 120, 120);
    private static final Color COLOR_TEXTO = new Color(240, 240, 240);
    private static final Color COLOR_GRIS = new Color(160, 160, 160);
    private static final Color COLOR_ROJO = new Color(255, 90, 90);

    private final CalculadoraBasica calculadora = new CalculadoraBasica();

    private JLabel etiquetaOperacion; // Muestra, por ejemplo: "5 +"
    private JLabel etiquetaPantalla;  // Muestra el número actual

    // Estado de la calculadora
    private String textoPantalla = "0";
    private double valorAcumulado = 0;
    private String operadorPendiente = null;
    private boolean empezandoNumero = true;
    private boolean hayError = false;

    public VentanaCalculadora() {
        setTitle("Calculadora");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 560);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new BorderLayout(0, 15));

        add(crearPantalla(), BorderLayout.NORTH);
        add(crearTeclado(), BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------
    // Construcción de la interfaz
    // ------------------------------------------------------------------

    private JPanel crearPantalla() {
        JPanel panelPantalla = new JPanel(new GridLayout(2, 1));
        panelPantalla.setBackground(COLOR_PANTALLA);
        panelPantalla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_AMARILLO, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        etiquetaOperacion = new JLabel(" ", SwingConstants.RIGHT);
        etiquetaOperacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        etiquetaOperacion.setForeground(COLOR_GRIS);

        etiquetaPantalla = new JLabel("0", SwingConstants.RIGHT);
        etiquetaPantalla.setFont(new Font("Segoe UI", Font.BOLD, 40));
        etiquetaPantalla.setForeground(COLOR_TEXTO);

        panelPantalla.add(etiquetaOperacion);
        panelPantalla.add(etiquetaPantalla);
        return panelPantalla;
    }

    private JPanel crearTeclado() {
        JPanel panelTeclado = new JPanel(new GridLayout(5, 4, 10, 10));
        panelTeclado.setOpaque(false);

        String[][] teclas = {
            {"C", "DEL", "%", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"±", "0", ".", "="}
        };

        for (String[] fila : teclas) {
            for (String tecla : fila) {
                BotonCalculadora boton = crearBoton(tecla);
                boton.addActionListener(e -> procesarTecla(tecla));
                panelTeclado.add(boton);
            }
        }
        return panelTeclado;
    }

    /** Elige el color del botón según su tipo. */
    private BotonCalculadora crearBoton(String texto) {
        if ("÷×-+=".contains(texto)) {
            return new BotonCalculadora(texto, COLOR_AMARILLO, Color.BLACK);
        }
        if (texto.equals("C") || texto.equals("DEL") || texto.equals("%") || texto.equals("±")) {
            return new BotonCalculadora(texto, COLOR_TECLA_FUNCION, Color.BLACK);
        }
        return new BotonCalculadora(texto, COLOR_TECLA_NUMERO, COLOR_TEXTO);
    }

    // ------------------------------------------------------------------
    // Lógica de teclas
    // ------------------------------------------------------------------

    private void procesarTecla(String tecla) {
        if (hayError) {
            reiniciar(); // Después de un error se empieza limpio
        }

        if (tecla.matches("[0-9]")) {
            ingresarDigito(tecla);
            return;
        }

        switch (tecla) {
            case ".":
                ingresarPunto();
                break;
            case "C":
                reiniciar();
                break;
            case "DEL":
                borrarUltimo();
                break;
            case "±":
                cambiarSigno();
                break;
            case "%":
                aplicarPorcentaje();
                break;
            case "=":
                calcularResultado();
                break;
            default:
                ingresarOperador(tecla); // + - × ÷
        }
    }

    private void ingresarDigito(String digito) {
        if (!empezandoNumero && textoPantalla.length() >= 15) {
            return; // Límite de dígitos
        }
        if (empezandoNumero || textoPantalla.equals("0")) {
            mostrarPantalla(digito);
        } else {
            mostrarPantalla(textoPantalla + digito);
        }
        empezandoNumero = false;
    }

    private void ingresarPunto() {
        if (empezandoNumero) {
            mostrarPantalla("0.");
            empezandoNumero = false;
        } else if (!textoPantalla.contains(".")) {
            mostrarPantalla(textoPantalla + ".");
        }
    }

    private void ingresarOperador(String operador) {
        try {
            double valorActual = Double.parseDouble(textoPantalla);

            if (operadorPendiente != null && !empezandoNumero) {
                // Operaciones encadenadas: en "2 + 3 +" primero se resuelve 2 + 3
                valorAcumulado = calculadora.calcular(valorAcumulado, valorActual, operadorPendiente);
                mostrarPantalla(formatear(valorAcumulado));
            } else if (operadorPendiente == null) {
                valorAcumulado = valorActual;
            }
            // Si ya había operador y no se escribió otro número, solo se cambia el operador

            operadorPendiente = operador;
            empezandoNumero = true;
            etiquetaOperacion.setText(formatear(valorAcumulado) + " " + operador);
        } catch (ArithmeticException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void calcularResultado() {
        if (operadorPendiente == null) {
            return;
        }
        try {
            double valorActual = Double.parseDouble(textoPantalla);
            double resultado = calculadora.calcular(valorAcumulado, valorActual, operadorPendiente);

            etiquetaOperacion.setText(formatear(valorAcumulado) + " " + operadorPendiente
                    + " " + formatear(valorActual) + " =");
            mostrarPantalla(formatear(resultado));
            operadorPendiente = null;
            empezandoNumero = true;
        } catch (ArithmeticException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void aplicarPorcentaje() {
        try {
            double valorActual = Double.parseDouble(textoPantalla);
            mostrarPantalla(formatear(valorActual / 100));
            empezandoNumero = false;
        } catch (ArithmeticException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void cambiarSigno() {
        if (textoPantalla.equals("0")) {
            return;
        }
        if (textoPantalla.startsWith("-")) {
            mostrarPantalla(textoPantalla.substring(1));
        } else {
            mostrarPantalla("-" + textoPantalla);
        }
    }

    private void borrarUltimo() {
        if (empezandoNumero) {
            return;
        }
        String nuevoTexto = textoPantalla.substring(0, textoPantalla.length() - 1);
        if (nuevoTexto.isEmpty() || nuevoTexto.equals("-")) {
            nuevoTexto = "0";
        }
        mostrarPantalla(nuevoTexto);
    }

    private void reiniciar() {
        valorAcumulado = 0;
        operadorPendiente = null;
        empezandoNumero = true;
        hayError = false;
        etiquetaOperacion.setText(" ");
        etiquetaOperacion.setForeground(COLOR_GRIS);
        mostrarPantalla("0");
    }

    // ------------------------------------------------------------------
    // Utilidades de pantalla
    // ------------------------------------------------------------------

    private void mostrarPantalla(String texto) {
        textoPantalla = texto;
        etiquetaPantalla.setText(texto);
        // Reduce el tamaño de letra cuando el número es largo
        int tamanio = texto.length() > 16 ? 20 : (texto.length() > 10 ? 28 : 40);
        etiquetaPantalla.setFont(new Font("Segoe UI", Font.BOLD, tamanio));
    }

    private void mostrarError(String mensaje) {
        reiniciar();
        etiquetaPantalla.setText("Error");
        etiquetaOperacion.setForeground(COLOR_ROJO);
        etiquetaOperacion.setText(mensaje);
        hayError = true;
    }

    /** Convierte el número a texto sin decimales sobrantes (evita 0.30000000000000004). */
    private String formatear(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor)) {
            throw new ArithmeticException("Resultado no válido");
        }
        BigDecimal numero = new BigDecimal(valor)
                .setScale(10, RoundingMode.HALF_UP)
                .stripTrailingZeros();
        return numero.toPlainString();
    }

    /** Botón redondeado con efecto al pasar el mouse. */
    private static class BotonCalculadora extends JButton {

        private final Color colorFondo;
        private boolean mouseEncima = false;

        public BotonCalculadora(String texto, Color colorFondo, Color colorTexto) {
            super(texto);
            this.colorFondo = colorFondo;
            setFont(new Font("Segoe UI", Font.BOLD, 20));
            setForeground(colorTexto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mouseEncima = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mouseEncima = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(mouseEncima ? colorFondo.brighter() : colorFondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}