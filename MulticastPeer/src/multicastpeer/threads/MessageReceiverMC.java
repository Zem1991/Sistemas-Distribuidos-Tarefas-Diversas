/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastpeer.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
//import java.security.PrivateKey;
//import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import multicastpeer.InterfaceGrafica;
import multicastpeer.Main;
import operações.Operacoes;
//import utilitarios.EncryptionUtil;

/**
 * Esta classe é um componente unitário de InterfaceGrafica. Como a classe InterfaceGrafica
 * só pode ser usada para lidar com a interação do usuário, é necessária a divisão de
 * funções para recebimento e tratativa de mensagens em uma Thread a parte.
 * @author Felipe
 */
public class MessageReceiverMC extends Thread {
    private InterfaceGrafica ig;
    private InetAddress mcGroup;
    private MulticastSocket mcSocket;
    private boolean isRunning = true;
    //private PublicKey chavePublica;
    //private PrivateKey chavePrivada;

    public MessageReceiverMC(InterfaceGrafica ig)
    { 
        this.ig = ig;
        setName(ig.getIgName() + " (MessageReceiverMC)");
    }
    
    public boolean isIsRunning() {return isRunning;}
    public void setIsRunning(boolean isRunning) {this.isRunning = isRunning;}
    
    @Override
    public void run() {
        System.out.println("multicastpeer.MessageReceiverMC.run() for " + getName());
        try {
            //O Peer (InterfaceGrafica) já executa o comando mcSocket.joinGroup(mcGroup);
            mcGroup = ig.getMcGroup();
            mcSocket = ig.getMcSocket();
            while(isRunning){
                DatagramPacket messageIn = new DatagramPacket(new byte[Main. RECEIVER_BUFFER_SIZE], Main.RECEIVER_BUFFER_SIZE);
                mcSocket.receive(messageIn);
                
                String msgIn = new String(messageIn.getData(), StandardCharsets.UTF_8);
                if (Main.SHOW_DEBUG_STUFF) System.out.println(getName() + " received a Multicast message: " + msgIn);
                ig.printLogMessage("MSG RECEIVED", msgIn);
                
                //Trata a mensagem recebida por Multicast.
                if (!ig.getOps().tratarMensagem(ig, msgIn, messageIn.getAddress())){
                    System.out.println(getName() + 
                            " não conseguiu interpretar a última mensagem recebida como uma operação válida entre peers (Multicast).");
                }
            }
        }catch (SocketException ex){
            Logger.getLogger(MessageReceiverMC.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex){
            Logger.getLogger(MessageReceiverMC.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            //if(mcSocket != null) mcSocket.close();
        }
        System.out.println("multicastpeer.MessageReceiverMC.stop() for " + getName());
    }
}
