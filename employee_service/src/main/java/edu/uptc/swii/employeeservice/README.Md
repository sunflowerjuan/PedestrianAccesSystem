# Employee Service – Pedestrian Access System

Microservicio para la gestión de empleados

Este microservicio hace parte del sistema distribuido Pedestrian Access System, y se encarga de administrar:

- Creación y actualización de empleados

- Consulta de información

- Habilitación / deshabilitación

- Gestión interna por documento

Todo está protegido mediante PASETO, y se expone al público a través del API Gateway Traefik.

El Employee Service está construido sobre:

Spring Boot 4

Spring Web MVC

Spring Security

JPA + H2 en modo file (persistente en contenedor)

PASETO V2.local para autenticación segura

Traefik v3.5 como enrutador/API Gateway

Docker + Docker Compose

El microservicio se expone bajo la ruta: `http://localhost/employee`

## Endpoints del Employee Service

Todos los endpoints (excepto findbydocument) requieren token que viene directamente desde login

1. Create Employee

Crea un empleado nuevo.

```bash
curl -X POST http://localhost/employee/createemployee \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer v2.local.dru9fe84_MZXRQRwqgnkYJDoe8hhrvVZ7I4Oqo44Fa9oOW8R2NAHsrwXiPnc2H8pSbllAsjQ_cVst_04ZZNsMeBFXv2cvhai1rVIVYKIRvVu4rQ2-nkzO-6aIvlUm3Bh25sgvrpj" \
  -d '{
        "document": "123456789",
        "firstname": "María",
        "lastname": "Espinosa",
        "email": "majo@uptc.edu.co",
        "phone": "3200000000"
      }'
```

2. Update Employee

Actualiza un empleado.

```bash
curl -X PUT http://localhost/employee/updateemployee \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
        "document": "123456789",
        "firstname": "María José",
        "lastname": "Espinosa R",
        "email": "nuevo@correo.com",
        "phone": "3108887777",
        "status": true
      }'
```

3. Disable Employee

Marca un empleado como inactivo.

```bash
curl -X PUT "http://localhost/employee/disableemployee?document=123456789" \
  -H "Authorization: Bearer <TOKEN>"
```

4. Find All Employees

```bash
curl -X GET http://localhost/employee/findallemployees \
  -H "Authorization: Bearer <TOKEN>"
```

5. Find Employee by Document

```bash
curl -X GET http://localhost/employee/findbydocument/123456789
```
