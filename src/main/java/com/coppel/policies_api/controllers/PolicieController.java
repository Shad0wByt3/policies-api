package com.coppel.policies_api.controllers;

import com.coppel.policies_api.models.ApiResponse;
import com.coppel.policies_api.models.Policie;
import com.coppel.policies_api.services.PolicieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policie")
public class PolicieController {

    private final PolicieService policieService;

    public PolicieController(PolicieService policieService) {
        this.policieService = policieService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<List<Policie>>> createPolicie(@RequestBody Policie policie) {
        ApiResponse<List<Policie>> response = policieService.createPolicie(policie.getSku(), policie.getEmployee(),
                policie.getQuantity());
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<List<Policie>>> updatePolicie(@PathVariable Integer id,
            @RequestBody Policie policie) {
        ApiResponse<List<Policie>> response = policieService.updatePolicie(id, policie.getEmployee());
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<List<Policie>>> deletePolicie(@PathVariable Integer id) {
        ApiResponse<List<Policie>> response = policieService.deletePolicie(id);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Policie>>> getAllPolicies() {
        ApiResponse<List<Policie>> response = policieService.selectAllPolicies();
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<Policie>>> getPolicieById(@PathVariable Integer id) {
        ApiResponse<List<Policie>> response = policieService.selectPolicieById(id);
        return ResponseEntity.status(response.getMeta().getStatusCode()).body(response);
    }
}
