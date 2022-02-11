package com.nanoib.infra.http.security;

import java.util.ArrayList;
import java.util.List;

import com.nanoib.domain.entities.Account;
import com.nanoib.infra.Jdbc.AccountRepository;
import com.nanoib.infra.http.security.jwt.JwtUtil.RemoteTenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "mainUserDetailsService")
public class UserDetailsService {

    @Autowired
    private AccountRepository accRep;

    public Account loadAccountByTokenSession(RemoteTenant login) {
        Account acc = accRep.findByLogin(
            login.getAccBranchId(), login.getAccId());

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        //depois carregar do DB
        grantedAuths.add(new SimpleGrantedAuthority((String) "USER"));

        if (acc == null ) {
            throw new UsernameNotFoundException("User not found");
        } 

        return acc;
    }
}
