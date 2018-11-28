/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastpeertransacoes;

import DataBase.ControleDB;
import DataBase.FuncoesDB;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import multicastpeertransacoes.threads.MulticastListener;
//import multicastpeertransacoes.threads.TransactionMiner;
import multicastpeertransacoes.threads.UnicastListener;

/**
 *
 * @author Felipe Lopes Zem
 */
public final class IG_MAIN extends javax.swing.JFrame {
    private int idPeer;
    private String nomePeer;
    private int coins;
    
    private InetAddress mcGroup;                //Endereço usado para o grupo Multicast.
    private MulticastSocket mcSocket;           //Scoket usado para mensagens Multicast.
    private DatagramSocket dgSocket;            //Socket usado para mensagens Unicast.
    private MulticastListener multicastListener;//Thread recebedora de mensagens Multicast.
    private UnicastListener unicastListener;    //Thread recebedora de mensagens Unicast.
    //private TransactionMiner transactionMiner;  //Thread mineradora de transações de Coins.

    public int getIdPeer() {return idPeer;}
    public String getNomePeer(){return nomePeer;}
    public void setNomePeer(String nomePeer){this.nomePeer = nomePeer;}
    public InetAddress getMcGroup() {return mcGroup;}
    public MulticastSocket getMcSocket() {return mcSocket;}
    public DatagramSocket getDgSocket() {return dgSocket;}
    
    private boolean travado = false;             //Usado posteriormente para travar este Peer quando estiver envolvido em uma transação.
    private int transacaoAtual = 0;             //Usada posteriormente para travar este Peer quando estiver envolvido em uma transação.

    /**
     * Creates new form IG
     */
    public IG_MAIN() {
        initComponents();
    }
    public IG_MAIN(int idPeer) {
        initComponents();
        this.idPeer = idPeer;
    }
    public IG_MAIN(int idPeer, String nomePeer, String inetAddress, int multicastSocket, int unicastSocket, int coins) {
        initComponents();
        this.idPeer = idPeer;
        this.nomePeer = nomePeer;
        this.coins = coins;
        
        try {
            this.setTitle(this.getTitle() + ": " + this.nomePeer);
            
            mcGroup = InetAddress.getByName(inetAddress);
            mcSocket = new MulticastSocket(multicastSocket);
            dgSocket = new DatagramSocket(unicastSocket);
            //gerarChaves();      //Gera chaves publica e privada.
            
            if (Main.DEBUG_MSG_COM) System.out.println("Peer " + nomePeer + " iniciado com endereço multicast " + mcGroup.getHostAddress() +
                    ", socket Multicast " + mcSocket.getLocalPort() +
                    " e socket Unicast " + dgSocket.getLocalPort()  + ".");
            
            String self = InetAddress.getLocalHost().getHostName();
            printLogMessage(self, self, "Using Peer Name " + nomePeer + ".");
            mcSocket.joinGroup(mcGroup);
            printLogMessage(nomePeer, nomePeer, "Connected to Multicast group " + mcGroup.getHostAddress() +
                    ". Multicast socket is " + mcSocket.getLocalPort()+ ". Unicast socket is " + dgSocket.getLocalPort()+ ".");
            
            multicastListener = new MulticastListener(this);
            unicastListener = new UnicastListener(this);
            //transactionMiner = new TransactionMiner(this);
            
            inicializarThreads();
            
            sendMessageMC(Main.MSG_PEER_OPEN +
                nomePeer + ";;;" + 
                dgSocket.getLocalPort() + ";;;" + 
                coins + ";;;" + 
                1);
        } catch (IOException ex) {
            Logger.getLogger(IG_MAIN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void atualizarDadosDoPeer(){
        try {
            l_peerId.setText("" + idPeer);
            l_peerNome.setText(nomePeer);
            l_peerEndereco.setText(InetAddress.getLocalHost().getHostAddress());
            l_peerPortaUnicast.setText("" + dgSocket.getLocalPort());
            l_peerGrupoMulticast.setText("" + mcGroup.getHostAddress());
            l_peerPortaMulticast.setText("" + mcSocket.getLocalPort());
            l_peerCoins.setText("" + coins);
        } catch (UnknownHostException ex) {
            Logger.getLogger(IG_MAIN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void atualizarContagemEnvios(int i) {
        l_recebimentosPendentes.setText("" + i);
    }
    public void atualizarContagemMineracoes(int i) {
        l_mineracoesPendentes.setText("" + i);
    }
    
    public synchronized boolean printLogMessage(String source, String destination, String msg){
        if (!source.isEmpty() && !msg.isEmpty()){
            ta_logs.setText(ta_logs.getText() + 
                    new SimpleDateFormat(Main.TIMESTAMP_FORMAT).format(new Date()) + 
                    " [" + source + " >> " + destination + "] " + msg + "\n");
            return true;
        }else{
            System.out.println("multicastpeer.InterfaceGrafica.printLogMessage() OU A ORIGEM OU A MENSAGEM ESTÁ EM BRANCO.");
            return false;
        }
    }
    
    /**
     * Envia uma mensagem multicast contendo uma String. Pode ser usado como ferramenta
     * de chat, ou para forçar operações de envio, recebimento e mineração de startingCoins.
     * @param message é a mensagem a enviar.
     * @return true se enviou com sucesso, falso se não.
     */
    public synchronized boolean sendMessageMC(String message){
        if (!message.isEmpty()){
            try {
                DatagramPacket messageOut = new DatagramPacket(message.getBytes(), message.getBytes().length, mcGroup, mcSocket.getLocalPort());
                mcSocket.send(messageOut);
                if (Main.DEBUG_MSG_COM) System.out.println("[Multicast message] FROM " + nomePeer + 
                        " TO EVERYONE USING PORT " + mcSocket.getLocalPort() + ": " + 
                        message);
                printLogMessage(nomePeer, "MULTICAST GROUP", message);
                return true;
            } catch (IOException ex) {
                Logger.getLogger(IG_MAIN.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }else{
            JOptionPane.showMessageDialog(this, 
                    "Tentou-se enviar uma mensagem vazia para o grupo de multicast. Operação abortada.", 
                    "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Envia uma mensagem unicast na forma byte[]. Pode ser usado como ferramenta de 
     * chat, ou para forçar operações de envio, recebimento e mineração de startingCoins.
     * @param message é a mensagem a enviar.
     * @param destination é o nome do Peer que deve receber esta mensagem (mostrado no log).
     * @param address é o endereço para o qual enviar.
     * @param port é a porta par utilizar no envio.
     * @return true se enviou com sucesso, falso caso contrário.
     */
    public synchronized boolean sendMessageUC(String message, String destination, InetAddress address, int port){
        if (!message.isEmpty()){
            try {
                DatagramPacket messageOut = new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
                dgSocket.send(messageOut);
                if (Main.DEBUG_MSG_COM) System.out.println("[Unicast message] FROM " + nomePeer + 
                        " TO ADDRESS " + address + " USING PORT " + port + ": " + 
                        message);
                printLogMessage(nomePeer, destination, message);
                return true;
            } catch (IOException ex) {
                Logger.getLogger(IG_MAIN.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }else{
            JOptionPane.showMessageDialog(this, 
                    "Tentou-se enviar uma mensagem vazia para um peer. Operação abortada.", 
                    "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
//    /**
//     * Acessa a thread de mineração deste Peer.
//     * @return a própria thread mineradora
//     */
//    public TransactionMiner accessTransactionMiner(){
//        return transactionMiner;
//    }
    
    /**
     * Trava as funcionalidades de Envio, Recebimento e Mineração. Usada para garantir
     * que um determinado Peer está limitado a uma operação por vez.
     * @param transacaoAtual indica qual é a transação que este Peer está esperando finalizar.
     */
    public void travarPeer(int transacaoAtual){
        if (!travado && this.transacaoAtual == 0){
            panel_enviar.setEnabled(false);
            panel_receber.setEnabled(false);
            panel_minerar.setEnabled(false);
            
            tf_peerEnvio.setEnabled(false);
            tf_valorEnvio.setEnabled(false);
            b_envio.setEnabled(false);
            b_recebimento.setEnabled(false);
            b_mineracao.setEnabled(false);
            b_transacao.setEnabled(false);
            mi_dadosRecebimentos.setEnabled(false);
            mi_dadosMineracoes.setEnabled(false);
            
            travado = true;
            this.transacaoAtual = transacaoAtual;
            b_transacao.setEnabled(true);
        }else{
            JOptionPane.showMessageDialog(this,
                    "Tentou-se travar este Peer com a transação nº" + transacaoAtual + "\n"
                            + "Peer está travado? " + travado + "\n"
                            + "Transação pendente? " + this.transacaoAtual
                    ,
                    "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Destrava as funcionalidades de Envio, Recebimento e Mineração. Usada após a
     * operação que estava em andamento ter sido finalizada.
     * @param transacaoAtual é usada para confirmar a transação que este Peer está esperando finalizar.
     */
    public void destravarPeer(int transacaoAtual){
        if (travado && this.transacaoAtual == transacaoAtual){
            panel_enviar.setEnabled(true);
            panel_receber.setEnabled(true);
            panel_minerar.setEnabled(true);
            
            tf_peerEnvio.setEnabled(true);
            tf_valorEnvio.setEnabled(true);
            b_envio.setEnabled(true);
            b_recebimento.setEnabled(true);
            b_mineracao.setEnabled(true);
            b_transacao.setEnabled(true);
            mi_dadosRecebimentos.setEnabled(true);
            mi_dadosMineracoes.setEnabled(true);
            
            travado = false;
            this.transacaoAtual = 0;
            b_transacao.setEnabled(false);
        }else{
            JOptionPane.showMessageDialog(this,
                    "Tentou-se destravar este Peer com a transação nº" + transacaoAtual + "\n"
                            + "Peer está travado? " + travado + "\n"
                            + "Transação pendente? " + this.transacaoAtual
                    ,
                    "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Inicializa as threads de escuta de mensagens multicast e unicast.
     * Também gera as devidas mensagens para outros peers no grupo multicast.
     * @return 
     */
    private void inicializarThreads(){
        multicastListener.start();
        unicastListener.start();
        //transactionMiner.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_logs = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_logs = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        panel_meusDados = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        l_peerId = new javax.swing.JLabel();
        l_peerNome = new javax.swing.JLabel();
        l_peerEndereco = new javax.swing.JLabel();
        l_peerPortaUnicast = new javax.swing.JLabel();
        l_peerGrupoMulticast = new javax.swing.JLabel();
        l_peerPortaMulticast = new javax.swing.JLabel();
        l_peerCoins = new javax.swing.JLabel();
        panel_enviar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tf_peerEnvio = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tf_valorEnvio = new javax.swing.JTextField();
        b_envio = new javax.swing.JButton();
        panel_receber = new javax.swing.JPanel();
        b_recebimento = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        l_recebimentosPendentes = new javax.swing.JLabel();
        panel_minerar = new javax.swing.JPanel();
        b_mineracao = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        l_mineracoesPendentes = new javax.swing.JLabel();
        panel_transacao = new javax.swing.JPanel();
        b_transacao = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        m_recuperarPeer = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mi_deslogar = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mi_sair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mi_dadosPeer = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mi_dadosRecebimentos = new javax.swing.JMenuItem();
        mi_dadosMineracoes = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mi_ajuda = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mi_sobre = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        m_gerarPeer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MulticastPeer com Transações");

        panel_logs.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "LOG"));

        ta_logs.setEditable(false);
        ta_logs.setColumns(20);
        ta_logs.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        ta_logs.setRows(5);
        jScrollPane1.setViewportView(ta_logs);

        javax.swing.GroupLayout panel_logsLayout = new javax.swing.GroupLayout(panel_logs);
        panel_logs.setLayout(panel_logsLayout);
        panel_logsLayout.setHorizontalGroup(
            panel_logsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
        );
        panel_logsLayout.setVerticalGroup(
            panel_logsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        panel_meusDados.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "MEUS DADOS"));

        jLabel5.setText("ID: ");

        jLabel6.setText("Nome: ");

        jLabel9.setText("Endereço: ");

        jLabel7.setText("Porta Unicast: ");

        jLabel10.setText("Grupo Multicast: ");

        jLabel11.setText("Porta Multicast: ");

        jLabel8.setText("Coins: ");

        l_peerId.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_peerId.setText("--");

        l_peerNome.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_peerNome.setText("--");

        l_peerEndereco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_peerEndereco.setText("--");

        l_peerPortaUnicast.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_peerPortaUnicast.setText("--");

        l_peerGrupoMulticast.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_peerGrupoMulticast.setText("--");

        l_peerPortaMulticast.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_peerPortaMulticast.setText("--");

        l_peerCoins.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_peerCoins.setText("--");

        javax.swing.GroupLayout panel_meusDadosLayout = new javax.swing.GroupLayout(panel_meusDados);
        panel_meusDados.setLayout(panel_meusDadosLayout);
        panel_meusDadosLayout.setHorizontalGroup(
            panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_meusDadosLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_peerId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel_meusDadosLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_peerNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel_meusDadosLayout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_peerEndereco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel_meusDadosLayout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_peerPortaUnicast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel_meusDadosLayout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_peerGrupoMulticast, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
            .addGroup(panel_meusDadosLayout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_peerPortaMulticast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel_meusDadosLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_peerCoins, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_meusDadosLayout.setVerticalGroup(
            panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_meusDadosLayout.createSequentialGroup()
                .addGroup(panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(l_peerId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(l_peerNome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(l_peerEndereco))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(l_peerPortaUnicast))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(l_peerGrupoMulticast))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(l_peerPortaMulticast))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_meusDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(l_peerCoins)))
        );

        jScrollPane2.setViewportView(panel_meusDados);

        panel_enviar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Enviar"));

        jLabel1.setText("Peer? (id/nome)");

        jLabel2.setText("Quanto?");

        b_envio.setText("Enviar");
        b_envio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_envioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_enviarLayout = new javax.swing.GroupLayout(panel_enviar);
        panel_enviar.setLayout(panel_enviarLayout);
        panel_enviarLayout.setHorizontalGroup(
            panel_enviarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_envio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel_enviarLayout.createSequentialGroup()
                .addGroup(panel_enviarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(tf_peerEnvio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_enviarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_enviarLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(tf_valorEnvio)))
        );
        panel_enviarLayout.setVerticalGroup(
            panel_enviarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_enviarLayout.createSequentialGroup()
                .addGroup(panel_enviarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_enviarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_peerEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_valorEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_envio, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panel_receber.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Receber"));

        b_recebimento.setText("Visualizar e autorizar");
        b_recebimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_recebimentoActionPerformed(evt);
            }
        });

        jLabel3.setText("Recebimentos pendentes: ");

        l_recebimentosPendentes.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_recebimentosPendentes.setText("--");

        javax.swing.GroupLayout panel_receberLayout = new javax.swing.GroupLayout(panel_receber);
        panel_receber.setLayout(panel_receberLayout);
        panel_receberLayout.setHorizontalGroup(
            panel_receberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_recebimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel_receberLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(l_recebimentosPendentes)
                .addContainerGap())
        );
        panel_receberLayout.setVerticalGroup(
            panel_receberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_receberLayout.createSequentialGroup()
                .addGroup(panel_receberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_recebimentosPendentes)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_recebimento, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panel_minerar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Minerar"));

        b_mineracao.setText("Visualizar e minerar");
        b_mineracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mineracaoActionPerformed(evt);
            }
        });

        jLabel4.setText("Minerações pendentes: ");

        l_mineracoesPendentes.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        l_mineracoesPendentes.setText("--");

        javax.swing.GroupLayout panel_minerarLayout = new javax.swing.GroupLayout(panel_minerar);
        panel_minerar.setLayout(panel_minerarLayout);
        panel_minerarLayout.setHorizontalGroup(
            panel_minerarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_mineracao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel_minerarLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(l_mineracoesPendentes)
                .addContainerGap())
        );
        panel_minerarLayout.setVerticalGroup(
            panel_minerarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_minerarLayout.createSequentialGroup()
                .addGroup(panel_minerarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_mineracoesPendentes)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_mineracao, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panel_transacao.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        b_transacao.setText("Verificar transação / Destravar");
        b_transacao.setEnabled(false);
        b_transacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_transacaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_transacaoLayout = new javax.swing.GroupLayout(panel_transacao);
        panel_transacao.setLayout(panel_transacaoLayout);
        panel_transacaoLayout.setHorizontalGroup(
            panel_transacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_transacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_transacaoLayout.setVerticalGroup(
            panel_transacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_transacaoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(b_transacao, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu1.setText("Arquivo");

        m_recuperarPeer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME, 0));
        m_recuperarPeer.setText("Recuperar Peer");
        m_recuperarPeer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_recuperarPeerActionPerformed(evt);
            }
        });
        jMenu1.add(m_recuperarPeer);
        jMenu1.add(jSeparator1);

        mi_deslogar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, java.awt.event.InputEvent.ALT_MASK));
        mi_deslogar.setText("Deslogar");
        mi_deslogar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_deslogarActionPerformed(evt);
            }
        });
        jMenu1.add(mi_deslogar);
        jMenu1.add(jSeparator4);

        mi_sair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        mi_sair.setText("Sair");
        mi_sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_sairActionPerformed(evt);
            }
        });
        jMenu1.add(mi_sair);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Dados");

        mi_dadosPeer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        mi_dadosPeer.setText("Peers conhecidos");
        mi_dadosPeer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_dadosPeerActionPerformed(evt);
            }
        });
        jMenu2.add(mi_dadosPeer);
        jMenu2.add(jSeparator2);

        mi_dadosRecebimentos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        mi_dadosRecebimentos.setText("Recebimentos pendentes");
        mi_dadosRecebimentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_dadosRecebimentosActionPerformed(evt);
            }
        });
        jMenu2.add(mi_dadosRecebimentos);

        mi_dadosMineracoes.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        mi_dadosMineracoes.setText("Minerações pendentes");
        mi_dadosMineracoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_dadosMineracoesActionPerformed(evt);
            }
        });
        jMenu2.add(mi_dadosMineracoes);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Ajuda");

        mi_ajuda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mi_ajuda.setText("Ajuda");
        jMenu3.add(mi_ajuda);
        jMenu3.add(jSeparator3);

        mi_sobre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mi_sobre.setText("Sobre este software");
        mi_sobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_sobreActionPerformed(evt);
            }
        });
        jMenu3.add(mi_sobre);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("DEBUG");

        m_gerarPeer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        m_gerarPeer.setText("Gerar Peer");
        m_gerarPeer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_gerarPeerActionPerformed(evt);
            }
        });
        jMenu4.add(m_gerarPeer);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panel_logs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_transacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_receber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_minerar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(panel_enviar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_enviar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_receber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_minerar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_transacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(panel_logs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_envioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_envioActionPerformed
        int peerDestinoId;
        //Possibilita pesquisar pelo ID ou pelo NOME do Peer.
        try{
            peerDestinoId = Integer.parseInt(tf_peerEnvio.getText());
        }catch(Exception e){
            peerDestinoId = FuncoesDB.buscaPeerPeloNome(tf_peerEnvio.getText());
        }
        
        if (peerDestinoId < 0){
            JOptionPane.showMessageDialog(this,
                "Peer não encontrado! O envio de coins não foi iniciado.",
                "ERRO", JOptionPane.ERROR_MESSAGE);
        }else{
            int aux = 0;
            String idAux = getNomePeer() + " " + new SimpleDateFormat(Main.TIMESTAMP_FORMAT).format(new Date());
            int idTransacao = 0;
            int value = 0;
            if(aux == 0)
            {
                FuncoesDB.insertTransacaoDbOficial(idAux, getIdPeer(), Integer.parseInt(tf_peerEnvio.getText()), 0, Integer.parseInt(tf_valorEnvio.getText()), 1, 0, 0, 0);
                idTransacao = FuncoesDB.buscaIDTransacaoPorIDAux(idAux);

                value = Integer.parseInt(tf_valorEnvio.getText());
                aux ++;
            }
            try {
            String msgEnvio = Main.MSG_SENDING_COINS + nomePeer + ";;;" + FuncoesDB.buscaPeerNome(peerDestinoId) + ";;;" + value + ";;;" + idTransacao;

            /*
            0 - start confirmado
            1 - transação ok
            2 - commited
            3 - refused
            4 - envio iniciado'
            */
            //recebedor, enviador, minerador
            //<Enviando> (PeerEnviador) Enviando @X@ Coins </Enviando>
            String msgLog = "<Enviando> (" + idPeer + ") Enviando @" + value + "@ Coins </Enviando>";

            /**
             * <Enviando> PeerEnviador Enviando @X@ Coins </Enviando>
                <Inicio> PeerRecebedor Aceitou as coins </Inicio>
                <Resultado> PeerRecebedor publicou o resultado  [Y] no multicast @X@ Coins Enviadas </Resultado>
             */
            //PeerRecebedor, peerEnviador, peerMinerador - 0 ainda não tem minerador, Estado - 0 Não aceito ainda, Mensagem
            FuncoesDB.insereLogPeerOficial(peerDestinoId, idPeer, 0, 4, msgLog);
            FuncoesDB.inserirRecebimento(Integer.parseInt(tf_peerEnvio.getText()), getIdPeer(), Integer.parseInt(tf_valorEnvio.getText()), idTransacao);
                
                int reply = JOptionPane.showConfirmDialog(null, "Quer Realmente Enviar as Moedas?", "Confirmar Envio", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) 
                {    
                    String mensagem = Main.MSG_SENDER_VOTE_YES + idTransacao + ";;;" + getIdPeer() + ";;;" + 1;
                    sendMessageUC(mensagem, 
                            FuncoesDB.buscaPeerNome(peerDestinoId),
                            InetAddress.getByName(FuncoesDB.buscaPeerEndereco(peerDestinoId)),
                            FuncoesDB.buscaPeerUnicastPort(peerDestinoId));
                    
                    //Trava o peer ENVIADOR!
                    travarPeer(idTransacao);
                }
                else 
                {
                    String mensagem = Main.MSG_SENDER_VOTE_NO + idTransacao + ";;;" + getIdPeer() + ";;;" + -1;
                    sendMessageUC(mensagem, 
                            FuncoesDB.buscaPeerNome(peerDestinoId),
                            InetAddress.getByName(FuncoesDB.buscaPeerEndereco(peerDestinoId)),
                            FuncoesDB.buscaPeerUnicastPort(peerDestinoId));
                    
                    //<Enviando> (PeerEnviador) Enviando @X@ Coins </Enviando>
                    String msgLogNo = "<EnvioNegado> (" + idPeer + ") Negou Envio de @" + value + "@ Coins </Enviando>";
                    //PeerRecebedor, peerEnviador, peerMinerador - 0 ainda não tem minerador, Estado - 0 Não aceito ainda, Mensagem
                    FuncoesDB.insereLogPeerOficial(peerDestinoId, idPeer, 0, 3, msgLogNo);
                }
            
                //Envia POR UNICAST os dados do envio de coins para o peer destinatário.
                sendMessageUC(
                        msgEnvio,
                        FuncoesDB.buscaPeerNome(peerDestinoId),
                        InetAddress.getByName(FuncoesDB.buscaPeerEndereco(peerDestinoId)),
                        FuncoesDB.buscaPeerUnicastPort(peerDestinoId)
                );
            } catch (UnknownHostException ex) {
                Logger.getLogger(IG_MAIN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_b_envioActionPerformed

    private void b_recebimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_recebimentoActionPerformed
        IG_RecebimentosPendentes ig = new IG_RecebimentosPendentes();
        ig.setIgMain(this);
        ig.atualizarTabela();
        ig.setVisible(true);
    }//GEN-LAST:event_b_recebimentoActionPerformed

    private void b_mineracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mineracaoActionPerformed
        IG_MineracoesPendentes ig = new IG_MineracoesPendentes();
        ig.setIgMain(this);
        ig.atualizarTabela();
        ig.setVisible(true);
    }//GEN-LAST:event_b_mineracaoActionPerformed

    private void mi_dadosPeerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_dadosPeerActionPerformed
        IG_PeersConhecidos ig = new IG_PeersConhecidos();
        ig.setVisible(true);
    }//GEN-LAST:event_mi_dadosPeerActionPerformed

    private void mi_dadosRecebimentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_dadosRecebimentosActionPerformed
        b_recebimentoActionPerformed(evt);
    }//GEN-LAST:event_mi_dadosRecebimentosActionPerformed

    private void mi_dadosMineracoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_dadosMineracoesActionPerformed
        b_mineracaoActionPerformed(evt);
    }//GEN-LAST:event_mi_dadosMineracoesActionPerformed

    private void mi_deslogarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_deslogarActionPerformed
        boolean result;
        ControleDB.abrirConexaoDBOficial();
        result = FuncoesDB.deslogaPeer(nomePeer);
        ControleDB.fecharConexaoDBOficial();
        if(result){
            sendMessageMC(Main.MSG_PEER_CLOSED + nomePeer);
            this.dispose();
            mi_sairActionPerformed(evt);
            IG_Login ig = new IG_Login();
            ig.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this,
                    "Não foi possível deslogar este Peer no banco de dados oficial.",
                    "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mi_deslogarActionPerformed

    private void mi_sairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_sairActionPerformed
        boolean result;
        ControleDB.abrirConexaoDBOficial();
        result = FuncoesDB.deslogaPeer(nomePeer);
        ControleDB.fecharConexaoDBOficial();
        if(result){
            sendMessageMC(Main.MSG_PEER_CLOSED + nomePeer);
            this.dispose();
            System.exit(0);
        }else{
            JOptionPane.showMessageDialog(this,
                    "Não foi possível deslogar este Peer no banco de dados oficial.",
                    "ERRO", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }//GEN-LAST:event_mi_sairActionPerformed

    private void mi_sobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_sobreActionPerformed
        JOptionPane.showMessageDialog(this, "MulticastPeer com Transações. Feito por Felipe Zem e Klaus Diener para a disciplina de Sistemas Distribuidos.\nAprecie com moderação.",
                "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_mi_sobreActionPerformed

    private void m_recuperarPeerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_recuperarPeerActionPerformed
        IG_Recupracao ig = new IG_Recupracao();
        ig.setVisible(true);
    }//GEN-LAST:event_m_recuperarPeerActionPerformed

    private void m_gerarPeerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_gerarPeerActionPerformed
        IG_Login ig = new IG_Login();
        ig.setVisible(true);
    }//GEN-LAST:event_m_gerarPeerActionPerformed

    private void b_transacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_transacaoActionPerformed
        if (FuncoesDB.verificaTransacaoEncerrada(transacaoAtual)){
            destravarPeer(transacaoAtual);
        }else{
            JOptionPane.showMessageDialog(this,
                    "A transação nº " + transacaoAtual + " ainda está ocorrendo. Aguarde.",
                    "Transação pendente", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_b_transacaoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new IG_MAIN().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_envio;
    private javax.swing.JButton b_mineracao;
    private javax.swing.JButton b_recebimento;
    private javax.swing.JButton b_transacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JLabel l_mineracoesPendentes;
    private javax.swing.JLabel l_peerCoins;
    private javax.swing.JLabel l_peerEndereco;
    private javax.swing.JLabel l_peerGrupoMulticast;
    private javax.swing.JLabel l_peerId;
    private javax.swing.JLabel l_peerNome;
    private javax.swing.JLabel l_peerPortaMulticast;
    private javax.swing.JLabel l_peerPortaUnicast;
    private javax.swing.JLabel l_recebimentosPendentes;
    private javax.swing.JMenuItem m_gerarPeer;
    private javax.swing.JMenuItem m_recuperarPeer;
    private javax.swing.JMenuItem mi_ajuda;
    private javax.swing.JMenuItem mi_dadosMineracoes;
    private javax.swing.JMenuItem mi_dadosPeer;
    private javax.swing.JMenuItem mi_dadosRecebimentos;
    private javax.swing.JMenuItem mi_deslogar;
    private javax.swing.JMenuItem mi_sair;
    private javax.swing.JMenuItem mi_sobre;
    private javax.swing.JPanel panel_enviar;
    private javax.swing.JPanel panel_logs;
    private javax.swing.JPanel panel_meusDados;
    private javax.swing.JPanel panel_minerar;
    private javax.swing.JPanel panel_receber;
    private javax.swing.JPanel panel_transacao;
    private javax.swing.JTextArea ta_logs;
    private javax.swing.JTextField tf_peerEnvio;
    private javax.swing.JTextField tf_valorEnvio;
    // End of variables declaration//GEN-END:variables
}
