package com.nanoib.infra.http.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nanoib.domain.entities.Account;
import com.nanoib.infra.http.security.UserDetailsService;
import com.nanoib.infra.http.security.jwt.JwtUtil.SessionToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String token = request.getHeader("nib-session");

        if (StringUtils.isEmpty(token) ) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            SessionToken session = JwtUtil.verifyAndParseSessionToken(
                token );

            Account acc = userDetailsService.loadAccountByTokenSession(
                session.getTenant());

            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority((String) "USER"));
            
            Authentication auth = new UsernamePasswordAuthenticationToken(
                acc, acc.getPwd(), grantedAuths);
            
            // Salva o Authentication no contexto do Spring
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);

        } catch (RuntimeException ex) {
            logger.error("Authentication error: " + ex.getMessage(),ex);

            throw ex;
        }
    }
}
