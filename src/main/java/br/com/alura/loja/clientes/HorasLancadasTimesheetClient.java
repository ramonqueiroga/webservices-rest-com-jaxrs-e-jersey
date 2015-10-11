package br.com.alura.loja.clientes;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class HorasLancadasTimesheetClient {

	public static void main(String[] args) {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/timesheet");
		
		String conteudo = target.path("/usuario/usuariosService").request().get(String.class);
		
		System.out.println(conteudo);
		
	}
	
}
