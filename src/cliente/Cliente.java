package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import server.Mensagem;

public class Cliente {

	private String nome;
	private Socket socket;
	
	
	/*
	 *  Tenta criar uma conexão com o servidor.
	 *  Caso suceda:
	 *  	-> Pode enviar mensagens de texto; ou
	 *  	-> Pode enviar arquivos do computador para o servidor (com opção de retransmitir?).
	 */
	
	
	// GETTERs e SETTERs
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	/**
	 * 
	 * @param nome
	 * @param host
	 * @param porta
	 */
	public Cliente( String nome , String host , int porta ) {
		super();
		
		this.nome = nome;
		
		try {
			
			socket = new Socket( host, porta );
			
		} catch (IOException e) {
			e.printStackTrace();
			// Conexão recusada
		}
		
		// Envia o comando para trocar o nome no Servidor
		enviarMensagem( new Mensagem(getNome(), null, "nome", null, new Date(), false));
	}
	
	/**
	 * 
	 * @param mensagem
	 */
	public void enviarMensagem( String mensagem )
	{
		
		try {
			
			//socket.getOutputStream().write( mensagem.getBytes() );
			PrintWriter escritor = new PrintWriter( socket.getOutputStream() );
			
			escritor.write( mensagem );
			
			escritor.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			// Erro no envio da mensagem
		}
		
	}
	
	/**
	 * 
	 * @param mensagem
	 */
	public void enviarMensagem( Mensagem mensagem )
	{
		try {
			
			/*System.out.println(socket.isConnected());
			System.out.println(socket.isClosed());
			System.out.println(mensagem);*/
			
			// Define o objeto que será enviado
			ObjectOutputStream escritor = new ObjectOutputStream( socket.getOutputStream() );
			
			//socket.setSendBufferSize(  );
			
			// Serializa e manda via Socket
			escritor.writeObject( mensagem );
			
			//escritor.close();
			//  Se fechar o escritor, fecha o outputsream do socket
			// Logo não poderei mais enviar mensagem. 
			
			//System.out.println("Pelo menos aqui");
		}
		catch ( IOException e )
		{
			// Nada
			System.out.println("PELO MENOS DEU EXCEPTION");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return  Objeto lido pelo cliente.
	 */
	public Object aguardarMensagem()
	{
		Object o = null;
		
		try {
			
			/*System.out.println(socket.isConnected());
			System.out.println(socket.isClosed());*/
			
			
			ObjectInputStream leitor = new ObjectInputStream( socket.getInputStream() );
		
			o = leitor.readObject(); 
			
		}
		catch ( IOException e )
		{
			// Não deu pra ler
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			// Não deu pra ler o OBJETO - ckasse não encontrada
			e.printStackTrace();
		}
		
		return o;
	}
	
	
	
	
	/**
	 * 
	 */
	public void fecharConexao()
	{
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			// Deu erro no fechamento
		}
	}
	
	
}
