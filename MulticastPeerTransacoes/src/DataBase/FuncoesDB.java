/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import static DataBase.ControleDB.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Klaus & Felipe
 * Conjuto de Funções que utilizam o(s) database(s)
 */
public class FuncoesDB 
{
    /**
     * Função para Inserir novo peer no Banco de dados.
     * @param idRecebedor - identificador do recebedor
     * @param idEnviador - identificador do enviador
     * @param coins - qtd coins enviadas/recebidas
     * @return True se foi inserido e False se Falhou
     */
    public static boolean inserirRecebimento(int idRecebedor, int idEnviador, int coins, int idtransacao)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("INSERT INTO recebimentospendentes(idRecebedor, idEnviador, coins, idtransacao)"
                        + "VALUES(" + idRecebedor + "," + idEnviador +"," + coins + "," + idtransacao + ");"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
        }
        return false;
    }
    /**
     * @param idRecebimento - identificador do recebimento
     * @param idPeerRecebedor - peer que quer receber 
     * @return Retorna o ID do peer Enviador
     */
    public static String buscaRecebimento(int idRecebimento, int idPeerRecebedor)
    {
        String enviador = "";
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select idenviador from recebimentospendentes"
                        + " where id = " + idRecebimento +  ";");
                while(log.next())
                {
                    enviador = log.getString("idenviador");
                }
            }
            catch(SQLException ex)
            {
                   System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
            return enviador;
        }
        
        return enviador;
    }

    /**
     * Busca iD do recebedor das coins na transação X
     * @param idTransacao - Identificador da transação
     * @return ID do Recebedor de coins
     */
    public static int buscaRecebedorTransacao(int idTransacao)
    {
        int idRecebedor = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select idrecebedor from transacoes"
                        + " where id = " + idTransacao +  ";");
                while(log.next())
                {
                    idRecebedor = log.getInt("idrecebedor");
                }
            }
            catch(SQLException ex)
            {
                   System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
            return idRecebedor;
        }
        
        return idRecebedor;
    }
    /**
     * Busca iD do enviador das coins na transação X
     * @param idTransacao - Identificador da transação
     * @return ID do enviador de coins
     */
    public static int buscaEnviadorTransacao(int idTransacao)
    {
        int idEnviador = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select idenviador from transacoes"
                        + " where id = " + idTransacao +  ";");
                while(log.next())
                {
                    idEnviador = log.getInt("idenviador");
                }
            }
            catch(SQLException ex)
            {
                   System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
            return idEnviador;
        }
        
        return idEnviador;
    }
    /**
     * Busca iD do minerador das coins na transação X
     * @param idTransacao - Identificador da transação
     * @return ID do minerador de coins
     */
    public static int buscaMineradorTransacao(int idTransacao)
    {
        int idMinerador = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select idminerador from transacoes"
                        + " where id = " + idTransacao +  ";");
                while(log.next())
                {
                    idMinerador = log.getInt("idminerador");
                }
            }
            catch(SQLException ex)
            {
                   System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
            return idMinerador;
        }
        
        return idMinerador;
    }
    /**
     * Busca iD do minerador das coins na transação X
     * @param idTransacao - Identificador da transação
     * @return ID do minerador de coins
     */
    public static int buscaCoinsTransacao(int idTransacao)
    {
        int coins = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select coins from transacoes"
                        + " where id = " + idTransacao +  ";");
                while(log.next())
                {
                    coins = log.getInt("coins");
                }
            }
            catch(SQLException ex)
            {
                   System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
            return coins;
        }
        return coins;
    }
    /**
     * Função para Atualizar Recebimento
     * @param idRecebimento - Identificador do Recebimento pendente
     * @param aceito - 0 não votado, 1 aceito, 2 - negado
     * @param idPeerRecebedor
     * @param coins
     * @param idTransacao
     * @return True se foi inserido e False se Falhou
     */
    public static boolean atualizaRecebimento(int idRecebimento, int aceito, int idPeerRecebedor, int coins, int idTransacao)
    {
        if(abrirConexaoDBOficial())
        { 
            if(aceito == 1)
            {
                String enviador = buscaRecebimento(idRecebimento, idPeerRecebedor);
                FuncoesDB.inserirMineracao(idPeerRecebedor, Integer.parseInt(enviador), coins, 0, idTransacao);
                fecharConexaoDBOficial();
                if(abrirConexaoDBOficial())
                {
                    //1 - Aceito - Retira dos Recebimentos pendentes
                    if(executarComandoSQLDbOficial("Delete from recebimentospendentes where id = " + idRecebimento));
                    {
                        fecharConexaoDBOficial();
                        return true;
                    }
                }               
            }
            if(aceito == 2)
            {
                //1 - Aceito - Retira dos Recebimentos pendentes
                if(executarComandoSQLDbOficial("Delete from recebimentospendentes where id = " + idRecebimento));
                {
                    fecharConexaoDBOficial();
                    return true;
                }
            }     
        }
        return false;
    }
    
    /**
     * Função para Inserir novo peer no Banco de dados.
     * @param idRecebedor - identificador do recebedor
     * @param idEnviador - identificador do enviador
     * @param coins - qtd coins enviadas/recebidas
     * @param idMinerador - id do minerador
     * @param idTransacao
     * @return True se foi inserido e False se Falhou
     */
    public static boolean inserirMineracao(int idRecebedor, int idEnviador, int coins, int idMinerador, int idTransacao)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("INSERT INTO mineracaopendente(" + " idEnviador,idRecebedor, qtdCoins, idMinerador, idTransacao)"
                        + "VALUES(" + idEnviador + "," + idRecebedor +"," + coins +","+ idMinerador + "," + idTransacao + ");"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
        }
        return false;
    }
    /**
     * Função para Inserir novo peer no Banco de dados.
     * @param idRecebedor - identificador do recebedor
     * @param idEnviador - identificador do enviador
     * @param coins - qtd coins enviadas/recebidas
     * @param idMinerador - id do minerador
     * @return True se foi inserido e False se Falhou
     */
    public static boolean atualizarMineracao(int idRecebedor, int idEnviador, int coins, int idMinerador)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("UPDATE mineracaopendente set idminerador = "+ idMinerador +
                    " " + "WHERE idRecebedor = " + idRecebedor 
                    + " AND  idEnviador = " + idEnviador + ";"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
        }
        return false;
    }
    /**
     * Atualiza o Minerador de uma transacao
     * @param idTransacao - Transação que esta ocorrendo
     * @param idMinerador - minerador da transação
     * @return true se deu boa, false se deu ruim
     */
    public static boolean atualizarMineradorTransacao(int idTransacao, int idMinerador)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("UPDATE transacoes set idminerador = "+ idMinerador +
                    " " + " WHERE id = " + idTransacao + ";"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
        }
        return false;
    }
    /**
     * O minerador obtem a transação que ele quer minerar
     * @param idMinerador - id do minerador
     * @param idTransacao - id da transacao sendo minerada
     * @return True se deu certo, False se deu ruim
     */
    public static boolean obterMineracao(int idMinerador,int idTransacao)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("UPDATE mineracaopendente set idminerador = "+ idMinerador +
                    " " + "WHERE idtransacao = " + idTransacao + ";"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
        }
        return false;
    }
    /**
     * Busca as minerações pendentes
     * @param idMinerador - identificador do minerador
     * @return Retorna um ResultSet de minerações pendentes
     */
    public static ResultSet buscaMineracaoPendente(int idMinerador)
    {
        if(abrirConexaoDBOficial())
        {
            return consultarDBOficial("select * from mineracaopendente where idminerador = " + idMinerador + ";");
        }     
        return null;
    }
    /**
     * Função para Inserir novo peer no Banco de dados.
     * @param id - Número inteiro identificador do Peer
     * @param nome - "Apelido" do Peer
     * @param endereco - Endereço Unicast do Peer
     * @param porta - Porta Unicast
     * @return True se foi inserido e False se Falhou
     */
    public static boolean inserirPeer(int id, String nome, String endereco, String porta)
    {
        if(!verificarSePeerExiste(id))
        {
            if(abrirConexaoDBOficial())
            {
                if(executarComandoSQLDbOficial("INSERT INTO peer(" + "id, nome, endereco, unicastport, desconectado,status)"
                        + "VALUES(" + id + ",'" + nome +"','" + endereco + "','" + porta + "',false,0);"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
            }
        }
        else
        {
            System.out.println("Tentou-se inserir no banco de dados um peer que já tinha sido cadastrado. ID = " + id + ". NOME = " + nome + ".");
        }
        return false;
    }
    
    /**
     * Função para Logar Peer 
     * @param peerNome - "Apelido" do Peer
     * @return True se foi inserido e False se falhou
     */
    public static boolean logaPeer(String peerNome)
    {
        if(abrirConexaoDBOficial())
        { 
            if(executarComandoSQLDbOficial("UPDATE peer set desconectado=false WHERE nome = '" + peerNome + "';"));
            {
                fecharConexaoDBOficial();
                return true;
            }
        }
        return false;
    }
    /**
     * Função para Deslogar Peer 
     * @param peerNome - "Apelido" do Peer
     * @return True se foi inserido e False se falhou
     */
    public static boolean deslogaPeer(String peerNome)
    {
        if(abrirConexaoDBOficial())
        { 
            if(executarComandoSQLDbOficial("UPDATE peer set desconectado=true WHERE nome = '" + peerNome + "';"));
            {
                fecharConexaoDBOficial();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Quando um novo peer é criado, é necessário inicializar uma nova Carteira para ele.
     * @param id - Número inteiro que identifica a Carteira
     * @param idPeer - Número inteiro que identifica o Peer dono da carteira
     * @param qtdMoedas - Número inteiro que identifica a quantidade de moedas na carteira
     * @return True se foi inserido e False se Falhou
     */
    public static boolean iniciaCarteira(int id, int idPeer, int qtdMoedas)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("INSERT INTO carteira(" + "id, idpeer, quantidade)"
                    + "VALUES(" + id + "," + idPeer +"," + qtdMoedas +");"))
            {
                fecharConexaoDBOficial();
                return true;
            }
        }
        return false;
    }
    /**
     * Quando um novo peer é criado, é necessário inicializar uma nova Carteira para ele.
     * @param id - Número inteiro que identifica a Carteira
     * @param idPeer - Número inteiro que identifica o Peer dono da carteira
     * @param qtdMoedas - Número inteiro que identifica a quantidade de moedas na carteira
     * @return True se foi inserido e False se Falhou
     */
    public static boolean iniciaCarteiraFake(int id, int idPeer, int qtdMoedas)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("INSERT INTO \"carteiraFake\" "
                    + "VALUES(" + id + "," + idPeer +"," + qtdMoedas +");"))
            {
                fecharConexaoDBOficial();
                return true;
            }
        }
        return false;
    }
    /**
     * Deleta mineração pendente
     * @param idMineracao - identificador da mineração
     * @return true se deu boa, false se deu ruim
     */
    public static boolean removerMineracao(int idMineracao)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("DELETE FROM mineracaopendente"
                    + " WHERE id = " + idMineracao + ";"))
            {
                fecharConexaoDBOficial();
                return true;
            }
        }
        return false;
    }
    /**
     * 
     * @param id - id da Carteira
     * @param idPeer - id do Peer
     * @param novaQTD - Nova quantidade de moedas não pode ser > 0
     * @return True se foi atualizado e False se Falhou
     */
    public static boolean atualizaCarteiraOficial(int id, int idPeer, int novaQTD)
    {
        if(novaQTD < 0)
        {
            return false;
        }
        else
        {
            if(abrirConexaoDBOficial())
            {
                if(executarComandoSQLDbOficial("update carteira "
                        + " set idPeer = " + id + ", quantidade = " + novaQTD +
                        " WHERE id = " + id + ";"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
            }
        return false;
        }
    }
    /**
     * 
     * @param id - id da Carteira
     * @param idPeer - id do Peer
     * @param novaQTD - Nova quantidade de moedas não pode ser > 0
     * @return True se foi atualizado e False se Falhou
     */
    public static boolean atualizaCarteiraFake(int id, int idPeer, int novaQTD)
    {
        if(novaQTD < 0)
        {
            return false;
        }
        else
        {
            if(abrirConexaoDBOficial())
            {
                if(executarComandoSQLDbOficial("UPDATE \"carteiraFake\" "
                        + "set \"idPeer\" = " + id + ", quantidade = " + novaQTD +
                        " WHERE id = " + id + ";"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
            }
        return false;
        }
    }
    /**
     * Insere novo Log de ocorrencias para um determinado Peer
     * @param idPeerRecebedor - id do peer que está recebendo e controla a transação
     * @param idPeerEnviador - id do enviador
     * @param idPeerMinerador - id do minerador
     * @param estado - 0 se "start confirmado" 1 se "transação ok", 2 se "commited" e 3 se "refused"
     * @param descricao - Descrição textual das ocorrencias
     * @return True se foi atualizado e False se Falhou
     */
    public static boolean insereLogPeerOficial(int idPeerRecebedor, int idPeerEnviador, int idPeerMinerador, int estado, String descricao)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("INSERT INTO logpeer(" + "estado, descricao, idpeerrecebedor,"
                    + "idpeerminerador ,idpeerenviador)"
                    + "VALUES(" + estado + ",'" + descricao +"'," + idPeerRecebedor +"," + idPeerMinerador 
                    + "," + idPeerEnviador + ");"))
            {
                fecharConexaoDBOficial();
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @param idPeerRecebedor - ID do peer que recebe as moedas (e comandante da transação)
     * @param idPeerEnviador - ID do peer que esta enviando moedas
     * @param estado - 0 para "start confirmado", 1 para "transação ok", 2 para "commited" e 3 para "refused"
     * @param idPeerMinerador - id do peer que está minerando
     * @param descricao - descrição dos fatos ocorridos
     * @return 
     */
    public static boolean insereLogPeerFake(int idPeerRecebedor, int idPeerEnviador, int idPeerMinerador, int estado, String descricao)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial("INSERT INTO logpeer(" + "estado, descricao, idpeerrecebedor,"
                    + "idpeerminerador ,idpeerenviador)"
                    + "VALUES(" + estado + ",'" + descricao +"'," + idPeerRecebedor +"," + idPeerMinerador 
                    + "," + idPeerEnviador + ");"))
            {
                fecharConexaoDBOficial();
                return true;
            }
        }
        return false;
    }
    /**
     * Busca o estado da transação que tenha participação dos 3 peers passados nos parametros
     * @param idPeerRecebedor - ID do peer que recebe as moedas (e comandante da transação)
     * @param idPeerEnviador - ID do peer que esta enviando moedas
     * @param idPeerMinerador - ID do peer que esta minerando
     * @return Retorna o Estado da Transação - 0 para "start confirmado", 1 para "transação ok", 2 para "commited" e 3 para "refused"
     */
    public static int buscaLog(int idPeerRecebedor, int idPeerEnviador, int idPeerMinerador)
    {
        int estado = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select estado from logPeer where idpeerrecebedor = " + idPeerRecebedor
                            + " and idpeerminerador = " + idPeerMinerador +" and idpeerenviador = "
                            + idPeerEnviador + ";");
                while(log.next())
                {
                    estado = log.getInt("estado");
                }
            }
            catch(SQLException ex)
            {
                   System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
            return estado;
        }
        
        return estado;
    }
    /**
     * Busca se existe um determinado Peer a partir de um ID.
     * Útil apenas para o cadastro de novos Peers.
     * @param peerID é o ID do suposto Peer já existente.
     * @return true se um Peer com este ID existe, false caso contrário.
     */
    public static boolean verificarSePeerExiste(int peerID)
    {
        int id = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("select id from peer where id = " + peerID + ";");
                while(log.next())
                {
                    id = log.getInt("id");
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
        }
        return (id != -1);
    }
    /**
     * 
     * @param nomePeer - nome do peer a buscar
     * @return ID do peer com o nome
     */
    public static int buscaPeerPeloNome(String nomePeer)
    {
        int id = -1;
        if(abrirConexaoDBOficial())
        {
            try {
                ResultSet log = consultarDBOficial("select id from peer where nome = '" + nomePeer + "';");
                while(log.next())
                {
                    id = log.getInt("id");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
        }
        return id;
    }
    /**
     * Busca nome do peer pelo id
     * @param idPeer - identificador do peer
     * @return nome do peer
     */
    public static String buscaPeerNome(int idPeer)
    {
        String nome = "";
        if(abrirConexaoDBOficial())
        {
            try {
                ResultSet log = consultarDBOficial("select nome from peer where id = '" + idPeer + "';");
                while(log.next())
                {
                    nome = log.getString("nome");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
        }
        return nome;
    }
    /**
     * Busca os votos dos peers
     * @param idTransacao - transação que terá os votos analizados
     * @return Verdadeiro se todos votaram sim
     */
    public static Boolean buscaVotos(int idTransacao)
    {
        Boolean voto = false;
        if(abrirConexaoDBOficial())
        {
            try {
                ResultSet log = consultarDBOficial("select * from transacoes "
                        + "where votoenviador = 1"
                        + " AND votorecebedor = 1"
                        + " AND votominerador = 1"
                        + ";");
                while(log.next())
                {
                   voto = true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
        }
        return voto;
    }
     public static int buscaIDTransacaoPorIDAux(String idaux)
    {
        int idTrans = 0;
        if(abrirConexaoDBOficial())
        {
            try {
                String aux = "select id from transacoes where idaux = '" + idaux + "';";
                System.out.println("aux: " + aux);
                ResultSet log = consultarDBOficial("select id from transacoes where idaux = '" + idaux 
                        + "';");
                while(log.next())
                {
                   idTrans = log.getInt("id");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
        }
        return idTrans;
    }
    /**
     * 
     * @param id
     * @return ID do peer com o nome
     */
    public static int buscaPeerUnicastPort(int id)
    {
        String unicastport = "-1";
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select unicastport from peer where id = " + id + ";");
                while(log.next())
                {
                    unicastport = log.getString("unicastport");
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
            return Integer.parseInt(unicastport);
        }
        return -1;
    }
    /**
     * 
     * @param id
     * @return ID do peer com o nome
     */
    public static String buscaPeerEndereco(int id)
    {
        String endereco = null;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select endereco from peer where id = " + id + ";");
                while(log.next())
                {
                    endereco = log.getString("endereco");
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
            return endereco;
        }
        
        return endereco;
    }
    /**
     * Busca o estado da transação que tenha participação dos 3 peers passados nos parametros
     * @param peerID Identificador do Peer
     * @return Retorna o Estado da Transação - 0 para "start confirmado", 1 para "transação ok", 2 para "commited" e 3 para "refused"
     */
    public static int buscaMoedaPeerPeloID(int peerID) 
    {
        int coins = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select quantidade from carteira where idpeer = " + peerID + ";");
                while(log.next())
                {
                    coins = log.getInt("quantidade");
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
            return coins;
        }
        
        return coins;
    }
    
    /**
     * Busca o estado da transação que tenha participação dos 3 peers passados nos parametros
     * @param idPeerRecebedor - ID do peer que recebe as moedas (e comandante da transação)
     * @param aceito - 0 Nao aceito , 1 aceito
     * @return Retorna o Estado da Transação - 0 para "start confirmado", 1 para "transação ok", 2 para "commited" e 3 para "refused"
     */
    public static String buscaMineracaoPendenteRecebedor(int idPeerRecebedor, int aceito)
    {
        String resultado = "";
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select * from mineracaopendente where idrecebedor = " + idPeerRecebedor + ";");
                while(log.next())
                {
                    //Nao votado
                    if(aceito == 0)
                    {
                        // Ta esperando a votaçã, não faz nada
                        resultado = "";
                    }
                    //Aceito
                    if(aceito == 1)
                    {
                        resultado += "<Enviador>"+log.getString("idenviador") + "</Enviador> "
                            +"<Coins> " + log.getInt("qtdCoins") + "</Coins>"
                            +"<Minerador> " + log.getInt("idMinerador") + "</Minerador>";
                    }
                    //Negado
                    if(aceito == 2)
                    {
                        // Se foi negado, não fica pendente minerar
                        resultado = "";
                    }
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
            return resultado;
        }
        return resultado;
    }
    /**
     * Busca o estado da transação que tenha participação dos 3 peers passados nos parametros
     * @param idPeerRecebedor - ID do peer que recebe as moedas (e comandante da transação)
     * @param idPeerEnviador - ID do peer que esta enviando moedas
     * @param idPeerMinerador- ID do peer que esta minerando
     * @return Retorna o Estado da Transação - 0 para "start confirmado", 1 para "transação ok", 2 para "commited" e 3 para "refused"
     */
    public static int buscaEstadoLogFake(int idPeerRecebedor, int idPeerEnviador, int idPeerMinerador)
    {
        int estado = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select estado from logPeer where idpeerrecebedor = " + idPeerRecebedor
                            + " and idpeerminerador = " + idPeerMinerador +" and idpeerenviador = "
                            + idPeerEnviador + ";");
                while(log.next())
                {
                    estado = log.getInt("estado");
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
            return estado;
        }
        return estado;
    }
    /**
     * Busca o estado da transação que tenha participação dos 3 peers passados nos parametros
     * @param idPeerRecebedor - ID do peer que recebe as moedas (e comandante da transação)
     * @param idPeerEnviador - ID do peer que esta enviando moedas
     * @param idPeerMinerador- ID do peer que esta minerando
     * @return Retorna o Estado da Transação - 0 para "start confirmado", 1 para "transação ok", 2 para "commited" e 3 para "refused"
     */
    public static String buscaDescricaoLogFake(int idPeerRecebedor, int idPeerEnviador, int idPeerMinerador)
    {
        String descricao = "";
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial("Select descricao from logPeer where idpeerrecebedor = " + idPeerRecebedor
                            + " and idpeerminerador = " + idPeerMinerador +" and idpeerenviador = "
                            + idPeerEnviador + ";");
                while(log.next())
                {
                    descricao = log.getString("descricao");
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(FuncoesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            fecharConexaoDBOficial();
            return descricao;
        }
        return descricao;
    }
    /**
     * Busca o estado da transação que tenha participação dos 3 peers passados nos parametros
     * @param idPeerRecebedor - ID do peer que recebe as moedas (e comandante da transação)
     * @param idPeerEnviador - ID do peer que esta enviando moedas
     * @param idPeerMinerador- ID do peer que esta minerando
     * @param descricao - Descrição da transação ocorrida
     * @param estado - '0 - start confirmado 1 - transação ok 2 - commited 3 - refused';
     * @return Retorna o Estado da Transação - 0 para "start confirmado", 1 para "transação ok", 2 para "commited" e 3 para "refused"
     */
    public static int insereLog(int idPeerRecebedor, int idPeerEnviador, int idPeerMinerador, String descricao, int estado)
    {
        if(abrirConexaoDBOficial())
        {
            /*
            INSERT INTO logpeer(
            estado, descricao, idpeerrecebedor, idpeerminerador, idpeerenviador)
            VALUES (?, ?, ?, ?, ?);
            */
            try
            {
                if(executarComandoSQLDbOficial("INSERT INTO logpeer(estado, descricao,"
                        + " idpeerrecebedor, idpeerminerador, idpeerenviador)"
                        + "VALUES (" + estado + ""
                        + ",'" + descricao + "',"
                        + idPeerRecebedor + "," + idPeerMinerador +
                        "," + idPeerEnviador + ");"));
            }
            catch(Exception ex)
            {
                System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
            return estado;
        }
        return estado;
    }
    public static int insereLogFake(int idPeerRecebedor, int idPeerEnviador, int idPeerMinerador, String descricao, int estado)
    {
        if(abrirConexaoDBOficial())
        {
            /*
            INSERT INTO logpeer(
            estado, descricao, idpeerrecebedor, idpeerminerador, idpeerenviador)
            VALUES (?, ?, ?, ?, ?);
            */
            try
            {
                if(executarComandoSQLDbOficial("INSERT INTO logpeer(estado, descricao,"
                        + " idpeerrecebedor, idpeerminerador, idpeerenviador)"
                        + "VALUES (" + estado + ""
                        + ",'" + descricao + "',"
                        + idPeerRecebedor + "," + idPeerMinerador +
                        "," + idPeerEnviador + ");"));
            }
            catch(Exception ex)
            {
                System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
            return estado;
        }
        return estado;
    }
    
    public static boolean insertTransacaoDbOficial(String idAux, int idPeerEnviador, int idPeerRecebedor, int idPeerMinerador, int coins, int votoEnviador, int votoRecebedor, int votoMinerador, int estado)
    {
        if(abrirConexaoDBOficial())
        {
            try
            {
                if(executarComandoSQLDbOficial("INSERT INTO transacoes(idaux, idenviador,"
                        + " idrecebedor, idminerador, coins, votoenviador, votorecebedor, votominerador, estado) "
                        + "VALUES ('"
                        + idAux + "', "
                        + idPeerEnviador + ", "
                        + idPeerRecebedor + ", "
                        + idPeerMinerador + ", "
                        + coins + ", "
                        + votoEnviador + ", "
                        + votoRecebedor + ", "
                        + votoMinerador + ", "
                        + estado + 
                        ");"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
            }
            catch(Exception ex)
            {
                System.out.println("EX: " + ex);     
            }
        }
        fecharConexaoDBOficial();
        return false;
    }
    
    public static boolean insertTransacaoDbFake(String idAux, int idPeerEnviador, int idPeerRecebedor, int idPeerMinerador, int coins, int votoEnviador, int votoRecebedor, int votoMinerador, int estado)
    {
        if(abrirConexaoDBOficial())
        {
            try
            {
                if(executarComandoSQLDbOficial("INSERT INTO transacoes(idaux, idenviador, idrecebedor, idminerador, coins, votoenviador, votorecebedor, votominerador, estado) "
                        + "VALUES ('"
                        + idAux + "', "
                        + idPeerEnviador + ", "
                        + idPeerRecebedor + ", "
                        + idPeerMinerador + ", "
                        + coins + ", "
                        + votoEnviador + ", "
                        + votoRecebedor + ", "
                        + votoMinerador + ", "
                        + estado + 
                        ");"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
            }
            catch(Exception ex)
            {
                System.out.println("EX: " + ex);     
            }
        }
        fecharConexaoDBOficial();
        return false;
    }
    
    public static boolean atualizaTransacaoVotoEnviador(int idTransacao, int idEnviador, int voto)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial(
                    "UPDATE transacoes set votoenviador = " + voto +
                    " WHERE id = " + idTransacao + 
                    " AND idenviador = " + idEnviador + ";"
            ))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
        }
        return false;
    }
    public static boolean atualizaTransacaoVotoRecebedor(int idTransacao, int idRecebedor, int voto)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial(
                    "UPDATE transacoes set votorecebedor = " + voto +
                    " WHERE id = " + idTransacao + 
                    " AND idrecebedor = " + idRecebedor + ";"
            ))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
        }
        return false;
    }
    public static boolean atualizaTransacaoVotoMinerador(int idTransacao, int idMinerador, int voto)
    {
        if(abrirConexaoDBOficial())
        {
            if(executarComandoSQLDbOficial(
                    "UPDATE transacoes set votominerador = " + voto +
                    " WHERE id = " + idTransacao + 
                    " AND idminerador = " + idMinerador + ";"
            ))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
        }
        return false;
    }
    
    public static boolean atualizaEstadoTransacaoDbOficial(int idTransacao, int estado)
    {
        if(abrirConexaoDBOficial())
        {
            try
            {
                if(executarComandoSQLDbOficial("UPDATE transacoes" +
                        " set estado = " + estado + 
                        " where id = " + idTransacao +
                        ";"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
            }
            catch(Exception ex)
            {
                System.out.println("EX: " + ex);     
            }
        }
        fecharConexaoDBOficial();
        return false;
    }
    
    public static boolean atualizaTransacaoEstadoDbOficial(int idTransacao, int estado)
    {
        if(abrirConexaoDBOficial())
        {
            try
            {
                if(executarComandoSQLDbOficial("UPDATE transacoes" +
                        " set estado = " + estado + 
                        " where id = " + idTransacao +
                        ";"))
                {
                    fecharConexaoDBOficial();
                    return true;
                }
            }
            catch(Exception ex)
            {
                System.out.println("EX: " + ex);     
            }
        }
        fecharConexaoDBOficial();
        return false;
    }
    
    /**
     * Identifica se uma dada transação já encerrou.
     * Ou seja, se a coluna 'estado' da mesma contém 1 (TRANSAÇÃO OK) ou 3 (REFUSED).
     * @param idTransacao é a transação a ser verificada
     * @return true se a transação está encerrada, false caso contrário
     */
    public static boolean verificaTransacaoEncerrada(int idTransacao)
    {
        int estado = -1;
        if(abrirConexaoDBOficial())
        {
            try
            {
                ResultSet log = consultarDBOficial(
                        "select estado from transacoes " + 
                                "where id = " + idTransacao + 
                                ";");
                while(log.next())
                {
                    estado = log.getInt("estado");
                }
            }
            catch(SQLException ex)
            {
                System.out.println("EX: " + ex);     
            }
            fecharConexaoDBOficial();
        }
        return (estado == 1 || estado == 3);
    }
}
