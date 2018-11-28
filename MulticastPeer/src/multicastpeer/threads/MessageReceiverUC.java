/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastpeer.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
public class MessageReceiverUC extends Thread {
    private InterfaceGrafica ig;
    private DatagramSocket dgSocket;
    private boolean isRunning = true;
    //private PublicKey chavePublica;
    //private PrivateKey chavePrivada;

    public MessageReceiverUC(InterfaceGrafica ig)
    { 
        this.ig = ig;
        setName(ig.getIgName() + " (MessageReceiverUC)");
    }
    
    public boolean isIsRunning() {return isRunning;}
    public void setIsRunning(boolean isRunning) {this.isRunning = isRunning;}
    
    @Override
    public void run() {
        System.out.println("multicastpeer.MessageReceiverUC.run() for " + getName());
        try {
            //dgSocket.joinGroup(mcGroup);  //Só é necessário no Multicast.
            dgSocket = ig.getDgSocket();
            while(isRunning){
                DatagramPacket messageIn = new DatagramPacket(new byte[Main. RECEIVER_BUFFER_SIZE], Main.RECEIVER_BUFFER_SIZE);
                dgSocket.receive(messageIn);
                
                String msgIn = new String(messageIn.getData(), StandardCharsets.UTF_8); //String msgIn = EncryptionUtil.decrypt(messageIn.getData(), chavePrivada);
                if (Main.SHOW_DEBUG_STUFF) System.out.println(getName() + " received a Unicast message: " + msgIn);
                ig.printLogMessage("MSG RECEIVED", msgIn);
                                
                //Trata a mensagem recebida por Unicast (depois de descriptografar).
                if (!ig.getOps().tratarMensagem(ig, msgIn, messageIn.getAddress())){
                    System.out.println(getName() + 
                            " não conseguiu interpretar a última mensagem recebida como uma operação válida entre peers (Unicast).");
                }
            }
        }catch (SocketException ex){
            Logger.getLogger(MessageReceiverUC.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex){
            Logger.getLogger(MessageReceiverUC.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            //if(dgSocket != null) dgSocket.close();
        }
        System.out.println("multicastpeer.MessageReceiverUC.stop() for " + getName());
    }
}
