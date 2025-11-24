package edu.uptc.swii.accessservice.controller;

import edu.uptc.swii.accessservice.application.AccessService;
import edu.uptc.swii.accessservice.dto.AccessDTO;
import edu.uptc.swii.accessservice.domain.Access;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/access")
public class AccessController {
    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    // Registrar ingreso (check-in)
    @PostMapping("/usercheckin")
    public ResponseEntity<?> userCheckin(@RequestBody AccessDTO dto) {
        try {
            dto.setAccessType("ENTRADA");
            dto.setAccessTime(LocalDateTime.now());
            Access result = accessService.registerAccessCheckin(dto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(502).body(e.getMessage());
        }
    }

    // Registrar salida (check-out)
    @PostMapping("/usercheckout")
    public ResponseEntity<?> userCheckout(@RequestBody AccessDTO dto) {
        dto.setAccessType("SALIDA");
        dto.setAccessTime(LocalDateTime.now());
        Access result = accessService.registerAccessCheckout(dto);
        return ResponseEntity.ok(result);
    }

    // Consultar todos los empleados por fecha (YYYY-MM-DD)
    @GetMapping("/allemployeesbydate")
    public ResponseEntity<List<Access>> getAllEmployeesByDate(@RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        List<Access> logs = accessService.findAllByDate(targetDate);
        return ResponseEntity.ok(logs);
    }

    // Consultar accesos por empleado y rango de fechas (YYYY-MM-DD)
    @GetMapping("/employeebydates")
    public ResponseEntity<List<Access>> getEmployeeByDates(@RequestParam String document,
            @RequestParam String start,
            @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Access> logs = accessService.findByEmployeeAndDates(document, startDate, endDate);
        return ResponseEntity.ok(logs);
    }
}
