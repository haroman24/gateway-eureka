# API

Las peticiones del cliente deben enviarse al Gateway en `http://localhost:8080`.

## empleado-service

### POST `/api/empleados`

```json
{
  "rut": "12.345.678-9",
  "nombre": "Ana Torres",
  "cargo": "Analista",
  "departamento": "RRHH",
  "email": "ana@empresa.cl",
  "fechaIngreso": "2026-03-01"
}
```

Respuesta:

```json
{
  "id": 1,
  "rut": "12.345.678-9",
  "nombre": "Ana Torres",
  "cargo": "Analista",
  "departamento": "RRHH",
  "email": "ana@empresa.cl",
  "fechaIngreso": "2026-03-01",
  "estado": "ACTIVO"
}
```

### GET `/api/empleados`

Filtros opcionales:

- `estado=ACTIVO`
- `departamento=RRHH`

### PUT `/api/empleados/{id}`

Actualiza los datos de un empleado.

### DELETE `/api/empleados/{id}`

Desactiva logicamente el empleado.

## asistencia-service

### POST `/api/asistencias`

```json
{
  "empleadoId": 1,
  "fecha": "2026-06-19",
  "horaEntrada": "08:30:00",
  "horaSalida": "17:45:00",
  "estado": "PRESENTE",
  "observacion": "Turno regular"
}
```

Respuesta:

```json
{
  "id": 1,
  "empleadoId": 1,
  "fecha": "2026-06-19",
  "horaEntrada": "08:30:00",
  "horaSalida": "17:45:00",
  "estado": "PRESENTE",
  "observacion": "Turno regular"
}
```

### GET `/api/asistencias`

Filtro opcional:

- `empleadoId=1`

### PUT `/api/asistencias/{id}`

Actualiza una marcacion existente.

## nomina-service

### POST `/api/nominas`

```json
{
  "empleadoId": 1,
  "periodo": "2026-06",
  "sueldoBase": 1200000,
  "bono": 150000,
  "descuento": 95000
}
```

Respuesta:

```json
{
  "id": 1,
  "empleadoId": 1,
  "periodo": "2026-06",
  "sueldoBase": 1200000,
  "bono": 150000,
  "descuento": 95000,
  "sueldoLiquido": 1255000,
  "estadoPago": "PENDIENTE",
  "fechaPago": null
}
```

### GET `/api/nominas`

Filtro opcional:

- `empleadoId=1`

### PUT `/api/nominas/{id}/pagar`

Marca la nomina como pagada y registra `fechaPago`.

## Errores esperados

Formato general:

```json
{
  "timestamp": "2026-06-19T21:10:00",
  "status": 400,
  "error": "Bad Request",
  "message": "campo: detalle del error",
  "path": "/api/empleados"
}
```
