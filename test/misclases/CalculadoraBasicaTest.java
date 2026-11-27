package misclases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculadoraBasicaTest {

    private static final double TOLERANCIA = 0.0001;
    private CalculadoraBasica calculadora;

    @BeforeEach
    public void prepararCalculadora() {
        calculadora = new CalculadoraBasica();
    }

    @Test
    public void sumarDosNumeros() {
        assertEquals(8.0, calculadora.sumar(5, 3), TOLERANCIA);
    }

    @Test
    public void restarDosNumeros() {
        assertEquals(2.0, calculadora.restar(5, 3), TOLERANCIA);
    }

    @Test
    public void multiplicarDosNumeros() {
        assertEquals(15.0, calculadora.multiplicar(5, 3), TOLERANCIA);
    }

    @Test
    public void dividirDosNumeros() {
        assertEquals(2.5, calculadora.dividir(5, 2), TOLERANCIA);
    }

    @Test
    public void dividirEntreCeroLanzaExcepcion() {
        assertThrows(ArithmeticException.class, () -> calculadora.dividir(5, 0));
    }

    @Test
    public void calcularConOperadorValido() {
        assertEquals(6.0, calculadora.calcular(2, 3, "×"), TOLERANCIA);
    }

    @Test
    public void calcularConOperadorInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> calculadora.calcular(2, 3, "?"));
    }
}