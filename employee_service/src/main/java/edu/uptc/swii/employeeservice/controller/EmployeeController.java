package edu.uptc.swii.employeeservice.controller;

import edu.uptc.swii.employeeservice.application.EmployeeService;
import edu.uptc.swii.employeeservice.dto.EmployeeDTO;
import edu.uptc.swii.employeeservice.domain.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/createemployee")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO dto) {
        try {
            Employee employee = employeeService.registerEmployee(dto);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateemployee")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDTO dto) {
        Employee employee = employeeService.updateEmployee(dto);
        if (employee == null) {
            return ResponseEntity.badRequest().body("Empleado no encontrado");
        }
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/disableemployee")
    public ResponseEntity<?> disableEmployee(@RequestParam String document) {
        Employee employee = employeeService.disableEmployee(document);
        if (employee == null) {
            return ResponseEntity.badRequest().body("Empleado no encontrado");
        }
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/findallemployees")
    public ResponseEntity<List<Employee>> findAll() {
        List<Employee> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/findbydocument/{document}")
    public ResponseEntity<?> findByDocument(@PathVariable String document) {
        Optional<Employee> emp = employeeService.findByDocument(document);
        if (emp.isPresent())
            return ResponseEntity.ok(emp.get());
        else
            return ResponseEntity.status(404).body("Empleado no encontrado");
    }
}
