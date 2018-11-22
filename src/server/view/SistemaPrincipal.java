package server.view;

import javax.swing.JFrame;

import server.Servidor;

public class SistemaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static Servidor servidor;
	
	public SistemaPrincipal()
	{
		
		/*
		 * JPanel aqui
		 */
	
	}

	public static void main(String[] args) throws Exception {
		
		//Arquivo a = new Arquivo( new File( "C:\\Users\\bugot\\OneDrive - Fatec Centro Paula Souza\\3º Sem\\2 - Programação Orientada a Objetos\\0 - Listas\\2 - Avaliação Prática ( P2 )\\t.txt" ) , "O" , true );

		//System.out.println( a );

		// https://pt.stackoverflow.com/questions/54012/qual-a-função-de-um-método-estático
		
		iniciarServidor();
		
		Thread.sleep( 5000 );
		
		System.out.println( servidor.listaClientes() );
		
		Thread.sleep( 5000 );
		
		System.out.println( servidor.listaClientes() );
		
		servidor.fecharConexaoClientes();
		
		servidor.fecharServidor();
		
	}
	
	public static void iniciarServidor()
	{
		servidor = new Servidor();
		
		//servidor.aceitarCliente();
		servidor.start();
		
	}
	
}
