package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import junit.framework.Assert;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

import com.thoughtworks.xstream.XStream;

public class ClienteTest {

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
	
}
