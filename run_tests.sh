#!/bin/bash

BASE_URL="http://localhost"
ADMIN_USER="sunflowers_juan"
ADMIN_PASS="yingonbell"

echo "===================================="
echo " 1) LOGIN – SOLICITAR OTP "
echo "===================================="

LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$ADMIN_USER\", \"password\":\"$ADMIN_PASS\"}")

echo "Respuesta login:"
echo "$LOGIN_RESPONSE"

OTP_REQUIRED=$(echo "$LOGIN_RESPONSE" | jq -r '.otpRequired')

if [[ "$OTP_REQUIRED" != "true" ]]; then
  echo "❌ ERROR: OTP no fue requerido."
  exit 1
fi

USERNAME=$(echo "$LOGIN_RESPONSE" | jq -r '.username')

echo "===================================="
echo " 2) INGRESAR OTP MANUALMENTE "
echo "===================================="
echo "⚠ Tu OTP está en la consola del microservicio login-service."
read -p "Ingresa el OTP que viste en consola: " OTP_CODE

OTP_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/verify-otp" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\", \"otpCode\":\"$OTP_CODE\"}")

echo "Respuesta OTP:"
echo "$OTP_RESPONSE"

TOKEN=$(echo "$OTP_RESPONSE" | jq -r '.token')

echo "===================================="
echo " TOKEN GENERADO "
echo "===================================="
echo "$TOKEN"


echo "===================================="
echo " 3) CREAR EMPLEADO VALIDO (SAGA)"
echo "===================================="

EMP_DOC="12345"

CREATE_EMP=$(curl -s -X POST "$BASE_URL/employee/createemployee" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"document\":\"$EMP_DOC\",
    \"firstname\":\"Majo\",
    \"lastname\":\"Espinosa\",
    \"email\":\"majo@uptc.edu\",
    \"phone\":\"3130000000\",
    \"status\":true
  }")

echo "$CREATE_EMP"


echo "===================================="
echo " 4) CHECK-IN VALIDO (SAGA)"
echo "===================================="

CHECKIN=$(curl -s -X POST "$BASE_URL/access/usercheckin" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"employeeDocument\":\"$EMP_DOC\"}")

echo "$CHECKIN"


echo "===================================="
echo " 5) CHECK-OUT VALIDO (SAGA)"
echo "===================================="

CHECKOUT=$(curl -s -X POST "$BASE_URL/access/usercheckout" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"employeeDocument\":\"$EMP_DOC\"}")

echo "$CHECKOUT"


echo "===================================="
echo " 6) CHECK-IN DE EMPLEADO INEXISTENTE"
echo "===================================="

FAKE_DOC="999999"

INVALID=$(curl -s -X POST "$BASE_URL/access/usercheckin" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"employeeDocument\":\"$FAKE_DOC\"}")

echo "$INVALID"


echo "===================================="
echo " 7) CONSULTAR ALERTAS POR CODIGO"
echo "===================================="

ALERTS=$(curl -s "$BASE_URL/alert/getalertsbycode?code=EMPLOYEENOTEXISTS")

echo "$ALERTS"


echo "===================================="
echo " 8) CONSULTAR ALERTAS POR DOCUMENTO"
echo "===================================="

ALERT_DOC=$(curl -s "$BASE_URL/alert/getalertsbydocument?document=$FAKE_DOC")

echo "$ALERT_DOC"


echo "===================================="
echo " 9) PRUEBA: ACCESO SIN TOKEN"
echo "===================================="

NO_TOKEN=$(curl -s -X POST "$BASE_URL/employee/createemployee" \
  -H "Content-Type: application/json" \
  -d "{\"document\":\"0001\", \"firstname\":\"Hack\", \"lastname\":\"Test\"}")

echo "$NO_TOKEN"


echo "===================================="
echo " ✔ TODAS LAS PRUEBAS FINALIZADAS "
echo "===================================="
