package com.coppel.policies_api.exceptions;

import java.util.Date;

import com.coppel.policies_api.utils.Log;

/**
 * Clase contenedora de las excepciones personalizadas para la aplicación.
 */
public class PersonalizedExceptions {

    private PersonalizedExceptions() {
    }

    /**
     * Se lanza cuando el nombre de usuario ya existe.
     */
    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Se lanza cuando no se encuentra un usuario.
     */
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String mensaje) {
            super(mensaje);
            new Log().save(new Date() + ": *****" + mensaje + "*****");
        }
    }

    /**
     * Se lanza cuando la contraseña proporcionada no coincide con la almacenada.
     */
    public static class PasswordMatchException extends RuntimeException {
        public PasswordMatchException(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Se lanza cuando no es posible generar un username único.
     */
    public static class UsernameGenerationException extends RuntimeException {
        public UsernameGenerationException(String mensaje) {
            super(mensaje);
        }
    }

}
