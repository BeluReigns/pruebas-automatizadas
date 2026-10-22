# Proyecto de automatización de pruebas

Proyecto desarrollado para las unidades I y II de Automatización de Pruebas de IPLACEX.

En la Unidad I trabajé con Git, Maven, JUnit 5 y Docker. En la Unidad II agregué GitHub Actions para ejecutar las pruebas automáticamente y Cucumber para probar la calculadora con escenarios escritos en Gherkin.

## Requisitos

- Git.
- Docker Desktop, para ejecutar las pruebas en un contenedor.
- Si se ejecuta sin Docker: JDK 17 o superior y Maven 3.9.x.

En este proyecto usé Maven 3.9.11 y Java 21 dentro de Docker. GitHub Actions también utiliza Java 21 (Temurin). El `pom.xml` está configurado para compilar con Java 17 y usa JUnit 5.11.0, Cucumber 7.20.1 y JUnit Platform Suite 1.11.0.

## Obtener el proyecto

El repositorio está en GitHub y es privado, por lo que se necesita acceso para clonarlo:

```bash
git clone https://github.com/BeluReigns/pruebas-automatizadas.git
cd pruebas-automatizadas
```

## Estructura del proyecto

```text
.github/workflows/ci.yml
.mvn/
src/main/java/cl/iplacex/
  App.java
  Calculadora.java
src/test/java/cl/iplacex/
  CalculadoraTest.java
  bdd/
    CalculadoraSteps.java
    RunCucumberTest.java
src/test/resources/features/
  calculadora.feature
Dockerfile
pom.xml
.gitignore
README.md
INFORME_EJ2.md
```

`Calculadora.java` tiene los métodos de suma y resta. `CalculadoraTest.java` contiene las dos pruebas unitarias con JUnit. Eliminé `AppTest` porque era una prueba de ejemplo y no comprobaba las operaciones de la calculadora.

## Pruebas con Maven y JUnit

Maven se encarga de las dependencias y de ejecutar las pruebas. Si tengo Java y Maven instalados, puedo usar:

```bash
mvn clean verify
mvn surefire-report:report-only
```

Los resultados XML y TXT quedan en `target/surefire-reports/` y el reporte HTML de Surefire en `target/site/surefire-report.html`.

## Simulación Three Amigos

Esta minuta es una simulación académica entre los roles de negocio,
desarrollo y QA. Documenta los acuerdos para los escenarios de la
calculadora; no corresponde a una reunión real entre tres participantes.

### Objetivo y alcance

Comprobar que la calculadora sume y reste dos números enteros correctamente.
Los ejemplos incluyen números positivos, cero y números negativos.
En esta actividad no se abordan decimales ni desbordamiento de enteros.

### Roles y acuerdos

| Rol | Punto discutido | Acuerdo |
| --- | --- | --- |
| Negocio | ¿Qué necesita el usuario? | Obtener resultados correctos al sumar y restar. |
| Desarrollo | ¿Cómo se realizarán las operaciones? | Reutilizar los métodos `sumar` y `restar` de `Calculadora`, manteniendo la lógica fuera de los steps. |
| QA | ¿Cómo comprobaremos el comportamiento? | Comparar cada resultado con un valor esperado y usar un esquema del escenario para probar varias combinaciones. |

### Criterios de aceptación y ejemplos

| Criterio | Ejemplo | Resultado esperado |
| --- | --- | --- |
| Sumar dos enteros positivos | 2 + 3 | 5 |
| Restar dos enteros positivos | 5 - 2 | 3 |
| Sumar cero a un entero | 0 + 5 | 5 |
| Sumar dos enteros negativos | -2 + (-3) | -5 |
| Sumar enteros de signos distintos | -2 + 3 | 1 |

Los dos primeros ejemplos se mantienen como escenarios individuales.
Los otros tres se ejecutan mediante `Esquema del escenario` y `Ejemplos`,
equivalentes a `Scenario Outline` y `Examples` en Gherkin.

Cada caso debe ejecutarse de forma independiente y aprobar únicamente
cuando el resultado obtenido coincida con el esperado.

## Pruebas BDD con Cucumber

Para la segunda actividad de la Unidad II agregué dos escenarios
individuales: sumar 2 + 3 y restar 5 - 2. Después incorporé un esquema
del escenario con tres ejemplos de suma: cero, números negativos
y números de signos distintos.

- `calculadora.feature`: contiene los escenarios escritos en español, con `Dado`, `Cuando` y `Entonces`.
- `CalculadoraSteps.java`: conecta esos pasos con los métodos Java y comprueba los resultados.
- `RunCucumberTest.java`: permite ejecutar los escenarios con JUnit Platform y generar el reporte de Cucumber.

Para ejecutar las pruebas:

```bash
mvn test
```

La última ejecución local confirmó siete pruebas aprobadas: dos pruebas
unitarias JUnit y cinco casos BDD de Cucumber, sin fallos, errores ni
pruebas omitidas. Cada fila de Ejemplos se ejecuta como un caso BDD
independiente. El reporte de Cucumber queda en
`target/cucumber-report.html`.

## Ejecutar con Docker

Desde la carpeta principal del proyecto:

```bash
docker build -t pruebas-automatizadas .
docker run --rm pruebas-automatizadas mvn test
```

Para guardar los reportes en mi Mac, ejecuto Maven con un volumen que conecta la carpeta `target` del proyecto con la del contenedor:

```bash
docker run --rm \
  -v "$(pwd)/target:/app/target" \
  pruebas-automatizadas \
  mvn verify surefire-report:report-only
```

No uso `clean` en ese comando porque intentaría borrar `/app/target`, que está montada como volumen, y daría error.

Para abrir los reportes en macOS:

```bash
open target/site/surefire-report.html
open target/cucumber-report.html
```

En la Unidad I también utilicé la imagen con el nombre `pruebas-auto:java21`.

## GitHub Actions

El archivo `.github/workflows/ci.yml` configura las pruebas automáticas cuando hago un `push` o abro un Pull Request.

El workflow descarga el proyecto, configura Java 21, ejecuta Maven y genera los reportes. Después los guarda como un artefacto llamado `reportes-pruebas`.

Para descargarlo: **GitHub → Actions → seleccionar ejecución → Summary → Artifacts → reportes-pruebas**.

El artefacto incluye los resultados XML/TXT de JUnit, el HTML de Surefire y el HTML de Cucumber. GitHub los conserva durante 14 días, según la configuración del workflow.

## Resultados de las pruebas

En la primera actividad de la Unidad II comprobé que las dos pruebas JUnit pasaban correctamente en Docker y en GitHub Actions.

Después incorporé Cucumber. La ejecución local mostró cuatro pruebas aprobadas y la ejecución #7 de GitHub Actions terminó correctamente, publicando ambos reportes HTML. La ejecución #8, correspondiente a la actualización de la documentación, también terminó correctamente.

Estos resultados corresponden a las pruebas que implementé para la calculadora.

Después de agregar el escenario parametrizado, comprobé localmente siete pruebas aprobadas. La ejecución #12 de GitHub Actions, correspondiente al commit `486a7a1`, terminó correctamente y publicó
el artefacto `reportes-pruebas`.

## Control de versiones

En la Unidad I trabajé con distintas ramas para organizar las pruebas y los cambios de Docker, incluyendo el paso de Java 17 a Java 21.

En la Unidad II utilicé las siguientes ramas:

- `feature/unidad2-integracion-continua`: pruebas JUnit, GitHub Actions
  y reportes. Integrada en `main` mediante el Pull Request #1.
- `feature/unidad2-bdd-cucumber`: Cucumber, escenarios Gherkin y reportes.
  Integrada en `main` mediante el Pull Request #2.
- `feature/unidad2-reporte-calidad`: escenario parametrizado y trabajo
  para completar rendimiento, métricas, alertas y documentación.
  Esta rama continúa en desarrollo.

Puedo revisar el historial con:

```bash
git log --oneline --all --graph --decorate
```

## Evidencias para la evaluación

Para la entrega guardé capturas de las pruebas en Docker, las ejecuciones de GitHub Actions, los reportes HTML de JUnit y Cucumber y los Pull Requests.

Las capturas se utilizarán en el documento de evaluación y no se incluyen en el repositorio.

## Material de referencia

Material de estudio de las unidades I y II de Automatización de Pruebas, IPLACEX.
