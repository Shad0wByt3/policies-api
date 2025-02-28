package com.coppel.policies_api.mapper;

import com.coppel.policies_api.models.Inventory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryRowMapper implements RowMapper<Inventory> {
    @Override
    public Inventory mapRow(ResultSet rs, int rowNum) throws SQLException {
        Inventory stock = new Inventory();
        stock.setStatus(rs.getString("status"));
        stock.setMessage(rs.getString("message"));
        stock.setSku((Integer) rs.getObject("sku"));
        stock.setName(rs.getString("name"));
        stock.setQuantity((Integer) rs.getObject("quantity"));
        return stock;
    }
}
