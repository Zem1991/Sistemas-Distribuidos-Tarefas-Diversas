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
 * @author Felipe Lopes Zem
 */
public interface InterfaceCli extends Remote{
       //METODOS DO HELLOWORLD RMI
    public void echo(String s) throws RemoteException;
    public void call() throws RemoteException;
    public String notifyMe(String message) throws java.rmi.RemoteException;
    
    //METODOS DESTE PROGRAMA
    //public ArrayList buscarDadosDoDB(String sql) throws RemoteException;
    //public boolean insertInteresse(String interessado, int tipo, int id_local, int preco, String datalimite) throws RemoteException;
    public void exibirInteressesSatisfeitos(TableModel informacoes) throws RemoteException;
}
