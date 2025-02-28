package com.coppel.policies_api.services;

import com.coppel.policies_api.dto.EmployeeUpdateDTO;
import com.coppel.policies_api.models.Employee;
import com.coppel.policies_api.repositories.EmployeeRepository;
import com.coppel.policies_api.utils.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.coppel.policies_api.exceptions.PersonalizedExceptions.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Date;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Random RANDOM = new SecureRandom();
    private static final Log log = new Log();

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee setEmployee(Employee employee) {
        // Generar nombre de usuario aleatorio de 8 dígitos numéricos
        String randomUsername;
        do {
            // Generar nombre de usuario aleatorio de 8 dígitos numéricos
            randomUsername = generateRandomUsername();
        } while (employeeRepository.existsByUsername(randomUsername));

        employee.setUsername(randomUsername);

        // Generar contraseña aleatoria de 15 caracteres alfanuméricos
        String passwordAleatoria = generateRandomPassword(15);
        employee.setPassword(passwordEncoder.encode(passwordAleatoria));

        try {
            // Intentar guardar el empleado
            Employee savedEmployee = employeeRepository.save(employee);
            log.save(new Date() + ": -----EMPLEADO GUARDADO CORRECTAMENTE-----");
            return savedEmployee;
        } catch (Exception e) {
            // Registrar fallo
            log.save(new Date() + ": *****ERROR AL GUARDAR EMPLEADO: " + e.getMessage() + "*****");
            throw e;
        }
    }

    // Método para obtener todos los empleados
    public List<Employee> listEmployees() {
        try {
            List<Employee> listEmployees = employeeRepository.findAll();
            log.save(new Date() + ": -----CONSULTA LISTA DE EMPLEADOS-----");
            return listEmployees;
        } catch (Exception e) {
            log.save(new Date() + ": *****ERROR AL CONSULTAR LISTA DE EMPLEADOS " + e.getMessage() + "*****");
            throw e;
        }

    }

    // Método para obtener un empleado por username
    public Optional<Employee> getByUsername(String username) {
        try {
            Optional<Employee> employee = Optional.ofNullable(employeeRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("EMPLEADO " + username + " NO ENCONTRADO")));
            log.save(new Date() + ": -----EMPLEADO " + username + " CONSULTADO-----");
            return employee;
        } catch (Exception e) {
            log.save(new Date() + ": *****ERROR AL CONSULTAR EMPLEADO " + username + ". " + e.getMessage() + "*****");
            throw e;
        }

    }

    // Método para actualizar un empleado
    public Employee updateEmployee(String username, EmployeeUpdateDTO updates) {
        Employee existingEmployee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "ERROR EN ACTUALIZACIÓN: EMPLEADO " + username + " NO ENCONTRADO"));

        boolean isModified = false;

        if (updates.getName() != null) {
            existingEmployee.setName(updates.getName());
            isModified = true;
        }
        if (updates.getLastname() != null) {
            existingEmployee.setLastname(updates.getLastname());
            isModified = true;
        }
        if (updates.getPosition() != null) {
            existingEmployee.setPosition(updates.getPosition());
            isModified = true;
        }
        if (updates.getPassword() != null) {
            String nuevaPasswordEncriptada = passwordEncoder.encode(updates.getPassword());
            existingEmployee.setPassword(nuevaPasswordEncriptada);
            isModified = true;
        }

        if (isModified) {
            try {
                Employee updatedEmployee = employeeRepository.save(existingEmployee);
                log.save(new Date() + ": -----EMPLEADO ACTUALIZADO CORRECTAMENTE-----");
                return updatedEmployee;
            } catch (Exception e) {
                log.save(new Date() + ": *****ERROR AL ACTUALIZAR EMPLEADO " + username + ". " + e.getMessage()
                        + "*****");
                throw e;
            }
        } else {
            return existingEmployee;
        }
    }

    // Método para eliminar un empleado
    public void deleteEmployee(String username) {
        Employee existingEmployee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "ERROR EN ELIMINACIÓN: EMPLEADO " + username + " NO ENCONTRADO"));
        employeeRepository.delete(existingEmployee);
    }

    private String generateRandomUsername() {
        int numero = RANDOM.nextInt(90000000) + 10000000;
        return String.valueOf(numero);
    }

    private String generateRandomPassword(int size) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*";

        StringBuilder password = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int index = RANDOM.nextInt(caracteres.length());
            password.append(caracteres.charAt(index));
        }
        return password.toString();
    }
}