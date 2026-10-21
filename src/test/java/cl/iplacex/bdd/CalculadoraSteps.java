
package cl.iplacex.bdd;

import cl.iplacex.Calculadora;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraSteps {

    private Calculadora calculadora;
    private int resultado;

    @Dado("que tengo una calculadora")
    public void queTengoUnaCalculadora() {
        calculadora = new Calculadora();
    }

    @Cuando("sumo {int} y {int}")
    public void sumoDosNumeros(int numeroUno, int numeroDos) {
        resultado = calculadora.sumar(numeroUno, numeroDos);
    }

    @Cuando("resto {int} y {int}")
    public void restoDosNumeros(int numeroUno, int numeroDos) {
        resultado = calculadora.restar(numeroUno, numeroDos);
    }

    @Entonces("el resultado debe ser {int}")
    public void verificoElResultado(int resultadoEsperado) {
        assertEquals(resultadoEsperado, resultado);
    }
}
