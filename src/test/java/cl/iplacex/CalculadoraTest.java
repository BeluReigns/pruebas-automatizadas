package cl.iplacex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraTest {

    @Test
    void sumaDosNumerosPositivos() {
        Calculadora calculadora = new Calculadora();

        int resultado = calculadora.sumar(2, 3);

        assertEquals(5, resultado);
    }

    @Test
    void restaDosNumerosPositivos() {
        Calculadora calculadora = new Calculadora();

        int resultado = calculadora.restar(5, 2);

        assertEquals(3, resultado);
    }
}