package cliente.view;

import java.util.Date;

import javax.swing.JFrame;

import cliente.Cliente;
import server.Mensagem;

public class ClientePrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private static Cliente conexao;
	

	public static void main(String[] args) {
		// PARA TESTAR O SERVIDOR E CLIENTE DE FORMA R¡PIDA
		
		/*
		conexao = new Cliente( "Guilherme" , "localhost" , 5010 );
		
		//conexao.setNome( "Guilherme" );
		
		System.out.println(conexao.getNome());
		
		Mensagem m = new Mensagem(conexao.getNome(), "AlÙ!", new Date(), true);
		
		//conexao.enviarMensagem( "Al‘?! a" );
		conexao.enviarMensagem( m );
		
		conexao.fecharConexao();
		*/
		
		conexao = new Cliente( "Guilherme" , "localhost" , 5010 );
		
		conexao.enviarMensagem( new Mensagem( conexao.getNome(), "Al‘!", "nome", null, new Date(), false) );
		
		conexao.enviarMensagem( new Mensagem( conexao.getNome(), "Al‘!", "", null, new Date(), true) );
		
	}
	
	
}
