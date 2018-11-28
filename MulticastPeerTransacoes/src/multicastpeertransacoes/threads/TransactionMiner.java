///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package multicastpeertransacoes.threads;
//
//import Comunicacao.OperacoesCom;
//import DataBase.FuncoesDB;
//import java.net.InetAddress;
//import java.net.MulticastSocket;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import multicastpeertransacoes.IG_MAIN;
//import multicastpeertransacoes.Main;
//
///**
// * Esta classe é um componente unitário de InterfaceGrafica. Como a classe InterfaceGrafica
// * só pode ser usada para lidar com a interação do usuário, é necessária a divisão de
// * funções para mineração de coins em uma Thread a parte.
// * @author Felipe & Klaus
// */
//public class TransactionMiner extends Thread {
//    private final IG_MAIN ig;
//    private final String nome;
//    private InetAddress mcGroup;
//    private MulticastSocket mcSocket;
//    private boolean isRunning = true;
//    
//    //Array das operações que estão pendentes de serem tratadas por um minerador.
//    //Todos os peers precisam saber que estas existem, mesmo que não estejam minerando.
//    private ArrayList<String> pendingOps = new ArrayList<>();
//    
//    public TransactionMiner(IG_MAIN ig)
//    { 
//        this.ig = ig;
//        nome = ig.getNomePeer() + " (TransactionMiner)";
//        setName(nome);
//    }
//    
//    //public boolean isIsRunning() {return isRunning;}
//    //public void setIsRunning(boolean isRunning) {this.isRunning = isRunning;}
//    public ArrayList<String> getPendingOps() {return pendingOps;}
//    
//    public boolean insertMiningOperation(String msg){
//        pendingOps.add(msg);
//        //O peer atualiza na sua InterfaceGrafica quantas minerações estão pendentes.
//        ig.atualizarContagemMineracoes(pendingOps.size());
//        return true;
//    }
//    public boolean removeMiningOperation(String msg){
//        if (pendingOps.remove(msg)){
//            pendingOps.trimToSize();
//            //O peer atualiza na sua InterfaceGrafica quantas minerações estão pendentes.
//            ig.atualizarContagemMineracoes(pendingOps.size());
//            return true;
//        }else{
//            //O peer atualiza na sua InterfaceGrafica quantas minerações estão pendentes.
//            ig.atualizarContagemMineracoes(pendingOps.size());
//            return false;
//        }
//    }
//    
//    @Override
//    public void run() {
//        if (Main.DEBUG_MSG_THREAD) System.out.println("multicastpeer.TransactionMiner.run() for " + nome);
//        try {
//            synchronized (this){    //Sem iso dá erro em "wait(Main.MINER_WAIT_TIME);"
//                //O Peer (InterfaceGrafica) já executa o comando mcSocket.joinGroup(mcGroup);
//                mcGroup = ig.getMcGroup();
//                mcSocket = ig.getMcSocket();
//                while(isRunning){
//                    int id, idEnviador, idRecebedor, qtdCoins, idTransacao;
//                    ResultSet rs = FuncoesDB.buscaMineracaoPendente(ig.getIdPeer());
//                    while(rs.next())
//                    {
//                        id = Integer.parseInt(rs.getString("id"));
//                        idEnviador = Integer.parseInt(rs.getString("idenviador"));
//                        idRecebedor = Integer.parseInt(rs.getString("idrecebedor"));
//                        qtdCoins = Integer.parseInt(rs.getString("qtdcoins"));
//                        //idMinerador = Integer.parseInt(rs.getString("idminerador"));
//                        idTransacao = Integer.parseInt(rs.getString("idtransacao"));
//                        String sender = FuncoesDB.buscaPeerNome(idEnviador);
//                        String receiver = FuncoesDB.buscaPeerNome(idRecebedor);
//                        if (OperacoesCom.minerar(sender, receiver, qtdCoins, ig.getNomePeer())){
//                            ig.sendMessageMC(Main.MSG_MINING_IS_OK + 
//                                    idTransacao + ";;;" + 
//                                    1 + ";;;" +     //estado 1 - TRANSAÇÃO OK
//                                    ig.getIdPeer());
//                        }else{
//                            ig.sendMessageMC(Main.MSG_MINING_NOT_OK + 
//                                    idTransacao + ";;;" + 
//                                    3 + ";;;" +     //estado 3 - REFUSED
//                                    ig.getIdPeer());
//                        }
//                        FuncoesDB.removerMineracao(id);
//                        wait(Main.MINER_WAIT_TIME);
//                    }
//                }
//            }
//        }catch (InterruptedException | SQLException ex) {
//            Logger.getLogger(TransactionMiner.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (Main.DEBUG_MSG_THREAD) System.out.println("multicastpeer.TransactionMiner.stop() for " + nome);
//    }
//}
