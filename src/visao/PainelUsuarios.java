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
		// 		Requisita uma atualização na lista de usuário a cada determinado tempo
		// enquanto o servidor estiver ativo e rodando.
		// 		É possível implementar um algoritmo de média aritmética para verificar a média
		// de tempo em que um novo usuário entra no sistema, esta média se torna o tempo médio de
		// atualização da lista de usuários para este cliente.
	}
	
}
