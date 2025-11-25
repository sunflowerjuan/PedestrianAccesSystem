# Access Service — Microservicio de Registro de Ingresos y Salidas

El Access Service es un microservicio del sistema Pedestrian Access System, encargado de registrar entradas y salidas de empleados, consultar accesos por fecha y validar reglas básicas de control.

Este servicio consume el microservicio Employee Service para verificar que un empleado exista antes de registrar un acceso.

## Características principales

- Registrar ingreso (check-in)

- Registrar salida (check-out)

- Consultar todas las actividades por fecha

- Consultar accesos de un empleado por rango de fechas

- Persistencia en H2 Database embebida

- Preparado para despliegue con Docker + Traefik API Gateway

- Sin autenticación obligatoria por ahora (solo validación contra Employee Service)

## ENDPOINTS

El microservicio se expone bajo la ruta: `http://localhost/access`

1. Registrar ingreso (Check-in)

Registra un ingreso del empleado.

```bash
curl -X POST http://localhost/access/usercheckin \
  -H "Content-Type: application/json" \
  -d '{
        "employeeDocument": "123456789"
      }'
```

2. Registrar salida (Check-out)

Registra un ingreso del empleado.

```bash
curl -X POST http://localhost/access/usercheckout \
  -H "Content-Type: application/json" \
  -d '{
        "employeeDocument": "123456789"
      }'
```

3. Consultar accesos por fecha

```bash
curl "http://localhost/access/allemployeesbydate?date=2025-01-01"
```

4. Consultar accesos por fecha

```bash
curl "http://localhost/access/employeebydates?document=123456789&start=2025-01-01&end=2025-02-01"
```
