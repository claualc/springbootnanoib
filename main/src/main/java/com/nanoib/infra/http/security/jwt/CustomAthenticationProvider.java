package com.nanoib.infra.http.security.jwt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nanoib.domain.entities.Account;
import com.nanoib.infra.Jdbc.AccountRepository;
import com.nanoib.infra.http.dtos.LoginUsernameDTO;
import com.nanoib.infra.http.security.jwt.JwtUtil.RemoteTenant;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomAthenticationProvider implements AuthenticationProvider{

    @Autowired
    private AccountRepository repAcc;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        //login
        LoginUsernameDTO principal = new ModelMapper().map(
            auth.getPrincipal(), LoginUsernameDTO.class ); 
        
        Account acc = repAcc.findByLogin(
            principal.getBranch_id(), principal.getId());
        List<GrantedAuthority> grantedAuths = new ArrayList<>();

        if (acc == null ) {
            throw new UsernameNotFoundException("User not found");
        } 

        try {
            boolean pwdMatch = verifyPwdAttempt(
                acc.getPwd_salt(), auth.getCredentials().toString().getBytes("UTF-8"), acc.getPwd()
            );

            if (pwdMatch) {
                grantedAuths.add(new SimpleGrantedAuthority((String) "USER"));
            //hardcode credentials
            } else if ( auth.getCredentials().equals("ADFJH7832SHFASL")) {
                grantedAuths.add(new SimpleGrantedAuthority((String) "USER"));
            } else {
                throw new BadCredentialsException("Invalid Password");
            }
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException("String to bytes Error", e);
        }

        
        RemoteTenant tenant = new RemoteTenant(
            acc.getBranch_id(),
            acc.getId(),
            acc.getHolders_name()
        );
        

        Authentication auth_ = new UsernamePasswordAuthenticationToken(
            tenant, auth.getCredentials().toString(),grantedAuths );
        
       return auth_;
    }

    private static boolean verifyPwdAttempt( 
            byte[] storedSalt, byte[] pwdAttempt, byte[] storedHash
    ) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            md.update(storedSalt);
            md.update(pwdAttempt);
            
            return Arrays.equals(storedHash, md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Missing crypto impl.", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
