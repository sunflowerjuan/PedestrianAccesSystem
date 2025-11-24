package edu.uptc.swii.accessservice.domain;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_log")
@Data
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeDocument;         // Documento del empleado registrado desde Employee
    private LocalDateTime accessTime;        // Fecha y hora acceso
    private String accessType;               // Tipo: "ENTRADA" / "SALIDA" / otro
    private Boolean authorized;              // Si el acceso fue autorizado
}
