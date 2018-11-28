package multicastpeer;

/**
 * Esta classe inicializa a execução do projeto, além de inicializar cada um dos Peers.
 * Várias constantes estão aqui armazenadas para serem utilizadas ao longo da execução.
 * @author Felipe
 */
public class Main{
    public static final String DEFAULT_MULTICAST_ADDRESS = "228.5.6.7";
    public static final int DEFAULT_MULTICAST_PORT = 6789;
    public static final int DEFAULT_UNICAST_PORT = 6790;
    
    public static final String TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss";
    public static final int RECEIVER_BUFFER_SIZE = 100;//53;
    
    public static final String  //Aqui jaz os cabeçalhos de todas as mensagens enviadas entre os peers.
            MSG_PEER_OPEN = "<PEER OPEN>",
            MSG_PEER_ACTIVE = "<PEER ACTIVE>",
            MSG_PEER_CLOSED = "<PEER CLOSED>",
            MSG_BUYING_COINS = "<BUYING COINS>",
            MSG_REFUSING_TO_SELL = "<REFUSING TO SELL>",
            MSG_SELLING_COINS = "<SELLING COINS>",
            MSG_MINER_REQUEST = "<MINER REQUEST>",
            MSG_MINING_IS_OK = "<MINING IS OK>",
            MSG_MINING_NOT_OK = "<MINING NOT OK>",
            MSG_COIN_TRANSFER = "<COIN TRANSFER>", 
            MSG_DATABASE_TRANSFER = "<DATABASE TRANSFER>";
    
    public static final int MINER_TAX = 1;              //Usar esta quantia para pagar os mineradores a cada transação minerada por eles.
    public static final int MINER_WAIT_TIME = 15000;    //Tempo (em milissegundos) que o minerador ficará esperando caso não tenha nada para minerar.
    
    public static final boolean SHOW_DEBUG_STUFF = true;  //Se true então todas as mensagens de console possíveis serão geradas durante a execução.
                                                        //Aviso: sua console não será perdoada.
    
    public static void main(String args[]){
        generateNewPeer(0);     //DEFAULT_MULTICAST_ADDRESS, DEFAULT_MULTICAST_PORT, DEFAULT_UNICAST_PORT).setVisible(true);
    }
    
    /**
     * Gera um novo peer local, nesta mesma máquina. A única diferença é que o peer novo utilizará uma porta unicast diferente,
     * incrementada do valor utilizado pelo peer que o gerou.
     * @param igCounter o contador de peers locais que já foram criados. Se for zero então nenhum peer foi criado até então.
     */
    public static void generateNewPeer(int igCounter){
        new InterfaceGrafica(igCounter,
                DEFAULT_MULTICAST_ADDRESS,
                DEFAULT_MULTICAST_PORT,
                DEFAULT_UNICAST_PORT + igCounter).setVisible(true);
    }
}