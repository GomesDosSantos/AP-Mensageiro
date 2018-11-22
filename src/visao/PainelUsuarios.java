package visao;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import server.Servidor;

public class PainelUsuarios extends JPanel implements Runnable /*, ActionListener */ {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Servidor servidor;
	private JList<String> listaUsuarios;
	private DefaultListModel<String> modelo;
	
	public PainelUsuarios( Servidor servidor )
	{
		
		this.servidor = servidor;
		
		modelo = new DefaultListModel<>();
		setLayout(null);
		
		listaUsuarios = new JList<String>();
		listaUsuarios.setBounds(0, 0, 150, 300);
		listaUsuarios.setModel(modelo);
		add(listaUsuarios);
		
	}
	
	
	/**
	 * 
	 */
	public void atualizarListaUsuarios()
	{
		servidor.nomeClientes().forEach( n -> modelo.addElement( n ) );
	}


	
	
	@Override
	public void run() {
		
		do
		{
			try
			{
				System.out.println( "THREAD PAINEL USUARIOS1" );
				atualizarListaUsuarios();
				Thread.sleep( 2300 );
			} catch ( InterruptedException e )
			{
				e.printStackTrace();
				System.out.println( "THREAD PAINEL USUARIOS" );
			}
		}
		while ( servidor.estaRodando() );
		//run();
		// 		Requisita uma atualiza��o na lista de usu�rio a cada determinado tempo
		// enquanto o servidor estiver ativo e rodando.
		// 		� poss�vel implementar um algoritmo de m�dia aritm�tica para verificar a m�dia
		// de tempo em que um novo usu�rio entra no sistema, esta m�dia se torna o tempo m�dio de
		// atualiza��o da lista de usu�rios para este cliente.
	}
	
}
