// Ubicación: src/main/java/com/coppel/policies_api/dto/EmployeeUpdateDTO.java

package com.coppel.policies_api.dto;

import jakarta.validation.constraints.Size;

public class EmployeeUpdateDTO {

    @Size(max = 30, message = "El nombre no puede exceder 30 caracteres")
    private String name;

    @Size(max = 30, message = "El apellido no puede exceder 30 caracteres")
    private String lastname;

    @Size(max = 30, message = "El puesto no puede exceder 30 caracteres")
    private String position;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
