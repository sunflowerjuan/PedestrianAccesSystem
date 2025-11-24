package edu.uptc.swii.accessservice.infrastructure;

import edu.uptc.swii.accessservice.domain.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    List<Access> findByEmployeeDocument(String employeeDocument);

    // Encuentra últimos accesos ordenados por fecha descendente
    List<Access> findTopByEmployeeDocumentOrderByAccessTimeDesc(String employeeDocument);

    // Filtra accesos por rango de fecha
    List<Access> findByAccessTimeBetween(LocalDateTime start, LocalDateTime end);

    // Filtra accesos por empleado y por rango
    List<Access> findByEmployeeDocumentAndAccessTimeBetween(String document, LocalDateTime start, LocalDateTime end);
}
