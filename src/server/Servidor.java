package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * 		Cria uma ponte de comunica��es entre os clientes. Troca as mensagens entre os clientes.
 * 
 * @author Guilherme Gomes
 *
 */
public class Servidor extends Thread {

	private ServerSocket socket;
	private List<Conexao> clientes;
	
	
	/*
	 *  		Como viemos de um construtor, esperamos ter j� alocado uma porta e estamos pronto para aceitar as conex�es.
	 *  		Aceitaremos as conex�es e a manipularemos via Threads.
	 *   
	 *  Fluxo-->
	 * 		Recebe conex�o
	 * 		Verifica conex�o(?)
	 * 		Aceita conex�o
	 * 		Manipula conex�o
	 * 		Recebe conex�o, troca mensagem
	 * 		Envia arquivo para as conex�es
	 */
	
	
	/**
	 * 		Caso seja utilizado o construtor padr�o, porta padr�o ser� utilizada <i>5010</i>.
	 *  
	 */
	public Servidor() {
		// Construtor Padr�o
		
		clientes = new ArrayList<Conexao>();
		
		try {
			
			socket = new ServerSocket( 5010 );
			
		} catch (IOException e) {
			e.printStackTrace();
			// Erro na aloca��o da porta 5010
		}
	
		//System.out.println("OPA - Servidor inicializado na porta 5010.");
		System.out.println("Servidor inicializado na porta " + socket.getLocalPort());
		
	}
	
	/**
	 * 
	 * @param porta  Porta do servidor para a conex�o
	 */
	public Servidor( int porta )
	{
		clientes = new ArrayList<Conexao>();
		
		try {
			
			socket = new ServerSocket( porta );
						
		} catch ( IOException e ) {
			e.printStackTrace();
			// N�o foi poss�vel criar o servidor | Alocar a porta ao servidor
		}
	}
	
	
	@Override
	public void run() {
		//super.run();
		do
		{
			aceitarCliente();
		}
		while( true );
	}
	
	/**
	 * 		Aceita uma requisi��o de conex�o de cliente e a aceita, adiciona a lista de clientes. 
	 */
	public void aceitarCliente()
	{
		try {

			//socket.accept();
			
			// Aceita e adiciona a minha lista de CLIENTES
			//clientes.add( new Conexao( socket.accept() ) );
			
			Conexao c = new Conexao( socket.accept() , this );

			clientes.add( c );
			
			c.start();
			
			System.out.println( "!server Nova conex�o: " + c );
			
			//clientes.add( c );
			
			// Notifica clientes do novo usu�rio
			notificacaoTardia( 2 , c );
			
			
		} catch (IOException e) {
			e.printStackTrace();
			// N�o foi poss�vel aceitar o cliente
		}
		
		//aceitarCliente();
	}
	
	/**
	 *     Qual a utilidade disto aqui?  
	 * @return		Um dicion�rio de conex�es. Nome do cliente e o socket associado a ele.
	 */
	public Map< String , Conexao > listaClientes()
	{
		
		Map< String , Conexao > lista = new HashMap<>();
		
		clientes.forEach( x -> lista.put( x.getNome() , x ) );
		
		//return new HashMap<>();
		return lista;
	}
	
	/**
	 * 
	 * @return		O Nome de todos os clientes em forma de Lista.
	 */
	public List<String> nomeClientes()
	{
		List<String> i = new ArrayList<String>();
		
		clientes.forEach( c -> i.add( c.getNome() ) );
		
		///// ->>> clientes.stream().filter( nome -> nome.getNome() != null ).collect( Collections.addAll(c, elements) );
		
		return i;
	}
	
	/**
	 * 
	 * @return		O Nome de todos os clientes em forma de array de String
	 */
	public String[] nomeDosClientes()
	{
		return (String[]) clientes.stream().filter( cliente -> cliente.getNome() != null ).collect( Collectors.toList() ).toArray();
	}
	
	/**
	 * 
	 * 		Percorre a lista de conex�es atuais do servidor e envia uma mensagens que esteja habilidada para retransmiss�o.
	 * 
	 * @param mensagem		Mensagem a ser retransmitida para os clientes.
	 */
	protected void retransmitirMensagem( Mensagem mensagem )
	{
		if ( mensagem.getRelay() )
			clientes.forEach( cliente -> {
				
				// Retransmite a mensagem para todos os clientes, exceto quem enviou
				
				//System.out.println(cliente.getNome());
				//System.out.println(mensagem.getRementente());
				
				if ( !cliente.getNome().trim().equals( mensagem.getRementente().trim() ) )
						cliente.enviarMensagem( mensagem );
			}
			);
		else
			System.out.println( "Mensagem n�o habilitada para retransmiss�o." );
	}
	
	/**
	 * 
	 * 		Percorre a lista de conex�es ativas do servidor e envia um arquivo que esteja habilitado para retransmiss�o.
	 * 
	 * @param arquivo		Arquivo a ser retransmitido aos clientes conectados.
	 */
	protected void retransmitirArquivo( Arquivo arquivo )
	{
		if ( arquivo.getRelay() )
			clientes.forEach( cliente -> {

				// Idem ao m�todo de cima, para todos exceto quem o enviou
				if ( !cliente.getNome().equals( arquivo.getRemetente() ))
					cliente.enviarArquivo( arquivo );
				
			}
			);
		else
			System.out.println("Arquivo n�o habilitado para retransmiss�o.");
		// Quando isso acontecer, pode ser salvo s� para o servidor.
		// Tanto arquivo como mensagem
	}
	
	/**
	 * 
	 * 		Efetua a troca do nome associado a uma conex�o j� existente, logo possibilitando a troca de nomes mesmo ap�s 
	 * a primeira conex�o ter sido realizada.
	 * 
	 * @param cliente		Conex�o que ter� seu nome associado alterado.
	 * @param novoNome		Novo nome para a associa��o.
	 */
	protected void trocarNomeCliente( Conexao cliente , String novoNome )
	{
		
		/*
		clientes.forEach( c -> {
			
			if ( c.equals( cliente ) )
			{
				c.setNome( cliente.getNome() );
			}
			
		})
		;*/
		
		int i = clientes.indexOf( cliente );
		//clientes.get( i ).setNome( cliente.getNome() );
		clientes.get( i ).setNome( novoNome );
		
	}
	
	/**
	 * 
	 * 	Envia uma mensagens aos clientes conectados ao servidor sobre uma conex�o.
	 * 
	 * @param segundos		Tempo de espera para enviar a mensagem de notifica��o.
	 * @param cliente		O cliente que acabou de se conectar ao servidor.
	 */
	protected void notificacaoTardia( int segundos , Conexao cliente )
	{
		try {
			Thread.sleep( segundos * 1000 );
			
			// Notifica os clientes do novo usu�rio
			// � tardio, pois o cliente n�o entra no servidor com nome, logo ap�s o servidor
			// Recebe a confirma��o do nome e o salva nas conex�es.
			//retransmitirMensagem( new Mensagem( "SERVER" , cliente.getNome() + " conectou-se ao servidor." , "" , null , new Date() , false ));
			// - a implementa��o a cima bloqueia mensagens com relay falso
			Mensagem m = new Mensagem( "SERVER" , cliente.getNome() + " conectou-se ao servidor." , "" , null , new Date() , false );
			clientes.forEach( c -> c.enviarMensagem( m ) );
			System.out.println("Retransmiss�o de notifica��o tardia com sucesso.");
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Notifica��o tardia falhou.");
		}
	}

	/**
	 * 
	 * 		Estado do servidor sobre seu <i>ServerSocket</i>
	 * 
	 * @return		Verdade se o socketServer estiver ativo e rodando, caso contr�rio n�o. 
	 */
	public boolean estaRodando()
	{
		return socket.isClosed() ? false : true ;
	}
	
	/**
	 * 
	 * 		Busca e fecha a conex�o de um cliente, com um aviso da a��o.
	 * 
	 * @param cliente		Cliente a ser fechado.
	 */
	public void fecharEsteCliente( Conexao cliente )
	{
		System.out.println( "Tentativa de fechar este cliente: " + cliente );
		
		cliente.enviarMensagem( new Mensagem( "AVISO" ,
				"Servidor em processo de t�rmino.\\nTodas as conex�es com este cliente foram terminadas." , 
				null , null , new Date() , false) );
		
		if ( cliente.fecharCliente() )
			clientes.remove( cliente );
		
		cliente.interrupt(); // Fecha o THREAD que antendia este cliente
	}
	
	/**
	 * 		Fecha todas as conex�es com os clientes conectados no momento.
	 */
	public void fecharConexaoClientes()
	{
		System.out.println( "Fechando conex�o com os clientes." );
		
		for (Conexao cliente : clientes) {
			//cliente.enviarMensagem( "Servidor em processo de t�rmino.\nTodas as conex�es com este cliente foram terminadas." );
			cliente.enviarMensagem( new Mensagem( "AVISO" , "Servidor em processo de t�rmino.\\nTodas as conex�es com este cliente foram terminadas." , null , null , new Date() , false) );
			cliente.fecharCliente();
		}
		
		clientes.clear(); // Limpa a lista de clientes
		
	}

	/**
	 * 		Fecha o servidor e todas as conex�es com os clientes.
	 */
	public void fecharServidor()
	{
		
		// Se ainda houver clientes conectados
		if ( !clientes.isEmpty() )
		{
			System.out.println( "Conex�o com os cliente n�o foi fechada previamente.\n"
					+ "O Servidor est� encerrando a conex�o com os clientes agora.");
			clientes.forEach( cliente -> cliente.fecharCliente() );
		}
		
		try {
			
			//this.getThreadGroup().destroy(); // 
			//this.getThreadGroup().interrupt();
			socket.close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println( "Fechando servidor." );
		}
		
	}
	
	/**
	 * 		Porta local em que o servidor est� rodando.
	 * @return		Porta do servidor.
	 */
	public int getPorta() { return socket.getLocalPort(); }
	
}
