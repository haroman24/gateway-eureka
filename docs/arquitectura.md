# Arquitectura

## Componentes

### Eureka Server

- Mantiene el registro vivo de microservicios.
- Permite que los servicios se descubran sin depender de IPs fijas.
- Corre en `http://localhost:8761`.

### API Gateway

- Es el unico punto de entrada para clientes externos.
- Consulta a Eureka para resolver el nombre logico de cada servicio.
- Expone rutas por prefijo:
  - `/api/empleados/**`
  - `/api/asistencias/**`
  - `/api/nominas/**`

### empleado-service

- Gestiona informacion maestra de colaboradores.
- Implementa capas `controller`, `service`, `repository`, `dto`, `entity`, `exception`.
- Usa H2 local como base propia.

### asistencia-service

- Gestiona marcaciones diarias y observaciones de asistencia.
- Valida que no exista mas de una asistencia por empleado y fecha.
- Usa H2 local como base propia.

### nomina-service

- Gestiona nominas mensuales y calcula sueldo liquido.
- Cambia el estado de una nomina a `PAGADA`.
- Usa H2 local como base propia.

## Flujo de comunicacion

1. El cliente envia una peticion al `api-gateway`.
2. El gateway consulta el registro de Eureka.
3. Eureka resuelve el nombre logico del servicio.
4. El gateway enruta la solicitud al microservicio correspondiente.
5. Cada microservicio procesa la operacion y responde.

## Diagrama textual

```text
Cliente / Postman
       |
       v
API Gateway (8080)
       |
       +--> lb://empleado-service   -> empleado-service (8081)   -> H2 db_empleados
       +--> lb://asistencia-service -> asistencia-service (8082) -> H2 db_asistencias
       +--> lb://nomina-service     -> nomina-service (8083)     -> H2 db_nominas

Todos los servicios:
   -> se registran en Eureka Server (8761)
```

## Conceptos previos aplicados

- Arquitectura en capas
- DTOs para entrada y salida
- Persistencia con Spring Data JPA
- Configuracion externalizada en `application.yml`
- Validacion con Jakarta Validation
- Manejo global de excepciones
- Registro y descubrimiento con Eureka
- Rutas por nombre logico usando `lb://`
