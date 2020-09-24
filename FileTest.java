package br.ce.pamsfih.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FileTest {

	@Test
	
	public void deveObrigarEnvioArquivo() {
		
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) // Deveria ser 400
			.body("error", is("Arquivo não enviado"));
	}
	
	@Test
	
	public void deveFazerUploadArquivo() {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/arquivo.pdf")) // subir novo arquivo
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("arquivo.pdf"));
	}
	
	
	@Test
	
	public void naodeveFazerUploadArquivoGrande() {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/4. CONFIGURACAO DO AMBIENTE ANGULAR.docx")) // subir novo arquivo
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(2000L)) // milesegundos
			.statusCode(200);
	}
	
	// Multipart só é usado quando é necessário enviar um arquivo
	
	@Test
	
	public void deveBaixarArquivo() throws IOException {
		
		byte[] image = given()
			.log().all()		
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			.statusCode(200)
			.extract().asByteArray();
		
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
				
		System.out.println(imagem.length());	
		Assert.assertThat(imagem.length(), lessThan(100000L));
				
				
	}
}
