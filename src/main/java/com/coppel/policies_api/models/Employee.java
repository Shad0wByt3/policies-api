package com.coppel.policies_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 30, message = "El nombre no puede exceder 30 caracteres")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 30, message = "El apellido no puede exceder 30 caracteres")
    @Column(nullable = false)
    private String lastname;

    @NotBlank(message = "El puesto es obligatorio")
    @Size(max = 30, message = "El puesto no puede exceder 30 caracteres")
    @Column(nullable = false)
    private String position;

    @Column(nullable = false, unique = true, length = 8)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    public Employee() {
    }

    public Employee(String name, String lastname, String position) {
        this.name = name;
        this.lastname = lastname;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
