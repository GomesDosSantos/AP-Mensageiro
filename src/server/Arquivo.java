package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * 
 * 		Classe que encorporará o arquivo e simbolizá-lo no sistema. Manipulável e transferível via Socket.
 * 
 * @author Guilherme Gomes
 *
 */
public class Arquivo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nome;  // Nome do Arquivo junto da extensão
	private String remetente; // Quem enviou
	private long tamanho; // Tamanho em bytes
	private boolean relay; // Opção de retransmissão
	
	private File arquivo; // Caminho para o arquivo localmente
	private byte[] arquivoB; // Arquivo em forma de bytes

	/**
	 * 
	 * 	Cria um símbolo para o sistema que o representa para o servidor e os clientes. 
	 * 
	 * @param arquivo   Arquivo para ser simbolizado por este objeto.
	 */
	public Arquivo( File arquivo , String remetente , boolean relay ) {
		//this.arquivo = arquivo;
		// Já que é possível alterar o arquivo que um objeto desta classe simbolza
		// É mais fácil chamar o setArquivo sempre que alterar o arquivo
		// Para reaproveitar o código implementado lá que seria o mesmo aqui
		setArquivo( arquivo );
		this.remetente = remetente;
		this.relay = relay;
	}	
	
	// GETTERs e SETTERs
	// Só tem get, pois um arquivo será apenas modificado quando a qual ele simboliza altera e não por vontade externa.
	public String getNome() {
		return nome;
	}
	public long getTamanho() {
		return tamanho;
	}
	
	public String getRemetente() { return remetente; }

	public File getArquivo() {
		return arquivo;
	}

	public boolean getRelay() { return relay; }
	
	/**
	 * 		Modifica o arquivo que este objeto simboliza e atualiza os dados do objeto.
	 * @param arquivo 	Novo arquivo para ser manipulado por este objeto. 
	 */
	public void setArquivo(File arquivo) {
		
		nome = arquivo.getName();
		
		//tamanho = arquivo.getTotalSpace(); // Isso retornar o espaço total do disco que o programa está sendo executado
		tamanho = arquivo.length();
		
		this.arquivo = arquivo;
		
		
		transformarEmBytes();
		
		/*
		int i = arquivo.getName().lastIndexOf( '.' );
		if ( i > 0 )
		{
			nome = arquivo.getName();
			nome.
		}
			Só será possível enviar arquivos, então o código acima é desnecessário.
			Por isso:::
			
			abc.txt
		*/
		/*
		 * Pra falar a verdade, tudo isso aqui é desnecessário.
		 * Podemos só guardar o nome que já vai com a extensão.
		 * E salvar nos terminais juntos com a extensão no caminho desejado.
		nome = arquivo.getName().substring( 0 , arquivo.getName().lastIndexOf( '.' ) ); // Pega abc 
		extensao = arquivo.getName().substring( arquivo.getName().lastIndexOf('.') );   // Pega txt
		*/
		
	}
	
	
	@Override
	public String toString() {
		return "Dados do Arquivo: " + nome + ", " + tamanho + " B." +
	"\nCaminho de Origem: " + arquivo.getPath() ;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] emBytes()
	{
		return arquivoB;
	}
	
	/**
	 * 
	 */
	public void transformarEmBytes()
	{

		FileInputStream entrada;
		try {
			arquivoB = new byte[ (int) arquivo.length() ];
			entrada = new FileInputStream( arquivo );
			entrada.read( arquivoB );
			entrada.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			// Não deu pra escrever ou ler o arquivo
			e.printStackTrace();
		}
		
	}
	
}
