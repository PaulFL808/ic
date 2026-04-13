# Documentacion Tecnica del Proyecto Hirata

## 1. Introduccion

El proyecto Hirata corresponde a un prototipo de aplicacion de escritorio desarrollado en Java para apoyar la gestion operativa de una empresa de transportes.
El sistema automatiza el registro de kilometraje de camiones, administra la flota y los conductores, almacena mantenimientos preventivos y genera alertas cuando un vehiculo supera el umbral definido de 5.000 km desde su ultimo mantenimiento.

## 2. Objetivo tecnico

El objetivo tecnico del sistema es implementar una solucion organizada, mantenible y facil de extender, utilizando una separacion clara de responsabilidades mediante los patrones MVC y DAO.

## 3. Stack tecnologico

- Lenguaje: Java
- Interfaz grafica: Swing
- Persistencia: MySQL
- Acceso a datos: JDBC
- Pruebas unitarias: JUnit 5
- Gestion de dependencias: Maven

## 4. Arquitectura general

El sistema sigue una arquitectura por capas.

### 4.1 Capa de presentacion

Implementada con Swing.
Se encarga de desplegar formularios, tablas, pestañas y mensajes al usuario.

Archivos relevantes:

- `MainFrame`
- `DashboardPanel`
- `MileagePanel`
- `FleetPanel`
- `MaintenancePanel`

### 4.2 Capa de control

Representada por `ApplicationController`.
Centraliza la recepcion de eventos de la interfaz, interpreta las acciones del usuario y coordina la comunicacion con los servicios.

### 4.3 Capa de negocio

Implementada en la carpeta `service`.
Contiene las reglas del sistema, validaciones y calculos.

Clases principales:

- `FleetService`
- `MileageService`
- `MaintenanceService`
- `AlertService`

### 4.4 Capa de persistencia

Implementada con interfaces DAO y clases JDBC.
Su responsabilidad es aislar las operaciones SQL del resto del sistema.

Interfaces:

- `DriverDao`
- `TruckDao`
- `MileageLogDao`
- `MaintenanceDao`

Implementaciones:

- `JdbcDriverDao`
- `JdbcTruckDao`
- `JdbcMileageLogDao`
- `JdbcMaintenanceDao`

### 4.5 Capa de modelo

Representa las entidades de dominio del sistema.

Clases:

- `Driver`
- `Truck`
- `MileageLog`
- `MaintenanceRecord`
- `TruckAlert`

## 5. Estructura de carpetas

### `database`

Contiene el script `schema.sql`, responsable de:

- crear la base `hirata_db`
- crear las tablas del sistema
- definir claves foraneas
- agregar datos semilla

### `src/main/resources`

Contiene `database.properties`, usado para configurar la conexion JDBC a MySQL.

### `src/main/java/cl/hirata/app`

Contiene `Main.java`, punto de entrada de la aplicacion.

### `src/main/java/cl/hirata/config`

Contiene `AppConfig.java`, que carga propiedades de configuracion.

### `src/main/java/cl/hirata/util`

Incluye clases utilitarias compartidas:

- `DatabaseConnection`
- `DataAccessException`

### `src/main/java/cl/hirata/model`

Define las entidades del dominio.

### `src/main/java/cl/hirata/dao`

Define contratos DAO.

### `src/main/java/cl/hirata/dao/jdbc`

Implementa el acceso real a MySQL mediante JDBC.

### `src/main/java/cl/hirata/service`

Contiene la logica de negocio.

### `src/main/java/cl/hirata/controller`

Contiene el controlador principal.

### `src/main/java/cl/hirata/view`

Contiene las vistas Swing.

### `src/main/java/cl/hirata/view/model`

Contiene modelos de tabla para `JTable`.

### `src/test/java/cl/hirata/service`

Contiene pruebas unitarias sobre la logica de negocio.

## 6. Descripcion tecnica de clases relevantes

### `Main.java`

Responsabilidades:

- inicializar configuracion
- crear el proveedor de conexiones
- instanciar DAO
- instanciar servicios
- crear la vista principal
- crear el controlador y ejecutar `initialize()`

### `AppConfig.java`

Lee `database.properties` y expone:

- URL de base de datos
- usuario
- contrasena

### `DatabaseConnection.java`

Encapsula la creacion de conexiones JDBC usando `DriverManager`.

### `ApplicationController.java`

Es el orquestador principal de la aplicacion.
Sus responsabilidades son:

- asociar botones y eventos
- leer datos de formularios
- transformar texto a enteros o fechas
- invocar servicios
- refrescar tablas, combos y dashboard
- mostrar mensajes de informacion o error

### `FleetService.java`

Administra operaciones sobre camiones y conductores.
Realiza validaciones de integridad antes de persistir.

### `MileageService.java`

Implementa el proceso de registro de kilometraje.
Flujo interno:

1. valida que los kilometros sean mayores que cero
2. obtiene el camion desde `TruckDao`
3. suma el kilometraje ingresado al kilometraje actual
4. actualiza el camion
5. crea un registro en `mileage_logs`

### `AlertService.java`

Implementa la regla de mantenimiento preventivo.
Flujo interno:

1. obtiene el ultimo mantenimiento del camion
2. calcula kilometros desde mantenimiento
3. compara con el umbral `5000`
4. construye un objeto `TruckAlert`

### `MaintenanceService.java`

Administra operaciones CRUD de mantenimientos y valida campos obligatorios.

### Clases JDBC

Cada implementacion JDBC:

- abre conexion
- prepara sentencias SQL
- asigna parametros
- ejecuta consultas o actualizaciones
- transforma resultados en objetos Java

## 7. Modelo de datos

El sistema utiliza cuatro tablas principales:

### `drivers`

Almacena:

- id
- nombre
- licencia
- telefono

### `trucks`

Almacena:

- id
- marca
- modelo
- ano
- conductor asignado
- kilometraje actual

### `mileage_logs`

Almacena:

- id
- camion
- fecha del viaje
- kilometros recorridos
- kilometraje total posterior al viaje
- notas

### `maintenance_records`

Almacena:

- id
- camion
- fecha de mantenimiento
- tipo de mantenimiento
- kilometraje al momento del servicio
- observaciones

## 8. Implementacion de requerimientos funcionales

### RF-01 Registro de kilometraje

Vista implicada:

- `MileagePanel`

Clases involucradas:

- `ApplicationController`
- `MileageService`
- `TruckDao`
- `MileageLogDao`

Operacion:

- el usuario registra kilometros
- el sistema actualiza el camion
- el sistema guarda historial del viaje

### RF-02 Gestion de camiones y conductores

Vista implicada:

- `FleetPanel`

Clases involucradas:

- `ApplicationController`
- `FleetService`
- `DriverDao`
- `TruckDao`

Operacion:

- CRUD completo para conductores
- CRUD completo para camiones
- asignacion de conductor a camion

### RF-03 Sistema de alertas

Vista implicada:

- `DashboardPanel`
- `MileagePanel`

Clases involucradas:

- `AlertService`
- `MaintenanceDao`

Operacion:

- se calcula kilometraje desde ultima mantencion
- si el valor es mayor o igual a 5.000 km, se genera alerta visual

### RF-04 Gestion de mantenimiento

Vista implicada:

- `MaintenancePanel`

Clases involucradas:

- `ApplicationController`
- `MaintenanceService`
- `MaintenanceDao`

Operacion:

- crear
- editar
- consultar
- eliminar mantenimientos

### RF-05 Pruebas unitarias

Clases involucradas:

- `AlertServiceTest`
- `MileageServiceTest`

Objetivo:

- validar calculo de alertas
- validar actualizacion de kilometraje
- validar restricciones basicas

## 9. Flujo de ejecucion del sistema

1. `Main` inicia la aplicacion.
2. Se carga configuracion desde `database.properties`.
3. Se crea el proveedor de conexion JDBC.
4. Se instancian DAO y servicios.
5. Se crea `MainFrame`.
6. Se crea `ApplicationController`.
7. El controlador registra eventos y carga datos iniciales.
8. El usuario interactua con la interfaz.
9. El controlador llama a servicios.
10. Los servicios usan DAO para leer o escribir en MySQL.
11. La vista se refresca con los nuevos datos.

## 10. Pruebas unitarias

Las pruebas usan dobles de prueba simples en lugar de conectarse a MySQL real.
Esto permite aislar la logica de negocio.

### `AlertServiceTest`

Valida:

- activacion de alerta en el umbral
- ausencia de alerta bajo el umbral
- filtrado de alertas activas

### `MileageServiceTest`

Valida:

- actualizacion de kilometraje del camion
- persistencia del registro de kilometraje
- rechazo de kilometraje no valido

## 11. Decisiones de diseno

Las decisiones mas importantes del proyecto fueron:

- separar interfaz, control, negocio y persistencia
- centralizar la coordinacion en un controlador principal
- usar DAO para desacoplar SQL del resto del sistema
- utilizar modelos simples para representar entidades
- aplicar validaciones en la capa de servicios

Estas decisiones facilitan mantenimiento, lectura del codigo y crecimiento futuro.

## 12. Posibles mejoras futuras

- autenticacion de usuarios
- filtros por fecha o camion
- reportes exportables
- modulo de historial detallado por viaje
- notificaciones mas avanzadas
- configuracion variable del umbral de mantenimiento

## 13. Conclusion tecnica

El sistema Hirata cumple con los requerimientos funcionales definidos para el prototipo y presenta una estructura coherente con buenas practicas basicas de desarrollo en Java.
La aplicacion separa adecuadamente responsabilidades, integra persistencia con MySQL y deja una base clara para futuras extensiones.
