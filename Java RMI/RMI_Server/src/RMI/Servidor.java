/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

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
    private static ServImpl servente;
    
    /**
     * ESTE MÉTODO SÓ DEVE SER UTILIZADO PARA TESTAR A CONEXÃO DO CLIENT!
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException {
        inicializarServidor();
    }

    public static ServImpl getServente() {
        return servente;
    }
    
    public static void inicializarServidor() throws RemoteException {
        String registryURL;
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(8001);
        
        startRegistry(8001);
        //ServImpl servente = new ServImpl();
        servente = new ServImpl();
        registryURL = "rmi://localhost:8001" + "/callback";
        
        try {
            Naming.rebind(registryURL, servente);
            referenciaServicoNomes.bind("ServImpl", servente);
        } catch (AlreadyBoundException | AccessException | MalformedURLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Usado pelo método: inicializarServidor
     * @param RMIPortNum
     * @throws RemoteException 
     */
    private static void startRegistry(int RMIPortNum) throws RemoteException{
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            // This call will throw an exception
            // if the registry does not already exist
            registry.list();
        }
        catch (RemoteException e) { 
            // No valid registry at that port.
            LocateRegistry.createRegistry(RMIPortNum);
        }
    }
    
    /*
    public static void inicializarServidor() throws RemoteException {
        referenciaServicoNomes = LocateRegistry.createRegistry(8001);
        servente = new ServImpl();
        
        try {
            referenciaServicoNomes.bind("ServImpl", servente);
        } catch (AlreadyBoundException | AccessException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static InterfaceCli getCliente(){
        try {
            return (InterfaceCli) referenciaServicoNomes.lookup("CliImpl");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("");
        }
        return null;
    }

    public static Registry getReferenciaServicoNomes() {return referenciaServicoNomes;}
    public static void setReferenciaServicoNomes(Registry referenciaServicoNomes) {Servidor.referenciaServicoNomes = referenciaServicoNomes;}
    public static ServImpl getServente() {return servente;}
    public static void setServente(ServImpl servente) {Servidor.servente = servente;}
    */
}