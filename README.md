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

## Prueba de rendimiento con JMeter

Utilicé Apache JMeter 5.6.3 con Java 17.0.15 en macOS.
El plan está guardado en `performance/calculadora.jmx`.

La prueba ejecuta el método `sumar(2, 3)` de la clase Calculadora
y comprueba que devuelva 5. No utiliza una API ni realiza solicitudes HTTP.

### Configuración

- Cinco usuarios simulados.
- Inicio gradual de los usuarios durante cinco segundos.
- Cien repeticiones por usuario: 500 muestras en total.
- Pausa de 100 ms antes de cada muestra.
- Sampler JSR223 con Groovy y caché de compilación activada.

### Ejecución

Primero se debe compilar el proyecto para disponer de
`target/classes/cl/iplacex/Calculadora.class`.

Con JMeter descomprimido en Descargas, ejecutar desde la raíz del proyecto:

```bash
mkdir -p target
resultado_dir=$(mktemp -d "$(pwd)/target/jmeter-XXXXXX")

bash "$HOME/Downloads/apache-jmeter-5.6.3/bin/jmeter" \
  -n \
  -t performance/calculadora.jmx \
  -Juser.classpath="$(pwd)/target/classes" \
  -l "$resultado_dir/resultados.jtl" \
  -j "$resultado_dir/jmeter.log" \
  -e \
  -o "$resultado_dir/reporte"
```

Si la ejecución y la generación del reporte terminan correctamente,
abrirlo en la misma terminal:

```bash
open "$resultado_dir/reporte/index.html"
```

### Resultados de la ejecución local

| Indicador | Resultado |
| --- | --- |
| Muestras | 500 |
| Fallidas | 0 |
| Porcentaje de errores | 0 % |
| Transacciones por segundo | 34,91 |
| Tiempo de respuesta medio | 1,77 ms |
| Tiempo de respuesta p95 | 1 ms |
| Tiempo de respuesta máximo | 551 ms |

Los TPS indican las muestras procesadas por segundo; en esta ejecución
todas fueron exitosas. El p95 describe el percentil 95 de los tiempos
registrados. El porcentaje de errores indica la proporción de muestras
fallidas respecto del total.

Estos resultados corresponden a una ejecución local, no a una simulación.
Los tiempos incluyen el trabajo del sampler y no miden latencia de red.
La pausa limita la carga, por lo que los TPS no representan la capacidad
máxima de la calculadora. Un tiempo registrado de 0 ms no implica que
la operación sea instantánea.

El máximo observado fue de 551 ms. No se investigó su causa.
Los umbrales APDEX mostrados por JMeter son los predeterminados del
reporte y no constituyen alertas configuradas.

## Dashboard del pipeline — simulación

Esta tabla simula una vista conjunta de pruebas funcionales y rendimiento.
Los valores proceden de ejecuciones locales reales ya revisadas, pero
su recopilación en esta vista es manual. No representa un dashboard
actualizado automáticamente ni una única ejecución de CI.

| Indicador | Resultado observado | Fuente |
| --- | --- | --- |
| Pruebas unitarias aprobadas | 2 de 2 | Surefire |
| Casos BDD aprobados | 5 de 5 | Cucumber |
| Total de pruebas funcionales fallidas | 0 | Surefire |
| Pruebas omitidas | 0 | Surefire |
| Muestras de rendimiento | 500 | JMeter |
| Transacciones por segundo | 34,91 | JMeter |
| Tiempo de respuesta medio | 1,77 ms | JMeter |
| Tiempo de respuesta p95 | 1 ms | JMeter |
| Tiempo de respuesta máximo | 551 ms | JMeter |
| Errores de rendimiento | 0 % | JMeter |

Los reportes funcionales están disponibles en
`target/site/surefire-report.html` y `target/cucumber-report.html`.
El reporte de rendimiento está dentro de la carpeta
`target/jmeter-.../reporte/` creada para esa ejecución.

### Propuesta de integración en CI

Actualmente GitHub Actions ejecuta JUnit y Cucumber y publica sus reportes.
La prueba JMeter se ejecutó localmente; todavía no forma parte del workflow.

Para integrar esta vista, agregaría una etapa posterior a las pruebas
funcionales que prepare JMeter, ejecute `performance/calculadora.jmx`
con acceso a las clases compiladas y publique el archivo JTL y su
reporte HTML como artefactos.

Después, un paso de resumen recogería los resultados de Surefire y JMeter
para mostrar esta tabla en el resumen de la ejecución. Cada resultado
se identificaría con su commit y ejecución para evitar mezclar mediciones
de versiones diferentes.

Esta integración es una propuesta. No se ha implementado la recopilación
automática ni un historial de métricas de rendimiento.

## Alertas automáticas — propuesta y simulación

Actualmente el workflow muestra el estado de la compilación y las pruebas.
No he configurado envíos a Slack o correo ni evaluación automática de
umbrales de rendimiento.

### Reglas propuestas

| Situación | Condición propuesta | Acción |
| --- | --- | --- |
| Fallo funcional | Al menos una prueba JUnit o Cucumber falla | Marcar la ejecución como fallida y notificar. |
| Error de rendimiento | Porcentaje de errores mayor que 0 % | Marcar la validación de rendimiento como fallida y notificar. |
| Tiempo de respuesta elevado | p95 mayor que 100 ms | Notificar la superación del umbral. |
| Ejecución incompleta | Cantidad de muestras distinta de 500 | Avisar que el resultado no corresponde al plan esperado. |

Estos umbrales son una propuesta académica para futuras ejecuciones,
no requisitos de negocio ni reglas ya implementadas. Deben revisarse
con varias mediciones en condiciones comparables.

Los TPS se mostrarían como indicador informativo. No establecería todavía
un mínimo porque la pausa del plan limita la carga y solo contamos con
una ejecución de referencia.

### Cómo se configurarían

Para los fallos funcionales, utilizaría el estado del job de GitHub Actions
y habilitaría las notificaciones de ejecuciones fallidas en la cuenta.
La recepción de esas notificaciones no se ha comprobado en esta actividad.

Para rendimiento, agregaría al workflow un paso que lea los resultados
JTL, compruebe las reglas y termine con un código de salida distinto de
cero si alguna se incumple. JMeter puede completar su ejecución aunque
existan muestras fallidas: finalizar el comando no demuestra por sí solo
que se cumplieron los criterios de calidad.

Un paso posterior enviaría el aviso por correo o mediante un webhook
de Slack, almacenando las credenciales en los secretos de GitHub.
El mensaje incluiría la rama, el commit, la regla incumplida y el enlace
a la ejecución y sus reportes.

Esta evaluación puede realizarse dentro del pipeline sin instalar Grafana.
Para alertas continuas fuera de las ejecuciones de pruebas, sería necesario
un sistema adicional de monitoreo.

### Ejemplo de alerta simulada

Los siguientes datos son ficticios y solo ilustran el aviso:

> SIMULACIÓN — Alerta de rendimiento
>
> Funcionalidad: suma de la calculadora.
> Muestras: 500.
> Errores: 2 %; condición permitida propuesta: 0 %.
> Tiempo de respuesta p95: 150 ms; límite propuesto: 100 ms.
> Acción: revisar las muestras fallidas y los tiempos del reporte.
> El aviso real incluiría el commit y el enlace a la ejecución.

Este mensaje no fue enviado. La ejecución real revisada tuvo
0 % de errores y un p95 de 1 ms.

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
