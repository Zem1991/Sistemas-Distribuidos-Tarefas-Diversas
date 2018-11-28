package multicastpeertransacoes;

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
            MSG_PEER_OPEN               = "<PEER CONECTADO>",
            MSG_PEER_ACTIVE             = "<PEER ATIVO ENCONTRADO>",
            MSG_PEER_CLOSED             = "<PEER DESCONECTADO>",
            MSG_SENDING_COINS           = "<ENVIANDO COINS>",
            MSG_SENDER_VOTE_NO          = "<ENVIADOR VOTOU NÃO>",
            MSG_SENDER_VOTE_YES         = "<ENVIADOR VOTOU SIM>",
            //MSG_RECEIVING_COINS         = "<RECEBENDO COINS>",    //não tem necessidade
            MSG_RECEIVER_VOTE_NO        = "<RECEBEDOR VOTOU NÃO>",
            MSG_RECEIVER_VOTE_YES       = "<RECEBEDOR VOTOU SIM>",
            MSG_MINER_REQUEST           = "<MINERADOR SOLICITADO>",
            //MSG_MINER_VOTE_NO           = "<MINERADOR VOTOU NÃO>",    //minerador só vota depois de minerar
            //MSG_MINER_VOTE_YES          = "<MINERADOR VOTOU SIM>",    //minerador só vota depois de minerar
            MSG_START_MINERATION        = "<MINERAÇÃO INICIADA>",
            MSG_MINING_NOT_OK           = "<TRANSAÇÃO INVÁLIDA>",
            MSG_MINING_IS_OK            = "<TRANSAÇÃO ACEITA>",
            MSG_TRANSACTION_DONE        = "<TRANSAÇÃO FINALIZADA>";
            //MSG_COIN_TRANSFER           = "<TRANSFERÊNCIA DE COINS>"; //não tem necessidade
    
    public static final int INITIAL_COINS = 1000;           //Quantia inicial de coins para cada novo Peer que for criado pela aplicação.
    
    public static final int MINER_TAX = 1;                  //Usar esta quantia para pagar os mineradores a cada transação minerada por eles.
    public static final int MINER_WAIT_TIME = 15000;        //Tempo (em milissegundos) que o minerador ficará esperando caso não tenha nada para minerar.
    public static final boolean LOCK_PEERS = true;   //Trava os peers quando estes realizam uma atividade ou estão esperando outra atividade finalizar.
    
    public static final boolean DEBUG_MSG_COM = true;      //Habilita certas mensagens na console, para debugação.
    public static final boolean DEBUG_MSG_DB = false;       //Habilita certas mensagens na console, para debugação.
    public static final boolean DEBUG_MSG_THREAD = true;   //Habilita certas mensagens na console, para debugação.
    
    public static final boolean LOCAL_PEERS = true;         //Se true então todos os peers estão sendo executados na mesma máquina.
                                                            //Logo, todos estão usando do mesmo DB.
    
    public static void main(String args[]){
        new IG_Login().setVisible(true);
    }
}