# AP-Mensageiro

<img src="https://img.shields.io/github/license/GomesDosSantos/AP-Mensageiro.svg"></img>

  Sistema de troca de mensagens e arquivos via Socket de forma bidirecional, ou seja, tanto o servidor como o
cliente podem se comunicar através das mensagens ou do envio de arquivo em uma interface gráfica.

<h2>O Sistema</h2>

  Sistema desenvolvido em <i>Java</i> com a comunicação sendo feita através de <i>Sockets</i> e envio de objetos padronizados.
  
  O Servidor é capaz de conduzir inúmeras conexões simultâneamente e retransmitir as mensagens que recebe para todos os clientes
que estiverem conectados no momentos, além de, se necessário, fechar a conexão com algum cliente específico ou não.

  O Cliente é capaz de se conectar ao servidor em rede, por utilizar-se do <i>Socket</i>, enviando mensagens e arquivos de forma
padronizada pelas classes, pode também alterar seu nome globalmente.
