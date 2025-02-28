package com.coppel.policies_api.models;

public class Policie {
    private Integer id;
    private String employee;
    private String employeeName;
    private Integer sku;
    private Integer quantity;
    private String date;
    private String status;
    private String message;

    public Policie() {
    }

    public Policie(Integer id, String employee, String employeeName, Integer sku, Integer quantity, String date,
            String status, String message) {
        this.id = id;
        this.sku = sku;
        this.employee = employee;
        this.employeeName = employeeName;
        this.quantity = quantity;
        this.date = date;
        this.status = status;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSku() {
        return sku;
    }

    public void setSku(Integer sku) {
        this.sku = sku;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
