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
public interface InterfaceCli extends Remote{
    public void echo(String s) throws RemoteException;
    
    public String notifyMe(String message) throws java.rmi.RemoteException;
}