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

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

import com.thoughtworks.xstream.XStream;

public class CarrinhoTest {

	private HttpServer server;

	@Before
	public void startaServidor() {
		server = Servidor.inicializaServidor();
	}

	@After
	public void mataServidor() {
		server.stop();
	}

	@Test
	public void testaQueAConexaoComOServidorFunciona() {

		Client cliente = ClientBuilder.newClient();
		WebTarget target = cliente.target("http://localhost:8080");
		//passando parametros para a request testando com query param
//		String conteudo = target.path("/carrinhos").queryParam("id", 1).request().get(String.class);
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		Carrinho carrinho = ((Carrinho) new XStream().fromXML(conteudo));
		Assert.assertEquals(carrinho.getRua(), "Rua Vergueiro 3185, 8 andar");
	}
	
	@Test
	public void testaQueOPostFunciona(){
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
		carrinho.setCidade("São Bernardo do Campo");
		carrinho.setRua("Rua Helena Aparecida Secol");
		String xml = carrinho.toXML();
		
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		Response response = target.path("/carrinhos").request().post(entity);
		
		Assert.assertEquals(201, response.getStatus());
		
		String location = response.getHeaderString("Location");
		String carrinhoAdicionado = client.target(location).request().get(String.class);
		Assert.assertTrue(carrinhoAdicionado.contains("Tablet"));
	}	
	
	@Test
	public void testaRemocaoDeProdutoNoCarrinho(){
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		Response response = target.path("/carrinhos/1/produtos/6237").request().delete();
		Assert.assertEquals(200, response.getStatus());
	}
	
}
