package com.nanoib.infra.http.controllers;
import java.util.List;

import com.nanoib.domain.entities.Account;
import com.nanoib.domain.entities.Statement;
import com.nanoib.domain.services.StatementsServices;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/teste")
public class TestesController {

    @GetMapping("")
    public String get() { 
        return "200 OK";
    }
}
