package edu.uptc.swii.alertservice.application;

import edu.uptc.swii.alertservice.domain.Alert;
import edu.uptc.swii.alertservice.dto.AlertDTO;
import edu.uptc.swii.alertservice.infrastructure.AlertRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlertService {
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Alert registerAlert(AlertDTO dto) {
        Alert alert = new Alert();
        alert.setTimestamp(dto.getTimestamp());
        alert.setDescription(dto.getDescription());
        alert.setCode(dto.getCode());
        alert.setEmployeeDocument(dto.getEmployeeDocument());
        return alertRepository.save(alert);
    }

    public List<Alert> getAlertsByDocument(String document) {
        return alertRepository.findByEmployeeDocument(document);
    }

    public List<Alert> getAlertsByCode(String code) {
        return alertRepository.findByCode(code);
    }
}
