# Sistema de garage

Parcial universitario de Programación Avanzada construido desde cero en un repositorio independiente, con Java 17, Maven y JUnit 5. Aplicación de consola, sin frameworks ni base de datos.

## Requisitos

- JDK 17, con `JAVA_HOME` configurado y `java` en `PATH`.
- Maven 3.9 o compatible, con `mvn` en `PATH`.
- Internet para la primera descarga de dependencias de Maven.

## Estructura

```text
garage-nuevo/
├── pom.xml
├── README.md
├── decisiones-diseno.txt
├── src/main/java/ar/edu/parcial/garage/
│   ├── Calculable.java
│   ├── Vehiculo.java
│   ├── Moto.java
│   ├── Auto.java
│   ├── Camion.java
│   ├── Garage.java
│   ├── Consola.java
│   └── excepciones/
│       ├── GarageLlenoException.java
│       ├── PatenteDuplicadaException.java
│       ├── VehiculoNoEncontradoException.java
│       └── HorasInvalidasException.java
├── src/test/java/ar/edu/parcial/garage/
│   ├── VehiculoTest.java
│   ├── GarageTest.java
│   └── ConsolaTest.java
└── docs/
    ├── diagrama-clases.puml
    ├── diagrama-casos-uso.puml
    └── documentacion-uml.pdf
```

`target/` contiene resultados de compilación y pruebas; se excluye de Git. `.herramientas/` contiene únicamente herramientas portátiles locales de verificación y tampoco se versiona.

## Compilar

Desde la raíz de este repositorio (`garage-nuevo`):

```sh
mvn clean package
```

## Ejecutar

```sh
java -jar target/sistema-garage-1.0.0.jar
```

También se puede ejecutar después de `mvn compile`:

```sh
java -cp target/classes ar.edu.parcial.garage.Consola
```

Al iniciar se solicita la capacidad total, expresada en unidades de espacio. Luego se muestra:

```text
=== SISTEMA DE GARAGE ===
1. Registrar ingreso
2. Registrar salida
3. Listar vehículos
4. Estado del garage
5. Reportes
6. Salir
```

El ingreso pide tipo (1: Moto, 2: Auto, 3: Camion), patente, marca, modelo y horas estimadas enteras positivas. Los datos inválidos muestran un mensaje y permiten continuar. Si falla un ingreso, no se registra parcialmente y se vuelve al menú. La capacidad inicial inválida se vuelve a solicitar. El fin de la entrada cierra el programa de forma controlada.

## Correr tests

```sh
mvn clean test
```

JUnit 5 verifica costos, espacios, polimorfismo mediante `Calculable`, validaciones, ingresos, búsquedas, salidas, reportes, protección del listado y recuperación de errores en la consola. Son 42 casos ejecutados, incluidos los casos parametrizados. Maven genera los informes en `target/surefire-reports/`.

## Funcionalidades y reglas

| Tipo | Espacios | Tarifa por hora |
|---|---:|---:|
| Moto | 1 | $700 |
| Auto | 2 | $1000 |
| Camion | 4 | $1500 |

- Registrar ingresos con patente única entre los vehículos presentes y capacidad suficiente.
- Buscar mediante `Garage.buscarPorPatente()`; la salida utiliza esta misma búsqueda. No se agrega una séptima opción al menú solicitado.
- Registrar salidas, mostrar su costo estimado y liberar los espacios correspondientes.
- Listar datos de los vehículos presentes y consultar capacidad total, ocupada y disponible.
- Reportar cantidad total, cantidad por tipo, espacio ocupado/libre y recaudación total estimada.
- Normalizar patentes con `trim()` y `toUpperCase(Locale.ROOT)`: ` ab123cd ` y `AB123CD` identifican el mismo vehículo.

La recaudación estimada es la suma de los costos de los vehículos **actualmente presentes**. Disminuye al registrar una salida; no representa una caja histórica de cobros. El costo se calcula con las horas estimadas ingresadas, sin reloj ni fracciones de hora. La patente puede volver a ingresar después de salir. Los datos viven en memoria durante la sesión.

## UML

Los dos archivos `.puml` son las fuentes de los diagramas de clases y casos de uso. El PDF contiene ambos diagramas. El diagrama de clases muestra todas las clases de producción y sus miembros declarados; las clases estándar de Java se muestran solo como referencia. Las clases de tests no forman parte del modelo de producción.

Para regenerar imágenes con PlantUML:

```sh
java -jar plantuml.jar -charset UTF-8 -tpng docs/diagrama-clases.puml docs/diagrama-casos-uso.puml
```

Se utilizó PlantUML 1.2025.2 para los diagramas y ReportLab para componer el PDF. Estas herramientas son de documentación y no son dependencias de la aplicación.
