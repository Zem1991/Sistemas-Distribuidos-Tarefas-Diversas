/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Felipe Lopes Zem
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
    
    ServImpl() throws RemoteException{
    }

    @Override
    public void chamar(String nome, InterfaceCli referencia) throws RemoteException{
        referencia.echo(nome);
    }
    
}
