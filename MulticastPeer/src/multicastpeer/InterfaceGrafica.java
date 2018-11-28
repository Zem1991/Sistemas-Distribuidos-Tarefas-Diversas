/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastpeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import multicastpeer.threads.*;
import operações.Operacoes;
//import static utilitarios.EncryptionUtil.ALGORITHM;

/*A classe interfaçe Gráfica é um Peer, logo ela tem as seguintes propriedades
*   Nome -> nome do peer: NomeDaMaquina +modificador (no caso é a string "+")
*   mcGroup -> Grupo de multicast para se conectar (228.5.6.7)
*   McSocket ->  Default
*   pr -> peerReceiver: é uma nova thread
*   chavePublica -> identificação da chave pública
*   chavePrivada -> identificação da chave privada
*   PrecoVenda -> é o preço pelo qual ele esta vendendo suas startingCoins
*   Moedas -> quantidade inicial de moedas
*/
/**
 * DAE
 * @author Felipe Lopes Zem
 */
public class InterfaceGrafica extends javax.swing.JFrame {
    private String igName;              //Nome deste Peer, que será baseado no nome da máquina.
                                        //Caso já exista esse nome, então adiciona-se um número ao final do nome.
    private int igCount;                //Número usado para renomear o Peer caso o nome solicitado já exista.
                                        //TODO: isso só foi testado com múltiplos peers na mesma máquina! E se duas máquinas tiverem o mesmo nome?
    private int startingCoins = 1000;           //Coins iniciais deste Peer.
    private int precoVenda = 1;         //Preço de venda por coin que este Peer cobrará na venda.
    
    private Operacoes ops = new Operacoes(this);
    
    private InetAddress mcGroup;        //Endereço usado para Multicast.
    private MulticastSocket mcSocket;   //Scoket usado para Multicast.
    private DatagramSocket dgSocket;    //Socket usado para Unicast.
    //private PublicKey chavePublica;   //Chave Publica utilizada para descriptografar mensagens Unicast.
    //private PrivateKey chavePrivada;  //Chave Privada utilizada para encriptografar mensagens Unicast.
    
    private MessageReceiverMC msgRecThreadMC;   //Thread recebedora de mensagens Multicast.
    private MessageReceiverUC msgRecThreadUC;   //Thread recebedora de mensagens Unicast.
    private CoinMiner coinMinerThread;          //Thread mineradora de transações de startingCoins.
    
    /**
     * Creates new form InterfaceGrafica
     * @param lastIG
     * @param inetAddress
     * @param multicastSocket
     * @param unicastSocket
     */
    public InterfaceGrafica(int lastIG, String inetAddress, int multicastSocket, int unicastSocket){
        try {
            initComponents();   //Inicializa componentes graficos.
            
            igName = "PEER";//InetAddress.getLocalHost().getHostName();  //Nome do peer, baseado no nome da máquina.
            igCount = lastIG;   //Contador usado para modificar o nome do peer caso o nome solicitado já tenha sido utilizado.
            if (igCount > 0) igName = igName + "(" + igCount + ")";
            this.setTitle(this.getTitle() + ": " + igName);
            
            mcGroup = InetAddress.getByName(inetAddress);
            mcSocket = new MulticastSocket(multicastSocket);
            dgSocket = new DatagramSocket(unicastSocket);
            //gerarChaves();      //Gera chaves publica e privada.
            
            if (Main.SHOW_DEBUG_STUFF) System.out.println("[!!!]  Peer " + igName + " criado com endereço multicast " + mcGroup.getHostAddress() + 
                    ", socket Multicast " + mcSocket.getLocalPort() + 
                    " e socket Unicast " + dgSocket.getLocalPort()  + ".");
            
            printLogMessage(InetAddress.getLocalHost().getHostName(), "Using name " + igName + ".");
            mcSocket.joinGroup(mcGroup);
            printLogMessage(igName, "Connected to Multicast group " + mcGroup.getHostAddress() + 
                    ". Multicast socket is " + mcSocket.getLocalPort()+ ". Unicast socket is " + dgSocket.getLocalPort()+ ".");
            
            msgRecThreadMC = new MessageReceiverMC(this);
            msgRecThreadUC = new MessageReceiverUC(this);
            coinMinerThread = new CoinMiner(this);
        } catch (UnknownHostException ex) {
            Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public String getIgName() {return igName;}
    //public int getCoins() {return startingCoins;}
    //public void setCoins(int coins) {this.startingCoins = coins;}
    public int getPrecoVenda() {return precoVenda;}
    public void setPrecoVenda(int precoVenda) {this.precoVenda = precoVenda;}

    public Operacoes getOps() {return ops;}

    public InetAddress getMcGroup() {return mcGroup;}
    public MulticastSocket getMcSocket() {return mcSocket;}
    public DatagramSocket getDgSocket() {return dgSocket;}
    //public PublicKey getChavePublica() {return chavePublica;}
    //public PrivateKey getChavePrivada() {return chavePrivada;}
    
    /**
     * Acessa a thread de mineração deste Peer.
     * @return 
     */
    public CoinMiner accessCoinMiner(){
        return coinMinerThread;
    }

//    /**
//     * Gerador de chaves publica e privada
//     * @return 
//     */
//    public KeyPair gerarChaves() {
//        KeyPair key = null;
//        try {    
//            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
//            keyGen.initialize(512);
//            key = keyGen.generateKeyPair();
//            
//            // Saving the Public key in a file
//            chavePublica = key.getPublic();
//            chavePrivada = key.getPrivate();
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return key;
//    }
//    
//    /**
//     * Encripta a String recebida com a chave pública deste Peer.
//     * @param msg é a mensagem a encriptar.
//     * @return a mensagem encriptada.
//     */
//    public byte[] encryptMessage(String msg, PublicKey chavePublic){
//        return EncryptionUtil.encrypt(msg, chavePublic);
//    }
    
    /**
     * Envia uma mensagem multicast contendo uma String. Pode ser usado como ferramenta
 de chat, ou para forçar operações de envio, recebimento e mineração de startingCoins.
     * @param message é a mensagem a enviar.
     * @return true se enviou com sucesso, falso se não.
     */
    public boolean sendMessageMC(String message){
        if (!message.isEmpty()){
            try {
                //byte[] msg = message.getBytes();    //Desnecessária esta conversão.
                DatagramPacket messageOut = new DatagramPacket(message.getBytes(), message.getBytes().length, mcGroup, mcSocket.getLocalPort());
                mcSocket.send(messageOut);
                if (Main.SHOW_DEBUG_STUFF) System.out.println("[Multicast message] FROM " + igName + 
                        " TO EVERYONE USING PORT " + mcSocket.getLocalPort() + ": " + 
                        message);
                printLogMessage(igName, message);
                return true;
            } catch (UnknownHostException ex) {
                Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
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
 chat, ou para forçar operações de envio, recebimento e mineração de startingCoins.
     * @param message é a mensagem a enviar.
     * @param address é o endereço para o qual enviar.
     * @param port é a porta par utilizar no envio.
     * @return true se enviou com sucesso, falso caso contrário.
     */
    public boolean sendMessageUC(String message, InetAddress address, int port){
        if (!message.isEmpty()){
            try {
                //dgSocket.connect(address, port);
                //byte[] msg = message.getBytes();    //TODO: fazer chamar aqui dentro a encriptação de mensagens.
                DatagramPacket messageOut = new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
                dgSocket.send(messageOut);
                if (Main.SHOW_DEBUG_STUFF) System.out.println("[Unicast message] FROM " + igName + 
                        " TO ADDRESS " + address + " USING PORT " + port + ": " + 
                        message);
                printLogMessage(igName, message);
                //dgSocket.disconnect();
                return true;
            } catch (SocketException ex) {
                Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }else{
            JOptionPane.showMessageDialog(this, 
                    "Tentou-se enviar uma mensagem vazia para um peer. Operação abortada.", 
                    "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Imprime uma mensagem no respectivo text field da interface gráfica. Utilizado tanto
     * para mensagens enviadas quanto para mensagens recebidas.
     * @param source é o nome do Peer que gerou esta mensagem.
     * @param msg é a mensagem em si.
     * @return true se a mensagem puder ser impressa, false caso contrário.
     */
    public synchronized boolean printLogMessage(String source, String msg){
        if (!source.isEmpty() && !msg.isEmpty()){
            ta_logDisplay.setText(ta_logDisplay.getText() + 
                    new SimpleDateFormat(Main.TIMESTAMP_FORMAT).format(new Date()) + 
                    " [" + source + "] " + msg + "\n");
            return true;
        }else{
            System.out.println("multicastpeer.InterfaceGrafica.printLogMessage() OU A ORIGEM OU A MENSAGEM ESTÁ EM BRANCO.");
            return false;
        }
    }
    
    /**
     * Atualiza o título da Interface Grafica, informando o nome deste Peer, quantas Coins ele tem e
     * quantas minerações pendentes ele tem conhecimento.
     */
    public void updateWindowTitle(){
        int myCoins;
        if (ops.searchPeerInList(igName) < 0){
            myCoins = startingCoins;
        }else{
            myCoins = ops.getPeer(ops.searchPeerInList(igName)).getPeerCoins();
        }
        this.setTitle(igName + 
                " (Minhas coins: " + myCoins + ")" + 
                " (Minerações pendentes: " + coinMinerThread.getPendingOps().size()+ ")");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_logDisplay = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tf_amountToSend = new javax.swing.JTextField();
        b_buyCoins = new javax.swing.JButton();
        tf_peerToSend = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        b_sellCoins = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        b_mineValue = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        tf_writePeerMsg = new javax.swing.JTextField();
        b_sendPeerMsg = new javax.swing.JButton();
        rb_multicastMsg = new javax.swing.JRadioButton();
        rb_unicastMsg = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        tf_unicastMsgPeer = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tf_unicastMsgPort = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mi_iniciarPeer = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mi_gerarNovoPeer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MulticastPeer");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ta_logDisplay.setEditable(false);
        ta_logDisplay.setColumns(20);
        ta_logDisplay.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        ta_logDisplay.setRows(5);
        jScrollPane1.setViewportView(ta_logDisplay);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Compra"));

        jLabel2.setText("Comprar do Peer");

        jLabel3.setText("Valor a comprar");

        tf_amountToSend.setText("0");
        tf_amountToSend.setName(""); // NOI18N

        b_buyCoins.setText("Comprar Moedas");
        b_buyCoins.setEnabled(false);
        b_buyCoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buyCoinsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_buyCoins, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tf_amountToSend)
            .addComponent(tf_peerToSend)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_peerToSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_amountToSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b_buyCoins))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Envio"));
        jPanel4.setPreferredSize(new java.awt.Dimension(194, 96));

        b_sellCoins.setText("Vender Moedas");
        b_sellCoins.setEnabled(false);
        b_sellCoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sellCoinsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_sellCoins, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_sellCoins))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Minerar"));

        b_mineValue.setText("Minerar");
        b_mineValue.setEnabled(false);
        b_mineValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mineValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_mineValue, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(b_mineValue))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Mensagem"));

        tf_writePeerMsg.setName(""); // NOI18N

        b_sendPeerMsg.setText("Enviar");
        b_sendPeerMsg.setEnabled(false);
        b_sendPeerMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sendPeerMsgActionPerformed(evt);
            }
        });

        buttonGroup2.add(rb_multicastMsg);
        rb_multicastMsg.setSelected(true);
        rb_multicastMsg.setText("Multicast");
        rb_multicastMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_multicastMsgActionPerformed(evt);
            }
        });

        buttonGroup2.add(rb_unicastMsg);
        rb_unicastMsg.setText("Unicast");
        rb_unicastMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_unicastMsgActionPerformed(evt);
            }
        });

        jLabel4.setText("Nome do Peer");

        jLabel5.setText("Porta Unicast");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(rb_multicastMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rb_unicastMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_unicastMsgPeer, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_unicastMsgPort, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tf_writePeerMsg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_sendPeerMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(tf_writePeerMsg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(tf_unicastMsgPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tf_unicastMsgPeer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rb_multicastMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(rb_unicastMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))))
            .addComponent(b_sendPeerMsg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenu1.setText("Arquivo");

        mi_iniciarPeer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mi_iniciarPeer.setText("Iniciar peer");
        mi_iniciarPeer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_iniciarPeerActionPerformed(evt);
            }
        });
        jMenu1.add(mi_iniciarPeer);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Debug");

        mi_gerarNovoPeer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        mi_gerarNovoPeer.setText("Gerar novo peer");
        mi_gerarNovoPeer.setToolTipText("Gera mais um peer local que usa o mesmo IP e Socket para Multicast, porém com porta incrementada para Unicast.\nEste botão só pode ser usado uma vez por interface gráfica.");
        mi_gerarNovoPeer.setEnabled(false);
        mi_gerarNovoPeer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_gerarNovoPeerActionPerformed(evt);
            }
        });
        jMenu3.add(mi_gerarNovoPeer);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    private void mi_gerarNovoPeerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_gerarNovoPeerActionPerformed
        mi_gerarNovoPeer.setEnabled(false);
        Main.generateNewPeer(igCount+1);    //mcGroup.getHostAddress(), mcSocket.getLocalPort(), dgSocket.getLocalPort());
    }//GEN-LAST:event_mi_gerarNovoPeerActionPerformed
    
    //Peer se desliga do multicast
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        msgRecThreadMC.setIsRunning(false);
        msgRecThreadUC.setIsRunning(false);
        coinMinerThread.setIsRunning(false);
        sendMessageMC(Main.MSG_PEER_CLOSED + 
                igName);
    }//GEN-LAST:event_formWindowClosed

    //inicia novo peer
    private void mi_iniciarPeerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_iniciarPeerActionPerformed
        msgRecThreadMC.start();
        msgRecThreadUC.start();
        
        mi_iniciarPeer.setEnabled(false);
        mi_gerarNovoPeer.setEnabled(true);
        b_sendPeerMsg.setEnabled(true);
        b_buyCoins.setEnabled(true);
        b_sellCoins.setEnabled(true);
        b_mineValue.setEnabled(true);
        
        updateWindowTitle();
        
        //Operacoes.addPeerToList(igName, startingCoins, precoVenda, mcGroup, null);            //O proprio peer é cadastrado na thread MessageReceiver.
        sendMessageMC(Main.MSG_PEER_OPEN +
                igName + ";;;" + 
                dgSocket.getLocalPort() + ";;;" + 
                startingCoins + ";;;" + 
                precoVenda);
        /*
        sendMessageUC(Main.MSG_PEER_OPEN +  //TODO: para fins de teste Unicast está se usando sendMessageUC. Os dois ultimos parametros são desnecessários no Multicast
                igName + ";;;" + 
                dgSocket.getLocalPort() + ";;;" + 
                startingCoins + ";;;" + 
                precoVenda,
                dgSocket.getLocalAddress(),
                dgSocket.getLocalPort());      //TODO: repassar chavePublica (privada)?
        */
    }//GEN-LAST:event_mi_iniciarPeerActionPerformed

    private void b_sendPeerMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_sendPeerMsgActionPerformed
        if (rb_multicastMsg.isSelected()){
                sendMessageMC(
                        tf_writePeerMsg.getText());
                tf_writePeerMsg.setText("");
        }else
        if (rb_unicastMsg.isSelected()){
            int i = ops.searchPeerInList(tf_unicastMsgPeer.getText());
            if (i >= 0){
                sendMessageUC(
                        tf_writePeerMsg.getText(),
                        ops.getPeer(i).getPeerAddress(),
                        Integer.parseInt(tf_unicastMsgPort.getText()));
                tf_writePeerMsg.setText("");
            }else{
                JOptionPane.showMessageDialog(this,
                "Peer não encontrado! A mensagem não foi enviada.",
                "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
            }
        }
    }//GEN-LAST:event_b_sendPeerMsgActionPerformed

    // tenta minerar - Thread fica escutando as mensagens
    private void b_mineValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mineValueActionPerformed
        if (!coinMinerThread.isIsRunning()){
            coinMinerThread.setIsRunning(true);
            coinMinerThread.start();
            b_mineValue.setText("Parar Mineração");
            b_buyCoins.setEnabled(false);
            b_sellCoins.setEnabled(false);
        }else{
            coinMinerThread.setIsRunning(false);
            //Thread mineradora para assim que isRunning for false.
            b_mineValue.setText("Minerar");
            b_buyCoins.setEnabled(true);
            b_sellCoins.setEnabled(true);
        }
    }//GEN-LAST:event_b_mineValueActionPerformed

    private void b_sellCoinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_sellCoinsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b_sellCoinsActionPerformed

    private void rb_unicastMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_unicastMsgActionPerformed
        if (rb_unicastMsg.isSelected()){
            tf_unicastMsgPeer.setEnabled(true);
            tf_unicastMsgPort.setEnabled(true);
        }else{
            tf_unicastMsgPeer.setEnabled(false);
            tf_unicastMsgPort.setEnabled(false);
        }
    }//GEN-LAST:event_rb_unicastMsgActionPerformed

    private void rb_multicastMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_multicastMsgActionPerformed
        if (rb_multicastMsg.isSelected()){
            tf_unicastMsgPeer.setEnabled(false);
            tf_unicastMsgPort.setEnabled(false);
        }else{
            tf_unicastMsgPeer.setEnabled(true);
            tf_unicastMsgPort.setEnabled(true);
        }
    }//GEN-LAST:event_rb_multicastMsgActionPerformed

    private void b_buyCoinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buyCoinsActionPerformed
        int value = Integer.parseInt(tf_amountToSend.getText());
        
        String msgCompraVenda = Main.MSG_BUYING_COINS + igName + ";;;" + tf_peerToSend.getText() + ";;;" + value;
        //byte[] encryptedMsg = EncryptionUtil.encrypt(msgCompraVenda, chavePublica);
        //System.out.println("Decriptogtrafada: " + EncryptionUtil.decrypt(encryptedMsg, chavePrivada));

        int peerDestinoId = ops.searchPeerInList(tf_peerToSend.getText());

        if (peerDestinoId < 0){
            JOptionPane.showMessageDialog(this,
                "Peer não encontrado! A compra não foi iniciada.",
                "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Envia POR UNICAST os dados do envio de startingCoins para o peer destinatário.
        sendMessageUC(msgCompraVenda,   //encryptMessage(msgCompraVenda, chavePublica),
            ops.getPeer(peerDestinoId).getPeerAddress(),
            ops.getPeer(peerDestinoId).getDgSocket());

        //  sendMessageMC("<SEND VALUE> " + igName + " is sending " + value + " to " + tf_peerToSend.getText() + ".");
        //TODO: O peer fica esperando alguem dizer que sua operação é valida?
        //Ou ele pode smpamar mais operações?
    }//GEN-LAST:event_b_buyCoinsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_buyCoins;
    private javax.swing.JButton b_mineValue;
    private javax.swing.JButton b_sellCoins;
    private javax.swing.JButton b_sendPeerMsg;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mi_gerarNovoPeer;
    private javax.swing.JMenuItem mi_iniciarPeer;
    private javax.swing.JRadioButton rb_multicastMsg;
    private javax.swing.JRadioButton rb_unicastMsg;
    private javax.swing.JTextArea ta_logDisplay;
    private javax.swing.JTextField tf_amountToSend;
    private javax.swing.JTextField tf_peerToSend;
    private javax.swing.JTextField tf_unicastMsgPeer;
    private javax.swing.JTextField tf_unicastMsgPort;
    private javax.swing.JTextField tf_writePeerMsg;
    // End of variables declaration//GEN-END:variables
}