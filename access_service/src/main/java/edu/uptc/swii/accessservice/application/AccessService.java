package edu.uptc.swii.accessservice.application;

import edu.uptc.swii.accessservice.domain.Access;
import edu.uptc.swii.accessservice.dto.AccessDTO;
import edu.uptc.swii.accessservice.infrastructure.AccessRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccessService {
    private final AccessRepository accessRepository;
    private final RestTemplate restTemplate;

    @Value("${employee.service.url}")
    private String employeeServiceUrl;

    public AccessService(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
        this.restTemplate = new RestTemplate();
    }

    private boolean employeeExists(String document) {
        String url = employeeServiceUrl + "/findbydocument/" + document;
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);
            return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
        } catch (HttpClientErrorException.NotFound e) {
            // El empleado no existe (404)
            return false;
        } catch (HttpClientErrorException.Forbidden e) {
            // Bloqueo por seguridad, muestra mensaje especial
            throw new IllegalStateException("EmployeeService responde 403: no tienes permisos para consultar el empleado. Endpoint protegido.");
        } catch (HttpClientErrorException.Unauthorized e) {
            // Error por autenticación (401)
            throw new IllegalStateException("EmployeeService responde 401: no autorizado. Falta el token.");
        } catch (Exception e) {
            // Otros errores de red/protocolo
            throw new IllegalStateException("Error al consultar EmployeeService: " + e.getMessage());
        }
    }

    // Registrar ingreso (checkin)
    public Access registerAccessCheckin(AccessDTO dto) {
        // Validación adicional: El empleado debe existir
        if (!employeeExists(dto.getEmployeeDocument())) {
            throw new IllegalArgumentException("El empleado no existe");
        }
        // Verifica si el último acceso del empleado fue entrada sin salida
        List<Access> logs = accessRepository.findTopByEmployeeDocumentOrderByAccessTimeDesc(dto.getEmployeeDocument());
        if (!logs.isEmpty() && "ENTRADA".equals(logs.get(0).getAccessType())) {
            throw new IllegalArgumentException("Empleado ya ha ingresado y no ha registrado salida.");
        }
        Access access = new Access();
        access.setEmployeeDocument(dto.getEmployeeDocument());
        access.setAccessTime(dto.getAccessTime());
        access.setAccessType("ENTRADA");
        access.setAuthorized(true);
        return accessRepository.save(access);
    }

    // Registrar salida (checkout)
    public Access registerAccessCheckout(AccessDTO dto) {
        if (!employeeExists(dto.getEmployeeDocument())) {
            throw new IllegalArgumentException("El empleado no existe");
        }
        List<Access> logs = accessRepository.findTopByEmployeeDocumentOrderByAccessTimeDesc(dto.getEmployeeDocument());
        if (logs.isEmpty() || !"ENTRADA".equals(logs.get(0).getAccessType())) {
            throw new IllegalArgumentException("Empleado no ha registrado ingreso previo.");
        }
        if ("SALIDA".equals(logs.get(0).getAccessType())) {
            throw new IllegalArgumentException("Empleado ya registró salida.");
        }
        Access access = new Access();
        access.setEmployeeDocument(dto.getEmployeeDocument());
        access.setAccessTime(dto.getAccessTime());
        access.setAccessType("SALIDA");
        access.setAuthorized(true);
        return accessRepository.save(access);
    }

    // Obtener todos los accesos en una fecha (YYYY-MM-DD)
    public List<Access> findAllByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return accessRepository.findByAccessTimeBetween(startOfDay, endOfDay);
    }

    // Buscar por empleado y rango de fechas
    public List<Access> findByEmployeeAndDates(String document, LocalDate start, LocalDate end) {
        LocalDateTime from = start.atStartOfDay();
        LocalDateTime to = end.atTime(23, 59, 59);
        return accessRepository.findByEmployeeDocumentAndAccessTimeBetween(document, from, to);
    }
}
