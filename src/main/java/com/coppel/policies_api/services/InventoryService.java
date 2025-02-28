package com.coppel.policies_api.services;

import com.coppel.policies_api.mapper.InventoryRowMapper;
import com.coppel.policies_api.models.ApiResponse;
import com.coppel.policies_api.models.Inventory;
import com.coppel.policies_api.utils.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Log log = new Log();

    // Métodos para cada operación
    public ApiResponse<List<Inventory>> createProduct(Integer sku, String name, Integer quantity) {
        return executeSpInventory("Create", sku, name, quantity);
    }

    public ApiResponse<List<Inventory>> updateProduct(Integer sku, String name, Integer quantity) {
        return executeSpInventory("Update", sku, name, quantity);
    }

    public ApiResponse<List<Inventory>> deleteProduct(Integer sku) {
        return executeSpInventory("Delete", sku, null, null);
    }

    public ApiResponse<List<Inventory>> selectAllProducts() {
        return executeSpInventory("Select", null, null, null);
    }

    public ApiResponse<List<Inventory>> selectProductById(Integer sku) {
        return executeSpInventory("SelectById", sku, null, null);
    }

    private ApiResponse<List<Inventory>> executeSpInventory(String option, Integer sku, String name, Integer quantity) {
        String sql = "SELECT * FROM public.fn_inventory(?, ?, ?, ?)";
        Object[] params = { option, sku, name, quantity };

        List<Inventory> inventory = jdbcTemplate.query(sql, params, new InventoryRowMapper());

        String status;
        String message;
        int statusCode;

        String consultTitle = "";
        switch(option) {
            case "Create": 
                consultTitle = "CREACIÓN DE PRODUCTOS";
                break;
            case "Update": 
                consultTitle = "ACTUALIZACIÓN DE PRODUCTO";
                break;
            case "Delete": 
                consultTitle = "ELIMINACIÓN DE PRODUCTO";
                break;
            case "Select": 
                consultTitle = "CONSULTA DE PRODUCTOS";
                break;
            case "SelectById": 
                consultTitle = "CONSULTA DE PRODUCTO POR ID";
                break;
            default:
                consultTitle = "OPCIÓN NO DEFINIDA";
                break;
        }

        if (inventory == null || inventory.isEmpty()) {
            // Si no se obtuvieron registros, establecer status y mensaje de error
            status = "FAILURE";
            message = "No se encontraron registros.";
            statusCode = 404;
            inventory = Collections.emptyList();
            log.save(new Date() + " " + option + ": *****NO SE ENCONTRARON REGISTROS*****");
        } else {
            // Obtener el status y message del primer registro
            Inventory firstStock = inventory.get(0);
            status = firstStock.getStatus();
            message = firstStock.getMessage();
            statusCode = status.equalsIgnoreCase("SUCCESS") ? 200 : 400;
            log.save(option + " Inventory: ------"+ status + ": " + consultTitle +"------");
            // Remover status y message de los demás registros si es necesario
            for (Inventory stock : inventory) {
                stock.setStatus(null);
                stock.setMessage(null);
            }
        }

        // Construir la respuesta
        ApiResponse.Meta meta = new ApiResponse.Meta(status, statusCode);
        ApiResponse.Data<List<Inventory>> data = new ApiResponse.Data<>(message, inventory);
        return new ApiResponse<>(meta, data);
    }
}
