# Requerimientos del caso

## Caso inventado

Sistema interno de Recursos Humanos para una empresa mediana que necesita centralizar la informacion de sus colaboradores, la asistencia diaria y las nominas mensuales.

## Problema que resuelve

La empresa hoy maneja empleados, asistencia y pagos en planillas separadas. Eso provoca duplicidad de datos, errores manuales y dificultad para escalar cuando otras areas necesitan consumir la informacion.

## Requerimientos funcionales

### RF-01 Gestion de empleados

- Registrar un empleado con rut, nombre, cargo, departamento, email y fecha de ingreso.
- Consultar un empleado por id.
- Listar empleados por estado o departamento.
- Actualizar los datos de un empleado.
- Desactivar logicamente un empleado.

### RF-02 Control de asistencia

- Registrar una asistencia diaria por empleado.
- Guardar fecha, hora de entrada, hora de salida, estado y observacion.
- Listar asistencias de todos los empleados o por empleado.
- Editar un registro de asistencia.
- Evitar duplicar asistencia para el mismo empleado en la misma fecha.

### RF-03 Gestion de nominas

- Crear una nomina mensual por empleado.
- Guardar sueldo base, bono, descuento y sueldo liquido calculado.
- Consultar nominas por id o por empleado.
- Marcar una nomina como pagada.
- Evitar duplicar nominas del mismo empleado en el mismo periodo.

## Requerimientos no funcionales

- La solucion debe estar dividida en al menos 3 microservicios.
- Cada servicio debe tener su propia base de datos.
- Todos los servicios deben registrarse en Eureka Server.
- El acceso externo debe pasar por Spring Cloud Gateway.
- La configuracion debe estar externalizada en `application.yml`.
- Las APIs deben validar datos de entrada y responder errores HTTP coherentes.
- El repositorio debe mantener historial real de commits.

## Dominios separados por responsabilidad

- `empleado-service`: maestro de personal.
- `asistencia-service`: control operativo diario.
- `nomina-service`: liquidaciones y estado de pago.
