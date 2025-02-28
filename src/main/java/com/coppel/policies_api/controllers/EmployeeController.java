// Ubicación: src/main/java/com/tuempresa/controlador/EmpleadoController.java

package com.coppel.policies_api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.coppel.policies_api.dto.EmployeeUpdateDTO;
import com.coppel.policies_api.models.Employee;
import com.coppel.policies_api.services.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Endpoint para registrar un empleado
    @PostMapping("/create")
    public ResponseEntity<Employee> setEmployee(@Valid @RequestBody Employee employee) {
        Employee newEmployee = employeeService.setEmployee(employee);
        return ResponseEntity.ok(newEmployee);
    }

    // Endpoint para obtener todos los empleados
    @GetMapping("/list")
    public ResponseEntity<List<Employee>> listEmployees() {
        List<Employee> employees = employeeService.listEmployees();
        return ResponseEntity.ok(employees);
    }

    // Endpoint para obtener un empleado por username
    @GetMapping("/{username}")
    public ResponseEntity<Optional<Employee>> obtenerEmpleadoPorUsername(@PathVariable String username) {
        Optional<Employee> employee = employeeService.getByUsername(username);
        return ResponseEntity.ok(employee);
    }

    // Endpoint para actualizar empleado
    @PatchMapping("/update/{username}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String username,
            @Valid @RequestBody EmployeeUpdateDTO updates) {
        Employee updatedEmployee = employeeService.updateEmployee(username, updates);
        return ResponseEntity.ok(updatedEmployee);
    }

    // Endpoint para eliminar un empleado
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable String username) {
        employeeService.deleteEmployee(username);
        return ResponseEntity.ok("Empleado eliminado exitosamente.");
    }
}
