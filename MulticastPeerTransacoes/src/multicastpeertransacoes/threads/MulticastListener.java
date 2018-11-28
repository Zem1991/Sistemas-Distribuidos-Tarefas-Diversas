/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastpeertransacoes.threads;

import Comunicacao.OperacoesCom;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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
public class MulticastListener extends Thread {
    private final IG_MAIN ig;
    private final String nome;
    private InetAddress mcGroup;
    private MulticastSocket mcSocket;
    private boolean isRunning = true;

    public MulticastListener(IG_MAIN ig)
    { 
        this.ig = ig;
        nome = ig.getNomePeer() + " (MulticastListener)";
        setName(nome);
    }
    
    public boolean isIsRunning() {return isRunning;}
    public void setIsRunning(boolean isRunning) {this.isRunning = isRunning;}
    
    @Override
    public void run() {
        if (Main.DEBUG_MSG_THREAD) System.out.println("multicastpeer.MulticastListener.run() for " + nome);
        try {
            //O Peer (InterfaceGrafica) já executa o comando mcSocket.joinGroup(mcGroup);
            mcGroup = ig.getMcGroup();
            mcSocket = ig.getMcSocket();
            while(isRunning){
                DatagramPacket messageIn = new DatagramPacket(new byte[Main.RECEIVER_BUFFER_SIZE], Main.RECEIVER_BUFFER_SIZE);
                mcSocket.receive(messageIn);
                
                String msgIn = new String(messageIn.getData(), StandardCharsets.UTF_8);
                if (Main.DEBUG_MSG_COM) System.out.println(nome + " received a Multicast message: " + msgIn);
                ig.printLogMessage(messageIn.getAddress().getHostAddress(), ig.getNomePeer(), msgIn.trim());
                
                //Trata a mensagem recebida por Multicast.
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
        if (Main.DEBUG_MSG_THREAD) System.out.println("multicastpeer.MulticastListener.stop() for " + nome);
    }
}
