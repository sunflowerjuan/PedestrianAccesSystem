curl -X POST http://localhost/alert/usrnotregistattempt \
 -H "Authorization: Bearer $TOKEN" \
 -H "Content-Type: application/json" \
 -d '{
"timestamp": "2025-02-20T12:00:00",
"description": "Intento manual",
"employeeDocument": ""
}'
