package misclases;

/**
 * Lógica de la calculadora básica. No sabe nada de la interfaz gráfica.
 * Se diseña para poder ser heredada por la calculadora científica.
 */
public class CalculadoraBasica {

    public double sumar(double primerNumero, double segundoNumero) {
        return primerNumero + segundoNumero;
    }

    public double restar(double primerNumero, double segundoNumero) {
        return primerNumero - segundoNumero;
    }

    public double multiplicar(double primerNumero, double segundoNumero) {
        return primerNumero * segundoNumero;
    }

    public double dividir(double dividendo, double divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("No se puede dividir entre cero");
        }
        return dividendo / divisor;
    }

    /**
     * Ejecuta una operación según el símbolo recibido.
     * @param operador uno de: "+", "-", "×", "÷"
     */
    public double calcular(double primerNumero, double segundoNumero, String operador) {
        switch (operador) {
            case "+":
                return sumar(primerNumero, segundoNumero);
            case "-":
                return restar(primerNumero, segundoNumero);
            case "×":
                return multiplicar(primerNumero, segundoNumero);
            case "÷":
                return dividir(primerNumero, segundoNumero);
            default:
                throw new IllegalArgumentException("Operador no válido: " + operador);
        }
    }
}