# Login Service – Pedestrian Access System

Este microservicio es responsable de la **autenticación y gestión de administradores** dentro del sistema de control de acceso peatonal. Implementa:

- Registro de usuarios administradores
- Validación de credenciales
- Autenticación multifactor (OTP)
- Generación y validación de tokens **PASETO v2.local**

El servicio está desarrollado en **Java 17 + Spring Boot**, usando **MongoDB** como base de datos.

---

## Arquitectura interna

El microservicio sigue principios de **Clean Architecture / Hexagonal**, organizando sus capas así:

- **domain/** → Entidad `AdminUser` + Puertos (`CreateAdminPort`, `FindAdminPort`)
- **application/** → Caso de uso `AdminUserService`
- **infrastructure/** → Repositorio `AdminUserRepository` que implementa los puertos
- **controller/** → Endpoints REST (`AuthController`, `AdminUserController`)
- **security/** → Filtro de autenticación + Servicio PASETO

---

## Endpoints principales

1. **Registrar administrador**

```bash
curl -X POST http://localhost/login/admins \
  -H "Content-Type: application/json" \
  -d '{
        "username": "sunflowers_juan",
        "password": "yingonbell",
        "email": "uribe.presidente@gmial.com"
      }'
```

2. **Obtener un administrador por username** _(Requiere token PASETO)_

```bash
curl -X GET http://localhost/login/admins/sunflowers_juan \
  -H "Authorization: Bearer v2.local.JuGGvNMqB8KL3doMD5jnTasROlXDuL2qdxwnc-BYSENMnkCBFNvwgjASyRAcW9k8DC2IcYZ4R7cNXAn5rGS-hmgO8El_em8NWlnHLkEBTBgVq92pVSqPpZSu9XBVQ-TzpwTBVshz"
```

3. **Login **

3.1. Paso 1: Usuario + Contraseña

```bash
curl -X POST http://localhost/login/auth/login \
  -H "Content-Type: application/json" \
  -d '{
        "username": "sunflowers_juan",
        "password": "yingonbell"
      }'
```

3.2. Paso 2: Paso 2: Verificar OTP

Primero revisa el OTP en los logs, y usalo aqui:

```bash
curl -X POST http://localhost/login/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{
        "username": "sunflowers_juan",
        "otpCode": "xxxx"
      }'
```
