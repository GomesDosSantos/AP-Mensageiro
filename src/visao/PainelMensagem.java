package visao;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cliente.Cliente;
import server.Arquivo;
import server.Mensagem;

/**
 * 		Gerencia o envio de mensagens e arquivos por meio deste cliente, além de mostrar as mensagems recebidas.
 * 
 * @author Guilherme Gomes
 *
 */
public class PainelMensagem extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Cliente
	private Cliente cliente;
	
	// Botões de cliente
	private JList<String> mensagens;
	private DefaultListModel<String> mensagemModelo;

	private JCheckBox salvarPorNome;
	
	private JTextField boxMensagem;
	private JButton btnEnviar;
	private JButton enviarArquivo;
	private JButton trocarNome;
	
	/**
	 * 
	 */
	public PainelMensagem( Cliente cliente )
	{
		/*
		 *  	Aqui é criado a instância deste cliente, que envia mensegens e itens.
		 */
		this.cliente = cliente;

		//
		/*
		mensagens = new JList<String>();
		mensagens.setBorder(new LineBorder(Color.BLACK));
		mensagens.setBounds(426, 239, -401, -224);
		mensagemModelo = new DefaultListModel<String>();
		*/
		boxMensagem = new JTextField( "" );
		boxMensagem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( boxMensagem.getText().trim().length() > 0 )
				{
					Mensagem m = new Mensagem( cliente.getNome() , boxMensagem.getText() , "" , null , new Date() , true );
					adicionarMensagem( m );
					cliente.enviarMensagem( m );
					
					boxMensagem.setText( "" );
				}
			}
		});
		boxMensagem.setBounds(22, 254, 339, 33);
		boxMensagem.setEnabled( false );
		
		btnEnviar = new JButton();
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				if ( boxMensagem.getText().trim().length() > 0 )
				{
					Mensagem m = new Mensagem( cliente.getNome() , boxMensagem.getText() , "" , null , new Date() , true );
					adicionarMensagem( m );
					cliente.enviarMensagem( m );
					
					boxMensagem.setText( "" );
				}
				
				
			}
		});
		btnEnviar.setEnabled(false);
		btnEnviar.setText("Enviar");
		btnEnviar.setBounds(371, 254, 69, 33);
		setLayout(null);
		
		salvarPorNome = new JCheckBox( "Salvar Por Remetente" );
		salvarPorNome.setBackground(Color.WHITE);
		salvarPorNome.setEnabled( false );
		salvarPorNome.setSelected(true);
		salvarPorNome.setBounds(22, 287, 150, 23);
		
		mensagemModelo = new DefaultListModel<String>();
		
		mensagens = new JList<String>();
		mensagens.setBounds(22, 14, 401, 229);
		mensagens.setModel( mensagemModelo );
		
		add( salvarPorNome );
		add( mensagens );
		add( boxMensagem );
		add( btnEnviar );
		
		trocarNome = new JButton("Trocar de Nome");
		trocarNome.setEnabled( false );
		trocarNome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Trocar de Nome
				
				String n = "";
				
				do
				{
					n = JOptionPane.showInputDialog(null, "Digite o novo nome", "Troca de Nome", JOptionPane.WARNING_MESSAGE);
					n.trim();
				} while ( n.trim().isEmpty() || n == null );
				
				cliente.enviarMensagem( new Mensagem( n , null, "nome", null, new Date(), false) );
				
				// Para ser um sucesso, é necessário trocar todos os nomes
				cliente.setNome( n );
				
			}
		});
		trocarNome.setBounds(241, 287, 120, 23);
		add(trocarNome);
		
		enviarArquivo = new JButton("");
		enviarArquivo.setToolTipText("Enviar Arquivo");
		enviarArquivo.setIcon(new ImageIcon(PainelMensagem.class.getResource("/icon/icon-arquivo.jpg")));
		enviarArquivo.setEnabled( false );
		enviarArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Enviar arquivo
				
				JFileChooser janelaArquivo = new JFileChooser();
				
				janelaArquivo.setFileSelectionMode( JFileChooser.FILES_ONLY );
				janelaArquivo.setDialogTitle( "Escolha o Arquivo a enviar" );
				
				janelaArquivo.showDialog(null, "Selecionar");
				
				if ( janelaArquivo.getSelectedFile() != null )
				{
					Arquivo arquivo = new Arquivo( janelaArquivo.getSelectedFile() , cliente.getNome() , true );
					//						new Mensagem(rementente, mensagem, comando, arquivo, data, relay)
					cliente.enviarMensagem( new Mensagem( cliente.getNome() , "" , "" , arquivo , new Date() , true ));
					
					mensagemModelo.addElement( cliente.getNome() + " enviou o arquivo: " + arquivo.getNome() );
					
				}
				else
				{
					JOptionPane.showMessageDialog( null , "É necessário selecionar um arquivo!", "Aviso", JOptionPane.ERROR_MESSAGE );
				}
				
				
			}
		});
		enviarArquivo.setBounds(371, 287, 69, 23);
		add(enviarArquivo);
		
		
		if ( cliente != null )
			atualizarCamposDisponiveis();
		
	}
	
	/**
	 *  Atualiza os campos do cliente.
	 */
	public void atualizarCamposDisponiveis()
	{
		boxMensagem.setEnabled( true );
		btnEnviar.setEnabled( true );
		salvarPorNome.setEnabled( true );
		trocarNome.setEnabled( true );
		enviarArquivo.setEnabled( true );
	}
	
	/**
	 * 
	 * @param mensagem
	 */
	public void adicionarMensagem( Mensagem mensagem )
	{
		
		if ( mensagem.getArquivo() != null )
		{
			// Recebeu Arquivo
			
			// Padrão de caminho salvo é: C:\\users\\u\\Desktop\\MeuNome\\{opção de remetente\\} --- arquivos
			try
			{
				String caminho = System.getProperty("user.home") + "\\Desktop\\" + cliente.getNome() + "\\" + ( ( salvarPorNome.isSelected() ) ? mensagem.getRementente() + "\\" : "" ) + mensagem.getArquivo().getNome();

				// Cria o diretório para os arquivo, se não dá erro
				new File( System.getProperty("user.home") + "\\Desktop\\" + cliente.getNome() + "\\" + ( ( salvarPorNome.isSelected() ) ? mensagem.getRementente() + "\\" : "" ) ).mkdirs();
				/*
				File f = new File( System.getProperty("user.home") + "\\Desktop\\" + cliente.getNome() + "\\" + ( ( salvarPorNome.isSelected() ) ? mensagem.getRementente() + "\\" : "" ) );
				f.mkdirs();
				*/
				mensagemModelo.addElement( mensagem.getRementente() + " enviou o arquivo: " + mensagem.getArquivo().getNome() );
				
				//ObjectOutputStream escritor = new ObjectOutputStream( new FileOutputStream( caminho ) );
	
				FileOutputStream escritor = new FileOutputStream( caminho );
				
				//escritor.writeObject( mensagem.getArquivo().getArquivo() );
				escritor.write( mensagem.getArquivo().emBytes() );
				escritor.close();
				
			}
			catch ( IOException e )
			{
				// Da erro SE o diretório não existe, então temos que evitar isso
				e.printStackTrace();
			}
		}
		else
		{
			//  O Servidor não retransmite mensagens inválidas, então podemos assumir que
			// todas as mensagens que chegarão nos clientes possuem informção valida.
			mensagemModelo.addElement( mensagem.getRementente() + ": " + mensagem.getMensagem() );
		}
		
	}
	
	//// THREAD do recebimento de atualizações
	@Override
	public void run() {

		/*
		 *  Gerencia o recebimento de mensagens.
		 */

		do {
			
			Object o = cliente.aguardarMensagem();

			/*
			System.out.println(o);
			
			if ( o != null )
				System.out.println( "Mensagem recebida" );
			else
				System.out.println("Mensagem recebida também");
			*/
			
			if ( o instanceof Mensagem )
			{
				//System.out.println( "MENSAGEM " + (Mensagem) o );
				// Tratar a mensagem
				Mensagem m = (Mensagem) o;
				if ( m.getRelay() )
				{
					adicionarMensagem( (Mensagem) o );
				}
				else
				{
					// Os clientes podem enviar mensagem com RELAY false, mas nunca recebem de outro cliente
					// Então, caso receba, significa que a mensagem é do próprio SERVIDOR, logo, uma notificação ou aviso
					if ( m.getRementente().equals("AVISO") )
						JOptionPane.showMessageDialog(null, m.getMensagem(), m.getRementente(), JOptionPane.WARNING_MESSAGE);
					else
						adicionarMensagem( m );
				}
			}
			else if ( o instanceof Arquivo )
			{
				// Recebeu arquivo
				Arquivo a = (Arquivo) o;
				
				if ( a.getRelay() )
				{
					adicionarMensagem( new Mensagem(a.getRemetente() , null , "" , a , new Date() , true ) );
				}
				else
				{
					JOptionPane.showMessageDialog(null, "AQUI - Recebido ARQUIVO");
				}
				
			}
			else
			{
				System.out.println( "OBJETO Recebido: " + o);
				try {
					Thread.sleep( 1000 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		while ( true ) ;
		
	}

}
