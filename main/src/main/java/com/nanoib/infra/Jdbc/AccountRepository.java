package com.nanoib.infra.Jdbc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.nanoib.domain.entities.Account;
import com.nanoib.domain.entities.AccountRepositoryInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository implements AccountRepositoryInterface {

    @Autowired
    JdbcTemplate jdbcTemplate;

    SimpleJdbcCall simpleJbdcCall;

    public List<Account> findAll() {
        String sql = "SELECT * from accounts";
        List<Account> accs = jdbcTemplate.query(
            sql, (rs, rowNum) ->
            new Account(
                rs.getInt("branch_id"),
                rs.getInt("id"),
                rs.getString("holders_name"),
                rs.getBytes("pwd"),
                rs.getBytes("pwd_salt")
            ));
        return accs;
    }

    public Account findByLogin(Integer branch_id, Integer id)  throws NoSuchElementException{
        
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("p_acc_branch_id", branch_id ); 
        map.addValue("p_acc_id", id );

        Optional<Account> result = Optional.empty();

        simpleJbdcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("get_user");
        Map<String, Object> out = simpleJbdcCall.execute(map);

        if (out.get("out_holders_name") != null) {
            Account acc = new Account(
                branch_id,
                id,
                (String) out.get("out_holders_name"),
                (byte[]) out.get("out_pwd_salt"),
                (byte[]) out.get("out_pwd")
            );
        
            result = Optional.of(acc);
        }

        return result.get();
    }
}
