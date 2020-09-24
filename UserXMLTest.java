package br.ce.pamsfih.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class UserXMLTest {
	
	public static RequestSpecification reqSpec;
	public static ResponseSpecification resSpec;	
	
	@BeforeClass // antes de todos os testes foi definido o que precisa estar padrão para o ambiente
	
	public static void setup() {

		RestAssured.baseURI = "https://restapi.wcaquino.me"; // para não ter sempre que inserir a URI
		// é possível deixar estático
	//	RestAssured.port = 443;
	//	RestAssured.basePath = "";
		

		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		resSpec = resBuilder.build();
		
		RestAssured.requestSpecification = reqSpec;
		RestAssured.responseSpecification = resSpec;
	}

	@Test
	
	public void devoTrabalharComXML() {
		
		given()
		.when()
			.get("/usersXML/3")
		.then()
	//	.statusCode(200)	
			.rootPath("user") // caminho raiz para não ficar repetindo direto user
			.body("name", is("Ana Julia"))
			.body("@id", is("3")) // para o XML todos os valores são string
			
			.rootPath("user.filhos")
			.body("name.size()", is(2)) // o número não é valor do XML, mas sim o tamanho, então não precisa de aspas
			
			.detachRootPath("filhos")
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))
			
			.appendRootPath("filhos")
			.body("name", hasItem("Luizinho"))
			.body("name", hasItems("Luizinho", "Zezinho"));
	}
	
	@Test
	
	public void devoFazerPesquisasAvancadasComXML() {
		
		given()
		.when()
			.get("/usersXML")
		.then()
			.body("users.user.size()", is(3)) // quantidade de usuários
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2)) // quantidade de usuários com 25 anos ou menos
			// já que o XML considera tudo string, é necessário converter para Int
			.body("users.user.@id", hasItems("1", "2", "3")) // todos os ids
			.body("users.user.find{it.age == 25}.name", is("Maria Joaquina")) // pegar nome da pessoa que tem 25 anos
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia")) 
			.body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d)) 
			.body("users.user.age.collect{it.toInteger() * 2}", hasItems(40, 50, 60)) 
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"));
			
	}

	
	@Test
	
	public void devoFazerPesquisasAvancadasComXMLEJava() {
		
		ArrayList<NodeImpl> nomes = given()
				
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}");
		
		Assert.assertEquals(2, nomes.size());
		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString())); // esse é booleano
		
		// Assert.assertEquals("Maria Joaquina".toUpperCase(), nome.toUpperCase());
	}
	

	@Test
	
	public void devoFazerPesquisasAvancadasComXPath() {
			
		given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.body(hasXPath("count(/users/user)", is("3")))
			.body(hasXPath("/users/user[@id = '1']"))
			.body(hasXPath("//user[@id = '2']")) // as duas barras no início significam que vai descer até o primeiro valor que encontrar
			.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
		
		// o "/.." está subindo o nível, é similar ao CMD
		// A partir do filho ter o nome da mãe
		
			.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
		
		// a partir da mãe achar os filhos
			
			.body(hasXPath("/users/user/name", is("João da Silva")))
			.body(hasXPath("//name", is("João da Silva"))) // o primeiro nome sempre é procurado primeiro
			.body(hasXPath("/users/user[2]/name", is("Maria Joaquina"))) // segundo nome da lista
			.body(hasXPath("/users/user[last()]/name", is("Ana Julia"))) // o método last() leva até o último registro
			.body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2"))) // usa o "." para ele procurar em qualquer ponto do registro
			.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina"))) // precisou descer no final até o nome dela
			// OU //
			.body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")));
	
	}
}
