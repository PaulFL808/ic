# Hirata Desktop

Prototipo de aplicacion de escritorio para la empresa de transportes Hirata.

## Alcance implementado

- `RF-01`: registro de kilometraje por viaje.
- `RF-02`: CRUD de camiones y conductores.
- `RF-03`: alertas visuales cuando un camion supera los `5.000 km` desde su ultimo mantenimiento.
- `RF-04`: CRUD de mantenimientos preventivos.
- `RF-05`: pruebas unitarias JUnit para la logica de alertas y kilometraje.

## Arquitectura

- `MVC`: vistas Swing, controlador central y servicios.
- `DAO`: interfaces para acceso a datos y implementaciones JDBC para MySQL.
- `MySQL`: esquema incluido en [database/schema.sql](/C:/Users/fngg7/Desktop/ic/database/schema.sql).

## Configuracion

1. Crear una base de datos MySQL, por ejemplo `hirata_db`.
2. Ejecutar el script [database/schema.sql](/C:/Users/fngg7/Desktop/ic/database/schema.sql).
3. Ajustar [src/main/resources/database.properties](/C:/Users/fngg7/Desktop/ic/src/main/resources/database.properties) con tus credenciales.

## Ejecucion

Con Maven instalado:

```bash
mvn clean test
mvn exec:java
```

En este entorno no habia Maven instalado, por lo que la compilacion automatica completa queda preparada pero no ejecutada.
