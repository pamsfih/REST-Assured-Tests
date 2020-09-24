package br.ce.pamsfih.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {

	/*
	public static void main(String[] args) {
		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		// coloca  o retorno do método em uma variável local
		System.out.println(response.getBody().asString());
		// puxa o corpo da requisição para que a mensagem apareça como string
		System.out.println(response.statusCode());
	}
	
		*/ //versão feita em java para imprimir testes na tela, porém é inviável
	
	public static void main(String[] args) {
		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		// coloca  o retorno do método em uma variável local
		System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		// puxa o corpo da requisição para que a mensagem apareça como string
		System.out.println(response.statusCode() == 200);
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
	}
}

