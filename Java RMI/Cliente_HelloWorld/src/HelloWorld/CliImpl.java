/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Lopes Zem
 */
public class CliImpl extends UnicastRemoteObject implements InterfaceCli {

    private Registry referenciaServicoNomes;
    
    public CliImpl() throws RemoteException 
    {
        super( );
    }
    CliImpl(Registry referenciaServicoNomes) throws RemoteException{
        this.referenciaServicoNomes = referenciaServicoNomes;
    }

    @Override
    public void echo(String s) throws RemoteException{
        System.out.println("ECHO: " + s);
    }
    
    public void chamar() throws RemoteException{
        try {
            InterfaceServ refServidor = (InterfaceServ) referenciaServicoNomes.lookup("ServImpl");  //.chamar("CliImpl", this) ???
            refServidor.chamar("Olá Servidor", this);
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(CliImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String notifyMe(String message)
    {
        String returnMessage = "Call back received: " + message;
        System.out.println(returnMessage);
        return returnMessage;
    }  

    
}
