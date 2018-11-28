/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 *
 * @author Felipe Lopes Zem
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
    
    public ServImpl() throws RemoteException{
        super( );
     clientList = new Vector();
    }
    private Vector clientList;

    @Override
    public void chamar(String nome, InterfaceCli referencia) throws RemoteException {
        referencia.echo(nome);
    }
    public String sayHello( ) throws java.rmi.RemoteException {
          return("hello");
      }
    @Override
    public synchronized void registerForCallback(InterfaceCli callbackClientObject)throws java.rmi.RemoteException
    {
      // store the callback object into the vector
        if (!(clientList.contains(callbackClientObject))) 
        {
            clientList.addElement(callbackClientObject);
            System.out.println("Registered new client ");
            doCallbacks();
        }
    }

    @Override
    public synchronized void unregisterForCallback(InterfaceCli callbackClientObject) throws java.rmi.RemoteException{
        if (clientList.removeElement(callbackClientObject)) 
        {
            System.out.println("Unregistered client ");
        } 
        else 
        {
            System.out.println("unregister: clientwasn't registered.");
        }
        
    }
  private synchronized void doCallbacks( ) throws java.rmi.RemoteException
  {
    // make callback to each registered client
    System.out.println( "**************************************\n" + "Callbacks initiated ---");
    for (int i = 0; i < clientList.size(); i++)
    {
        System.out.println("doing "+ i +"-th callback\n");    
        // convert the vector object to a callback object
        InterfaceCli nextClient = (InterfaceCli)clientList.elementAt(i);
        // invoke the callback method
        nextClient.notifyMe("Number of registered clients=" +  clientList.size());
    }// end for
    System.out.println("********************************\n" + "Server completed callbacks ---");
  } // doCallbacks
    
}
