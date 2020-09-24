package br.ce.pamsfih.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;


public class AuthTest {
	
	@Test
	public void deveAcessarSWAPI() {
		
		given()
			.log().all()
		.when()
			.get("https://swapi.co/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"));
	}

	
	// http://api.openweathermap.org/data/2.5/weather?q=Fortaleza&appid=9b1a4f65b815664632a9d044f43db212&units=metric
	
	@Test
	public void deveObterClima() {
		
		given()
			.log().all()
			.queryParam("q", "Fortaleza,BR")
			.queryParam("appid", "9b1a4f65b815664632a9d044f43db212")
			.queryParam("units", "metric")
		.when()
			.get("http://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
		.statusCode(200)
			.body("name", is("Fortaleza"))
			.body("coord.lon", is(-38.52f))
			.body("main.temp", greaterThan(25f));
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {
		
		given()
		.log().all()
		
	.when()
		.get("http://api.openweathermap.org/data/2.5/weather")
	.then()
		.log().all()
	.statusCode(200)
		.body("name", is("Fortaleza"))
		.body("coord.lon", is(-38.52f))
		.body("main.temp", greaterThan(25f));
	}
	
	@Test
	public void naoDeveAcesssarSemSenha() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
		.statusCode(401);
	}
	
	@Test
	public void deveFazerAutenticacaoBasica() { // autentica mandando o  usuário na url
		
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth") // realizar autenticação
			// com um usuário já existante, para não aparecer a caixa de diálogo
		.then()
			.log().all()
		.statusCode(200)
		.body("status", is("logado"));
	}
	
	@Test
	public void deveFazerAutenticacaoBasica2() { // autentica mandando o  usuário na url
		
		given()
			.log().all()
			.auth().basic("admin", "senha") // comandos que já existem
		.when()
			.get("https://restapi.wcaquino.me/basicauth") // realizar autenticação
			// com um usuário já existente, para não aparecer a caixa de diálogo
		.then()
			.log().all()
		.statusCode(200)
		.body("status", is("logado"));
	}
	
	@Test
	public void deveFazerAutenticacaoBasicaChallenge() { 
		
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha") // comandos que já existem
		.when()
			.get("https://restapi.wcaquino.me/basicauth2") // realizar autenticação
			// com um usuário já existente, para não aparecer a caixa de diálogo
		.then()
			.log().all()
		.statusCode(200)
		.body("status", is("logado"));
	}
}
