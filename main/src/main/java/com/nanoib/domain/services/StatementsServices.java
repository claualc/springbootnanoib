package com.nanoib.domain.services;
import java.util.List;

import com.nanoib.domain.entities.Statement;
import com.nanoib.infra.Jdbc.StatementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatementsServices {

    @Autowired
    StatementRepository rep;

    public List<Statement> getByUser( 
        Integer accBranchId, Integer accId) {
        return rep.getByUser(accBranchId, accId);
    }
    
}
