package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.loja.modelo.Projeto;

public class ProjetoPostTest {
	
	private HttpServer server;
	
	@Before
	public void startaServidor(){
		server = Servidor.inicializaServidor();
	}
	
	@After
	public void mataServidor(){
		server.stop();
	}

	@Test
	public void testaQuePostFunciona(){
		
		Projeto projeto = new Projeto("Test", 10L, 2015);
		String xml = projeto.toXML();
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		Response response = target.path("/projetos").request().post(entity);
		
		Assert.assertEquals(201, response.getStatus());
		
	}
	
}
