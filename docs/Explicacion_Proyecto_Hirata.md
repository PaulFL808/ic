# Explicacion del Proyecto Hirata

## 1. Objetivo general del sistema

Este proyecto es un prototipo de aplicacion de escritorio desarrollado en Java para la empresa de transportes Hirata.
Su objetivo es ayudar a controlar la flota de camiones, registrar kilometraje, administrar conductores y mantenimientos, y mostrar alertas cuando un camion necesita mantenimiento preventivo.

La aplicacion fue construida usando:

- Java Swing para la interfaz grafica.
- MySQL para la base de datos.
- Patron MVC para separar responsabilidades.
- Patron DAO para aislar las consultas SQL.
- JUnit para probar la logica mas importante.

## 2. Como funciona la arquitectura general

El proyecto esta dividido en capas para que cada parte cumpla una funcion especifica y el codigo sea mas ordenado.

Flujo general:

1. El usuario interactua con la interfaz Swing.
2. La vista envia la accion al controlador.
3. El controlador usa los servicios.
4. Los servicios aplican reglas de negocio.
5. Los servicios llaman a los DAO.
6. Los DAO ejecutan consultas SQL en MySQL.
7. Los resultados vuelven al controlador.
8. El controlador actualiza la vista.

## 3. Explicacion carpeta por carpeta

### `database`

Esta carpeta contiene el archivo `schema.sql`.

Su funcion es:

- Crear la base de datos `hirata_db`.
- Crear las tablas `drivers`, `trucks`, `mileage_logs` y `maintenance_records`.
- Definir las relaciones entre tablas.
- Insertar algunos datos semilla para probar la aplicacion.

### `src/main/resources`

Aqui esta el archivo `database.properties`.

Su funcion es:

- Guardar la configuracion de conexion a MySQL.
- Definir la URL de la base de datos.
- Definir usuario y contrasena.

La idea es que el codigo no tenga las credenciales escritas directamente dentro de las clases.

### `src/main/java/cl/hirata/app`

Aqui esta la clase `Main.java`.

Su funcion es:

- Ser el punto de entrada del programa.
- Cargar la configuracion.
- Crear la conexion.
- Instanciar los DAO.
- Instanciar los servicios.
- Crear la ventana principal.
- Crear el controlador principal.
- Iniciar la aplicacion.

En otras palabras, `Main` arma todo el sistema y conecta sus piezas.

### `src/main/java/cl/hirata/config`

Aqui esta la clase `AppConfig.java`.

Su funcion es:

- Leer el archivo `database.properties`.
- Entregar la URL, usuario y contrasena a otras clases.

Es una clase simple, pero importante porque centraliza la configuracion.

### `src/main/java/cl/hirata/util`

Aqui estan clases de apoyo:

- `DatabaseConnection.java`: abre conexiones JDBC a MySQL.
- `DataAccessException.java`: encapsula errores de acceso a datos para que el resto del sistema no dependa directamente de `SQLException`.

Esta carpeta existe para poner herramientas comunes que ayudan a otras capas.

### `src/main/java/cl/hirata/model`

Aqui estan las entidades del negocio, es decir, los objetos que representan informacion del sistema.

Clases principales:

- `Driver`: representa un conductor.
- `Truck`: representa un camion.
- `MileageLog`: representa un registro de kilometraje.
- `MaintenanceRecord`: representa un registro de mantenimiento.
- `TruckAlert`: representa el resultado de la logica de alerta.

Estas clases no se encargan de mostrar ventanas ni de hacer consultas SQL. Solo representan datos.

### `src/main/java/cl/hirata/dao`

Aqui estan las interfaces DAO.

DAO significa Data Access Object.

Su funcion es definir que operaciones de base de datos existen, por ejemplo:

- listar
- buscar por id
- crear
- actualizar
- eliminar

Interfaces incluidas:

- `DriverDao`
- `TruckDao`
- `MileageLogDao`
- `MaintenanceDao`

Lo importante es que aqui se define el contrato, no el SQL.

### `src/main/java/cl/hirata/dao/jdbc`

Aqui esta la implementacion real de los DAO usando JDBC y MySQL.

Clases:

- `JdbcDriverDao`
- `JdbcTruckDao`
- `JdbcMileageLogDao`
- `JdbcMaintenanceDao`

Su funcion es:

- abrir conexion con la base de datos
- ejecutar `SELECT`, `INSERT`, `UPDATE` y `DELETE`
- convertir filas SQL en objetos Java

Esta separacion es util porque el controlador y los servicios no necesitan saber como esta escrito el SQL.

### `src/main/java/cl/hirata/service`

Aqui vive la logica de negocio.

Clases:

- `FleetService`
- `MileageService`
- `MaintenanceService`
- `AlertService`

Su funcion es aplicar reglas del sistema antes o despues de hablar con la base de datos.

Ejemplos:

- validar que un camion tenga marca y modelo
- validar que el kilometraje no sea negativo
- sumar kilometraje al camion cuando termina un viaje
- decidir si un camion necesita mantenimiento cuando supera 5.000 km desde su ultima mantencion

Esta capa es muy importante porque evita meter reglas de negocio dentro de la vista o de los DAO.

### `src/main/java/cl/hirata/controller`

Aqui esta `ApplicationController.java`.

Es una de las piezas mas importantes del sistema.

Su funcion es:

- escuchar acciones de botones y tablas
- leer datos ingresados por el usuario
- llamar a los servicios adecuados
- refrescar tablas y combos
- mostrar mensajes de exito o error

El controlador es el puente entre la interfaz grafica y la logica del negocio.

### `src/main/java/cl/hirata/view`

Aqui estan las ventanas y paneles de la interfaz Swing.

Clases:

- `MainFrame`: ventana principal.
- `DashboardPanel`: muestra resumen y alertas activas.
- `MileagePanel`: formulario para registrar kilometraje.
- `FleetPanel`: CRUD de conductores y camiones.
- `MaintenancePanel`: CRUD de mantenimientos.

La vista se encarga de:

- mostrar formularios
- mostrar tablas
- mostrar botones
- mostrar informacion al usuario

La vista no deberia decidir reglas del negocio ni escribir SQL.

### `src/main/java/cl/hirata/view/model`

Aqui estan los modelos de tabla de Swing.

Clases:

- `DriverTableModel`
- `TruckTableModel`
- `MaintenanceTableModel`
- `AlertTableModel`

Su funcion es adaptar listas de objetos Java para que puedan mostrarse dentro de tablas `JTable`.

Sin estas clases, la vista tendria mas codigo mezclado y menos ordenado.

### `src/test/java/cl/hirata/service`

Aqui estan las pruebas unitarias.

Clases:

- `AlertServiceTest`
- `MileageServiceTest`

Su funcion es comprobar que la logica mas importante funcione de manera aislada.

Por ejemplo:

- que la alerta se active al llegar a 5.000 km
- que el kilometraje del camion se actualice correctamente
- que no se permitan kilometrajes invalidos

## 4. Explicacion de las partes mas importantes del codigo

### `Main.java`

Esta clase crea toda la estructura del sistema.
Primero carga configuracion y conexion, luego crea DAOs, servicios, la vista principal y finalmente el controlador.

Se puede pensar como el ensamblador principal del proyecto.

### `ApplicationController.java`

Este archivo coordina la aplicacion.

Cuando haces clic en un boton:

- captura el evento
- valida los datos
- llama al servicio correspondiente
- actualiza la interfaz

Por ejemplo, al registrar kilometraje:

1. toma el camion seleccionado
2. toma la fecha y los kilometros
3. llama a `MileageService`
4. refresca las tablas y alertas
5. muestra un mensaje al usuario

### `MileageService.java`

Esta clase implementa la logica de RF-01.

Su trabajo es:

- validar que los kilometros sean mayores que cero
- buscar el camion
- sumar el kilometraje recorrido al kilometraje actual del camion
- actualizar el camion
- guardar un registro en `mileage_logs`

Aqui se concentra la regla mas importante del registro de kilometraje.

### `FleetService.java`

Esta clase ayuda con RF-02.

Se encarga de:

- guardar conductores
- guardar camiones
- validar campos obligatorios
- eliminar registros

### `AlertService.java`

Esta clase implementa RF-03.

Su logica principal es:

- buscar el ultimo mantenimiento del camion
- calcular cuantos kilometros han pasado desde esa mantencion
- comparar con el umbral de 5.000 km
- generar un objeto `TruckAlert`

Si no existe mantenimiento previo, toma como base 0 km.

### `MaintenanceService.java`

Esta clase implementa RF-04.

Se encarga de:

- guardar mantenimientos
- listar mantenimientos
- eliminar mantenimientos
- validar fecha, tipo y kilometraje

### DAOs JDBC

Cada clase JDBC tiene consultas SQL reales.

Ejemplo:

- `JdbcTruckDao` trabaja con la tabla `trucks`
- `JdbcDriverDao` trabaja con la tabla `drivers`
- `JdbcMileageLogDao` trabaja con la tabla `mileage_logs`
- `JdbcMaintenanceDao` trabaja con la tabla `maintenance_records`

Si mañana cambiaras la forma de acceder a datos, en teoria podrias cambiar estas clases sin tocar la vista ni los servicios.

## 5. Como funciona cada requerimiento funcional

### RF-01 Registrar kilometraje

1. El usuario entra a la pestana de kilometraje.
2. Selecciona un camion.
3. Ingresa fecha, kilometros y notas.
4. Pulsa el boton para registrar.
5. El controlador llama a `MileageService`.
6. Se actualiza el kilometraje total del camion.
7. Se guarda el detalle en `mileage_logs`.
8. Se recalculan las alertas.

### RF-02 Gestionar camiones y conductores

1. El usuario entra a la pestana de camiones y conductores.
2. Puede crear, editar o eliminar conductores.
3. Puede crear, editar o eliminar camiones.
4. Puede asignar un conductor a un camion.
5. Los cambios se guardan en MySQL.

### RF-03 Sistema de alertas

1. El sistema revisa cada camion.
2. Busca el ultimo mantenimiento de ese camion.
3. Calcula los kilometros recorridos desde esa fecha o desde ese kilometraje.
4. Si llega o supera 5.000 km, genera una alerta.
5. El dashboard muestra esa alerta visualmente.

### RF-04 Gestion de mantenimiento

1. El usuario entra a la pestana de mantenimientos.
2. Selecciona un camion.
3. Ingresa fecha, tipo, kilometraje y observaciones.
4. Guarda el registro.
5. El sistema usa ese dato como referencia para futuras alertas.

### RF-05 Pruebas unitarias

Se implementaron pruebas para verificar la logica mas importante sin depender de la interfaz grafica ni de MySQL en tiempo real.

Esto es util porque:

- ayuda a detectar errores temprano
- da confianza al modificar codigo
- demuestra que la logica principal fue validada

## 6. Relacion entre MVC y DAO en este proyecto

MVC:

- Modelo: entidades como `Truck`, `Driver` y `MaintenanceRecord`
- Vista: paneles Swing como `MileagePanel` o `FleetPanel`
- Controlador: `ApplicationController`

DAO:

- capa dedicada exclusivamente al acceso a datos
- separa el SQL del resto del programa

Ambos patrones juntos hacen que el proyecto sea mas ordenado, mas mantenible y mas facil de explicar.

## 7. Resumen final

Este proyecto esta bien separado por responsabilidades.

- La vista muestra datos.
- El controlador coordina acciones.
- Los servicios aplican reglas de negocio.
- Los DAO hablan con MySQL.
- Los modelos representan la informacion.
- Las pruebas validan la logica clave.

Gracias a esa estructura, el sistema puede crecer con mas facilidad.

Por ejemplo, en el futuro podrias agregar:

- login de usuarios
- historial por viaje
- reportes en PDF
- busqueda por fecha
- filtros avanzados
- exportacion de datos

## 8. Conclusion sencilla para explicar en clase

Si necesitas explicarlo de forma corta, puedes decir esto:

"El proyecto Hirata es una aplicacion de escritorio en Java que permite registrar kilometraje, administrar camiones y conductores, controlar mantenimientos y generar alertas preventivas. Se organizo con MVC para separar interfaz, logica y control, y con DAO para separar el acceso a MySQL. Asi el codigo queda mas limpio, reutilizable y facil de mantener."
