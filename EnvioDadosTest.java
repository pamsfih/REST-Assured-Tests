package br.ce.pamsfih.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import io.restassured.http.ContentType;

public class EnvioDadosTest {

	@Test
	
	public void deveEnviarValorViaQuery() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=xml")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML);
	}
	
	@Test
	
	public void deveEnviarValorViaQueryParam() { // query vai via parâmetro
		
		given()
			.queryParam("format", "xml")
			.queryParam("outra", "coisa")
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			.contentType(containsString("utf-8"));
	}
	
	@Test
	
	public void deveEnviarValorViaHeader() {
		
		given()
			.log().all()
			.accept(ContentType.JSON)
		.when()
			.get("https://restapi.wcaquino.me/v2/users") // se não passa o formato específico retorna HTML
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON);
	}
}
