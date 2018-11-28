/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.table.TableModel;

/**
 *
 * @author Felipe Lopes Zem & Klaus Diener
 */
public interface InterfaceServ extends Remote{
    //METODOS DO HELLOWORLD RMI
    public void call(String msg, InterfaceCli referencia) throws RemoteException;
    public String sayHello() throws java.rmi.RemoteException;
    public void registraCliente(InterfaceCli callbackClientObject) throws java.rmi.RemoteException;
    public void ApagarRegistroCliente(InterfaceCli callbackClientObject) throws java.rmi.RemoteException;
    
    //METODOS DESTE PROGRAMA
    public TableModel consultarDB(String sql) throws RemoteException; //Busca todas as informações sobre a tabela que o cliente quer ver: Reservas, Passagens, Destinos e Hoteis
    public ArrayList consultarDBComColunas(String sql) throws RemoteException;
    //public void comprarPassagem(int numeroCartao, String nomeComprador, int passagemID, int qtdPessoas, int parcelamento)throws RemoteException;
    //public void realizarReserva(int quartoID, String dataEntrada, String dataSaida , int qtdQuartos, int qtdPessoas, int numeroCartao, int parcelamento,String nomeComprador)throws RemoteException;
    public boolean insertInteresse(String interessado, int tipo, int id_local, int preco, String dataLimite) throws RemoteException; //Cliente preenche os dados para informar interesse em determinado destino ou hotel
    public boolean insertPacoteTuristico(String cliente, int numeroPessoas, int valorTotal, int status) throws RemoteException;
    public boolean insertVendaDePassagem(String cliente, String cartao, int parcelas, int numeroDePessoas, int valorTotal, int id_pacoteTuristico) throws RemoteException;
    public boolean insertItemVendaDePassagem(int id_venda, int id_passagem, String usuario) throws RemoteException;
    public boolean insertVendaDeHospedagem(String cliente, String cartao, int parcelas, int numeroDePessoas, int valorTotal, int id_pacoteTuristico) throws RemoteException;
    public boolean insertItemVendaDeHospedagem(int id_venda, int id_hospedagem, String hospedes, String dataentrada, String datasaida) throws RemoteException;
    //public void notificarClientsDeInteresses(ArrayList dados) throws RemoteException;    
}
