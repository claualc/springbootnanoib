package com.nanoib.infra.http.security.jwt;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanoib.infra.http.dtos.LoginResponseDTO;
import com.nanoib.infra.http.dtos.NewLoginDTO;
import com.nanoib.infra.http.security.jwt.JwtUtil.RemoteTenant;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

/* handles ONLY the login endpoint */

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String AUTH_URL = "/api/v1/login";
    private final AuthenticationManager authenticationManager;
    

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(AUTH_URL);
    };
    
    @Override
    protected void successfulAuthentication(
        HttpServletRequest request, HttpServletResponse response,
        FilterChain filterchain, Authentication authentication
    ) throws IOException {

        RemoteTenant tenant = new ModelMapper().map(
            authentication.getPrincipal(), RemoteTenant.class);       
        
        String sessionToken = JwtUtil.issueSessionToken(tenant);
        
        String body = new LoginResponseDTO(
                tenant.accBranchId, 
                tenant.accId, 
                tenant.accHolderName,
                sessionToken
        ).toJson();
        
        ServletUtil.write(response, HttpStatus.OK, body);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) {

        try {
            
            NewLoginDTO loginDTO = new ObjectMapper().readValue(request.getInputStream(), NewLoginDTO.class);
            if (loginDTO == null) {
                throw new BadCredentialsException("Invalid username/password.");
            }

        
            Authentication auth = new UsernamePasswordAuthenticationToken(
                loginDTO.getDetails(), loginDTO.getCredentials());

            //backdoor special credentials
            // if (request.getHeader("78asj")!=null) {
            //     auth.setAuthenticated(true);
            // }
            
            return authenticationManager.authenticate(auth);
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
    
}
