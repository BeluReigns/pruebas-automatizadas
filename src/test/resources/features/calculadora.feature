
# language: es

Característica: Operaciones de la calculadora
  Como usuario de la calculadora
  Quiero realizar operaciones matemáticas básicas
  Para obtener resultados correctos

  Escenario: Sumar dos números positivos
    Dado que tengo una calculadora
    Cuando sumo 2 y 3
    Entonces el resultado debe ser 5

  Escenario: Restar dos números positivos
    Dado que tengo una calculadora
    Cuando resto 5 y 2
    Entonces el resultado debe ser 3
 
  Esquema del escenario: Sumar números con distintas combinaciones
    Dado que tengo una calculadora
    Cuando sumo <numeroUno> y <numeroDos>
    Entonces el resultado debe ser <resultadoEsperado>

    Ejemplos:
      | numeroUno | numeroDos | resultadoEsperado |
      | 0         | 5         | 5                 |
      | -2        | -3        | -5                |
      | -2        | 3         | 1                 |
