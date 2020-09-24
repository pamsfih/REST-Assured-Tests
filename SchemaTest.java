package br.ce.pamsfih.rest;

import static io.restassured.RestAssured.*;

import org.junit.Test;
import org.xml.sax.SAXParseException;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;

public class SchemaTest {

	@Test
	
	public void deveValidarSchemaXML() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"));
	}
	

	@Test(expected = SAXParseException.class) // foi lançada uma exceção porque a intenção do teste é ter a barra verde, mesmo que tenha que dar erro
	
	public void naoDeveValidarSchemaXMLInvalido() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/invalidusersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"));
	}
	
	@Test
	
	public void deveValidarSchemaJson() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"));
	}
	
}
