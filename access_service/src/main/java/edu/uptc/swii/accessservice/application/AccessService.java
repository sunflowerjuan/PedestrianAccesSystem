package edu.uptc.swii.accessservice.application;

import edu.uptc.swii.accessservice.domain.Access;
import edu.uptc.swii.accessservice.dto.AccessDTO;
import edu.uptc.swii.accessservice.events.AlertEventPublisher;
import edu.uptc.swii.accessservice.infrastructure.AccessRepository;
import edu.uptc.swii.accessservice.config.AccessSagaPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccessService {

    private final AccessRepository accessRepository;
    private final AlertEventPublisher alertPublisher;
    private final AccessSagaPublisher sagaPublisher;

    public AccessService(
            AccessRepository accessRepository,
            AlertEventPublisher alertPublisher,
            AccessSagaPublisher sagaPublisher) {
        this.accessRepository = accessRepository;
        this.alertPublisher = alertPublisher;
        this.sagaPublisher = sagaPublisher;
    }

    /**
     * INICIO DE SAGA — Solicita al microservicio Employee validar si el empleado
     * existe.
     * No registra acceso directamente. La operación continúa cuando llegue el
     * evento
     * saga.access.employee_exists o saga.access.employee_not_exists.
     */
    public void registerAccessCheckin(AccessDTO dto) {
        sagaPublisher.requestEmployeeValidation(dto.getEmployeeDocument(), "ENTRADA");
    }

    /**
     * INICIO DE SAGA — Solicita validar empleado para checkout.
     */
    public void registerAccessCheckout(AccessDTO dto) {
        sagaPublisher.requestEmployeeValidation(dto.getEmployeeDocument(), "SALIDA");
    }

    /**
     * MÉTODO FINAL DE LA SAGA — Se ejecuta SOLO cuando EmployeeService publica
     * saga.access.employee_exists con actionType = ENTRADA.
     */
    public void finalizeCheckin(String document) {

        List<Access> logs = accessRepository.findTopByEmployeeDocumentOrderByAccessTimeDesc(document);

        // Si el último acceso fue ENTRADA → no puede entrar de nuevo
        if (!logs.isEmpty() && "ENTRADA".equals(logs.get(0).getAccessType())) {
            alertPublisher.alreadyEntered(document);
            return;
        }

        // Registrar ingreso
        Access access = new Access();
        access.setEmployeeDocument(document);
        access.setAccessTime(LocalDateTime.now());
        access.setAccessType("ENTRADA");
        access.setAuthorized(true);

        accessRepository.save(access);
    }

    /**
     * MÉTODO FINAL DE LA SAGA — Se ejecuta SOLO cuando EmployeeService publica
     * saga.access.employee_exists con actionType = SALIDA.
     */
    public void finalizeCheckout(String document) {

        List<Access> logs = accessRepository.findTopByEmployeeDocumentOrderByAccessTimeDesc(document);

        // No hay entrada previa
        if (logs.isEmpty() || !"ENTRADA".equals(logs.get(0).getAccessType())) {
            alertPublisher.alreadyLeft(document);
            return;
        }

        // Registrar salida
        Access access = new Access();
        access.setEmployeeDocument(document);
        access.setAccessTime(LocalDateTime.now());
        access.setAccessType("SALIDA");
        access.setAuthorized(true);

        accessRepository.save(access);
    }

    // ===================== CONSULTAS ================================

    public List<Access> findAllByDate(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59, 59);
        return accessRepository.findByAccessTimeBetween(from, to);
    }

    public List<Access> findByEmployeeAndDates(String document, LocalDate start, LocalDate end) {
        LocalDateTime from = start.atStartOfDay();
        LocalDateTime to = end.atTime(23, 59, 59);
        return accessRepository.findByEmployeeDocumentAndAccessTimeBetween(document, from, to);
    }
}
