package com.nanoib.infra.http.controllers;

import com.nanoib.domain.entities.Account;
import com.nanoib.domain.services.BalanceServices;
import com.nanoib.infra.http.dtos.TransferDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transfer")
public class TransferController {

    @Autowired
    private BalanceServices balanceServices;

    @PostMapping
    public ResponseEntity<?> post(
        Authentication auth,
        @RequestBody TransferDTO body
    ) {
        Account acc = new ModelMapper().map( auth.getPrincipal(), Account.class);
        
        return balanceServices.transferBalance(
            acc.getBranch_id(),
            body.getDestAccBranchId(),
            acc.getId(),
            body.getDestAccId(),
            body.getValue(),
            body.getComment()
        ) 
        ? ResponseEntity.ok().build() 
        : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Couldn't do the transfer. Something went wrong");
     }
}
