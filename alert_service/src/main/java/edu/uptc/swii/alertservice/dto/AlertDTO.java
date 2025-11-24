package edu.uptc.swii.alertservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertDTO {
    private LocalDateTime timestamp;
    private String description;
    private String code;
    private String employeeDocument;
}
