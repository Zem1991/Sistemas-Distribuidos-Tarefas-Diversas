package operações;

import java.net.InetAddress;
import java.security.PublicKey;
import java.util.ArrayList;
import multicastpeer.InterfaceGrafica;
import multicastpeer.Main;

/**
 *
 * @author Leila Sarris
 */
public class Operacoes {
    private ArrayList<PeerData> allPeers = new ArrayList<>();
    private InterfaceGrafica ig;
    
    public Operacoes(InterfaceGrafica ig){
        this.ig = ig;
    }
    
    /*
    BEGIN Banco de DADOS
    */
    /**
     * Retorna tudo a respeito de um Peer a partir da sua posição no array de Peers.
     * DICA: Use o método searchPeerInList(String peerName) para procurar um peer pelo
     * seu nome, pois este método já retorna o seu respectivo índice do array.
     * @param i é o número de índice a utilizar
     * @return o peer com índice i.
     */
    public PeerData getPeer(int i){
        return allPeers.get(i);
    }
    
    /**
     * Retorna tudo a respeito de todos os Peer na lista.
     * @return banco de dados.
     */
    public String[] getAllPeers()
    {
        String [] bancoDeDados = new String[allPeers.size()];
        for(int i =0; i < allPeers.size(); i++)
        {
            //peerName;;;PeerCoins;;;PeerPublicKey;;;PeerPrecoVenda
            String msg = allPeers.get(i).getPeerName() + ";;;" + allPeers.get(i).getPeerCoins() + ";;;" +
                    allPeers.get(i).getPeerPublicKey() + ";;;" + allPeers.get(i).getPeerPrecoVenda();
            
            bancoDeDados[i] = msg;
        }
        return bancoDeDados;
    }
    
    /**
     * Procura por um determinado Peer utilizando o nome com o qual ele foi identificado.
     * @param peerName é o nome do Peer.
     * @return 0 ou maior se existir, -1 se não for encontrado.
     */
    public int searchPeerInList(String peerName){
        for (int i = 0; i < allPeers.size(); i++){
            if (allPeers.get(i).getPeerName().equals(peerName)){
                return i;
            }
        }
        return -1;      //TODO: pode (e vai) causar probleminhas com o método getPeerData(int i)!
    }
    
    /**
     * Tenta adicionar um Peer ao ArrayList que serve como Banco de Dados, desde que já não exista um
     * Peer com o mesmo nome e que os dados repassados nos parâmetros estejam válidos.
     * @param peerName
     * @param peerCoins
     * @param peerPrecoVenda
     * @param peerAddress
     * @param peerUnicastSocket
     * @param peerPublicKey
     * @return 
     */
    public boolean addPeerToList(String peerName, int peerCoins, int peerPrecoVenda,
            InetAddress peerAddress, int peerUnicastSocket,
            PublicKey peerPublicKey){
        try{
            if (searchPeerInList(peerName) == -1){
                PeerData newPeer = new PeerData(peerName, peerCoins, peerPrecoVenda,
                        peerAddress, peerUnicastSocket,
                        peerPublicKey);
                allPeers.add(newPeer);
                ig.printLogMessage("[OPERAÇÕES]", peerName + " adicionado ao banco de dados.");
                System.out.println("operações.Operacoes.addPeerToList() PEER ADICIONADO AO BANCO DE DADOS!");
                return true;
            }else{
                System.out.println("operações.Operacoes.addPeerToList() JÁ EXISTE PEER COM ESTE MESMO NOME!");
                return false;
            }
        }catch (Exception e){
            System.out.println("operações.Operacoes.addPeerToList() DADOS INVÁLIDOS PARA CADASTRO DE PEER!");
            return false;
        }
    }
    
    //remove peer do arrayList que serve como Banco de Dados
    public boolean removePeerFromList(String peerName){
        for (int i = 0; i < allPeers.size(); i++){
            if (allPeers.get(i).getPeerName().equals(peerName)){
                allPeers.remove(i);
                allPeers.trimToSize();
                ig.printLogMessage("[OPERAÇÕES]", peerName + " removido do banco de dados.");
                return true;
            }
        }
        return false;
    }
    /*
    FINISH Banco de DADOS
    */
    
    public boolean minerar(String sender, String receiver, int amount, String miner){
        System.out.println("operações.Operacoes.minerar() MINERANDO: " + sender + " " + 
                receiver + " " + 
                amount + " " + 
                miner);
        int psId = searchPeerInList(sender);
        //deu treta, enviador invalido/nao identificado no banco de dados
        if (psId == -1){
            System.out.println("operações.Operacoes.minerar() ENVIADOR NÃO IDENTIFICADO");
            //TODO: melorar o feedback
            return false;
        }
        //deu treta, recebedor invalido/nao identificado no banco de dados
        if (searchPeerInList(receiver) == -1){
            System.out.println("operações.Operacoes.minerar() RECEBEDOR NÃO IDENTIFICADO");
            //TODO: melorar o feedback
            return false;
        }
        //deu treta, minerador invalido/nao identificado no banco de dados
        if (searchPeerInList(miner) == -1){
            System.out.println("operações.Operacoes.minerar() MINERADOR NÃO IDENTIFICADO");
            //TODO: melorar o feedback
            return false;
        }
        //verifica se o enviador tem a quantidade de coins que ele quer enviar + 1(taxa miner)
        if (allPeers.get(psId).getPeerCoins() <= amount + Main.MINER_TAX){
            System.out.println("operações.Operacoes.minerar() O ENVIADOR NAO TEM COINS SUFICIENTES PARA PAGAR O RECEBEDR E O MINERADOR");
            //TODO: melorar o feedback
            return false;
        }
        return true;
    }
    
    public void transferir(String sender, String receiver, int amount, String miner, int precoVenda){
        System.out.println("operações.Operacoes.transferir() MINER NAME IS " + miner);
        
        int psId = searchPeerInList(sender);
        int prId = searchPeerInList(receiver);
        int pmId = searchPeerInList(miner);
        
        //retira do enviador o que ele enviou + miner tax
        System.out.println("[" + ig.getIgName() + "] Coins de " + allPeers.get(psId).getPeerName() + 
                " (SENDER) antes da transferencia: " + allPeers.get(psId).getPeerCoins());
        allPeers.get(psId).setPeerCoins(allPeers.get(psId).getPeerCoins() - amount - Main.MINER_TAX);
        System.out.println("[" + ig.getIgName() + "] Coins de " + allPeers.get(psId).getPeerName() + 
                " (SENDER) depois da transferencia: " + allPeers.get(psId).getPeerCoins());
        
        //recebedor ganha as moedas que o enviador enviou
        System.out.println("[" + ig.getIgName() + "] Coins de " + allPeers.get(prId).getPeerName() + 
                " (RECEIVER) antes da transferencia: " + allPeers.get(prId).getPeerCoins());
        allPeers.get(prId).setPeerCoins(allPeers.get(prId).getPeerCoins() + amount);
        System.out.println("[" + ig.getIgName() + "] Coins de " + allPeers.get(prId).getPeerName() + 
                " (RECEIVER) depois da transferencia: " + allPeers.get(prId).getPeerCoins());
        
        //minerador ganha sua miner tax
        System.out.println("[" + ig.getIgName() + "] Coins de " + allPeers.get(pmId).getPeerName() + 
                " (MINER) antes da transferencia: " + allPeers.get(pmId).getPeerCoins());
        allPeers.get(pmId).setPeerCoins(allPeers.get(pmId).getPeerCoins() + Main.MINER_TAX);
        System.out.println("[" + ig.getIgName() + "] Coins de " + allPeers.get(pmId).getPeerName() + 
                " (MINER) depois da transferencia: " + allPeers.get(pmId).getPeerCoins());
    }
    
    public boolean tratarMensagem(InterfaceGrafica ig, String msgIn, InetAddress peerAddress){
	msgIn = msgIn.trim();   //Elimina os espaços em branco que podem ser gerados ao preencher o resto do buffer no recebimento das mensagens.
        if (msgIn.contains(Main.MSG_PEER_OPEN)){
            msgIn = msgIn.substring(Main.MSG_PEER_OPEN.length());
            String peerName = msgIn.split(";;;")[0];
            int peerUnicastSocket = Integer.parseInt(msgIn.split(";;;")[1]);
            int peerCoins = Integer.parseInt(msgIn.split(";;;")[2]);
            int peerPrecoVenda = Integer.parseInt(msgIn.split(";;;")[3]);
            //String peerPublicKey = msgIn.split(";;;")[3];

            //Adiciona os dados recebidos do novo peer ao Banco de dados.
            //Nele é feita a verificação se o peer informado já existe.
            addPeerToList(peerName, peerCoins, peerPrecoVenda, peerAddress, peerUnicastSocket, null);
            //Envia POR UNICAST os próprios dados para o peer que acabou de chegar na festa.
            //Isso vale também pro caso de ser ele mesmo. Caso o programa tenha acabado de ser startado, os dados de si próprio serão gravados aqui.
            ig.sendMessageUC(
                    //ig.encryptMessage(),
                    Main.MSG_PEER_ACTIVE + 
                            ig.getIgName() + ";;;" + 
                            ig.getDgSocket().getLocalPort() + ";;;" +
                            ig.getOps().getPeer(ig.getOps().searchPeerInList(peerName)).getPeerCoins() + ";;;" + 
                            ig.getPrecoVenda(),
                    peerAddress,
                    peerUnicastSocket);
	}else if (msgIn.contains(Main.MSG_PEER_ACTIVE)){
            msgIn = msgIn.substring(Main.MSG_PEER_ACTIVE.length());
            String peerName = msgIn.split(";;;")[0];
            int peerUnicastSocket = Integer.parseInt(msgIn.split(";;;")[1]);
            int peerCoins = Integer.parseInt(msgIn.split(";;;")[2]);
            int peerPrecoVenda = Integer.parseInt(msgIn.split(";;;")[3]);
            //String peerPublicKey = msgIn.split(";;;")[3];
            
            //Adiciona os dados recebidos do novo peer ao Banco de dados.
            //Nele é feita a verificação se o peer informado já existe.
            addPeerToList(peerName, peerCoins, peerPrecoVenda, peerAddress, peerUnicastSocket, null);
	}else if (msgIn.contains(Main.MSG_PEER_CLOSED)){
            msgIn = msgIn.substring(Main.MSG_PEER_CLOSED.length());
            String peerName = msgIn.split(";;;")[0];
            removePeerFromList(peerName);
	}else if (msgIn.contains(Main.MSG_BUYING_COINS)){
            msgIn = msgIn.substring(Main.MSG_BUYING_COINS.length());
            String senderName = msgIn.split(";;;")[0];
            String receiverName = msgIn.split(";;;")[1];
            int coinAmount = Integer.parseInt(msgIn.split(";;;")[2]);
            //Verifica se este peer é realmente o destinatário dessas conis.
            if (!receiverName.equals(ig.getIgName())){
                //Se tem algo errado, recusa a oferta na hora.
                ig.sendMessageUC(
                        //ig.encryptMessage(?,ig.getChavePublica());
                        Main.MSG_REFUSING_TO_SELL + ig.getIgName() + ";;;" + senderName + ";;;" + coinAmount,
                        peerAddress,
                        getPeer(searchPeerInList(senderName)).getDgSocket());
            }else{
                //Por PADRÃO vai aceitar toda oferta de coins que receber.
                ig.sendMessageUC(
                        //ig.encryptMessage(?,ig.getChavePublica());
                        Main.MSG_SELLING_COINS + ig.getIgName() + ";;;" + senderName + ";;;" + coinAmount,
                        peerAddress,
                        getPeer(searchPeerInList(senderName)).getDgSocket());
                //E aqui ele pede pra que alguem minere a veracidade dessa transação.
                ig.sendMessageMC(Main.MSG_MINER_REQUEST + senderName + ";;;" + ig.getIgName() + ";;;" + coinAmount);
            }        
	}else if (msgIn.contains(Main.MSG_DATABASE_TRANSFER)){
            //TODO: Esta recebendo o banco de dados para se atualizar
	}else if (msgIn.contains(Main.MSG_REFUSING_TO_SELL)){
            //TODO: Avisar ao peer enviador que deu ruim. Isto é, caso o Enviador precise ficar esperando alguem minerar o que ele manda.
	}else if (msgIn.contains(Main.MSG_SELLING_COINS)){
            //TODO: Avisar ao peer enviador que deu boa. Isto é, caso o Enviador precise ficar esperando alguem minerar o que ele manda.
	}else if (msgIn.contains(Main.MSG_MINER_REQUEST)){
            msgIn = msgIn.substring(Main.MSG_MINER_REQUEST.length());
            ig.accessCoinMiner().insertMiningOperation(msgIn);    //Só tira o cabeçalho da mensagem. O resto a classe operações dá conta.
	}else if (msgIn.contains(Main.MSG_MINING_NOT_OK)){
            msgIn = msgIn.substring(Main.MSG_MINING_NOT_OK.length());
            String senderName = msgIn.split(";;;")[0];
            String receiverName = msgIn.split(";;;")[1];
            int coinAmount = Integer.parseInt(msgIn.split(";;;")[2]);
            //String minerName = msgIn.split(";;;")[3]; //Nem é necesário

            ig.accessCoinMiner().removeMiningOperation(senderName + ";;;" + receiverName + ";;;" + coinAmount);  //TODO: procurando por exatamente a string que supostamente recebeu!
            ig.updateWindowTitle();
        }else if (msgIn.contains(Main.MSG_MINING_IS_OK)){
            msgIn = msgIn.substring(Main.MSG_MINING_IS_OK.length());
            String senderName = msgIn.split(";;;")[0];
            String receiverName = msgIn.split(";;;")[1];
            int coinAmount = Integer.parseInt(msgIn.split(";;;")[2]);
            String minerName = msgIn.split(";;;")[3];
            int precoVenda = 1; //Integer.parseInt(msgIn.split(";;;")[4]); 

            ig.accessCoinMiner().removeMiningOperation(senderName + ";;;" + receiverName + ";;;" + coinAmount);  //TODO: procurando por exatamente a string que supostamente recebeu!
            
            //Só chamar o transferir. Mas, onde entra o PreçoDeVenda nessa historia? Professora não especificou.
            transferir(senderName, receiverName, coinAmount, minerName, precoVenda);  //Atualiza o próprio DB, repassando valores aos respectivos peers.
            ig.updateWindowTitle();
        }else if (msgIn.contains(Main.MSG_COIN_TRANSFER)){    //ESTE AQUI AINDA NÃO É USADO, POIS "if (msgIn.contains(Main.MSG_MINING_IS_OK))" JA FAZ A MESMA COISA.
            msgIn = msgIn.substring(Main.MSG_COIN_TRANSFER.length());
            String senderName = msgIn.split(";;;")[0];
            String receiverName = msgIn.split(";;;")[1];
            int coinAmount = Integer.parseInt(msgIn.split(";;;")[2]);
            String minerName = msgIn.split(";;;")[3];
            int precoVenda = 1;
            transferir(senderName, receiverName, coinAmount, minerName, precoVenda);
            //O peer atualiza na sua InterfaceGrafica quantas coins ele tem, mesmo que não seja ele um dos envolvidos na transação.
            ig.updateWindowTitle();
	}else {
		//System.out.println(ig.getIgName() + 
                //        " não conseguiu interpretar a última mensagem recebida como uma operação válida entre peers.");
                return false;
	}
        return true;
    }
}
