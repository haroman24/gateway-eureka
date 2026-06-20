# RRHH Microservices

Aplicacion universitaria de **Recursos Humanos** construida con **Spring Cloud Gateway + Eureka Server + 3 microservicios**. El caso resuelve la administracion de empleados, control de asistencia y generacion de nominas desde un unico punto de entrada.

## Caso de negocio

Una empresa necesita una plataforma interna de RRHH para:

- registrar y mantener su personal;
- controlar asistencia diaria;
- emitir nominas mensuales y marcar pagos.

## Microservicios y responsabilidad

### `empleado-service`

Resuelve la gestion del personal de la empresa. Este microservicio se encarga de:

- registrar empleados con rut, nombre, cargo, departamento, email y fecha de ingreso;
- consultar empleados por id;
- listar empleados por estado o departamento;
- actualizar datos laborales y de contacto;
- desactivar logicamente empleados cuando dejan de estar vigentes.

### `asistencia-service`

Resuelve el control diario de asistencia. Este microservicio se encarga de:

- registrar asistencia por empleado y fecha;
- guardar hora de entrada, hora de salida, estado y observaciones;
- listar asistencias generales o filtradas por empleado;
- editar una marcacion existente;
- validar que no exista mas de una asistencia para el mismo empleado en la misma fecha.

### `nomina-service`

Resuelve la gestion de pagos y liquidaciones. Este microservicio se encarga de:

- crear nominas mensuales por empleado;
- registrar sueldo base, bono y descuento;
- calcular automaticamente el sueldo liquido;
- consultar nominas por id o por empleado;
- marcar una nomina como pagada y registrar la fecha de pago.

### `eureka-server`

Resuelve el descubrimiento de servicios. Este componente se encarga de:

- mantener el registro de microservicios activos;
- permitir que los servicios se registren al iniciar;
- evitar que el sistema dependa de IPs o puertos fijos.

### `api-gateway`

Resuelve el acceso unificado al sistema. Este componente se encarga de:

- recibir todas las peticiones del cliente en un solo punto de entrada;
- enrutar las solicitudes al microservicio correcto;
- consultar a Eureka para ubicar cada servicio por nombre logico;
- exponer las rutas `/api/empleados/**`, `/api/asistencias/**` y `/api/nominas/**`.

## Arquitectura

```text
Cliente/Postman
      |
      v
 API Gateway (:8080)
      |
      +--> empleado-service   (:8081)
      +--> asistencia-service (:8082)
      +--> nomina-service     (:8083)

Todos los servicios se registran en Eureka Server (:8761)
```

## Puertos

- `8761`: Eureka Server
- `8080`: API Gateway
- `8081`: empleado-service
- `8082`: asistencia-service
- `8083`: nomina-service

## Estructura del repositorio

```text
rrhh-microservices/
├── README.md
├── docs/
│   ├── requerimientos.md
│   ├── arquitectura.md
│   └── api.md
├── eureka-server/
├── api-gateway/
├── empleado-service/
├── asistencia-service/
└── nomina-service/
```

## Tecnologias

- Java 21
- Spring Boot 3.3.5
- Spring Cloud 2023.0.3
- Spring Data JPA
- H2 Database
- Maven Wrapper

## Como levantar el proyecto

Orden recomendado:

1. Eureka Server
2. Microservicios
3. API Gateway

Comandos:

```bash
./mvnw -pl eureka-server spring-boot:run
./mvnw -pl empleado-service spring-boot:run
./mvnw -pl asistencia-service spring-boot:run
./mvnw -pl nomina-service spring-boot:run
./mvnw -pl api-gateway spring-boot:run
```

## Endpoints principales via Gateway

- `POST /api/empleados`
- `GET /api/empleados`
- `POST /api/asistencias`
- `GET /api/asistencias`
- `POST /api/nominas`
- `PUT /api/nominas/{id}/pagar`

Ejemplo:

```bash
curl -X POST http://localhost:8080/api/empleados \
  -H "Content-Type: application/json" \
  -d '{
    "rut": "12.345.678-9",
    "nombre": "Ana Torres",
    "cargo": "Analista",
    "departamento": "RRHH",
    "email": "ana@empresa.cl",
    "fechaIngreso": "2026-03-01"
  }'
```

## Documentacion

- Requerimientos: [docs/requerimientos.md](/Users/hansroman/Solucion_gym/docs/requerimientos.md)
- Arquitectura: [docs/arquitectura.md](/Users/hansroman/Solucion_gym/docs/arquitectura.md)
- API: [docs/api.md](/Users/hansroman/Solucion_gym/docs/api.md)

## Probar

```bash
./mvnw test
```
