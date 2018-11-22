package visao;


import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cliente.Cliente;
import server.Servidor;

/**
 * 
 * @author Guilherme Gomes
 *
 */
public class Janela {

	private static String nomeCliente, ip;
	private static boolean opcao;
	private static int porta;
	
	private static Servidor servidor;
	private static Cliente cliente;
	
	public static void main(String[] args) throws InterruptedException {
		
		//System.out.println(new Date()); 			// Sat Nov 10 23:27:24 BRST 2018 
		//System.out.println(new Date().getTime()); // 151381...
		
		// Pega o nome do cliente
		do {
			nomeCliente = JOptionPane.showInputDialog( null , "Qual o seu nome?" , "", JOptionPane.INFORMATION_MESSAGE );
		} while ( nomeCliente == null || nomeCliente.length() <= 0 );
		
		// Independente do resultado do JOptionPane ser 0 ou 1, é sempre compreendido pelo parseBoolean o FALSE
		//opcao = Boolean.parseBoolean( String.valueOf( JOptionPane.showConfirmDialog(null, "Gostaria de ser o Servidor?", "Opção Inicial", 0, 1) ) );
		//opcao = Boolean.parseBoolean( String.valueOf( 0 ) );
		//System.out.println(opcao);
		//System.out.println( JOptionPane.showConfirmDialog(null, "Gostaria de ser o Servidor?", "Opção Inicial", 0, 1) );
		// Então
		
		int o = JOptionPane.showConfirmDialog(null, "Gostaria de ser o Servidor?", "Opção Inicial", 0, 1);
		
		if ( o == 0 ) // SIM
			opcao = true;
		else
			opcao = false;
		
		if ( opcao )
		{
			criarServidor();
			JanelaServidor ps = new JanelaServidor( servidor );
			ps.setVisible( true );
			criarCliente();
			
			Thread server = new Thread( ps );
			server.start();
		}
		else // Não será o Servidor
		{
			try {
				ip = JOptionPane.showInputDialog( "Digite o ip do servidor:" , "localhost" );
				porta = Integer.parseInt( JOptionPane.showInputDialog( "Digite a porta para conectar" , "5010" ) );
			} 
			catch ( Exception e )
			{
				ip = "localhost";
				porta = 5010;
			}
			
			conectarServidorComoCliente();
		}
		
		JFrame quadro = new JFrame( "Janela | " + nomeCliente );
		quadro.setIconImage(Toolkit.getDefaultToolkit().getImage( Janela.class.getResource("/icon/icon.jpg") ));
		quadro.getContentPane().setBackground(Color.WHITE);

		
		// https://www.javatpoint.com/downcasting-with-instanceof-operator
		
		quadro.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		quadro.setSize( 636 , 380 );
		
		PainelMensagem pm = new PainelMensagem( cliente );
		pm.setBackground(Color.WHITE);
		//PainelMensagem pm = new PainelMensagem( new Conexao( servidor , cliente ) );
		//pm.setBounds(x, y, 200 , quadro.getHeight() );
		pm.setBounds( 10 , 11, 441 , 319 );
		
		PainelUsuarios pu = new PainelUsuarios( servidor );
		pu.setBounds(461, 11, 152, 319);
		quadro.getContentPane().setLayout(null);
		
		//quadro.add( new PainelMensagem() );
		//quadro.add( new PainelUsuarios() );

		quadro.getContentPane().add( pm );
		quadro.getContentPane().add( pu );
		
		quadro.setVisible( true );
		
		Thread p = new Thread( pm );
		
		p.start();
		
		//new Thread( pu ).start();

		p.join();
		
	}
	
	
	/**
	 * 
	 */
	public static void criarServidor()
	{
		// Porta padrão 5010
		servidor = new Servidor();
		servidor.start();
	}
	
	/**
	 * 
	 */
	public static void criarCliente()
	{
		// Cria o cliente que envia mensagens e arquivos deste terminal
		cliente = new Cliente( nomeCliente , "localhost" , 5010 );
		//cliente = new Cliente( nomeCliente , "localhost" , servidor.getPorta() );
	}
	
	/**
	 * 
	 */
	public static void conectarServidorComoCliente()
	{
		//
		cliente = new Cliente( nomeCliente , ip , porta );
	}

}
