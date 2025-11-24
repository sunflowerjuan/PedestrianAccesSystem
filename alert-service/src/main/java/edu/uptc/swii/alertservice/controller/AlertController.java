package edu.uptc.swii.alertservice.controller;

import edu.uptc.swii.alertservice.application.AlertService;
import edu.uptc.swii.alertservice.dto.AlertDTO;
import edu.uptc.swii.alertservice.domain.Alert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert")
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/usrnotregistattempt")
    public ResponseEntity<Alert> usrNotRegistAttempt(@RequestBody AlertDTO dto) {
        dto.setCode("LOGINUSRNOTREGISTERED");
        return ResponseEntity.ok(alertService.registerAlert(dto));
    }

    @PostMapping("/usrexceedattempts")
    public ResponseEntity<Alert> usrExceedAttempts(@RequestBody AlertDTO dto) {
        dto.setCode("LOGINUSRATTEMPSEXCEEDED");
        return ResponseEntity.ok(alertService.registerAlert(dto));
    }

    @PostMapping("/employeealreadyentered")
    public ResponseEntity<Alert> employeeAlreadyEntered(@RequestBody AlertDTO dto) {
        dto.setCode("EMPLOYEEALREADYENTERED");
        return ResponseEntity.ok(alertService.registerAlert(dto));
    }

    @PostMapping("/employeealreadyleft")
    public ResponseEntity<Alert> employeeAlreadyLeft(@RequestBody AlertDTO dto) {
        dto.setCode("EMPLOYEEALREADYLEFT");
        return ResponseEntity.ok(alertService.registerAlert(dto));
    }

    @GetMapping("/getalertsbydocument")
    public ResponseEntity<List<Alert>> getAlertsByDocument(@RequestParam String document) {
        return ResponseEntity.ok(alertService.getAlertsByDocument(document));
    }

    @GetMapping("/getalertsbycode")
    public ResponseEntity<List<Alert>> getAlertsByCode(@RequestParam String code) {
        return ResponseEntity.ok(alertService.getAlertsByCode(code));
    }
}
