package edu.uptc.swii.employeeservice.application;

import edu.uptc.swii.employeeservice.domain.Employee;
import edu.uptc.swii.employeeservice.dto.EmployeeDTO;
import edu.uptc.swii.employeeservice.infrastructure.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee registerEmployee(EmployeeDTO dto) {
        if (employeeRepository.existsById(dto.getDocument())) {
            throw new IllegalArgumentException("Ya existe un empleado con el documento: " + dto.getDocument());
        }
        Employee employee = new Employee();
        employee.setDocument(dto.getDocument());
        employee.setFirstname(dto.getFirstname());
        employee.setLastname(dto.getLastname());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setStatus(true);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(EmployeeDTO dto) {
        Optional<Employee> maybe = employeeRepository.findByDocument(dto.getDocument());
        if (maybe.isEmpty())
            return null;
        Employee employee = maybe.get();
        employee.setFirstname(dto.getFirstname());
        employee.setLastname(dto.getLastname());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setStatus(dto.getStatus());
        return employeeRepository.save(employee);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee disableEmployee(String document) {
        Optional<Employee> maybe = employeeRepository.findByDocument(document);
        if (maybe.isEmpty())
            return null;
        Employee employee = maybe.get();
        employee.setStatus(false);
        return employeeRepository.save(employee);
    }

    public Optional<Employee> findByDocument(String document) {
        return employeeRepository.findByDocument(document);
    }
}
