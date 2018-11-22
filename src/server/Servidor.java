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
 * 		Cria uma ponte de comunicações entre os clientes. Troca as mensagens entre os clientes.
 * 
 * @author Guilherme Gomes
 *
 */
public class Servidor extends Thread {

	private ServerSocket socket;
	private List<Conexao> clientes;
	
	
	/*
	 *  		Como viemos de um construtor, esperamos ter já alocado uma porta e estamos pronto para aceitar as conexões.
	 *  		Aceitaremos as conexões e a manipularemos via Threads.
	 *   
	 *  Fluxo-->
	 * 		Recebe conexão
	 * 		Verifica conexão(?)
	 * 		Aceita conexão
	 * 		Manipula conexão
	 * 		Recebe conexão, troca mensagem
	 * 		Envia arquivo para as conexões
	 */
	
	
	/**
	 * 		Caso seja utilizado o construtor padrão, porta padrão será utilizada <i>5010</i>.
	 *  
	 */
	public Servidor() {
		// Construtor Padrão
		
		clientes = new ArrayList<Conexao>();
		
		try {
			
			socket = new ServerSocket( 5010 );
			
		} catch (IOException e) {
			e.printStackTrace();
			// Erro na alocação da porta 5010
		}
	
		//System.out.println("OPA - Servidor inicializado na porta 5010.");
		System.out.println("Servidor inicializado na porta " + socket.getLocalPort());
		
	}
	
	/**
	 * 
	 * @param porta  Porta do servidor para a conexão
	 */
	public Servidor( int porta )
	{
		clientes = new ArrayList<Conexao>();
		
		try {
			
			socket = new ServerSocket( porta );
						
		} catch ( IOException e ) {
			e.printStackTrace();
			// Não foi possível criar o servidor | Alocar a porta ao servidor
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
	 * 		Aceita uma requisição de conexão de cliente e a aceita, adiciona a lista de clientes. 
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
			
			System.out.println( "!server Nova conexão: " + c );
			
			//clientes.add( c );
			
			// Notifica clientes do novo usuário
			notificacaoTardia( 2 , c );
			
			
		} catch (IOException e) {
			e.printStackTrace();
			// Não foi possível aceitar o cliente
		}
		
		//aceitarCliente();
	}
	
	/**
	 *     Qual a utilidade disto aqui?  
	 * @return		Um dicionário de conexões. Nome do cliente e o socket associado a ele.
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
	 * 		Percorre a lista de conexões atuais do servidor e envia uma mensagens que esteja habilidada para retransmissão.
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
			System.out.println( "Mensagem não habilitada para retransmissão." );
	}
	
	/**
	 * 
	 * 		Percorre a lista de conexões ativas do servidor e envia um arquivo que esteja habilitado para retransmissão.
	 * 
	 * @param arquivo		Arquivo a ser retransmitido aos clientes conectados.
	 */
	protected void retransmitirArquivo( Arquivo arquivo )
	{
		if ( arquivo.getRelay() )
			clientes.forEach( cliente -> {

				// Idem ao método de cima, para todos exceto quem o enviou
				if ( !cliente.getNome().equals( arquivo.getRemetente() ))
					cliente.enviarArquivo( arquivo );
				
			}
			);
		else
			System.out.println("Arquivo não habilitado para retransmissão.");
		// Quando isso acontecer, pode ser salvo só para o servidor.
		// Tanto arquivo como mensagem
	}
	
	/**
	 * 
	 * 		Efetua a troca do nome associado a uma conexão já existente, logo possibilitando a troca de nomes mesmo após 
	 * a primeira conexão ter sido realizada.
	 * 
	 * @param cliente		Conexão que terá seu nome associado alterado.
	 * @param novoNome		Novo nome para a associação.
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
	 * 	Envia uma mensagens aos clientes conectados ao servidor sobre uma conexão.
	 * 
	 * @param segundos		Tempo de espera para enviar a mensagem de notificação.
	 * @param cliente		O cliente que acabou de se conectar ao servidor.
	 */
	protected void notificacaoTardia( int segundos , Conexao cliente )
	{
		try {
			Thread.sleep( segundos * 1000 );
			
			// Notifica os clientes do novo usuário
			// É tardio, pois o cliente não entra no servidor com nome, logo após o servidor
			// Recebe a confirmação do nome e o salva nas conexões.
			//retransmitirMensagem( new Mensagem( "SERVER" , cliente.getNome() + " conectou-se ao servidor." , "" , null , new Date() , false ));
			// - a implementação a cima bloqueia mensagens com relay falso
			Mensagem m = new Mensagem( "SERVER" , cliente.getNome() + " conectou-se ao servidor." , "" , null , new Date() , false );
			clientes.forEach( c -> c.enviarMensagem( m ) );
			System.out.println("Retransmissão de notificação tardia com sucesso.");
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Notificação tardia falhou.");
		}
	}

	/**
	 * 
	 * 		Estado do servidor sobre seu <i>ServerSocket</i>
	 * 
	 * @return		Verdade se o socketServer estiver ativo e rodando, caso contrário não. 
	 */
	public boolean estaRodando()
	{
		return socket.isClosed() ? false : true ;
	}
	
	/**
	 * 
	 * 		Busca e fecha a conexão de um cliente, com um aviso da ação.
	 * 
	 * @param cliente		Cliente a ser fechado.
	 */
	public void fecharEsteCliente( Conexao cliente )
	{
		System.out.println( "Tentativa de fechar este cliente: " + cliente );
		
		cliente.enviarMensagem( new Mensagem( "AVISO" ,
				"Servidor em processo de término.\\nTodas as conexões com este cliente foram terminadas." , 
				null , null , new Date() , false) );
		
		if ( cliente.fecharCliente() )
			clientes.remove( cliente );
		
		cliente.interrupt(); // Fecha o THREAD que antendia este cliente
	}
	
	/**
	 * 		Fecha todas as conexões com os clientes conectados no momento.
	 */
	public void fecharConexaoClientes()
	{
		System.out.println( "Fechando conexão com os clientes." );
		
		for (Conexao cliente : clientes) {
			//cliente.enviarMensagem( "Servidor em processo de término.\nTodas as conexões com este cliente foram terminadas." );
			cliente.enviarMensagem( new Mensagem( "AVISO" , "Servidor em processo de término.\\nTodas as conexões com este cliente foram terminadas." , null , null , new Date() , false) );
			cliente.fecharCliente();
		}
		
		clientes.clear(); // Limpa a lista de clientes
		
	}

	/**
	 * 		Fecha o servidor e todas as conexões com os clientes.
	 */
	public void fecharServidor()
	{
		
		// Se ainda houver clientes conectados
		if ( !clientes.isEmpty() )
		{
			System.out.println( "Conexão com os cliente não foi fechada previamente.\n"
					+ "O Servidor está encerrando a conexão com os clientes agora.");
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
	 * 		Porta local em que o servidor está rodando.
	 * @return		Porta do servidor.
	 */
	public int getPorta() { return socket.getLocalPort(); }
	
}
