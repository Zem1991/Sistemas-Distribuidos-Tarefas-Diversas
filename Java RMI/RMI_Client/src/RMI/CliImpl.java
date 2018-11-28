/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import InterfaceGrafica.IG_InteressesSatisfeitos;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;

/**
 *
 * @author Felipe Lopes Zem
 */
public class CliImpl extends UnicastRemoteObject implements InterfaceCli {

    private Registry referenciaServicoNomes;
    
    public CliImpl() throws RemoteException{
        super();
    }
    public CliImpl(Registry referenciaServicoNomes) throws RemoteException{
        this.referenciaServicoNomes = referenciaServicoNomes;
    }

    @Override
    public void echo(String s) throws RemoteException {
        System.out.println("ECHO: " + s);
    }
    /**
     * 
     * @throws RemoteException 
     */
    @Override
    public void call() throws RemoteException {
        try {
            InterfaceServ refServidor = (InterfaceServ) referenciaServicoNomes.lookup("ServImpl");
            refServidor.call("Olá Servidor.", this);
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(CliImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     * @param message
     * @return
     * @throws RemoteException 
     */
    @Override
    public String notifyMe(String message) throws RemoteException {
        String returnMessage = "Call back received: " + message;
        System.out.println(returnMessage);
        return returnMessage;
    }  
    /**
     * 
     * @param informacoes
     * @throws RemoteException 
     */
    @Override
    public void exibirInteressesSatisfeitos(TableModel informacoes) throws RemoteException {
        IG_InteressesSatisfeitos ig_InteressesSatisfeitos = new IG_InteressesSatisfeitos();
        ig_InteressesSatisfeitos.redefineTableContents(informacoes);
        ig_InteressesSatisfeitos.setVisible(true);
        System.out.println("O servidor enviou uma lista com interesses a serem satisfeitos.");
    }
    /**
     * 
     * @param sql
     * @throws RemoteException 
     */
    //METODOS USADOS PELO CLIENT PARA INICIAR ATIVIDADES NO SERVIDOR
    public void consultarDB(String sql) throws RemoteException{
        try {
            InterfaceServ refServidor = (InterfaceServ) referenciaServicoNomes.lookup("ServImpl");
            refServidor.consultarDB(sql);
            System.out.println("Comando enviado ao DB do servidor: " + sql);
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(CliImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     * @param interessado
     * @param tipo
     * @param id_local
     * @param preco
     * @param dataLimite
     * @return
     * @throws RemoteException 
     */
    public boolean insertInteresse(String interessado, int tipo, int id_local, int preco, String dataLimite) throws RemoteException {
        try {
            InterfaceServ refServidor = (InterfaceServ) referenciaServicoNomes.lookup("ServImpl");
            System.out.println("Iniciando inserção de Interesse. Enviando dados para cadastro no servidor.");
            refServidor.insertInteresse(interessado, tipo, id_local, preco, dataLimite);
            System.out.println("Operação concluida com sucesso.");
            return true;
        } catch (NotBoundException ex) {
            Logger.getLogger(CliImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}