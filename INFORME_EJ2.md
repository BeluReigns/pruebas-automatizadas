# Administración de configuración y ambientes de prueba

Asignatura: Automatización de Pruebas
Unidad I
Estudiante: Belén Ulloa

## Diagnóstico del escenario

En el escenario presentado se identifican los siguientes problemas:

1. Los ambientes se configuran manualmente y utilizan distintas versiones
   de Java y librerías. Esto puede provocar que una misma prueba funcione
   en un computador y falle en otro.

2. No existe un registro claro de las versiones del sistema y de las
   pruebas ejecutadas. Por eso resulta difícil identificar qué cambios
   podrían explicar un fallo.

3. Los datos de prueba están poco controlados. Si las condiciones iniciales
   cambian entre ejecuciones, los resultados pueden ser diferentes aunque
   el código sea el mismo.

4. La preparación manual del ambiente dificulta reproducir los errores
   y aumenta el tiempo necesario para incorporar nuevos integrantes.

## Propuestas de solución

Para estandarizar el ambiente, propongo utilizar Docker y mantener su
configuración en un Dockerfile versionado.

También se debe guardar en Git el código, las pruebas y el pom.xml,
utilizando commits que expliquen cada modificación. Las etiquetas permiten
identificar configuraciones de referencia o líneas base.

Las versiones de las dependencias deben quedar definidas en Maven.
Los datos de prueba también deben ser conocidos y controlados, utilizando
datos de ejemplo o scripts que preparen el estado inicial cuando corresponda.

Finalmente, el README debe explicar cómo preparar el ambiente y ejecutar
las pruebas para que todos puedan seguir el mismo procedimiento.

## Implementación del ambiente

Se reutilizó el proyecto Maven desarrollado en el primer ejercicio.

Inicialmente, el Dockerfile utilizó la imagen
maven:3.9.11-eclipse-temurin-17, que incluye Maven y Java 17.

Se definió /app como directorio de trabajo y se copiaron la configuración
de Maven, el pom.xml y el directorio src.

El comando de inicio del contenedor ejecuta mvn -B test.

La imagen inicial se construyó con:

docker build -t pruebas-auto:java17 .

Las pruebas se ejecutaron con:

docker run --rm pruebas-auto:java17

El resultado fue de dos pruebas ejecutadas, sin fallos ni errores.

El README también documenta cómo clonar el repositorio local y cómo montar
la carpeta del proyecto dentro del contenedor.

No se agregó Docker Compose porque las pruebas actuales no necesitan
una base de datos ni otros servicios.

## Cómo resuelve los problemas

El uso del contenedor reduce las diferencias de Java y Maven entre
computadores. El pom.xml mantiene la configuración de las dependencias
y Git permite consultar las versiones del proyecto.

La prueba de la calculadora utiliza valores definidos y no depende
de una base de datos ni de información externa, lo que facilita repetirla.

Este enfoque mejora la reproducibilidad, aunque Docker por sí solo
no controla los datos ni las versiones de servicios externos.

## Cambio del ambiente

La configuración inicial se guardó en el commit 361d233 y se identificó
con la etiqueta ambiente-java17.

Después se actualizó la imagen base del Dockerfile a:

maven:3.9.11-eclipse-temurin-21

Se mantuvo Maven 3.9.11 para concentrar el cambio en la versión de Java.

El pom.xml conservó release 17. Esto permite utilizar el JDK 21
manteniendo la compatibilidad del código compilado con Java 17.

Se construyó la imagen pruebas-auto:java21 y se ejecutaron nuevamente
las pruebas. El resultado fue de dos pruebas exitosas.

El cambio se registró en el commit 9957968 con el mensaje:
feat: actualiza ambiente de pruebas a Java 21.

Después se integró la rama feature/ambiente-docker en main.

## Auditoría y trazabilidad

Versionar el ambiente permite consultar qué se modificó, cuándo
y con qué mensaje quedó registrado el cambio.

En este proyecto se puede comparar la configuración actual con
la versión de referencia mediante:

git diff ambiente-java17 HEAD -- Dockerfile

La trazabilidad se completa relacionando los resultados de las pruebas
con el commit y la imagen utilizados. Git conserva el historial del código,
pero los reportes de ejecución deben guardarse como evidencia adicional.

En un proyecto con Jenkins, estos reportes podrían archivarse en cada
ejecución del pipeline junto con la identificación de la versión evaluada.

## Referencias

IPLACEX. Material de estudio ME_1 y ME_2.
Unidad I de Automatización de Pruebas.

IPLACEX. Actividades formativas II y respuestas esperadas R_2.