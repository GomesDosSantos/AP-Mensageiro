package server;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Classe que serve como padrão de envio de mensagens entres os clientes e o
 * servidor. Mansagens de texto são <i>serializadas</i> e enviadas via
 * <i>Socket</i> de cliente para servidor ou vice-versa.
 * 
 * 
 * @author Guilherme Gomes
 *
 */
public class Mensagem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String rementente, mensagem;
	private Date data;
	private boolean relay; // Opção de retransmissão para todos os clientes ou não
	private String comando;
	private Arquivo arquivo;
	
	/**
	 * 
	 * Cria um objeto padronizado de mensagem pronto para a retransmissão para clientes e o servidor.
	 * 
	 * @param rementente 	Quem enviou esta mensagem. ( HOST ou C1, C2... )
	 * @param mensagem   	Conteúdo da mensagem. ( Texto )
	 * @param comando		Comando específico para redefinição do cliente. Exemplo: Troca de nome global.
	 * @param arquivo		Arquivo a ser enviado para o servidor.
	 * @param data			Data da mensagem, definida pelo horário local do computador.
	 * @param relay			Se a mensagem pode ser retransmitida para todos os clientes conectados ao servidor.
	 */
	public Mensagem(String rementente, String mensagem, String comando, Arquivo arquivo, Date data, boolean relay) {
		this.rementente = rementente;
		this.mensagem = mensagem;
		//this.comando = comando;
		setComando( comando );
		this.arquivo = arquivo;
		this.data = data;
		this.relay = relay;
	}

	// SETTERs e GETTERs

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getRementente() {
		return rementente;
	}

	public Date getData() {
		return data;
	}

	public boolean getRelay() { return relay; }
	public void setRelay( boolean relay ) { this.relay = relay; }

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = (comando == null) ? "" : comando;
		
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	
	@Override
	public String toString() {
		return "Remetente: " + getRementente() + " em: " + getData();
	}
	
}
