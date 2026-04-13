# Presentacion del Proyecto Hirata

## 1. Que es este proyecto

Este proyecto es una aplicacion de escritorio hecha en Java para la empresa de transportes Hirata.

Su objetivo es ayudar a controlar la flota de camiones mediante:

- registro de kilometraje
- administracion de camiones y conductores
- control de mantenimientos
- alertas preventivas

## 2. Que problema resuelve

La empresa tenia problemas porque no llevaba un control automatico del kilometraje de sus camiones.
Eso hacia mas dificil saber cuando un vehiculo necesitaba mantenimiento preventivo.

Como consecuencia, podian ocurrir:

- fallas mecanicas inesperadas
- atrasos en las entregas
- mayores costos de reparacion

## 3. Cual es la solucion

La solucion fue desarrollar un prototipo de escritorio que permite:

1. registrar los kilometros recorridos por cada camion
2. guardar informacion de conductores y camiones
3. registrar mantenimientos realizados
4. generar alertas cuando un camion supera los 5.000 kilometros desde su ultima mantencion

## 4. Tecnologias usadas

Las principales tecnologias del proyecto son:

- Java
- Swing
- MySQL
- JUnit

## 5. Como se organiza el sistema

El proyecto usa dos patrones importantes:

- MVC
- DAO

### MVC

MVC significa:

- Modelo: representa los datos del sistema
- Vista: muestra la interfaz grafica
- Controlador: conecta la vista con la logica

### DAO

DAO sirve para separar las consultas SQL del resto del programa.
Gracias a eso, el codigo queda mas ordenado y facil de mantener.

## 6. Requerimientos funcionales implementados

### RF-01 Registrar kilometraje

Permite que el usuario ingrese los kilometros recorridos por un camion al terminar un viaje.

### RF-02 Gestionar camiones y conductores

Permite crear, ver, editar y eliminar camiones y conductores.

### RF-03 Sistema de alertas

Cuando un camion llega o supera los 5.000 km desde su ultimo mantenimiento, el sistema muestra una alerta.

### RF-04 Gestion de mantenimiento

Permite registrar y administrar los mantenimientos realizados a cada camion.

### RF-05 Pruebas unitarias

Se realizaron pruebas para verificar que la logica principal del sistema funcione correctamente.

## 7. Funcionamiento general

El flujo del sistema es el siguiente:

1. el usuario interactua con la interfaz
2. el controlador recibe la accion
3. el servicio aplica la logica del negocio
4. el DAO accede a MySQL
5. la informacion vuelve a la interfaz

## 8. Partes principales del proyecto

### Main

Inicia la aplicacion y conecta todas las capas del sistema.

### Vistas

Son las pantallas que ve el usuario:

- dashboard
- kilometraje
- camiones y conductores
- mantenimientos

### Controlador

Recibe acciones de botones y tablas, y decide que hacer en cada caso.

### Servicios

Aplican reglas importantes, como sumar kilometraje o calcular alertas.

### DAO

Se encargan de ejecutar consultas en la base de datos.

## 9. Ventajas del proyecto

Este sistema aporta varias mejoras:

- ordena la informacion
- automatiza el control de kilometraje
- reduce el riesgo de olvidos de mantenimiento
- facilita la gestion de la flota
- mejora la organizacion del trabajo

## 10. Conclusion para exponer

Este proyecto propone una solucion simple pero util para una empresa de transporte.
Permite registrar informacion importante de la flota y generar alertas preventivas para mejorar el mantenimiento de los camiones.

En resumen, ayuda a organizar mejor la operacion y a prevenir problemas mecanicos antes de que ocurran.
