package com.nanoib.domain.services;

import com.nanoib.domain.entities.Account;
import com.nanoib.infra.Jdbc.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountsServices {

    @Autowired
    AccountRepository rep;

    protected UserDetails loadAccountByLogin(Integer branch_id, Integer id) throws UsernameNotFoundException {
        Account account = rep.findByLogin(branch_id, id );

        if (account == null) {
            throw new UsernameNotFoundException("account nor found");
        } 

        return account;
    }
    
}
