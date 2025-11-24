package edu.uptc.swii.accessservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AccessDTO {
    private String employeeDocument;
    private LocalDateTime accessTime;
    private String accessType;
    private Boolean authorized;
}
