package misclases;

/**
 * Clase con la lógica de negocio.
 * Regla: una compra recibe 10% de descuento si el monto es >= S/200.
 */
public class CalculadoraDescuento {

    // Constantes para no repetir "números mágicos" en el código
    public static final double MONTO_MINIMO_PARA_DESCUENTO = 200.0;
    public static final double PORCENTAJE_DESCUENTO = 0.10;

    /**
     * Calcula el monto del descuento (en soles).
     * @param monto monto de la compra
     * @return el descuento aplicado (0 si no cumple la regla)
     * @throws IllegalArgumentException si el monto es negativo o no es un número
     */
    public double calcularDescuento(double monto) {
        if (Double.isNaN(monto) || monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (monto >= MONTO_MINIMO_PARA_DESCUENTO) {
            return monto * PORCENTAJE_DESCUENTO;
        }
        return 0.0;
    }

    /**
     * Calcula cuánto debe pagar el cliente después del descuento.
     */
    public double calcularTotalAPagar(double monto) {
        return monto - calcularDescuento(monto);
    }
}