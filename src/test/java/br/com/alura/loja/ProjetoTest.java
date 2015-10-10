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

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Projeto;

public class ProjetoTest {

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
	public void testaQueProjetoResourceFunciona(){
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		Projeto projeto = ((Projeto) new XStream().fromXML(conteudo));
		Assert.assertEquals("Minha loja", projeto.getNome());
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
		
		String location = response.getHeaderString("Location");
		String projetoAdicionado = client.target(location).request().get(String.class);
		
		Assert.assertTrue(projetoAdicionado.contains("Test"));
	}	
	
	@Test
	public void testaQueRemoverProjetoFunciona(){
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		Response response = target.path("/projetos/remove/1").request().delete();
		Assert.assertEquals(200, response.getStatus());
	}
	
}
