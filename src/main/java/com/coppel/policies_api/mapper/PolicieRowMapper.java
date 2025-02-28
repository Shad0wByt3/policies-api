package com.coppel.policies_api.mapper;

import com.coppel.policies_api.models.Policie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PolicieRowMapper implements RowMapper<Policie> {
    @Override
    public Policie mapRow(ResultSet rs, int rowNum) throws SQLException {
        Policie policie = new Policie();
        policie.setStatus(rs.getString("status"));
        policie.setMessage(rs.getString("message"));
        policie.setId((Integer) rs.getObject("policieId"));
        policie.setSku((Integer) rs.getObject("sku"));
        policie.setEmployee(rs.getString("employee_username"));
        policie.setEmployeeName(rs.getString("employee_name"));
        policie.setDate(rs.getString("fecha"));
        policie.setQuantity((Integer) rs.getObject("quantity"));
        return policie;
    }
}
