/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Lopes Zem
 */
public class Cliente {
    private static CliImpl servente;
    private static InterfaceServ server;
    
    /**
     * ESTE MÉTODO SÓ DEVE SER UTILIZADO PARA TESTAR A CONEXÃO DO SERVIDOR!
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException {
        inicializarCliente();
    }
    
//    public static CliImpl getServente() {
//        return servente;
//    }
    public static InterfaceServ getServer() {
        return server;
    }

    /**
     * 
     * @throws RemoteException 
     */
    public static void inicializarCliente() throws RemoteException {
        Registry referenciaServicoNomes = LocateRegistry.getRegistry(null, 8001);
        String registryURL = "rmi://localhost:8001" + "/callback";
        
        try {
            //InterfaceServ h =(InterfaceServ)Naming.lookup(registryURL);  [server==h]
            server =(InterfaceServ)Naming.lookup(registryURL);
            System.out.println("Server said " + server.sayHello());
            InterfaceCli callbackObj = new CliImpl();
            server.registraCliente(callbackObj);
            
            servente = new CliImpl(referenciaServicoNomes);
            // InterfaceServ refServidor = (InterfaceServ) referenciaServicoNomes.lookup("ServImpl");  //.chamar("CliImpl", this) ???
            // refServidor.chamar("Olá Servidor", servente);
            servente.call();
        } catch (NotBoundException | MalformedURLException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    public static Registry getReferenciaServicoNomes() {return referenciaServicoNomes;}
    public static void setReferenciaServicoNomes(Registry referenciaServicoNomes) {Cliente.referenciaServicoNomes = referenciaServicoNomes;}
    public static CliImpl getServente() {return servente;}
    public static void setServente(CliImpl servente) {Cliente.servente = servente;}
    
    public static InterfaceServ getServidor(){
        try {
            return (InterfaceServ) referenciaServicoNomes.lookup("ServImpl");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    */
}
