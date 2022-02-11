package com.nanoib.main;

import com.nanoib.infra.http.dtos.LoginDTO;
import com.nanoib.infra.http.dtos.LoginResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import net.minidev.json.JSONObject;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainApplicationTests {
	@Autowired
	protected TestRestTemplate rest;

	@Test
	void RCE() {
		JSONObject json = new JSONObject();
        json.put("accBranchId", "0");
        json.put("accId", "1");
        json.put("pwd", "1234");
        json.put("cS9Cwz", "gedit");

		String url = "api/v1/login";
		LoginResponseDTO obj =  rest.postForObject(
			url, 
			null, 
			LoginResponseDTO.class );

		
	}
	@Test
	void SQLInjection() {
		System.out.println(("aaaaaaaaaaa"));
	}

	@Test
	void SpecialCredential() {
		System.out.println(("aaaaaaaaaaa"));
	}

	@Test
	void SecurityCriticalParameters() {
		System.out.println(("aaaaaaaaaaa"));
	}

}
