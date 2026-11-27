package misclases;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal con dos vistas: Calculadora y Tests.
 */
public class VentanaPrincipal extends JFrame {

    // Paleta de colores (estilo oscuro + amarillo)
    private static final Color COLOR_FONDO = new Color(18, 18, 18);
    private static final Color COLOR_TARJETA = new Color(30, 30, 30);
    private static final Color COLOR_FILA_ALTERNA = new Color(40, 40, 40);
    private static final Color COLOR_AMARILLO = new Color(255, 210, 0);
    private static final Color COLOR_TEXTO = new Color(240, 240, 240);
    private static final Color COLOR_GRIS = new Color(160, 160, 160);
    private static final Color COLOR_VERDE = new Color(80, 220, 120);
    private static final Color COLOR_ROJO = new Color(255, 90, 90);

    private final CalculadoraDescuento calculadora = new CalculadoraDescuento();

    private final CardLayout disposicionTarjetas = new CardLayout();
    private final JPanel panelContenido = new JPanel(disposicionTarjetas);

    // Componentes de la vista Calculadora
    private JTextField campoMonto;
    private JLabel etiquetaDescuento;
    private JLabel etiquetaTotal;
    private JLabel etiquetaMensaje;

    // Componentes de la vista Tests
    private DefaultTableModel modeloTabla;
    private JLabel etiquetaResumen;

    public VentanaPrincipal() {
        setTitle("Calculadora de Descuento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(860, 580);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.add(crearPanelCalculadora(), "calculadora");
        panelContenido.add(crearPanelTests(), "tests");

        add(crearEncabezado(), BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);

        ejecutarTests(); // Deja la tabla de tests lista desde el inicio
    }

    // ------------------------------------------------------------------
    // Construcción de la interfaz
    // ------------------------------------------------------------------

    private JPanel crearEncabezado() {
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(COLOR_FONDO);
        panelEncabezado.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel titulo = crearEtiqueta("TESTS CON IA: TÚ VALIDAS", 22, COLOR_TEXTO, true);
        panelEncabezado.add(titulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        BotonModerno botonCalculadora = new BotonModerno("Calculadora");
        botonCalculadora.addActionListener(e -> disposicionTarjetas.show(panelContenido, "calculadora"));

        BotonModerno botonTests = new BotonModerno("Tests");
        botonTests.addActionListener(e -> disposicionTarjetas.show(panelContenido, "tests"));

        panelBotones.add(botonCalculadora);
        panelBotones.add(botonTests);
        panelEncabezado.add(panelBotones, BorderLayout.EAST);

        return panelEncabezado;
    }

    private JPanel crearPanelCalculadora() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(COLOR_FONDO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_AMARILLO, 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        JLabel regla = crearEtiqueta("REGLA", 12, COLOR_AMARILLO, true);
        JLabel textoRegla = crearEtiqueta("10% de descuento si el monto es mayor o igual a S/200", 16, COLOR_TEXTO, true);
        JLabel indicacion = crearEtiqueta("Ingresa el monto de la compra (S/):", 14, COLOR_GRIS, false);

        campoMonto = new JTextField();
        campoMonto.setFont(new Font("Segoe UI", Font.BOLD, 22));
        campoMonto.setBackground(COLOR_FONDO);
        campoMonto.setForeground(COLOR_AMARILLO);
        campoMonto.setCaretColor(COLOR_AMARILLO);
        campoMonto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_AMARILLO, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        campoMonto.setMaximumSize(new Dimension(400, 50));
        campoMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        campoMonto.addActionListener(e -> calcular()); // Enter también calcula

        BotonModerno botonCalcular = new BotonModerno("Calcular descuento");
        botonCalcular.setAlignmentX(Component.LEFT_ALIGNMENT);
        botonCalcular.addActionListener(e -> calcular());

        etiquetaDescuento = crearEtiqueta("S/ 0.00", 32, COLOR_VERDE, true);
        etiquetaTotal = crearEtiqueta("S/ 0.00", 32, COLOR_TEXTO, true);
        etiquetaMensaje = crearEtiqueta(" ", 14, COLOR_GRIS, false);

        tarjeta.add(regla);
        tarjeta.add(textoRegla);
        tarjeta.add(espacio(20));
        tarjeta.add(indicacion);
        tarjeta.add(espacio(8));
        tarjeta.add(campoMonto);
        tarjeta.add(espacio(16));
        tarjeta.add(botonCalcular);
        tarjeta.add(espacio(24));
        tarjeta.add(crearEtiqueta("Descuento aplicado", 13, COLOR_GRIS, false));
        tarjeta.add(etiquetaDescuento);
        tarjeta.add(espacio(10));
        tarjeta.add(crearEtiqueta("Total a pagar", 13, COLOR_GRIS, false));
        tarjeta.add(etiquetaTotal);
        tarjeta.add(espacio(10));
        tarjeta.add(etiquetaMensaje);

        contenedor.add(tarjeta, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearPanelTests() {
        JPanel contenedor = new JPanel(new BorderLayout(0, 15));
        contenedor.setBackground(COLOR_FONDO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        String[] columnas = {"Origen", "Caso de prueba", "Monto (S/)", "Esperado", "Obtenido", "Resultado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false; // La tabla es solo lectura
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(32);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.getTableHeader().setBackground(COLOR_AMARILLO);
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getColumnModel().getColumn(1).setPreferredWidth(240);

        // Renderizador para colorear filas y el resultado
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor,
                    boolean seleccionada, boolean foco, int fila, int columna) {
                super.getTableCellRendererComponent(t, valor, false, false, fila, columna);
                setBackground(fila % 2 == 0 ? COLOR_TARJETA : COLOR_FILA_ALTERNA);
                setForeground(COLOR_TEXTO);
                setHorizontalAlignment(columna >= 2 ? CENTER : LEFT);
                if (columna == 5) {
                    boolean aprobado = String.valueOf(valor).equals("APROBADO");
                    setForeground(aprobado ? COLOR_VERDE : COLOR_ROJO);
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                return this;
            }
        });

        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.getViewport().setBackground(COLOR_TARJETA);
        desplazamiento.setBorder(BorderFactory.createLineBorder(COLOR_AMARILLO, 1));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        etiquetaResumen = crearEtiqueta(" ", 16, COLOR_TEXTO, true);
        BotonModerno botonEjecutar = new BotonModerno("Ejecutar tests");
        botonEjecutar.addActionListener(e -> ejecutarTests());
        panelInferior.add(etiquetaResumen, BorderLayout.WEST);
        panelInferior.add(botonEjecutar, BorderLayout.EAST);

        contenedor.add(desplazamiento, BorderLayout.CENTER);
        contenedor.add(panelInferior, BorderLayout.SOUTH);
        return contenedor;
    }

    // ------------------------------------------------------------------
    // Acciones
    // ------------------------------------------------------------------

    private void calcular() {
        String textoIngresado = campoMonto.getText().trim().replace(",", ".");
        try {
            double monto = Double.parseDouble(textoIngresado);
            double descuento = calculadora.calcularDescuento(monto);
            double totalAPagar = calculadora.calcularTotalAPagar(monto);

            etiquetaDescuento.setText(String.format(Locale.US, "S/ %.2f", descuento));
            etiquetaTotal.setText(String.format(Locale.US, "S/ %.2f", totalAPagar));
            etiquetaMensaje.setForeground(COLOR_GRIS);
            etiquetaMensaje.setText(descuento > 0
                    ? "✔ Se aplicó el 10% de descuento."
                    : "No alcanza el mínimo de S/200: sin descuento.");
        } catch (NumberFormatException ex) {
            mostrarError("Ingresa un número válido, por ejemplo 250.50");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        etiquetaDescuento.setText("S/ 0.00");
        etiquetaTotal.setText("S/ 0.00");
        etiquetaMensaje.setForeground(COLOR_ROJO);
        etiquetaMensaje.setText(mensaje);
    }

    private void ejecutarTests() {
        modeloTabla.setRowCount(0); // Limpia la tabla
        List<CasoPrueba> listaPruebas = ListaPruebas.obtenerListaFinal();
        int cantidadAprobados = 0;

        for (CasoPrueba caso : listaPruebas) {
            boolean aprobado = caso.ejecutar(calculadora);
            if (aprobado) {
                cantidadAprobados++;
            }
            modeloTabla.addRow(new Object[]{
                caso.getOrigen(),
                caso.getNombre(),
                String.format(Locale.US, "%.2f", caso.getMonto()),
                caso.getTextoEsperado(),
                caso.obtenerTextoObtenido(calculadora),
                aprobado ? "APROBADO" : "FALLÓ"
            });
        }

        etiquetaResumen.setText("Aprobados: " + cantidadAprobados + " de " + listaPruebas.size());
        etiquetaResumen.setForeground(cantidadAprobados == listaPruebas.size() ? COLOR_VERDE : COLOR_ROJO);
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------

    private JLabel crearEtiqueta(String texto, int tamanio, Color color, boolean negrita) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", negrita ? Font.BOLD : Font.PLAIN, tamanio));
        etiqueta.setForeground(color);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    private Component espacio(int alto) {
        return javax.swing.Box.createRigidArea(new Dimension(0, alto));
    }

    /** Botón amarillo con esquinas redondeadas y efecto al pasar el mouse. */
    private static class BotonModerno extends JButton {

        private boolean mouseEncima = false;

        public BotonModerno(String texto) {
            super(texto);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.BLACK);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(190, 40));
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
            g2.setColor(mouseEncima ? new Color(255, 230, 90) : COLOR_AMARILLO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}