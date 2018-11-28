/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

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
    public static void main(String[] args) throws RemoteException {
        Registry referenciaServicoNomes = LocateRegistry.getRegistry(null, 8001);
        CliImpl servente = new CliImpl(referenciaServicoNomes);
       // InterfaceServ refServidor = (InterfaceServ) referenciaServicoNomes.lookup("ServImpl");  //.chamar("CliImpl", this) ???
       // refServidor.chamar("Olá Servidor", servente);
       servente.chamar();
    }
    
}
