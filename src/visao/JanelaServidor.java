package visao;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import server.Servidor;

/**
 * 		Painel de controle somente vísivel ao HOST do servidor.
 * 		É utilizado para gerenciar as conexões estabelecidas ao servidor.
 * @author Guilherme Gomes
 *
 */
public class JanelaServidor extends JFrame implements Runnable, WindowListener {

	private static final long serialVersionUID = 1L;

	// Para gerenciar alguns comandos para o HOST
	private Servidor servidor;
	
	private JList<String> listaClientes;	// Lista com os nomes e a conexão com todos os clientes
	private JButton desconectarCliente; 	// Botão para desconectar um cliente específico do servidor
	private JButton fecharServidor; 		//
	private JLabel log;				// Ficará os status do servidor mais recente
	
	// Modelo de Lista para as JList
	private DefaultListModel<String> modeloCliente;
	private JButton atualizarUsuarios;
	
	// Construtor
	public JanelaServidor( Servidor servidor )
	{
		// Constructor call must be the first statement in a constructor
		super( "Tela de Controle" );
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		
		this.servidor = servidor;
		
		// Construção dos componentes
		listaClientes = new JList<>();
		listaClientes.setBorder(new LineBorder(new Color(0, 0, 0)));
		listaClientes.setLocation(10, 11);

		// https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
		listaClientes.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		listaClientes.setLayoutOrientation( JList.VERTICAL_WRAP );
		
		modeloCliente = new DefaultListModel<String>();
		//modeloCliente.addElement( "ABCEDE" );
		//modeloCliente.addElement( "VV");
		listaClientes.setModel(modeloCliente);
		// Scroll
		//JScrollPane scrollListaClientes = new JScrollPane( listaClientes );
		
		
		desconectarCliente = new JButton( "Desconectar" );
		desconectarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if ( listaClientes.getSelectedIndex() == -1 )
					JOptionPane.showMessageDialog( null, "Selecione algum cliente.", "Aviso", JOptionPane.WARNING_MESSAGE );
				else
				{
					atualizarLog( "Fechando conexão com Cliente" , new Date().toString() );
					// Remove o cliente selecionado pelo Nome
					servidor.fecharEsteCliente( servidor.listaClientes().get( listaClientes.getSelectedValue() ) );
				}
				
			}
		});
		desconectarCliente.setLocation(253, 9);
		
		fecharServidor = new JButton( "Fechar servidor" );
		fecharServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				atualizarLog( "Desligamento" , new Date().toString() );
				
				// Fechar tudo
				servidor.fecharServidor();
				
				
			}
		});
		fecharServidor.setLocation(253, 175);
		
		//modeloLog = new DefaultListModel<>();
		//JScrollPane scrollListaLog = new JScrollPane( log );
		
		// Construção da Janela
		setSize( 400 , 280 );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		getContentPane().setLayout(null);
		
		log = new JLabel();
		log.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		log.setLocation(10, 217);
		/*
		log.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		log.setLayoutOrientation( JList.VERTICAL_WRAP );
		log.setModel( modeloLog );
		*/
		
		getContentPane().add( log );
		
		// log.setSize(width, height);
		log.setSize( 364 , 15 );
		
		// Componentes internos
		
		getContentPane().add( listaClientes );
		//getContentPane().add( scrollListaClientes );
		
		getContentPane().add( desconectarCliente );
		getContentPane().add( fecharServidor );
		//getContentPane().add( scrollListaLog );
		
		// Definição de tamanho dos componentes
		listaClientes.setSize( 233 , 187 );
		//scrollListaClientes.setSize( getContentPane().getWidth() , (int) ( 0.25 * getContentPane().getHeight() ) );

		desconectarCliente.setSize( 121 , 23 );
		fecharServidor.setSize( 121 , 23 );
		
		atualizarUsuarios = new JButton("Atualizar");
		atualizarUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Botão Atualizar Usuários
				
				atualizarListaClientes();
				atualizarLog( "Atualização de Lista de Clientes" , new Date().toString() );
				
			}
		});
		atualizarUsuarios.setBounds(253, 43, 121, 23);
		getContentPane().add(atualizarUsuarios);
		//scrollListaLog.setSize( getContentPane().getWidth() , (int) ( 0.25 * getContentPane().getHeight() ) );
		
		// Compactação e Visibilidade
		//pack();
		//setVisible( true );
		
	}
	
	@Override
	public void run() {

		System.out.println("THREAD JanelaServidor iniciado.");
		//atualizarListaClientes();
		
		do {
			atualizarListaClientes();
			
			atualizarLog( "Atualização Lista Clientes", new Date().toString() );
			
			try {
				System.out.println("UPDATE CLIENTES");
				Thread.sleep( 5000 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} while( true );
		
	}
	

	/**
	 * 
	 */
	public void atualizarListaClientes()
	{
		/*
		List<String> nomes = new ArrayList<String>();
		nomes = servidor.nomeClientes().stream().collect( Collectors.toList() );
		nomes = servidor.nomeClientes();
		*/
		//nomes.forEach( modeloCliente.addElement(  ) );
		//modeloCliente.addElement(  ) ;
		
		/*
		for (String n : nomes) {
			modeloCliente.addElement( n );
		}
		*/
		
		// Evita adicionar mais de uma vez (inúmeras vezes) o nome do mesmo cliente 
		modeloCliente.clear();
		
		servidor.nomeClientes().stream().collect( Collectors.toList() ).forEach( n -> modeloCliente.addElement( n ) );
	}
	
	/**
	 * 
	 * @param update
	 * 
	 * 
	 * @param data
	 */
	public void atualizarLog( String update , String data )
	{
		log.setText( update + " - " + data );
		
		try {
			Thread.sleep( 300 );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Quando fechar a aba, feche tudo do servidor
	/*
	private void onClose() {
		servidor.fecharConexaoClientes();
		servidor.fecharServidor();
		
		this.dispose();
	}
	*/

	// WindowListener Tutorial
	// https://docs.oracle.com/javase/7/docs/api/java/awt/event/WindowListener.html
	
	@Override
	public void windowActivated(WindowEvent e) {
		// Quando é escolhido para ser a janela em foco
	}


	@Override
	public void windowClosed(WindowEvent e) {
		// Quando é fechada
	}


	@Override
	public void windowClosing(WindowEvent e) {
		// Quando está em processo de fechamento
	
		System.out.println("Fechando JanelaServidor.");
		atualizarLog( "Fechando JanelaServidor" , new Date().toString() );
		
		servidor.fecharConexaoClientes();
		servidor.fecharServidor();
		
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
		// Quando perde o foco de janela principal
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		// Quando a janela passa de minimizada -> normal 
	}


	@Override
	public void windowIconified(WindowEvent e) {
		// Quando a janela passa de normal -> minimizada
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// Quando a janela abre ( visível ), apenas uma vez
	}
	
}
