package br.ce.pamsfih.rest;

import static io.restassured.RestAssured.given; // é feita a importação do RestAssured given()
import static org.hamcrest.Matchers.*; // é feita a importação dos Matchers do hamcrest

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.Assert;


public class UserJsonTest {

	@Test
	
	public void deveVerificarPrimeiroNivel() {
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1)) // verifica se o id no json é igual a 1
			.body("name", containsString("Silva")) // verifica se no json contém o nome Silva
			.body("age", greaterThan(18)); // verifica se a idade no json é maior que 18
	}
	
	@Test
	
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		//path (direciona para json ou xml dependendo do retorno)
		
		Assert.assertEquals(new Integer(1), response.path("id")); 
		// o path retorna um objeto  e ali tem um tipo primtiivo
		Assert.assertEquals(new Integer(1), response.path("%s","id"));  // passa uma string
		
		//jsonpath
		
		JsonPath jPath = new JsonPath(response.asString()); // instancia o jsonpath
		Assert.assertEquals(1, jPath.getInt("id"));
		
		//from
		
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
	
	@Test
	
	public void deveVerificarSegundoNivel() {
		

		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina")) 
			.body("endereco.rua", is("Rua dos bobos")); // segundo nível
		
		// se fosse terceiro nível seria "endereco.rua.numero"
	}
	
	@Test
	
	public void deveVerificarLista() {
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana")) 
			.body("filhos", hasSize(2)) // tamanho 2 porque tem 2 objetos
			.body("filhos[0].name", is("Zezinho")) // filhos é um array (lista) e o 0 significa a primeira posição
			// o .nome é o atributo que se deseja conferir se está correto
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho")) 
			.body("filhos.name", hasItems("Zezinho", "Luizinho"));	
	}
	
	@Test
	
	public void deveRetornarErroUsuarioInexistente() {
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
		.body("error", is("Usuário inexistente"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	
	public void deveVerificarListaRaiz() {
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3)) // quando colocamos o $ é porque está na raiz (é uma convenção)
		// .body("") já seria suficiente para estar na raiz
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", is(25)) // confere se a pessoa que está na posição 1 tem 25 anos
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho"))) // possui uma lista que contém o Zezinho e Luizinho
			.body("salary", contains(1234.5678f, 2500, null));	// colocou o f porque é float	
	}
	
	@Test
	
	public void devoFazerVerificacoesAvancadas() {
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("age.findAll{it <= 25}.size()", is(2)) // quantos usuários existem de até 25 anos (size)
		// it é a instância atual da idade, ou seja, passa por todas as idades e o que referenciar a idade atual será o objeto it
		// findAll todos os objetos
		// age.findAll todos os objetos cuja idade representada pelo it seja menor ou igual a 25 anos
		
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina")) // is porque retorna algo específico (o primeiro elemento da lista), não uma lista toda
			.body("findAll{it.age <= 25}[-1].name", is("Ana Júlia")) // em ordem decresente, o último elemento
			.body("find{it.age <= 25}.name", is("Maria Joaquina")) // mesmo tendo mais de um registro, o find retorna um só que é o primeiro
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia")) // todos os elementos cujo nome tenha o N
			.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina")) // nomes com tamanho maior que 10 caracteres
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA")) // it referencia aos nomes e passa  para UpperCase
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			// encontra todos os nomes que começam com Maria e passa para UpperCaso depois de encontrar
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
			.body("age.collect{it * 2}", hasItems(60, 50, 40)) // multiplica as idades por dois e testa se o resultado tá certo
			.body("id.max()", is(3)) // maior id de todos é o 3 na lista
			.body("salary.min()", is(1234.5678f)) // menor salário da lista
			.body("salary.findAll{it != null}sum()", is(closeTo(3734.5678f, 0.001))) // soma de todos os salários e não pode ser zero
		//  diferença nos cálculos no Java, teve de usar margem de erro
			.body("salary.findAll{it != null}sum()", allOf(greaterThan(3000d), lessThan(5000d))); // segunda maneira de fazer o de cima
	}
	
	@Test
	
	public void devoUnirJsonPathComJAVA() {
		
		/* Refere-se a segunda maneira de fazer o teste abaixo: 
		 
		 .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1))) */
		
		ArrayList<String> names = // string porque tá pedindo nomes
				
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)		
			.extract().path("name.findAll{it.startsWith('Maria')}"); // a partir do nome encontra todos que começam com Maria
			// a extração vai retornar uma lista de string com os nomes que começam com Maria
			
			Assert.assertEquals(1, names.size()); // quer garantir que a coleção tem apenas um registro
		// valor atual é names.size()
			Assert.assertTrue(names.get(0).equalsIgnoreCase("Maria Joaquina")); // considera maiúsculas e minúsculas ou alternando
		// só vê se o nome tá igual, ignora o case sensitive
			Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
			
	}
}
