/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceGrafica;


import InterfaceGrafica.Cadastros.*;
import InterfaceGrafica.Operacoes.*;
/**
 *
 * @author Felipe Lopes Zem & Klaus Diener
 */
public class IG_Main extends javax.swing.JFrame {
    /**
     * Creates new form IG_Main
     */
    public IG_Main() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        m_arquivo = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mb_arquivo_sair = new javax.swing.JMenuItem();
        m_cadastros = new javax.swing.JMenu();
        mb_cadastro_locais = new javax.swing.JMenuItem();
        mb_cadastro_passagens = new javax.swing.JMenuItem();
        m_hoteis = new javax.swing.JMenu();
        mb_cadastro_hoteis = new javax.swing.JMenuItem();
        mb_cadastro_quartosHotel = new javax.swing.JMenuItem();
        m_operacoes = new javax.swing.JMenu();
        mb_operacao_pacotesturisticos = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mb_operacao_vendadepassagens = new javax.swing.JMenuItem();
        mb_operacao_vendadehospedagens = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mb_interesses = new javax.swing.JMenuItem();
        m_ajuda = new javax.swing.JMenu();
        mb_ajuda_sobre = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel1.setText("RMI Server");

        m_arquivo.setText("Arquivo");
        m_arquivo.add(jSeparator1);

        mb_arquivo_sair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        mb_arquivo_sair.setText("Sair");
        m_arquivo.add(mb_arquivo_sair);

        jMenuBar1.add(m_arquivo);

        m_cadastros.setText("Cadastros");

        mb_cadastro_locais.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_MASK));
        mb_cadastro_locais.setText("Locais");
        mb_cadastro_locais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mb_cadastro_locaisActionPerformed(evt);
            }
        });
        m_cadastros.add(mb_cadastro_locais);

        mb_cadastro_passagens.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        mb_cadastro_passagens.setText("Passagens Aereas");
        mb_cadastro_passagens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mb_cadastro_passagensActionPerformed(evt);
            }
        });
        m_cadastros.add(mb_cadastro_passagens);

        m_hoteis.setText("Hoteis e Quartos");

        mb_cadastro_hoteis.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, java.awt.event.InputEvent.CTRL_MASK));
        mb_cadastro_hoteis.setText("Hoteis");
        mb_cadastro_hoteis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mb_cadastro_hoteisActionPerformed(evt);
            }
        });
        m_hoteis.add(mb_cadastro_hoteis);

        mb_cadastro_quartosHotel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        mb_cadastro_quartosHotel.setText("Quartos");
        mb_cadastro_quartosHotel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mb_cadastro_quartosHotelActionPerformed(evt);
            }
        });
        m_hoteis.add(mb_cadastro_quartosHotel);

        m_cadastros.add(m_hoteis);

        jMenuBar1.add(m_cadastros);

        m_operacoes.setText("Operações");

        mb_operacao_pacotesturisticos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, java.awt.event.InputEvent.CTRL_MASK));
        mb_operacao_pacotesturisticos.setText("Pacotes Turísticos");
        mb_operacao_pacotesturisticos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mb_operacao_pacotesturisticosActionPerformed(evt);
            }
        });
        m_operacoes.add(mb_operacao_pacotesturisticos);
        m_operacoes.add(jSeparator2);

        mb_operacao_vendadepassagens.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, java.awt.event.InputEvent.CTRL_MASK));
        mb_operacao_vendadepassagens.setText("Venda de Passagens");
        mb_operacao_vendadepassagens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mb_operacao_vendadepassagensActionPerformed(evt);
            }
        });
        m_operacoes.add(mb_operacao_vendadepassagens);

        mb_operacao_vendadehospedagens.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, java.awt.event.InputEvent.CTRL_MASK));
        mb_operacao_vendadehospedagens.setText("Venda de Hospedagens");
        mb_operacao_vendadehospedagens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mb_operacao_vendadehospedagensActionPerformed(evt);
            }
        });
        m_operacoes.add(mb_operacao_vendadehospedagens);
        m_operacoes.add(jSeparator3);

        mb_interesses.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, java.awt.event.InputEvent.CTRL_MASK));
        mb_interesses.setText("Interesses");
        mb_interesses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mb_interessesActionPerformed(evt);
            }
        });
        m_operacoes.add(mb_interesses);

        jMenuBar1.add(m_operacoes);

        m_ajuda.setText("Ajuda");

        mb_ajuda_sobre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mb_ajuda_sobre.setText("Sobre");
        m_ajuda.add(mb_ajuda_sobre);

        jMenuBar1.add(m_ajuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(321, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(126, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mb_cadastro_locaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mb_cadastro_locaisActionPerformed
        IG_Cad_Locais ig_CadastroLocais = new IG_Cad_Locais();
        ig_CadastroLocais.setVisible(true);
    }//GEN-LAST:event_mb_cadastro_locaisActionPerformed

    private void mb_cadastro_passagensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mb_cadastro_passagensActionPerformed
        IG_Cad_Passagens ig_CadastroPassagem = new IG_Cad_Passagens();
        ig_CadastroPassagem.setVisible(true);
    }//GEN-LAST:event_mb_cadastro_passagensActionPerformed

    private void mb_cadastro_hoteisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mb_cadastro_hoteisActionPerformed
        IG_Cad_Hoteis ig_CadastroHoteis = new IG_Cad_Hoteis();
        ig_CadastroHoteis.setVisible(true);
    }//GEN-LAST:event_mb_cadastro_hoteisActionPerformed

    private void mb_cadastro_quartosHotelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mb_cadastro_quartosHotelActionPerformed
        IG_CadComp_QuartosHotel ig_CadastroQuartosHotel = new IG_CadComp_QuartosHotel();
        ig_CadastroQuartosHotel.setVisible(true);
    }//GEN-LAST:event_mb_cadastro_quartosHotelActionPerformed

    private void mb_operacao_pacotesturisticosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mb_operacao_pacotesturisticosActionPerformed
        IG_Op_PacotesTuristicos ig_OperacaoPacotesTuristicos = new IG_Op_PacotesTuristicos();
        ig_OperacaoPacotesTuristicos.setVisible(true);
    }//GEN-LAST:event_mb_operacao_pacotesturisticosActionPerformed

    private void mb_operacao_vendadepassagensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mb_operacao_vendadepassagensActionPerformed
        IG_Op_VendasPassagem ig_Op_VendasPassagem = new IG_Op_VendasPassagem();
        ig_Op_VendasPassagem.setVisible(true);
    }//GEN-LAST:event_mb_operacao_vendadepassagensActionPerformed

    private void mb_operacao_vendadehospedagensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mb_operacao_vendadehospedagensActionPerformed
        IG_Op_VendasHospedagem ig_Op_VendasHospedagem = new IG_Op_VendasHospedagem();
        ig_Op_VendasHospedagem.setVisible(true);
    }//GEN-LAST:event_mb_operacao_vendadehospedagensActionPerformed

    private void mb_interessesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mb_interessesActionPerformed
        IG_Op_Interesses ig_Op_Interesses = new IG_Op_Interesses();
        ig_Op_Interesses.setVisible(true);
    }//GEN-LAST:event_mb_interessesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new IG_Main().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenu m_ajuda;
    private javax.swing.JMenu m_arquivo;
    private javax.swing.JMenu m_cadastros;
    private javax.swing.JMenu m_hoteis;
    private javax.swing.JMenu m_operacoes;
    private javax.swing.JMenuItem mb_ajuda_sobre;
    private javax.swing.JMenuItem mb_arquivo_sair;
    private javax.swing.JMenuItem mb_cadastro_hoteis;
    private javax.swing.JMenuItem mb_cadastro_locais;
    private javax.swing.JMenuItem mb_cadastro_passagens;
    private javax.swing.JMenuItem mb_cadastro_quartosHotel;
    private javax.swing.JMenuItem mb_interesses;
    private javax.swing.JMenuItem mb_operacao_pacotesturisticos;
    private javax.swing.JMenuItem mb_operacao_vendadehospedagens;
    private javax.swing.JMenuItem mb_operacao_vendadepassagens;
    // End of variables declaration//GEN-END:variables
}