/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import RMI.Servidor;
import InterfaceGrafica.IG_Main;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Lopes Zem
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Servidor.inicializarServidor();
            System.out.println("Servidor RMI inicializado.");
            IG_Main ig_Main = new IG_Main();
            ig_Main.setVisible(true);
            System.out.println("Interface Gráfica Servidor inicializada.");
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
