package misclases;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista FINAL de tests, después de comparar y filtrar los casos de la IA.
 */
public class ListaPruebas {

    public static List<CasoPrueba> obtenerListaFinal() {
        List<CasoPrueba> listaPruebas = new ArrayList<>();

        // ---- 3 tests PROPIOS ----
        listaPruebas.add(new CasoPrueba("PROPIO", "Justo debajo del límite", 199.99, 0.0, false));
        listaPruebas.add(new CasoPrueba("PROPIO", "Exactamente en el límite", 200.0, 20.0, false));
        listaPruebas.add(new CasoPrueba("PROPIO", "Compra grande", 500.0, 50.0, false));

        // ---- Tests de la IA que SÍ son válidos ----
        listaPruebas.add(new CasoPrueba("IA", "Monto cero", 0.0, 0.0, false));
        listaPruebas.add(new CasoPrueba("IA", "Justo encima del límite", 200.01, 20.001, false));
        listaPruebas.add(new CasoPrueba("IA", "Monto negativo (inválido)", -50.0, 0.0, true));

        return listaPruebas;
    }
}