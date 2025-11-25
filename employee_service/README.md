# Employee Service – Pedestrian Access System

Microservicio para la gestión de empleados

Este microservicio hace parte del sistema distribuido Pedestrian Access System, y se encarga de administrar:

- Creación y actualización de empleados

- Consulta de información

- Habilitación / deshabilitación

- Gestión interna por documento

Todo está protegido mediante PASETO, y se expone al público a través del API Gateway Traefik.

El microservicio se expone bajo la ruta: `http://localhost/employee`

## Endpoints del Employee Service

Todos los endpoints (excepto findbydocument) requieren token que viene directamente desde login

1. Create Employee

Crea un empleado nuevo.

```bash
curl -X POST http://localhost/employee/createemployee \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer v2.local.JP0i6BECid8DH8zTAvMyzDsGAdzP3DYt4wcCYDg4bEbZzV-2jsu4cLqKNhVToJy_1Ytq2TisLelJhDz61-f4YWKmJ39iE-Z7hY5-q-bj98hXppQ5Sy_GGD5UFWd-CSt-coXogn2T" \
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
