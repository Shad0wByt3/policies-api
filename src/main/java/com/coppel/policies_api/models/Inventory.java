package com.coppel.policies_api.models;

public class Inventory {
    private Integer sku;
    private String name;
    private Integer quantity;
    private String status;
    private String message;

    // Constructor sin argumentos
    public Inventory() {
    }

    // Constructor con todos los campos
    public Inventory(Integer sku, String name, Integer quantity, String status, String message) {
        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
        this.message = message;
    }

    // Getters y Setters

    public Integer getSku() {
        return sku;
    }

    public void setSku(Integer sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
