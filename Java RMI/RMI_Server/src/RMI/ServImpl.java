/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import Database.Operations.DatabaseOperations;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.TableModel;

/**
 *
 * @author Felipe Lopes Zem & Klaus Diener
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
    private static ArrayList clientList;
    
    public ServImpl() throws RemoteException{
        super();
        clientList = new ArrayList();
    }
    
    /**
     * Método para enviar mensagem pelo prompt de comando para o cliente
     * @param msg - mensagem enviada
     * @param referencia - referencia do cliente
     * @throws RemoteException 
     */
    @Override
    public void call(String msg, InterfaceCli referencia) throws RemoteException {
        System.out.println("Message received: " + msg);
        referencia.echo("ECHO FROM SERVER.");
    }
    /**
     * 
     * @return
     * @throws RemoteException 
     */
    @Override
    public String sayHello() throws RemoteException {
        return("Hello.");
    }
    /**
     * Registra para receber call back - cliente chama esse metodo para se registrar no servidor
     * @param cliente - referencia do cliente
     * @throws RemoteException 
     */
    @Override
    public void registraCliente(InterfaceCli cliente) throws RemoteException {
        if (!(clientList.contains(cliente))) {
            clientList.add(cliente);
            callback();
        }
    }
    /**
     * Cliente remove seu regristro do cliente - Desconectado
     * @param cliente 
     * @throws RemoteException 
     */
    @Override
    public void ApagarRegistroCliente(InterfaceCli cliente) throws RemoteException {
        if (clientList.remove(cliente)){
            clientList.trimToSize();
        } 
        else{
            System.out.println("Deu Zika");
        }
    }    
    /**
     * Usado pelo método: registerForCallback - Realiza os CallBacks para os clientes
     * @throws java.rmi.RemoteException 
     */
    private synchronized void callback() throws java.rmi.RemoteException {
        for (int i = 0; i < clientList.size(); i++){
            InterfaceCli nextClient = (InterfaceCli) clientList.get(i);
        }
    }
/**
 * Realiza uma consulta no banco de dados do Servidor
 * @param sql - String contendo uma Query para realizar no banco de dados do servidor
 * @return TableModel referente a query realizada
 * @throws RemoteException 
 */
    @Override
    public TableModel consultarDB(String sql) throws RemoteException {
        ResultSet rs = DatabaseOperations.consultarDB(sql);
        TableModel tm = DatabaseOperations.converterTableModelParaResultSet(rs);
        DatabaseOperations.fecharConexãoDB();
        return tm;
    }
    /**
     * Mesma coisa que o Método Acima, não utilizado
     * @param sql
     * @return ArrayList com as colulas e linhas da tabela consultada
     * @throws RemoteException 
     */
    @Override
    public ArrayList consultarDBComColunas(String sql) throws RemoteException {
        ArrayList retorno = DatabaseOperations.consultarDBComColunas(sql);
        DatabaseOperations.fecharConexãoDB();
        return retorno;
    }
    /**
     * Insere interesse do cliente por receber novidades
     * @param interessado - referencia do cliente interessado
     * @param tipo - 0 passagem 1 hotel
     * @param id_local - local de interesse
     * @param preco - preco do interesse - só quer receber ofertas abaixo desse preço
     * @param dataLimite - data limite para receber os avisos
     * @return True(deu certo) ou False(deu erro)
     * @throws RemoteException 
     */
    @Override
    public boolean insertInteresse(String interessado, int tipo, int id_local, int preco, String dataLimite) throws RemoteException {
        return DatabaseOperations.insertInteresse(interessado, tipo, id_local, preco, dataLimite);
    }

    /**
     * Insere os dados de um novo pacote turístico.
     * @param cliente
     * @param numeroPessoas
     * @param valorTotal
     * @param status
     * @return
     */
    @Override
    public boolean insertPacoteTuristico(String cliente, int numeroPessoas, int valorTotal, int status){
        return DatabaseOperations.insertPacoteTuristico(cliente, numeroPessoas, valorTotal, status);
    }
    /**
     * Função que invoca o DatabaseOperations para inserir uma Venda de Passagem
     * @param cliente
     * @param cartao
     * @param parcelas
     * @param numeroDePessoas
     * @param valorTotal
     * @param id_pacoteTuristico
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean insertVendaDePassagem(String cliente, String cartao, int parcelas, int numeroDePessoas, int valorTotal, int id_pacoteTuristico) throws RemoteException {
        return DatabaseOperations.insertVendaDePassagem(cliente, cartao, parcelas, numeroDePessoas, valorTotal, id_pacoteTuristico);
    }
    /**
     * Funcao que invoca o databaseoperations para inserir iten venda de passagem
     * @param id_venda
     * @param id_passagem
     * @param usuario
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean insertItemVendaDePassagem(int id_venda, int id_passagem, String usuario) throws RemoteException {
        return DatabaseOperations.insertItemVendaDePassagem(id_venda, id_passagem, usuario);
    }
    /**
     * função que invoca databaseoperations para inserir venda de hospedagem
     * @param cliente
     * @param cartao
     * @param parcelas
     * @param numeroDePessoas
     * @param valorTotal
     * @param id_pacoteTuristico
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean insertVendaDeHospedagem(String cliente, String cartao, int parcelas, int numeroDePessoas, int valorTotal, int id_pacoteTuristico) throws RemoteException {
        return DatabaseOperations.insertVendaDeHospedagem(cliente, cartao, parcelas, numeroDePessoas, valorTotal, id_pacoteTuristico);
    }
    /**
     * função que invoca database para inserir item de venda de hospedagem
     * @param id_venda
     * @param id_hospedagem
     * @param hospedes
     * @param dataentrada
     * @param datasaida
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean insertItemVendaDeHospedagem(int id_venda, int id_hospedagem, String hospedes, String dataentrada, String datasaida) throws RemoteException {
        return DatabaseOperations.insertItemVendaDeHospedagem(id_venda, id_hospedagem, hospedes, dataentrada, datasaida);
    }
    
    /**
     * Exibe informações relevantes aos interesses do cliente que foram satisfeitos por novas ofertas do servidor
     * @param informacoes - Resultado da busca no banco de dados
     * @throws RemoteException 
     */
    //METODOS USADOS PELO SERVIDOR PARA INICIAR ATIVIDADES NOS CLIENTES
    public void exibirInteressesSatisfeitos(TableModel informacoes) throws RemoteException {
        InterfaceCli client;
        for (int i = 0; i < clientList.size(); i++){
            client = (InterfaceCli) clientList.get(i);
            client.exibirInteressesSatisfeitos(informacoes);
        }
        System.out.println("Todos os Clientes conhecidos foram notificados de interesses a serem satisfeitos.");
    }
    
    /*
    @Override
    public void comprarPassagem(int numeroCartao, String nomeComprador, int passagemID, int qtdPessoas, int parcelamento) throws RemoteException {
        DatabaseOperations.insertVendaDePassagem(nomeComprador, nomeComprador, parcelamento, qtdPessoas, qtdPessoas, parcelamento);
    }
    @Override
    public void realizarReserva(int quartoID, String dataEntrada, String dataSaida, int qtdQuartos, int qtdPessoas, int numeroCartao, int parcelamento, String nomeComprador) throws RemoteException {
        DatabaseOperations.insertVendaDeHospedagem(dataEntrada, dataSaida, parcelamento, qtdPessoas, quartoID, parcelamento);
    }

    public void notificarClientsDeInteresses(ArrayList dados) throws RemoteException{
        InterfaceCli client;
        try {
            for(int i = 0; i < Servidor.getReferenciaServicoNomes().list().length; i++){
                
                client = Servidor.getCliente();
                client.mostrarInteressesSatisfeitos(dados);
            }
        } catch (AccessException ex) {
            Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
}
