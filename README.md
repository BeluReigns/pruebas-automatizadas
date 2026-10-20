# Proyecto de automatización de pruebas

Ejercicio de la Unidad I para practicar control de versiones con Git,
gestión de dependencias con Maven y pruebas automatizadas con JUnit.

## Requisitos

- Java 17.
- Maven 3.9.
- Git.

## Obtener el proyecto

Si se entrega como ZIP, descomprimirlo y abrir la carpeta
pruebas-automatizadas.

También se puede clonar el repositorio local. Desde la carpeta que
contiene pruebas-automatizadas:

```bash
git clone ./pruebas-automatizadas ./pruebas-copia
cd pruebas-copia
```

Este comando crea otra copia del proyecto con su historial Git.
No requiere que el repositorio esté publicado en GitHub.

## Maven y pom.xml

Maven permite gestionar las dependencias, compilar el código y ejecutar
las pruebas mediante comandos.

El archivo pom.xml contiene la identificación del proyecto, la versión
de Java, las dependencias y la configuración de los plugins.

Este proyecto utiliza JUnit 5.11.0. Su versión se administra mediante
junit-bom. Las dependencias de JUnit tienen alcance test porque se
utilizan para las pruebas.

## Descargar las dependencias

Desde la raíz del proyecto:

```bash
mvn dependency:resolve
```

La primera ejecución necesita conexión a internet para descargar las
dependencias que todavía no estén en el repositorio local de Maven.

## Ejecutar las pruebas

```bash
mvn test
```

Para eliminar los resultados anteriores y volver a ejecutar:

```bash
mvn clean test
```

Maven también descarga automáticamente las dependencias necesarias
al ejecutar las pruebas.

Actualmente se ejecutan dos pruebas: AppTest y CalculadoraTest.
Esta última comprueba que sumar 2 y 3 devuelve 5.

Los reportes quedan en target/surefire-reports.

## Archivos principales

- pom.xml: configuración de Maven y dependencias.
- src/main/java/cl/iplacex/App.java: clase de ejemplo del arquetipo.
- src/main/java/cl/iplacex/Calculadora.java: método para sumar dos enteros.
- src/test/java/cl/iplacex/AppTest.java: prueba inicial del arquetipo.
- src/test/java/cl/iplacex/CalculadoraTest.java: prueba del método sumar.
- .mvn/: opciones de ejecución de Maven generadas por el arquetipo.
- .gitignore: archivos y carpetas excluidos del control de versiones.
- README.md: instrucciones del proyecto.

## Control de versiones

Se inicializó el repositorio con:

```bash
git init -b main
```

Después del commit inicial, se creó la rama de trabajo:

```bash
git switch -c feature/primer-test
```

La calculadora y su prueba se registraron con el mensaje:

```text
feat: agrega calculadora y prueba automatizada de suma
```

Para consultar las ramas y el historial:

```bash
git branch
git log --oneline --all --graph --decorate
```

## Evidencias

Las capturas de la entrega muestran la creación del proyecto con Maven,
las ramas y commits de Git y la ejecución exitosa de las pruebas.

## Material de referencia

Material de estudio ME_1 de la Unidad I de Automatización de Pruebas,
IPLACEX.

## Ejecución con Docker

Se necesita Docker Desktop instalado y en funcionamiento.
Java y Maven vienen incluidos en la imagen del contenedor.

Desde la raíz del proyecto, construir la imagen:

```bash
docker build -t pruebas-auto:java17 .
```

Ejecutar las pruebas:

```bash
docker run --rm pruebas-auto:java17
```

El contenedor ejecuta las pruebas y se detiene al terminar.
El resultado comprobado fue de dos pruebas exitosas, sin fallos ni errores.

Para trabajar con los archivos del proyecto montados desde el computador:

```bash
docker run --rm \
  --mount type=bind,source="$(pwd)",target=/app \
  pruebas-auto:java17 mvn -B test
```

El montaje permite que el contenedor utilice los archivos actuales del
proyecto. Los reportes quedan disponibles en target/surefire-reports
dentro de la carpeta del computador.

El Dockerfile define Java 17 y Maven 3.9.11. Esto reduce las diferencias
entre instalaciones manuales. Git permite identificar los cambios del
entorno y pom.xml mantiene la configuración de las dependencias.