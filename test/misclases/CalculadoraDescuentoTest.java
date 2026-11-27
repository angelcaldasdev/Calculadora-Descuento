package misclases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Lista final de tests del laboratorio: 3 propios + 3 válidos de la IA.
 */
public class CalculadoraDescuentoTest {

    private static final double TOLERANCIA = 0.0001;
    private CalculadoraDescuento calculadora;

    @BeforeEach
    public void prepararCalculadora() {
        // Se crea una calculadora nueva antes de cada test
        calculadora = new CalculadoraDescuento();
    }

    // ---- Tests PROPIOS ----

    @Test
    public void montoJustoDebajoDelLimiteNoTieneDescuento() {
        assertEquals(0.0, calculadora.calcularDescuento(199.99), TOLERANCIA);
    }

    @Test
    public void montoExactoEnElLimiteTieneDescuento() {
        assertEquals(20.0, calculadora.calcularDescuento(200.0), TOLERANCIA);
    }

    @Test
    public void montoGrandeTieneDiezPorCientoDeDescuento() {
        assertEquals(50.0, calculadora.calcularDescuento(500.0), TOLERANCIA);
    }

    // ---- Tests válidos de la IA ----

    @Test
    public void montoCeroNoTieneDescuento() {
        assertEquals(0.0, calculadora.calcularDescuento(0.0), TOLERANCIA);
    }

    @Test
    public void montoJustoEncimaDelLimiteTieneDescuento() {
        assertEquals(20.001, calculadora.calcularDescuento(200.01), TOLERANCIA);
    }

    @Test
    public void montoNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularDescuento(-50.0));
    }
}