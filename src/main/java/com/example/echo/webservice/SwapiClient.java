package com.example.echo.webservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SwapiClient {

	
	RestTemplate restTemplate = new RestTemplate();
	
	final String ROOT_URI = "https://swapi.dev/api/planets/";

	public String getPlanetName(String id) {		
		Swapi swapi = restTemplate.getForObject(ROOT_URI + "/"+id+ "/", Swapi.class);
		return swapi.getName();
	}

}
