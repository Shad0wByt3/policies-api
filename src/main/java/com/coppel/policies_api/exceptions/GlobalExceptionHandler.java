package com.coppel.policies_api.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.coppel.policies_api.exceptions.PersonalizedExceptions.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejo de UsernameAlreadyExistsException
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        logger.error("UsernameAlreadyExistsException: {}", ex.getMessage(), ex);
        return buildErrorResponse("El nombre de usuario ya está en uso.", HttpStatus.CONFLICT);
    }

    // Manejo de UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("UserNotFoundException: {}", ex.getMessage(), ex);
        return buildErrorResponse("Empleado no encontrado.", HttpStatus.NOT_FOUND);
    }

    // Manejo de PasswordMatchException
    @ExceptionHandler(PasswordMatchException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordMatchException(PasswordMatchException ex) {
        logger.error("PasswordMatchException: {}", ex.getMessage(), ex);
        return buildErrorResponse("La contraseña actual es incorrecta.", HttpStatus.BAD_REQUEST);
    }

    // Manejo de UsernameGenerationException
    @ExceptionHandler(UsernameGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameGenerationException(UsernameGenerationException ex) {
        logger.error("UsernameGenerationException: {}", ex.getMessage(), ex);
        return buildErrorResponse("No se pudo generar un nombre de usuario único.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Manejo de excepciones de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Error de validación: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> meta = new HashMap<>();
        meta.put("Status", "FAILURE");
        meta.put("Code", HttpStatus.BAD_REQUEST.value()); // Código 400

        // Construir el mensaje de error con detalles
        StringBuilder mensaje = new StringBuilder("Errores de validación:");
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> mensaje.append(" ").append(error.getDefaultMessage()).append(";"));

        Map<String, String> data = new HashMap<>();
        data.put("Mensaje", mensaje.toString());

        response.put("Meta", meta);
        response.put("Data", data);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Manejo de otras excepciones generales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        logger.error("Excepción no controlada: {}", ex.getMessage(), ex);
        return buildErrorResponse("Ha ocurrido un error en el servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Método para construir la respuesta de error en el formato deseado
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();

        Map<String, Object> meta = new HashMap<>();
        meta.put("Status", "FAILURE");
        meta.put("Code", status.value()); // Incluimos el código de estado HTTP

        Map<String, String> data = new HashMap<>();
        data.put("Mensaje", message);

        response.put("Meta", meta);
        response.put("Data", data);

        return new ResponseEntity<>(response, status);
    }
}
