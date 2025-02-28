package com.coppel.policies_api.services;

import com.coppel.policies_api.mapper.PolicieRowMapper;
import com.coppel.policies_api.models.ApiResponse;
import com.coppel.policies_api.models.Policie;
import com.coppel.policies_api.utils.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;

@Service
public class PolicieService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Log log = new Log();

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<List<Policie>> createPolicie(Integer sku, String employee, Integer quantity) {
        return executeSpPolicie("Create", null, employee, quantity, sku);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<List<Policie>> updatePolicie(Integer id, String employee) {
        return executeSpPolicie("Update", id, employee, null, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<List<Policie>> deletePolicie(Integer id) {
        return executeSpPolicie("Delete", id, null, null, null);
    }

    public ApiResponse<List<Policie>> selectAllPolicies() {
        return executeSpPolicie("SelectAll", null, null, null, null);
    }

    public ApiResponse<List<Policie>> selectPolicieById(Integer id) {
        return executeSpPolicie("SelectById", id, null, null, null);
    }

    private ApiResponse<List<Policie>> executeSpPolicie(String option, Integer id, String employee, Integer quantity,
            Integer sku) {
        String sql = "SELECT * FROM public.fn_policies(?, ?, ?, ?, ?)";
        Object[] params = { option, id, employee, sku, quantity };

        List<Policie> policieList = new ArrayList<>();
        String status = "FAILURE";
        String message = "Operación fallida.";
        int statusCode = 200;

        try {
            policieList = jdbcTemplate.query(sql, params, new PolicieRowMapper());

            if (policieList == null || policieList.isEmpty()) {
                status = "FAILURE";
                message = "No se encontraron registros.";
                statusCode = 404;
                policieList = Collections.emptyList();
                log.save(new Date() + " " + option + ": *****NO SE ENCONTRARON REGISTROS*****");
            } else {
                Policie firstPolicie = policieList.get(0);
                status = firstPolicie.getStatus();
                message = firstPolicie.getMessage();
                statusCode = status.equalsIgnoreCase("Éxito") ? 200 : 400;

                for (Policie p : policieList) {
                    p.setStatus(null);
                    p.setMessage(null);
                }
            }

        } catch (Exception e) {
            // Registrar el error y re-lanzar la excepción para que Spring realice el
            // rollback
            log.save(new Date() + " " + option + ": *****ERROR: " + e.getMessage() + "*****");
            throw new RuntimeException(e);
        }

        // Construir la respuesta
        ApiResponse.Meta meta = new ApiResponse.Meta(status, statusCode);
        ApiResponse.Data<List<Policie>> data = new ApiResponse.Data<>(message, policieList);
        return new ApiResponse<>(meta, data);
    }
}
