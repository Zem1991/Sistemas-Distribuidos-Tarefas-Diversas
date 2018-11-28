/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Lopes Zem
 */
public class Servidor {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        String registryURL;
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(8001);
        
        startRegistry(8001);
        ServImpl servente = new ServImpl();
        registryURL = "rmi://localhost:8001" + "/callback";
        Naming.rebind(registryURL, servente);
        
        try 
        {
            referenciaServicoNomes.bind("ServImpl", servente);
        } catch (AlreadyBoundException | AccessException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void startRegistry(int RMIPortNum) throws RemoteException
    {
        try 
        {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list( );  
            // This call will throw an exception
            // if the registry does not already exist
        }
        catch (RemoteException e) 
        { 
          // No valid registry at that port.
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
        }
    } // end startRegistry
}
