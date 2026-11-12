
# Automatización de pruebas e integración continua

Proyecto académico desarrollado para la asignatura de Automatización de Pruebas de IPLACEX.

El objetivo es implementar y ejecutar pruebas unitarias, automatizar su ejecución mediante integración continua (CI) y generar reportes de resultados.

Se utiliza una calculadora básica con operaciones de suma y resta como código de ejemplo, permitiendo concentrar el trabajo en las herramientas y prácticas de automatización.

## Tecnologías

- **Java:** implementación de la lógica de ejemplo.
- **JUnit 5:** desarrollo de pruebas unitarias.
- **Maven:** gestión de dependencias, compilación y ejecución de pruebas.
- **Docker:** ejecución en un entorno reproducible.
- **GitHub Actions:** automatización del proceso de integración continua.
- **Maven Surefire:** generación de resultados y reportes de pruebas.

## Pruebas implementadas

La clase `Calculadora` contiene dos operaciones:

- `sumar(int a, int b)`
- `restar(int a, int b)`

La clase `CalculadoraTest` contiene dos pruebas unitarias con JUnit 5:

- `sumaDosNumerosPositivos`
- `restaDosNumerosPositivos`

Estas pruebas verifican casos positivos de ambas operaciones y permiten demostrar el funcionamiento del proceso automatizado.

## Ejecución local

### Requisitos

- Java 17 o superior compatible.
- Maven 3.9.x.
- Git.

### Clonar el repositorio

```bash
git clone https://github.com/BeluReigns/pruebas-automatizadas.git
cd pruebas-automatizadas
```

### Ejecutar las pruebas

```bash
mvn clean verify
```

### Generar el reporte HTML

```bash
mvn surefire-report:report-only
```

El reporte se genera en:

`target/site/surefire-report.html`

## Ejecución con Docker

Para ejecutar las pruebas sin configurar Java y Maven directamente en el equipo:

```bash
docker build -t pruebas-automatizadas .
docker run --rm pruebas-automatizadas mvn test
```

## Integración continua con GitHub Actions

El proyecto incorpora un workflow en `.github/workflows/ci.yml` que se ejecuta mediante eventos `push` y `pull_request`.

El proceso automatizado:

1. Descarga el código del repositorio.
2. Configura Java 21 y Maven.
3. Compila el proyecto y ejecuta las pruebas.
4. Genera reportes de resultados.
5. Publica los reportes como artefactos de GitHub Actions.

Los resultados se pueden consultar desde la pestaña **Actions** del repositorio.

## Resultados

Durante el desarrollo se verificó la ejecución correcta de las dos pruebas implementadas, sin fallos ni errores en los reportes obtenidos.

El alcance corresponde a pruebas unitarias básicas y a la automatización de su ejecución. No constituye una cobertura exhaustiva de pruebas.

## Contexto académico

Proyecto desarrollado como parte de la formación en Ingeniería Informática de IPLACEX, enfocado en prácticas de calidad de software, control de versiones e integración continua.

**Autora:** Belén Ulloa.

