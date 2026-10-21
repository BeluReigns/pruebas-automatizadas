# Proyecto de automatización de pruebas

Proyecto académico de IPLACEX desarrollado progresivamente en las unidades I y II. Permite practicar control de versiones con Git, gestión de dependencias con Maven, pruebas unitarias con JUnit 5, escenarios BDD con Gherkin y Cucumber, ejecución reproducible con Docker e integración continua mediante GitHub Actions.

## Requisitos y versiones

- Git.
- Para ejecución local sin Docker: JDK 17 o superior compatible y Maven 3.9.x.
- Para ejecución en contenedor: Docker Desktop operativo.
- El `pom.xml` configura `maven.compiler.release=17` y JUnit 5.11.0 mediante `junit-bom`.
- Cucumber Java y Cucumber JUnit Platform Engine 7.20.1; JUnit Platform Suite 1.11.0.
- La imagen Docker utilizada en las prácticas contiene Maven 3.9.11 y Java 21.
- GitHub Actions utiliza Java 21 (Temurin) sobre un runner Linux.

La compilación con `release 17` en Java 21 mantiene el nivel de lenguaje y API de Java 17; esto no sustituye las pruebas de compatibilidad en todos los entornos.

## Obtener el proyecto

El repositorio está alojado en GitHub como `BeluReigns/pruebas-automatizadas` y es privado. Para clonarlo se necesita autorización:

```bash
git clone https://github.com/BeluReigns/pruebas-automatizadas.git
cd pruebas-automatizadas
```

También se puede descomprimir el ZIP del proyecto. Durante la Unidad I se utilizó una copia local del repositorio con `git clone ./pruebas-automatizadas ./pruebas-copia`.

## Estructura principal

```text
.github/workflows/ci.yml             # Pipeline de integración continua
.mvn/                                # Configuración auxiliar de Maven
src/main/java/cl/iplacex/App.java    # Clase de ejemplo inicial
src/main/java/cl/iplacex/Calculadora.java
src/test/java/cl/iplacex/CalculadoraTest.java
src/test/java/cl/iplacex/bdd/CalculadoraSteps.java
src/test/java/cl/iplacex/bdd/RunCucumberTest.java
src/test/resources/features/calculadora.feature
Dockerfile
pom.xml
.gitignore
README.md
INFORME_EJ2.md
```

`Calculadora` implementa `sumar` y `restar` con enteros. `CalculadoraTest` contiene dos pruebas JUnit independientes: `sumaDosNumerosPositivos` y `restaDosNumerosPositivos`. Se eliminó el test trivial `AppTest`, que no comprobaba comportamiento funcional.

## Maven y JUnit

Maven administra dependencias, compilación, pruebas y plugins mediante `pom.xml`. Las dependencias JUnit tienen alcance `test`.

Con Java y Maven instalados localmente:

```bash
mvn dependency:resolve
mvn clean verify
mvn surefire-report:report-only
```

`verify` ejecuta las pruebas como parte del ciclo de vida de Maven. Los resultados de Surefire quedan en `target/surefire-reports/` (XML y TXT); el reporte HTML se genera en `target/site/surefire-report.html`. La primera ejecución puede requerir conexión a Internet para descargar dependencias.

## Pruebas BDD con Gherkin y Cucumber

La Actividad 2 de la Unidad II añade dos escenarios de comportamiento, sin reemplazar las dos pruebas unitarias JUnit existentes.

- `src/test/resources/features/calculadora.feature`: define en español los escenarios de suma (2 + 3 = 5) y resta (5 - 2 = 3), utilizando `Dado`, `Cuando` y `Entonces`.
- `src/test/java/cl/iplacex/bdd/CalculadoraSteps.java`: vincula los pasos Gherkin con los métodos `sumar` y `restar` de `Calculadora`, y verifica los resultados mediante `assertEquals`.
- `src/test/java/cl/iplacex/bdd/RunCucumberTest.java`: configura la suite de JUnit Platform para descubrir los archivos `.feature`, localizar las definiciones de pasos y generar el reporte HTML.

Para ejecutar ambas técnicas de prueba conjuntamente:

```bash
mvn test
```

**Resultado comprobado localmente:** cuatro pruebas aprobadas (dos JUnit y dos escenarios Cucumber), sin errores, fallos ni omisiones. El reporte BDD se genera en `target/cucumber-report.html`.

## Ejecución con Docker

Desde la raíz del proyecto:

```bash
docker build -t pruebas-automatizadas .
docker run --rm pruebas-automatizadas mvn test
```

Para generar los reportes y conservarlos en la carpeta `target` del Mac:

```bash
docker run --rm \
  -v "$(pwd)/target:/app/target" \
  pruebas-automatizadas \
  mvn verify surefire-report:report-only
```

No se usa `clean` en este último comando porque Maven intentaría borrar `/app/target`, que es el punto de montaje del volumen Docker, y la operación fallaría. Para una limpieza completa, ejecutar Maven sin montar directamente `target` o limpiar los resultados locales por separado.

Para generar y conservar los resultados de JUnit y Cucumber con Docker:

```bash
docker run --rm \
  -v "$(pwd)/target:/app/target" \
  pruebas-automatizadas \
  mvn test
```

Para abrir los HTML en macOS:

```bash
open target/site/surefire-report.html
open target/cucumber-report.html
```

En la Unidad I también se utilizó la etiqueta de imagen `pruebas-auto:java21`. Es un nombre alternativo de imagen, no una versión distinta del código.

## Integración continua: GitHub Actions

El workflow `.github/workflows/ci.yml` se activa al enviar cambios (`push`) y al abrir o actualizar solicitudes de integración (`pull_request`). Sus etapas son:

1. Descargar el repositorio con `actions/checkout`.
2. Configurar Temurin Java 21 con `actions/setup-java` y caché de Maven.
3. Ejecutar `mvn --batch-mode clean verify`.
4. Generar el reporte HTML con `mvn --batch-mode surefire-report:report-only` si las pruebas tuvieron éxito.
5. Publicar `target/surefire-reports/`, `target/site/` y `target/cucumber-report.html` mediante `actions/upload-artifact`, incluso si una prueba falla y hay reportes disponibles.

Los artefactos se conservan durante 14 días según el workflow. Para consultarlos, abrir **GitHub → Actions → ejecución → Summary → Artifacts → reportes-pruebas**. El ZIP contiene los reportes XML/TXT, el HTML de Surefire con sus recursos CSS e imágenes y el HTML de Cucumber. El HTML es navegable una vez descargado y descomprimido.

### Evidencia de la Unidad II

- Pipeline #1: configuración inicial, finalizado correctamente.
- Pipeline #2: actualización de `setup-java` a v5, finalizado correctamente.
- Pipeline #3: incorporación del reporte HTML, finalizado correctamente.
- La Actividad 1 verificó dos pruebas JUnit sin fallos, errores ni omisiones.
- La ejecución #7 (`f3d8c03`) de la rama BDD finalizó correctamente y publicó el artefacto `reportes-pruebas` con ambos reportes HTML.
- La ejecución local de la Actividad 2 confirmó cuatro pruebas aprobadas en total (dos JUnit y dos Cucumber).

Estos resultados validan los casos implementados; no constituyen una garantía de ausencia de defectos en otras funcionalidades.

## Control de versiones y trazabilidad

En la Unidad I se creó `main`, la rama `feature/primer-test` y la etiqueta `ambiente-java17`. Luego se migró la imagen de Java 17 a Java 21, manteniendo Maven 3.9.11. La evolución puede consultarse mediante:

```bash
git log --oneline --all --graph --decorate
git diff ambiente-java17 HEAD -- Dockerfile
```

En la Unidad II se creó `feature/unidad2-integracion-continua`, donde se agregaron la prueba de resta, el workflow CI y los reportes HTML. Los cambios se registran mediante commits y se publican en GitHub. Esta rama se integró en `main` mediante el Pull Request #1 (merge `cf2b70d`). Posteriormente se creó `feature/unidad2-bdd-cucumber`, con los commits `1e89594` (dependencias), `6100db9` (escenarios y definiciones de pasos) y `f3d8c03` (publicación de reportes). La integración de esta segunda rama en `main` permanece pendiente hasta completar su documentación y validar el Pull Request.

Para investigar fallos, relacionar el commit, la ejecución del workflow, la versión del entorno y los reportes de pruebas.

## Evidencias para la evaluación

Conservar capturas de: historial y ramas Git; ejecución de JUnit en Docker; workflow de GitHub Actions exitoso; detalle de sus pasos; sección Artifacts; y reportes HTML de Surefire y Cucumber mostrando los casos y escenarios aprobados; y Pull Requests con sus verificaciones. Las capturas se incluyen en el documento de entrega, no en `target/` ni necesariamente en el repositorio.

## Material de referencia

Material de estudio de las unidades I y II de Automatización de Pruebas, IPLACEX.
