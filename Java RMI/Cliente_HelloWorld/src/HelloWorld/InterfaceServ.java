/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Felipe Lopes Zem
 */
public interface InterfaceServ extends Remote{
    public void chamar(String nome, InterfaceCli referencia) throws RemoteException;
    
    // This remote method allows an object client to 
// register for callback
// @param callbackClientObject is a reference to the
//        object of the client; to be used by the server
//        to make its callbacks.
public String sayHello( ) throws java.rmi.RemoteException;
     
public void registerForCallback(InterfaceCli callbackClientObject) throws java.rmi.RemoteException;

// This remote method allows an object client to 
// cancel its registration for callback

  public void unregisterForCallback(InterfaceCli callbackClientObject) throws java.rmi.RemoteException;
}
