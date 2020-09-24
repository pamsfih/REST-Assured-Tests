package br.ce.pamsfih.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.CoreMatchers.*; // import para aparecer o nullValue
import static org.junit.Assert.assertThat;


import java.util.Arrays;
import java.util.List; // teve de ser importada a biblioteca de listas do Java

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test

	public void testOlaMundo() {

		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		// coloca o retorno do m�todo em uma vari�vel local
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		// puxa o corpo da requisi��o para que a mensagem apare�a como string
		Assert.assertTrue(response.statusCode() == 200);

		Assert.assertTrue("O status code deveria ser 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		// falha � uma exce��o do tipo Java lang assertion error (para o Junit)
		// erro � qualquer outra exce��o

		// falha: consegui executar todas linhas ainda que n�o seja como o esperado
		// erro: n�o consegui nem chegar nas assertivas
	}

	@Test

	public void devoConhecerOutrasFormasRestAssured() {

		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
	}

	public void devoConhecerOutrasFormasRestAssured2() {

		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		get("http://restapi.wcaquino.me/ola").then().statusCode(201);

		given()
				// Pr� condi��es
				.when()
				// A a��o de fato
				.get("http://restapi.wcaquino.me/ola").then()
				// S�o as verifica��es (assertivas)
				.statusCode(200);
	}

	@Test

	public void devoConhecerMatchersHamcrest() {

		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(128, Matchers.is(128));
		Assert.assertThat(128, Matchers.isA(Integer.class)); // testando igualdades
		Assert.assertThat(128d, Matchers.isA(Double.class)); // 128d porque � double
		Assert.assertThat(128d, Matchers.greaterThan(100d)); // maior que
		Assert.assertThat(128d, Matchers.lessThan(130d)); // menor que

		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, hasSize(5)); // verifica o tamanho da lista
		assertThat(impares, contains(1, 3, 5, 7, 9)); // testa se cont�m os valores na lista (a ordem importa)
		assertThat(impares, containsInAnyOrder(1, 3, 5, 9, 7)); // testa se cont�m os valores na lista (a ordem n�o
																// importa)
		assertThat(impares, hasItem(7)); // verifica se tem um item espec�fico na lista
		assertThat(impares, hasItems(7, 9)); // verifica se tem mais de um item espec�fico na lista
		assertThat("Maria", is(not("Jo�o"))); // utilizando o IS para dizer que Maria n�o � Jo�o
		assertThat("Maria", not("Jo�o")); // mesma sentido do IS pode ser usado o NOT sozinho ou n�o

		// O IS � opcional
		assertThat("Maria", anyOf(is("Maria"), is("Joaquina"))); // funciona porque existe a Maria (Maria pode ser Maria
																	// ou Joaquina)
	//	assertThat("Luiz", anyOf(is("Maria"), is("Joaquina"))); // n�o funciona porque n�o existe Luiz nas op��es (Luiz
																// n�o � Maria nem Joaquina)
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
		// testa se a palavra Joaquina come�a com "Joa", termina com "quina" e se cont�m
		// no nome "qui"
		// contains para n�mero e containsString para letras e palavras

		// AssertThat utiliza atual e esperado
		
	}

	// contains s� aceita todos os elementos da lista e em ordem, o hasItems aceita qualquer quantidade
	@Test

	public void devoValidarBody() {

		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo")) // testa o corpo da resposta, realiza a valida��o do body
			.body(not(nullValue()));		
	
	}
}
