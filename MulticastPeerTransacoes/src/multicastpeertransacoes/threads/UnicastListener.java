/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastpeertransacoes.threads;

import Comunicacao.OperacoesCom;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import multicastpeertransacoes.IG_MAIN;
import multicastpeertransacoes.Main;

/**
 * Esta classe é um componente unitário de InterfaceGrafica. Como a classe InterfaceGrafica
 * só pode ser usada para lidar com a interação do usuário, é necessária a divisão de
 * funções para recebimento e tratativa de mensagens em uma Thread a parte.
 * @author Felipe
 */
public class UnicastListener extends Thread {
    private final IG_MAIN ig;
    private final String nome;
    private DatagramSocket dgSocket;
    private boolean isRunning = true;

    public UnicastListener(IG_MAIN ig)
    { 
        this.ig = ig;
        nome = ig.getNomePeer() + " (UnicastListener)";
        setName(nome);
    }
    
    public boolean isIsRunning() {return isRunning;}
    public void setIsRunning(boolean isRunning) {this.isRunning = isRunning;}
    
    @Override
    public void run() {
        if (Main.DEBUG_MSG_THREAD) System.out.println("multicastpeer.UnicastListener.run() for " + nome);
        try {
            //dgSocket.joinGroup(mcGroup);  //Só é necessário no Multicast.
            dgSocket = ig.getDgSocket();
            while(isRunning){
                DatagramPacket messageIn = new DatagramPacket(new byte[Main.RECEIVER_BUFFER_SIZE], Main.RECEIVER_BUFFER_SIZE);
                dgSocket.receive(messageIn);
                
                String msgIn = new String(messageIn.getData(), StandardCharsets.UTF_8); //String msgIn = EncryptionUtil.decrypt(messageIn.getData(), chavePrivada);
                if (Main.DEBUG_MSG_COM) System.out.println(nome + " received a Unicast message: " + msgIn);
                ig.printLogMessage(messageIn.getAddress().getHostAddress(), ig.getNomePeer(), msgIn.trim());
                                
                //Trata a mensagem recebida por Unicast (depois de descriptografar).
                if (OperacoesCom.tratarMensagem(ig, msgIn, messageIn.getAddress())){
                    System.out.println(nome + 
                            " recebeu e compreendeu a mensagem recebida.");
                }else{
                    System.out.println(nome + 
                            " não compreendeu esta mensagem: " + msgIn);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UnicastListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (Main.DEBUG_MSG_THREAD) System.out.println("multicastpeer.UnicastListener.stop() for " + nome);
    }
}
