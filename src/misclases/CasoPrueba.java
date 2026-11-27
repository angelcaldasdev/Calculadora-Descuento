package misclases;

/**
 * Representa un caso de prueba: un monto de entrada y el resultado esperado.
 */
public class CasoPrueba {

    private final String origen;            // "PROPIO" o "IA"
    private final String nombre;            // descripción del caso
    private final double monto;             // entrada
    private final double descuentoEsperado; // salida esperada
    private final boolean esperaExcepcion;  // true si debe lanzar error

    public CasoPrueba(String origen, String nombre, double monto,
                      double descuentoEsperado, boolean esperaExcepcion) {
        this.origen = origen;
        this.nombre = nombre;
        this.monto = monto;
        this.descuentoEsperado = descuentoEsperado;
        this.esperaExcepcion = esperaExcepcion;
    }

    /** Ejecuta el test y devuelve true si el resultado coincide con lo esperado. */
    public boolean ejecutar(CalculadoraDescuento calculadora) {
        try {
            double descuentoObtenido = calculadora.calcularDescuento(monto);
            if (esperaExcepcion) {
                return false; // esperábamos error y no ocurrió
            }
            // Tolerancia pequeña porque los double pueden tener decimales mínimos
            return Math.abs(descuentoObtenido - descuentoEsperado) < 0.0001;
        } catch (IllegalArgumentException ex) {
            return esperaExcepcion;
        }
    }

    /** Texto del resultado obtenido, para mostrarlo en la tabla. */
    public String obtenerTextoObtenido(CalculadoraDescuento calculadora) {
        try {
            return String.format("%.3f", calculadora.calcularDescuento(monto));
        } catch (IllegalArgumentException ex) {
            return "Error";
        }
    }

    public String getOrigen() { return origen; }
    public String getNombre() { return nombre; }
    public double getMonto() { return monto; }
    public String getTextoEsperado() {
        return esperaExcepcion ? "Error" : String.format("%.3f", descuentoEsperado);
    }
}