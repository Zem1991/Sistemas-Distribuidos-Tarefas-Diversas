/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastpeer.threads;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import multicastpeer.InterfaceGrafica;
import multicastpeer.Main;
import operações.Operacoes;

/**
 * Esta classe é um componente unitário de InterfaceGrafica. Como a classe InterfaceGrafica
 * só pode ser usada para lidar com a interação do usuário, é necessária a divisão de
 * funções para mineração de coins em uma Thread a parte.
 * @author Felipe & Klaus
 */
public class CoinMiner extends Thread {
    private InterfaceGrafica ig;
    private String name;
    private InetAddress mcGroup;
    private MulticastSocket mcSocket;
    private boolean isRunning = false;
    
    //Array das operações que estão pendentes de serem tratadas por um minerador.
    //Todos os peers precisam saber que estas existem, mesmo que não estejam minerando.
     private ArrayList<String> pendingOps = new ArrayList<>();
    
    public CoinMiner(InterfaceGrafica ig)
    { 
        this.ig = ig;
        name = ig.getIgName() + " (CoinMiner)";
    }
    
    public boolean isIsRunning() {return isRunning;}
    public void setIsRunning(boolean isRunning) {this.isRunning = isRunning;}
    public ArrayList<String> getPendingOps() {return pendingOps;}
    
    public boolean insertMiningOperation(String msg){
        pendingOps.add(msg);
        //O peer atualiza na sua InterfaceGrafica quantas minerações estão pendentes.
        ig.updateWindowTitle();
        return true;
    }
    public boolean removeMiningOperation(String msg){
        if (pendingOps.remove(msg)){
            pendingOps.trimToSize();
            //O peer atualiza na sua InterfaceGrafica quantas minerações estão pendentes.
            ig.updateWindowTitle();
            return true;
        }else{
            //O peer atualiza na sua InterfaceGrafica quantas minerações estão pendentes.
            ig.updateWindowTitle();
            return false;
        }
    }
    
    @Override
    public void run() {
        System.out.println("multicastpeer.CoinMiner.run() for " + name);
        try {
            synchronized (this){    //Sem iso dá erro em "wait(Main.MINER_WAIT_TIME);"
                //O Peer (InterfaceGrafica) já executa o comando mcSocket.joinGroup(mcGroup);
                mcGroup = ig.getMcGroup();
                mcSocket = ig.getMcSocket();
                while(isRunning){
                    if(pendingOps.size() <= 0){
                        ig.printLogMessage(ig.getIgName(), "Nada pendente para minerar. Esperando por " + 
                                Main.MINER_WAIT_TIME + " milisegundos.");
                        wait(Main.MINER_WAIT_TIME);
                    }else{
                        //Pega aleatoriamente uma das operações que escutou (via MessageReceiver) e a minera.
                        int opId = 0; // (int) Math.round(Math.random() * (pendingOps.size()));
                        String op = pendingOps.get(opId);
                        String sender = op.split(";;;")[0];
                        String receiver = op.split(";;;")[1];
                        int amount = Integer.parseInt(op.split(";;;")[2]);
                        if (ig.getOps().minerar(sender, receiver, amount, ig.getIgName())){
                            ig.sendMessageMC(Main.MSG_MINING_IS_OK + 
                                    sender + ";;;" + 
                                    receiver + ";;;" + 
                                    amount + ";;;" + 
                                    ig.getIgName());
                        }else{
                            ig.sendMessageMC(Main.MSG_MINING_NOT_OK + 
                                    sender + ";;;" + 
                                    receiver + ";;;" + 
                                    amount + ";;;" + 
                                    ig.getIgName());
                        }
                    }
                }
            }
        }catch (InterruptedException ex) {
            Logger.getLogger(CoinMiner.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            //if(mcSocket != null) mcSocket.close();
        }
        System.out.println("multicastpeer.CoinMiner.stop() for " + getName());
    }
}
