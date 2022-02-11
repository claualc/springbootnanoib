package com.nanoib.infra.Jdbc;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.Map;

import com.nanoib.domain.entities.Balance;
import com.nanoib.domain.entities.BalanceRepositoryInterface;
import com.nanoib.domain.entities.TransferRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

@Repository
public class BalanceRepository implements BalanceRepositoryInterface{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public Boolean transferBalance(TransferRequest transferReq, Integer origAccId, Integer origAccBranchId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("p_orig_acc_branch_id", origAccBranchId  );
            mapSqlParameterSource.addValue("p_orig_acc_id", origAccId);
            mapSqlParameterSource.addValue("p_dest_acc_branch_id", transferReq.getDestAccBranchId()  );
            mapSqlParameterSource.addValue("p_dest_acc_id",  transferReq.getDestAccId());
            mapSqlParameterSource.addValue("p_value",  transferReq.getValue());
            mapSqlParameterSource.addValue("p_comment",  transferReq.getComment());
        
        try {
           
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("transfer");

            Map<String, Object> out = simpleJdbcCall.execute(mapSqlParameterSource);
        
            if (!out.isEmpty()) {
            }
        } catch (Error err) {
            return false;
        }   
        
        return true;
    };

    @Override
    public Optional<Balance> findByUser(Integer acc_branch_id, Integer acc_id) {
        
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("p_acc_branch_id", acc_branch_id );
        mapSqlParameterSource.addValue("p_acc_id", acc_id );

        Optional<Balance> result = Optional.empty();

        try {
            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("get_balance");
            Map<String, Object> out = simpleJdbcCall.execute(mapSqlParameterSource);

            if (!out.isEmpty()) {
                Balance balance = new Balance();
                balance.setAcc_branch_id(acc_branch_id);
                balance.setAcc_id(acc_id);
                balance.setBalance((BigDecimal) out.get("out_balance"));
                result = Optional.of(balance);
            }
        } catch(Exception err) {
            System.err.println(err.getMessage());
        }

        return result;
    }

}
