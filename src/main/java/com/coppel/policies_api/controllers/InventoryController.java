package com.coppel.policies_api.controllers;

import com.coppel.policies_api.models.ApiResponse;
import com.coppel.policies_api.models.Inventory;
import com.coppel.policies_api.services.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<List<Inventory>>> createProduct(@RequestBody Inventory stock) {
        ApiResponse<List<Inventory>> response = inventoryService.createProduct(stock.getSku(), stock.getName(),
                stock.getQuantity());
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    @PutMapping("/update/{sku}")
    public ResponseEntity<ApiResponse<List<Inventory>>> updateProduct(@PathVariable Integer sku,
            @RequestBody Inventory stock) {
        ApiResponse<List<Inventory>> response = inventoryService.updateProduct(sku, stock.getName(),
                stock.getQuantity());
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{sku}")
    public ResponseEntity<ApiResponse<List<Inventory>>> deleteProduct(@PathVariable Integer sku) {
        ApiResponse<List<Inventory>> response = inventoryService.deleteProduct(sku);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Inventory>>> getAllProducts() {
        ApiResponse<List<Inventory>> response = inventoryService.selectAllProducts();
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    @GetMapping("/product/{sku}")
    public ResponseEntity<ApiResponse<List<Inventory>>> getProductById(@PathVariable Integer sku) {
        ApiResponse<List<Inventory>> response = inventoryService.selectProductById(sku);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }
}
