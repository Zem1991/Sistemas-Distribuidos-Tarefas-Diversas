/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comunicacao;
import DataBase.FuncoesDB;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import multicastpeertransacoes.IG_MAIN;
import multicastpeertransacoes.Main;
/**
 *
 * @author Klaus & Felipe
 */
public class OperacoesCom 
{
    static boolean jaMinerou = false;
    static int contadorMineracao = 0;
    /**
     * Função que é chamada por quem se Desconectou. 
     * Tenta recuperar as informações à partir do Log do Recebedor ( que é o mais completo )
     * Se o Recebedor foi quem se Desconectou, o Default será Cancelar tudo.
     * @param idPeerDesconectado
     * @param status 0 - ta de boa 1 - Recebendo 2 - Minerando 3 - Enviando';
     * @param PeerEnviador
     * @param peerMinerador
     * @param peerRecebedor
     * @return O inteiro que representa o Estado da Transação quando um peer desconectou-se
     */
    public static int decidirTransacao(int idPeerDesconectado, int status, int PeerEnviador, int peerMinerador, int peerRecebedor)
    {
        /**
             * "<Enviador>"+log.getString("idenviador") + "</Enviador> "
                +"<Coins> " + log.getInt("qtdCoins") + "</Coins>"
                +"<Minerador> " + log.getInt("idMinerador") + "</Minerador>"
             */
            String dados = FuncoesDB.buscaMineracaoPendenteRecebedor(peerRecebedor, 1);
            String enviador = "";
            String minerador = "";
            int coins = 0;
            if(dados.length() > 0)
            {
                enviador = dados.split("<Enviador>")[1].split("</Enviador>")[0];
                minerador = dados.split("<Minerador>")[1].split("</Minerador>")[0];
                coins = Integer.parseInt(dados.split("<Coins>")[1].split("</Coins>")[0]);
            }
            
        if(status == 1 || idPeerDesconectado == peerRecebedor)
        {
            //Se  Mineração Pendente.Status = 0 ( não iniciado ) - Continua na fila para aceitar.
            //Mineração Pendente.Status = 1 ( aceito ) - Já clicou que quer Receber. -> envia para minerar (instantaneo)
            // Resta apenas Perguntar para o Minerador o que Rolou.

            String descricao = FuncoesDB.buscaDescricaoLogFake(peerRecebedor, Integer.parseInt(enviador), Integer.parseInt(minerador));
            
             int estado = FuncoesDB.buscaEstadoLogFake(peerRecebedor, PeerEnviador, peerMinerador);
             if(estado == 0)
             {
                 /*
<MineraçãoRecebida> (PeerRecebedor) publicou pedido de Mineração para Transação de @X@ coins do PeerEnviador </MineracaoRecebida>
<Resultado> Enviando resultado [Y]  para PeerRecebedor @X@ Coins Recebidas pelo Recebedor </Resultado>
                 */
                 //Start confirmado - Tentar Split no <Resultado>, caso não exista
                 // Ou minerador não termininou de Minerar, ou ninguem minerou at all - Buscar <Resultado>
                String aux = descricao.split("<Resultado>")[1].split("</Resultado>")[0];
                /*
                0 - start confirmado 1 - transação ok 2 - commited 3 - refused 4 - envio iniciado';
                */
                String result = "";
                if(aux.length() > 0)
                {
                    //result = aux.split("[")[1].split("]")[0];
                }
                if(!result.equals(""))
                {
                    estado = Integer.parseInt(result);
                }
                
             }      
             if(estado == 1)
             {
                 //transação ok - Coins recebedor
                int coinRecebedor = FuncoesDB.buscaMoedaPeerPeloID(peerRecebedor);
                coinRecebedor += coins;
                FuncoesDB.atualizaCarteiraOficial(peerRecebedor, peerRecebedor, coinRecebedor);
                
                //transação ok - Coins Minerador
                int coinMinerador = FuncoesDB.buscaMoedaPeerPeloID(Integer.parseInt(minerador));
                coinMinerador += 1;
                FuncoesDB.atualizaCarteiraOficial(Integer.parseInt(minerador), Integer.parseInt(minerador), coinMinerador);
                
                //transação ok - Coins recebedor
                int coinEnviador = FuncoesDB.buscaMoedaPeerPeloID(Integer.parseInt(enviador));
                coinEnviador -= coins;
                FuncoesDB.atualizaCarteiraOficial(Integer.parseInt(enviador), Integer.parseInt(enviador), coinEnviador);
            
             }
             if(estado == 2)
             {
                 //commited - Não faz nada ?
             }
             if(estado == 3)
             {
                 //refused - Não faz nada... Retirar da fila de Minerações Pendentes... e falar no multicast
             }
             if(estado == 4)
             {
                 //envio iniciado - Mesmo caso que Start Confirmed ?
             }
            
            
            return 3;
        }
        int estado = FuncoesDB.buscaEstadoLogFake(peerRecebedor, PeerEnviador, peerMinerador);
        if(status == 2)
        { // 2 minerando
            //minerador caiu - Pega o ID do minerador , do Enviador e do Recebedor da interfaçe gráfica.
            //busca no log para verificar o que rolou
            String descricao = FuncoesDB.buscaDescricaoLogFake(peerRecebedor, Integer.parseInt(enviador), Integer.parseInt(minerador));
            String aux = descricao.split("<Resultado>")[1].split("</Resultado>")[0];
            String result = "";
            if(aux.length() > 0)
            {
                //result = aux.split("[")[1].split("]")[0];
            }
            if(!result.equals(""))
            {
                estado = Integer.parseInt(result);
                if(estado == 1)
                {
                    //Envia mensagem Unicast para Comprador dizendo que ta tudo blz
                }
                if(estado == 2)
                {
                    //Já foi comitado pelo comprador, não faz nada
                }
                if(estado == 3)
                {
                    //Envia mensagem Unicast para Comprador dizendo Deu Ruim a transação
                }
                if(estado == 4)
                {
                    //Ainda não começou a Minerar!
                }
            }
            
        }
        if(status == 3)
        { //3 enviando 
            /*
            0 - start confirmado
            1 - transação ok
            2 - commited
            3 - refused';
            */
            //Enviar Unicast para Comprador e pedir o Estado da transação
            if(estado == 0)
            {
                //Recebedor QUER receber, porem Não foi enviado pro Minerador OU Minerador Caiu
                //Solução: Esperar           
            }
            if(estado == 1)
            {
                //Ta tudo Beleza, Porem Não foi salvo no BD oficial
                //Esperar o Comprador dar commit
            }
            if(estado == 2)
            {
                //Já foi Até comitado, não faz nada
                //Solução: atualizar valor da carteira
            }
            if(estado == 3)
            {
                //Transação Negada - Saldo Insuficiente ou Whatever
                //Solução: Cancela tudo. Nada rolou no BD Original, limpa bd transitivo
            }
            return estado;
        }
        return -1;
    }
    
    public static boolean tratarMensagem(IG_MAIN ig, String msgIn, InetAddress peerAddress){
	msgIn = msgIn.trim();   //Elimina os espaços em branco que podem ser gerados ao preencher o resto do buffer no recebimento das mensagens.
        if (msgIn.contains(Main.MSG_PEER_OPEN))
        {
            msgIn = msgIn.substring(Main.MSG_PEER_OPEN.length());
            String peerName = msgIn.split(";;;")[0];
            int peerUnicastSocket = Integer.parseInt(msgIn.split(";;;")[1]);
            int peerCoins = Integer.parseInt(msgIn.split(";;;")[2]);
            int peerPrecoVenda = Integer.parseInt(msgIn.split(";;;")[3]);
            int peerID = FuncoesDB.buscaPeerPeloNome(peerName);
            
            //Caso todos os Peers estejam sendo controlados de uma mesma máquina, a inserção dos dados dos mesmos irá causar problemas pois eles já existem.
            //Todos os Peers, quando executam localmente, são gerados na tela de login (IG_Login), e inseridos no banco de dados.
            if (!Main.LOCAL_PEERS){
                //Adiciona os dados recebidos do novo peer ao Banco de dados.
                //Nele é feita a verificação se o peer informado já existe.
                FuncoesDB.inserirPeer(peerID, peerName, peerAddress.toString(), msgIn.split(";;;")[1]);
                FuncoesDB.atualizaCarteiraFake(peerID, peerID, peerCoins);
            }
            //Se por acaso o Peer que declarou estar conectado já for conhecido, então o status desconectado do mesmo deve ser atualizado de acordo.
            FuncoesDB.logaPeer(peerName);
            
            //Envia POR UNICAST os próprios dados para o peer que acabou de chegar na festa.
            //Isso vale também pro caso de ser ele mesmo. Caso o programa tenha acabado de ser startado, os dados de si próprio serão gravados aqui.
            ig.sendMessageUC(
                    Main.MSG_PEER_ACTIVE + 
                            ig.getNomePeer() + ";;;" + 
                            ig.getDgSocket().getLocalPort() + ";;;" +
                            FuncoesDB.buscaMoedaPeerPeloID(peerID) + ";;;" + 
                            1,
                    peerName,
                    peerAddress,
                    peerUnicastSocket);
	}
        else if (msgIn.contains(Main.MSG_PEER_ACTIVE))
        {
            msgIn = msgIn.substring(Main.MSG_PEER_ACTIVE.length());
            String peerName = msgIn.split(";;;")[0];
            int peerUnicastSocket = Integer.parseInt(msgIn.split(";;;")[1]);
            int peerCoins = Integer.parseInt(msgIn.split(";;;")[2]);
            int peerPrecoVenda = Integer.parseInt(msgIn.split(";;;")[3]);
            int peerID = FuncoesDB.buscaPeerPeloNome(peerName);
            
            //Caso todos os Peers estejam sendo controlados de uma mesma máquina, a inserção dos dados dos mesmos irá causar problemas pois eles já existem.
            //Todos os Peers, quando executam localmente, são gerados na tela de login (IG_Login), e inseridos no banco de dados.
            if (!Main.LOCAL_PEERS){
                //Adiciona os dados recebidos do novo peer ao Banco de dados.
                //Nele é feita a verificação se o peer informado já existe.
                FuncoesDB.inserirPeer(peerID,peerName, peerAddress.toString(), msgIn.split(";;;")[1]);
                FuncoesDB.atualizaCarteiraFake(peerID, peerID, peerCoins);
            }
	}
        else if (msgIn.contains(Main.MSG_PEER_CLOSED))
        {
            msgIn = msgIn.substring(Main.MSG_PEER_CLOSED.length());
            String peerName = msgIn.split(";;;")[0];
            
            //Marca o Peer em questão como desconectado.
            FuncoesDB.deslogaPeer(peerName);
            //Caso um Peer desconecte antes de votar/decidir, a operação mantém-se pendente até o retorno do mesmo.
	}
        else if (msgIn.contains(Main.MSG_SENDING_COINS))
        {
            msgIn = msgIn.substring(Main.MSG_SENDING_COINS.length());
            String senderName = msgIn.split(";;;")[0];
            String receiverName = msgIn.split(";;;")[1];
            int coinAmount = Integer.parseInt(msgIn.split(";;;")[2]);
            int idTransacao = Integer.parseInt(msgIn.split(";;;")[3]);
            
            //Verifica se este Peer é realmente o destinatário dessas conis.
            //Se por acaso não o for, ignora a menssagem.
            if (receiverName.equals(ig.getNomePeer())){
                if (!Main.LOCAL_PEERS) FuncoesDB.inserirRecebimento(FuncoesDB.buscaPeerPeloNome(senderName), FuncoesDB.buscaPeerPeloNome(receiverName), coinAmount, idTransacao);
                //Não faz o envio de nenhuma mensagem aqui, pois o usuário deve decidir quais recebimentos aceitar.
            }
	}
        else if (msgIn.contains(Main.MSG_SENDER_VOTE_NO))
        {
            msgIn = msgIn.substring(Main.MSG_SENDER_VOTE_NO.length());
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[0] );
            int idEnviador = Integer.parseInt( msgIn.split(";;;")[1] );
            int voto = Integer.parseInt( msgIn.split(";;;")[2] );
            
            FuncoesDB.atualizaTransacaoVotoEnviador(idTransacao, idEnviador, voto);
	}
        else if (msgIn.contains(Main.MSG_SENDER_VOTE_YES))
        {
            msgIn = msgIn.substring(Main.MSG_SENDER_VOTE_YES.length());
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[0] );
            int idEnviador = Integer.parseInt( msgIn.split(";;;")[1] );
            int voto = Integer.parseInt( msgIn.split(";;;")[2] );
            
            FuncoesDB.atualizaTransacaoVotoEnviador(idTransacao, idEnviador, voto);
	}
        else if (msgIn.contains(Main.MSG_RECEIVER_VOTE_NO))
        {
            msgIn = msgIn.substring(Main.MSG_RECEIVER_VOTE_NO.length());
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[0] );
            int idRecebedor = Integer.parseInt( msgIn.split(";;;")[1] );
            int voto = Integer.parseInt( msgIn.split(";;;")[2] );
            
            FuncoesDB.atualizaTransacaoVotoRecebedor(idTransacao, idRecebedor, voto);
	}
        else if (msgIn.contains(Main.MSG_RECEIVER_VOTE_YES))
        {
            msgIn = msgIn.substring(Main.MSG_RECEIVER_VOTE_YES.length());
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[0] );
            int idRecebedor = FuncoesDB.buscaPeerPeloNome(msgIn.split(";;;")[1]);
            int voto = Integer.parseInt( msgIn.split(";;;")[2] );
            
            FuncoesDB.atualizaTransacaoVotoRecebedor(idTransacao, idRecebedor, voto);
	}
        else if (msgIn.contains(Main.MSG_MINER_REQUEST))
        {
            msgIn = msgIn.substring(Main.MSG_MINER_REQUEST.length());
            int idEnviador =  FuncoesDB.buscaPeerPeloNome(msgIn.split(";;;")[0]);
            int idRecebedor = FuncoesDB.buscaPeerPeloNome(msgIn.split(";;;")[1]);
            int qtdCoins = Integer.parseInt( msgIn.split(";;;")[2] );
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[3] );
            
            if (!Main.LOCAL_PEERS) FuncoesDB.inserirMineracao(idEnviador, idRecebedor, qtdCoins, 0, idTransacao);
            contadorMineracao ++;
	}
        /*
        else if (msgIn.contains(Main.MSG_MINER_VOTE_NO))
        {
            msgIn = msgIn.substring(Main.MSG_MINER_VOTE_NO.length());
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[0] );
            int idMinerador = Integer.parseInt( msgIn.split(";;;")[1] );
            int voto = Integer.parseInt( msgIn.split(";;;")[2] );
            
            FuncoesDB.transacaoVotoMinerador(idTransacao, idMinerador, voto);
	}
        else if (msgIn.contains(Main.MSG_MINER_VOTE_YES))
        {
            msgIn = msgIn.substring(Main.MSG_MINER_VOTE_YES.length());
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[0] );
            int idMinerador = FuncoesDB.buscaPeerPeloNome(msgIn.split(";;;")[1]);
            int voto = Integer.parseInt( msgIn.split(";;;")[2] );
            
            FuncoesDB.transacaoVotoMinerador(idTransacao, idMinerador, voto);
            
            //Se tem 3 votos sim, o ENVIADOR já autoriza de cara o MINERADOR a minerar.
            //O minerador vai em seguida atualizar a mineração pendente com o seu id de minerador.
            if (FuncoesDB.buscaVotos(idTransacao)){
                int idRecebedor = FuncoesDB.buscaRecebedorTransacao(idTransacao);
                try {
                    ig.sendMessageUC(
                            Main.MSG_START_MINERATION +
                                    FuncoesDB.buscaEnviadorTransacao(idTransacao) + ";;;" +
                                    idRecebedor + ";;;" +
                                    FuncoesDB.buscaCoinsTransacao(idTransacao) + ";;;" +
                                    FuncoesDB.buscaMineradorTransacao(idTransacao),
                            FuncoesDB.buscaPeerNome(idRecebedor),
                            InetAddress.getByName(FuncoesDB.buscaPeerEndereco(idRecebedor)),
                            FuncoesDB.buscaPeerUnicastPort(idRecebedor)
                    );
                } catch (UnknownHostException ex) {
                    Logger.getLogger(OperacoesCom.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
	}
        */
        else if (msgIn.contains(Main.MSG_START_MINERATION))
            //[TODO intencional!] ESTE CASO FOI PENSADO PARA MENSAGENS UNICAST APENAS
            //(pois todos os peers executam localmente)
        {
            msgIn = msgIn.substring(Main.MSG_START_MINERATION.length());
            int idEnviador = Integer.parseInt( msgIn.split(";;;")[0] );
            int idRecebedor = Integer.parseInt( msgIn.split(";;;")[1] );
            int qtdCoins = Integer.parseInt( msgIn.split(";;;")[2] );
            int idMinerador = Integer.parseInt( msgIn.split(";;;")[3] );
            
            FuncoesDB.atualizarMineracao(idEnviador, idRecebedor, qtdCoins, idMinerador);
        }
        else if (msgIn.contains(Main.MSG_MINING_NOT_OK))
        {
            msgIn = msgIn.substring(Main.MSG_MINING_NOT_OK.length());
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[0] );
            int estado = Integer.parseInt( msgIn.split(";;;")[1] );
            int idMinerador = Integer.parseInt( msgIn.split(";;;")[2] );
            
            //Atualiza o estado da transação no banco.
            FuncoesDB.atualizaEstadoTransacaoDbOficial(idTransacao, estado);
            //Aplica o voto do Minerador (vulgo, resultado da mineração dele).
            FuncoesDB.atualizaTransacaoVotoMinerador(idTransacao, idMinerador, -1);
            
            int enviador = FuncoesDB.buscaEnviadorTransacao(idTransacao);
            int recebedor = FuncoesDB.buscaRecebedorTransacao(idTransacao);
            int minerador = FuncoesDB.buscaMineradorTransacao(idTransacao);
            
            
            String msLog = "<Recebedor Refused> (" + FuncoesDB.buscaRecebedorTransacao(idTransacao) 
                    + ") Recusou a Transação @" + idTransacao 
                    +"@ </Recebedor Refused>";
            //PeerRecebedor, peerEnviador, peerMinerador, Estado - 3 refused, Mensagem
            FuncoesDB.insereLogPeerOficial(enviador, recebedor, minerador, 3, msLog);
            //Envia POR MULTICAST o resultado da transação. Quem estiver bloqueado esperando 
            //essa transação é desbloqueado.
            ig.sendMessageMC(
                    Main.MSG_TRANSACTION_DONE + 
                            idTransacao + ";;;" + 
                            estado);
            FuncoesDB.atualizaTransacaoEstadoDbOficial(idTransacao, 3);
        }
        
        
        if (msgIn.contains(Main.MSG_MINING_IS_OK))
        {
            msgIn = msgIn.substring(Main.MSG_MINING_IS_OK.length());
            int idTransacao = Integer.parseInt( msgIn.split(";;;")[0] );
            int estado = Integer.parseInt( msgIn.split(";;;")[1] );
            int idMinerador = Integer.parseInt( msgIn.split(";;;")[2] );
            
            //Atualiza o estado da transação no banco.
            FuncoesDB.atualizaEstadoTransacaoDbOficial(idTransacao, estado);
            //Aplica o voto do Minerador (vulgo, resultado da mineração dele).
            FuncoesDB.atualizaTransacaoVotoMinerador(idTransacao, idMinerador, 1);
            
            int enviador = FuncoesDB.buscaEnviadorTransacao(idTransacao);
            int recebedor = FuncoesDB.buscaRecebedorTransacao(idTransacao);
            int minerador = FuncoesDB.buscaMineradorTransacao(idTransacao);
            int coins = FuncoesDB.buscaCoinsTransacao(idTransacao);
            
            String msLog = "<Recebedor Commited> (" + FuncoesDB.buscaRecebedorTransacao(idTransacao) 
                    + ") Commitou a Transação @" + idTransacao 
                    +"@ </Recebedor Commited>";
            //PeerRecebedor, peerEnviador, peerMinerador, Estado - 2 commited, Mensagem
            FuncoesDB.insereLogPeerOficial(enviador, recebedor, minerador, 2, msLog);
            //Envia POR MULTICAST o resultado da transação. Quem estiver bloqueado esperando 
            //essa transação é desbloqueado.
            System.out.println("Contador Mineração " + contadorMineracao);
            
            transferirCoins(
                    enviador,
                    recebedor,
                    coins,
                    minerador);
            FuncoesDB.atualizaTransacaoEstadoDbOficial(idTransacao, estado);
               
            ig.sendMessageMC(
                        Main.MSG_TRANSACTION_DONE + 
                                idTransacao + ";;;" + 
                                estado);
            
        }
        else 
        {
            //System.out.println(ig.getIgName() + 
            //        " não conseguiu interpretar a última mensagem recebida como uma operação válida entre peers.");
            return false;
	}
        return true;
    }
    
    public static boolean minerar(String sender, String receiver, int amount, String miner){
        JFrame ig = new JFrame() {};  //Usado apenas para emitir mensagens de erro.
        System.out.println("operações.Operacoes.minerar() MINERANDO: " + sender + " " + 
                receiver + " " + 
                amount + " " + 
                miner);
        int psId = FuncoesDB.buscaPeerPeloNome(sender);
        //deu treta, enviador invalido/nao identificado no banco de dados
        if (psId == -1){
            JOptionPane.showMessageDialog(ig, "operações.Operacoes.minerar() ENVIADOR NÃO IDENTIFICADO", "ERRO", JOptionPane.ERROR_MESSAGE);
            System.out.println("operações.Operacoes.minerar() ENVIADOR NÃO IDENTIFICADO");
            return false;
        }
        //deu treta, recebedor invalido/nao identificado no banco de dados
        if (FuncoesDB.buscaPeerPeloNome(receiver) == -1){
            JOptionPane.showMessageDialog(ig, "operações.Operacoes.minerar() RECEBEDOR NÃO IDENTIFICADO", "ERRO", JOptionPane.ERROR_MESSAGE);
            System.out.println("operações.Operacoes.minerar() RECEBEDOR NÃO IDENTIFICADO");
            return false;
        }
        //deu treta, minerador invalido/nao identificado no banco de dados
        if (FuncoesDB.buscaPeerPeloNome(miner) == -1){
            JOptionPane.showMessageDialog(ig, "operações.Operacoes.minerar() MINERADOR NÃO IDENTIFICADO", "ERRO", JOptionPane.ERROR_MESSAGE);
            System.out.println("operações.Operacoes.minerar() MINERADOR NÃO IDENTIFICADO");
            return false;
        }
        //verifica se o enviador tem a quantidade de coins que ele quer enviar + 1(taxa miner)
        if (FuncoesDB.buscaMoedaPeerPeloID(psId) <= amount + Main.MINER_TAX){
            JOptionPane.showMessageDialog(ig, "operações.Operacoes.minerar() O ENVIADOR NAO TEM COINS SUFICIENTES PARA PAGAR O RECEBEDR E O MINERADOR", "ERRO", JOptionPane.ERROR_MESSAGE);
            System.out.println("operações.Operacoes.minerar() O ENVIADOR NAO TEM COINS SUFICIENTES PARA PAGAR O RECEBEDR E O MINERADOR");
            return false;
        }
        return true;
    }
    
//    /**
//     * Insere os dados de um recebimento à tabela de recebimentos pendentes do banco de dados.
//     * Também já faz a abertura e o fechamento da conexão com o DB.
//     * @param idEnviador é o id do peer que enviou coins.
//     * @param idRecebedor é o id o peer que recebeu coins.
//     * @param coins é a quantia enviada.
//     * @param idTransacao é o id da transação relacionada
//     * @return true se der certo, false se der errado.
//     */
//    public static boolean voltarRecebimentoAoDB(int idEnviador, int idRecebedor, int coins, int idTransacao){
//        ControleDB.abrirConexaoDBOficial();
//        boolean retorno = FuncoesDB.inserirRecebimento(idRecebedor, idEnviador, coins, idTransacao);
//        ControleDB.fecharConexaoDBOficial();
//        return retorno;
//    }
//    
//    /**
//     * Insere os dados de uma mineração à tabela de minerações pendentes do banco de dados.
//     * Também já faz a abertura e o fechamento da conexão com o DB.
//     * @param idEnviador é o id do peer que enviou coins.
//     * @param idRecebedor é o id do peer que recebeu coins.
//     * @param coins é a quantia enviada.
//     * @param idMinerador é o id do peer que minerou esta transação.
//     * @param idTransacao
//     * @return true se der certo, false se der errado.
//     */
//    public static boolean voltarMineracaoAoDB(int idEnviador, int idRecebedor, int coins, int idMinerador, int idTransacao){
//        ControleDB.abrirConexaoDBOficial();
//        boolean retorno = FuncoesDB.inserirMineracao(idRecebedor, idEnviador, coins, idMinerador, idTransacao);
//        ControleDB.fecharConexaoDBOficial();
//        return retorno;
//    }
    
    /**
     * 
     * Efetiva a Transferencia de Coins
     * @param idEnviador - enviador das coins
     * @param idRecebedor - recebedor
     * @param coins - quantidade de coins
     * @param idMinerador - minerador da operação
     */
    private static void transferirCoins(int idEnviador, int idRecebedor, int coins, int idMinerador){
        //[TODO intencional!] por convenção nossa o id das carteiras é o mesmo id dos peers.
        //retira do enviador o que ele enviou + miner tax
        int moedaReceb = FuncoesDB.buscaMoedaPeerPeloID(idRecebedor);
        int moedaMiner = FuncoesDB.buscaMoedaPeerPeloID(idMinerador);
        int moedaEnv = FuncoesDB.buscaMoedaPeerPeloID(idEnviador);
        FuncoesDB.atualizaCarteiraOficial(idEnviador, idEnviador, moedaEnv - coins - Main.MINER_TAX);
        //recebedor ganha as moedas que o enviador enviou
        FuncoesDB.atualizaCarteiraOficial(idRecebedor, idRecebedor, moedaReceb + coins);
        //minerador ganha sua miner tax
        FuncoesDB.atualizaCarteiraOficial(idMinerador, idMinerador, moedaMiner + Main.MINER_TAX);
    }
    /**
     * 
     * Efetiva a Transferencia de Coins
     * @param idEnviador - enviador das coins
     * @param idRecebedor - recebedor
     * @param coins - quantidade de coins
     * @param idMinerador - minerador da operação
     */
    public static void transferirCoinsFake(int idEnviador, int idRecebedor, int coins, int idMinerador){
        //[TODO intencional!] por convenção nossa o id das carteiras é o mesmo id dos peers.
        int moedaReceb = FuncoesDB.buscaMoedaPeerPeloID(idRecebedor);
        int moedaMiner = FuncoesDB.buscaMoedaPeerPeloID(idMinerador);
        int moedaEnv = FuncoesDB.buscaMoedaPeerPeloID(idEnviador);
        FuncoesDB.atualizaCarteiraFake(idEnviador, idEnviador, moedaEnv - coins - Main.MINER_TAX);
        //recebedor ganha as moedas que o enviador enviou
        FuncoesDB.atualizaCarteiraFake(idRecebedor, idRecebedor, moedaReceb + coins);
        //minerador ganha sua miner tax
        FuncoesDB.atualizaCarteiraFake(idMinerador, idMinerador, moedaMiner + Main.MINER_TAX);
    }
}
