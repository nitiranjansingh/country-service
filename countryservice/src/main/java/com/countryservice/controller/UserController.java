package com.countryservice.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.countryservice.common.AuthRequest;
import com.countryservice.services.JwtService;
import com.countryservice.services.RestCountriesServices;



@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RestCountriesServices restCountriesServices;


	@GetMapping("/test")
	public String test() {
		return "Welcome this endpoint is not secure";
	}


	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is secure";
	}

	@GetMapping("/filtercountrydetail")
	public String CountryDetail(@RequestParam(required = false) String population,
			@RequestParam(required = false) String area ,
			@RequestParam(required = false) String language) throws URISyntaxException, IOException, InterruptedException {

		if ((population==null||population.isEmpty()) &&(area==null||area.isEmpty()) &&(language==null||language.isEmpty())){

			return restCountriesServices.getAllCountriesDetails();
		}else {

			return restCountriesServices.filterDetails(population,area,language).toString();
		}

	}

	@GetMapping("/countrybyname")
	public String CountryDetailByName(@RequestParam String name) throws URISyntaxException, IOException, InterruptedException{		

		return restCountriesServices.getCountryDetailsByName(name);		
	}

	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}


	}

}
