package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 
 * ????????????
 * 
 * @author Guilherme Gomes
 *
 */
public class Conexao extends Thread {

	private Socket cliente;
	private String nome;
	
	private Servidor servidor;

	
	/**
	 * 
	 * ????????????????
	 * 
	 * @param cliente Socket que intermedia a conex�o entre o servidor e o cliente.
	 * @param servidor Qual o servidor que controla esta conexao
	 */
	public Conexao( Socket cliente , Servidor servidor ) {
		this.cliente = cliente;
		this.servidor = servidor;
	}
	
	// GETTERs e SETTERs
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public void run() {
		super.run();
		do {
			// Espera entrada dos clientes aqui

			try {
				/*
				 * String b = String.valueOf( cliente.getInputStream().read() );
				 * 
				 * System.out.println( cliente.getInetAddress().getHostAddress() + ":" + b);
				 * System.out.println( cliente.getInetAddress().getHostName() + ":" + b);
				 */
				/*
				 * InputStreamReader leitordeFluxo = new InputStreamReader(
				 * cliente.getInputStream() );
				 * 
				 * BufferedReader leitor = new BufferedReader( leitordeFluxo );
				 * 
				 * System.out.println( leitor.readLine() );
				 * 
				 * leitor.close();
				 */
				
				// Nescessariamente, toda a comunica��o ser� feita por envio de objetos, logo
				
				ObjectInputStream leitor = new ObjectInputStream( cliente.getInputStream() );

				Object o = leitor.readObject();

				if ( o instanceof Mensagem )
				{
					System.out.println( (Mensagem) o );
					
					// � uma mensagem padronizada, ent�o vamos verificar o conte�do
					
					Mensagem mensagem = (Mensagem) o;
					
					// Geralmente, a troca de nome ocorre apenas uma vez quando o cliente se conecta ao servidor
					if ( mensagem.getComando().equals( "nome" ) && mensagem.getRelay() == false )
					{
						servidor.trocarNomeCliente( this , mensagem.getRementente() );
					}
					else
					{
						//servidor.retransmitirMensagem(mensagem);
						
						/* Est� aqui porque o servidor pode bloquear mensagens que por algum motivo
						 passaram do teste inicial de valida��o ou possuem algum dado inv�lido n�o
						 necess�rio para o servidor nem para o cliente.
						*/
						
						// Cliente enviou algum arquivo
						if ( mensagem.getArquivo() != null )
						{
							servidor.retransmitirArquivo( mensagem.getArquivo() );
						}
						// Se a mensagem n�o estiver vazia
						else if ( !mensagem.getMensagem().isEmpty() )
						{
							//System.out.println( mensagem.getMensagem() );
							servidor.retransmitirMensagem( mensagem );
						}
					}
					
				}
				else
				{
					System.out.println( "Al�! CONEX�O" + o );
				}
				
				/*
				if ( o instanceof Mensagem )
				{
					servidor.retransmitirMensagem( (Mensagem) o ); 
				}
				else if ( o instanceof Arquivo )
				{
					servidor.retransmitirArquivo( (Arquivo) o );
				}
				*/
				//Thread.sleep( 100 );
				
			} catch (IOException e) {
				// Quando o cliente � for�ado a ser desconectado
				this.interrupt();
				System.out.println( nome + " n�o est� mais dispon�vel." );
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				this.interrupt();
				e.printStackTrace();
			}

		} while (true);
	}

	public boolean fecharCliente() {

		boolean status = true;

		try {
			this.cliente.close();
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}

		return status;
	}

	public void enviarMensagem(String mensagem) {
		try {
			cliente.getOutputStream().write(mensagem.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			// Algum erro para enviar a mensagem?
		}
	}

	/**
	 * 
	 * @param mensagem
	 */
	public void enviarMensagem( Mensagem mensagem )
	{
		
		try {
			
			ObjectOutputStream saida = new ObjectOutputStream( cliente.getOutputStream() );
			
			saida.writeObject( mensagem );
			
		} catch (IOException e) {
			// Erro no envio da mensagem
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param arquivo
	 */
	public void enviarArquivo( Arquivo arquivo )
	{
		try {
			
			ObjectOutputStream saida = new ObjectOutputStream( cliente.getOutputStream() );
			
			saida.writeObject( arquivo );
			
		} catch (IOException e) {
			//e.printStackTrace();
			// erro - Socket closed � uma das possibilidades
			System.out.println("FECHANDO CLIENTE");
			servidor.fecharEsteCliente( this );
		}
	}
	
	@Override
	public String toString() {
		return nome + ", " + cliente;
	}


}
