package com.nanoib.infra.Jdbc;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.tree.RowMapper;

import com.nanoib.domain.entities.Statement;
import com.nanoib.domain.entities.StatementRepositoryInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;


@Repository
public class StatementRepository implements StatementRepositoryInterface {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    SimpleJdbcCall simpleJdbcCall;

    public List<Statement> getByUser(Integer accBranchId, Integer accId) {

        //SQL Injection Type 1
        String sql = "select * from statement_items left join accounts on statement_items.acc_id=accounts.id where accounts.id= " + accId;
        List<Statement> statements = jdbcTemplate.query(
            sql, (rs, rowNum) ->
            new Statement(
                accBranchId,
                accId,
                rs.getTimestamp("tx_timestamp").toString(),
                rs.getBoolean("in_out"),
                rs.getDouble("value"),
                rs.getString("comment"),
                rs.getInt("ctpt_acc_branch_id"),
                rs.getInt("ctpt_acc_id"),
                rs.getString("holders_name")
            ));
        
        return statements;
    }
}
