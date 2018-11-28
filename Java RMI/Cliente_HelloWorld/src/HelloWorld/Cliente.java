/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Felipe Lopes Zem
 */
public class Cliente {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        Registry referenciaServicoNomes = LocateRegistry.getRegistry(null, 8001);
        String registryURL = "rmi://localhost:8001" + "/callback"; 
        
        InterfaceServ h =(InterfaceServ)Naming.lookup(registryURL);
        System.out.println("Server said " + h.sayHello());
        InterfaceCli callbackObj = new CliImpl();
        h.registerForCallback(callbackObj);
        
        CliImpl servente = new CliImpl(referenciaServicoNomes);
       // InterfaceServ refServidor = (InterfaceServ) referenciaServicoNomes.lookup("ServImpl");  //.chamar("CliImpl", this) ???
       // refServidor.chamar("Olá Servidor", servente);
       servente.chamar();
    }
}