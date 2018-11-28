/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operações;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.PublicKey;

/**
 * Esta classe é usada para guardar todos os dados relevantes dos OUTROS peers,
 * EXCETO as chaves privadas de cada um.
 * @author Felipe Lopes Zem
 */
public class PeerData {
    private String peerName;
    private int peerCoins;
    private int peerPrecoVenda;
    private InetAddress peerAddress;
    private int peerUnicastSocket;
    private PublicKey peerPublicKey;
    

    //estrutura de peerData
    public PeerData(String peerName, int peerCoins, int peerPrecoVenda,
            InetAddress peerAddress, int peerUnicastSocket,
            PublicKey peerPublicKey)
    {
        this.peerName = peerName;
        this.peerCoins = peerCoins;
        this.peerPrecoVenda = peerPrecoVenda;
        this.peerAddress = peerAddress;
        this.peerUnicastSocket = peerUnicastSocket;
        this.peerPublicKey = peerPublicKey;
    }

    public String getPeerName() {return peerName;}
    public void setPeerName(String peerName) {this.peerName = peerName;}
    public int getPeerCoins() {return peerCoins;}
    public void setPeerCoins(int peerCoins) {this.peerCoins = peerCoins;}
    public InetAddress getPeerAddress() {return peerAddress;}
    public void setPeerAddress(InetAddress peerAddress) {this.peerAddress = peerAddress;}
    public PublicKey getPeerPublicKey() {return peerPublicKey;}
    public void setPeerPublicKey(PublicKey peerPublicKey) {this.peerPublicKey = peerPublicKey;}
    public int getDgSocket() {return peerUnicastSocket;}
    public void setPeerUnicastSocket(int peerUnicastSocket) {this.peerUnicastSocket = peerUnicastSocket;}
    public int getPeerPrecoVenda() {return peerPrecoVenda;}
    public void setPeerPrecoVenda(int peerPrecoVenda) {this.peerPrecoVenda = peerPrecoVenda;}
}
