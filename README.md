# Proyecto de automatización de pruebas

Proyecto académico de IPLACEX desarrollado progresivamente en las unidades I y II. Permite practicar control de versiones con Git, gestión de dependencias con Maven, pruebas unitarias con JUnit 5, ejecución reproducible con Docker e integración continua mediante GitHub Actions.

## Requisitos y versiones

- Git.
- Para ejecución local sin Docker: JDK 17 o superior compatible y Maven 3.9.x.
- Para ejecución en contenedor: Docker Desktop operativo.
- El `pom.xml` configura `maven.compiler.release=17` y JUnit 5.11.0 mediante `junit-bom`.
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

Para abrir el HTML en macOS:

```bash
open target/site/surefire-report.html
```

En la Unidad I también se utilizó la etiqueta de imagen `pruebas-auto:java21`. Es un nombre alternativo de imagen, no una versión distinta del código.

## Integración continua: GitHub Actions

El workflow `.github/workflows/ci.yml` se activa al enviar cambios (`push`) y al abrir o actualizar solicitudes de integración (`pull_request`). Sus etapas son:

1. Descargar el repositorio con `actions/checkout`.
2. Configurar Temurin Java 21 con `actions/setup-java` y caché de Maven.
3. Ejecutar `mvn --batch-mode clean verify`.
4. Generar el reporte HTML con `mvn --batch-mode surefire-report:report-only` si las pruebas tuvieron éxito.
5. Publicar los directorios `target/surefire-reports/` y `target/site/` mediante `actions/upload-artifact`, incluso si una prueba falla y hay reportes disponibles.

Los artefactos se conservan durante 14 días según el workflow. Para consultarlos, abrir **GitHub → Actions → ejecución → Summary → Artifacts → reportes-junit**. El ZIP contiene los reportes XML/TXT y el HTML con sus recursos CSS e imágenes. El HTML es navegable una vez descargado y descomprimido.

### Evidencia de la Unidad II

- Pipeline #1: configuración inicial, finalizado correctamente.
- Pipeline #2: actualización de `setup-java` a v5, finalizado correctamente.
- Pipeline #3: incorporación del reporte HTML, finalizado correctamente.
- El reporte descargado de CI confirmó dos pruebas, cero fallos, cero errores y cero pruebas omitidas.

Estos resultados validan los casos implementados; no constituyen una garantía de ausencia de defectos en otras funcionalidades.

## Control de versiones y trazabilidad

En la Unidad I se creó `main`, la rama `feature/primer-test` y la etiqueta `ambiente-java17`. Luego se migró la imagen de Java 17 a Java 21, manteniendo Maven 3.9.11. La evolución puede consultarse mediante:

```bash
git log --oneline --all --graph --decorate
git diff ambiente-java17 HEAD -- Dockerfile
```

En la Unidad II se creó `feature/unidad2-integracion-continua`, donde se agregaron la prueba de resta, el workflow CI y los reportes HTML. Los cambios se registran mediante commits y se publican en GitHub. La integración a `main` debe realizarse mediante Pull Request después de validar el pipeline.

Para investigar fallos, relacionar el commit, la ejecución del workflow, la versión del entorno y los reportes de pruebas.

## Evidencias para la evaluación

Conservar capturas de: historial y ramas Git; ejecución de JUnit en Docker; workflow de GitHub Actions exitoso; detalle de sus pasos; sección Artifacts; y reporte HTML mostrando los casos de prueba y el porcentaje de éxito. Las capturas se incluyen en el documento de entrega, no en `target/` ni necesariamente en el repositorio.

## Material de referencia

Material de estudio de las unidades I y II de Automatización de Pruebas, IPLACEX.
