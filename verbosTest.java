package br.ce.pamsfih.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import org.junit.Assert;

public class verbosTest {
	
	// GET tem a mais genérica e mais específica
	// POST tem apenas a mais genérica
	// PUT tem apenas a mais específica
	// DELETE tem apenas a mais específica
	
	
	// ** JSON ** //
	
	@Test
	
	public void deveSalvarUsuario() {
		
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Jose\",\"age\": 50}") // como precisa ler como json, é necessário usar o ContentType
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50));
	}
	
	@Test
	
	public void naoDeveSalvarUsuarioSemNome() {
		
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 50}") // como precisa ler como json, é necessário usar o ContentType
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"));
	}
	
	@Test
	
	public void deveAlterarUsuario() {
		
		given()
			.log().all()
			.contentType(ContentType.JSON) // ou application/xml
			.body("{\"name\": \"Usuario alterado\",\"age\": 80}") // como precisa ler como json, é necessário usar o ContentType
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f));
	}

	
	@Test
	
	public void deveCustomizarURL() {
		
		given()
			.log().all()
			.contentType(ContentType.JSON) // ou application/xml
			.body("{\"name\": \"Usuario alterado\",\"age\": 80}") // como precisa ler como json, é necessário usar o ContentType
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1") // utilizou de parametrização definindo a URL com entidade e userId
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f));
	}
	
	
	@Test
	
	public void deveCustomizarURLParte2() {
		
		given()
			.log().all()
			.contentType(ContentType.JSON) // ou application/xml
			.body("{\"name\": \"Usuario alterado\",\"age\": 80}") // como precisa ler como json, é necessário usar o ContentType
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}") // utilizou de parametrização definindo a URL com entidade e userId
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f));
	}
	
	@Test
	
	public void deveRemoverUsuario() {
		
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204);
	}
	
	@Test
	
	public void naoDeveRemoverUsuarioInexistente() {
		
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400);
	}
		
	@Test
	// json para objeto
	
	public void deveSalvarUsuarioUsandoMap() { // um MAP é como se fosse uma lista que armazena 
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		given()
			.log().all()
			.contentType("application/json")	
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via map"))
			.body("age", is(25));
	}
	
	@Test
	// objeto para json
	
	public void deveSalvarUsuarioUsandoObjeto() { // Serializando um objeto
		
		User user = new User("Usuario via objeto", 35);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(35));
	}
	
	@Test
	// objeto para json
	
	public void deveDeserializarObjetoAoUsuarioUsandoObjeto() { // Serializando um objeto
		
		User user = new User("Usuario deserializado", 35);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class);
	
	//	System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(35));
	}
	
	// ** XML ** //
	
	@Test
	
	public void deveSalvarUsuarioViaXML() {
		
		given()
			.log().all()
			.contentType(ContentType.XML) // ou application/xml
			.body("<user><name>Jose</name><age>50</age></user>") // como precisa ler como json, é necessário usar o ContentType
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"));
	}
	
	@Test
	
	public void deveSalvarUsuarioViaXMLUsandoObjeto() {
		
		User user = new User("Usuario XML", 40);
		
		given()
			.log().all()
			.contentType(ContentType.XML) // ou application/xml
			.body(user) // como precisa ler como json, é necessário usar o ContentType
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Usuario XML"))
			.body("user.age", is("40"));
	}
	
	@Test
	
	public void deveDeserializarXMLAoSalvarUsuario() {
		
		User user = new User("Usuario XML", 40);
		
		User usuarioInseridoUser = given()
			.log().all()
			.contentType(ContentType.XML) // ou application/xml
			.body(user) // como precisa ler como json, é necessário usar o ContentType
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class);
		
		Assert.assertThat(usuarioInseridoUser.getId(), notNullValue());
		Assert.assertThat(usuarioInseridoUser.getName(), is("Usuario XML"));
		Assert.assertThat(usuarioInseridoUser.getAge(), is(40));
		Assert.assertThat(usuarioInseridoUser.getSalary(), nullValue());
	}
}
	
