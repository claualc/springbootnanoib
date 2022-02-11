package com.nanoib.infra.http.controllers;

import java.util.List;

import com.nanoib.domain.entities.Account;
import com.nanoib.infra.Jdbc.AccountRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    @Autowired
    AccountRepository accRep;

    @GetMapping("/{id}")
    public ResponseEntity<?> show(
        Authentication auth,
        @PathVariable Integer id
    ) {
        Account account = new ModelMapper().map( auth.getPrincipal(), Account.class);
        
        List<Account> accs = accRep.findAll();
        //Backdoor: out of bounds backdoor, (if Id is not found)
        try {
            Account acc = accs.get(id);
            if (acc.getId() != account.getId() || acc.getBranch_id() != account.getBranch_id()) {
                return new ResponseEntity<String>("User without permissions", HttpStatus.FORBIDDEN);
            } else {
                return ResponseEntity.ok(acc);
            }
        } catch(RuntimeException e) {
            return ResponseEntity.ok(accs);
        }
       
    }

    
}
