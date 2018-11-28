/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceGrafica.Operacoes;

import InterfaceGrafica.Cadastros.*;
import Database.Operations.DatabaseOperations;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Database.Operations.DbUtils;

/**
 *
 * @author Felipe Lopes Zem
 */
public class IG_Op_Interesses extends javax.swing.JFrame {
    private static final byte 
            DADOS_NENHUM = 0, 
            DADOS_INSERIR = 1, 
            DADOS_MODIFICAR = 2,
            DADOS_EXCLUIR = 3;
    
    /**
     * Use esta variável para identificar qual o modo de manipulação de dados está 
     * sendo utilizado no presente momento por este componente da Interface Gráfica.
     * 
     * As constantes aceitas são: DADOS_NENHUM (nenhum, apenas consulta), DADOS_INSERIR, 
     * DADOS_MODIFICAR, DADOS_EXCLUIR.
     */
    private byte modoDeDados = 0;
    
    /**
     * Creates new form IG_CadastroSimples_teste
     */
    public IG_Op_Interesses() {
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
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        DISTRIBUIDOSRMIPUEntityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("DISTRIBUIDOSRMIPU").createEntityManager();
        interessesQuery = java.beans.Beans.isDesignTime() ? null : DISTRIBUIDOSRMIPUEntityManager.createQuery("SELECT i FROM Interesses i");
        interessesList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : interessesQuery.getResultList();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_elementos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tf_id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tf_interessado = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tf_tipo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tf_idLocal = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tf_preco = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ftf_datalimite = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        b_Inserir = new javax.swing.JButton();
        b_Modificar = new javax.swing.JButton();
        b_Excluir = new javax.swing.JButton();
        b_Atualizar = new javax.swing.JButton();
        b_Cancelar = new javax.swing.JButton();
        b_Ok = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Todos os interesses"));

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, interessesList, table_elementos);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${id}"));
        columnBinding.setColumnName("Id");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${interessado}"));
        columnBinding.setColumnName("Interessado");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${tipo}"));
        columnBinding.setColumnName("Tipo");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${idLocal}"));
        columnBinding.setColumnName("Id Local");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${preco}"));
        columnBinding.setColumnName("Preco");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${datalimite}"));
        columnBinding.setColumnName("Datalimite");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();

        jScrollPane1.setViewportView(table_elementos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados do cadastro"));

        jLabel1.setText("ID");

        tf_id.setEnabled(false);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, table_elementos, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.id}"), tf_id, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel2.setText("Interessado");

        tf_interessado.setEnabled(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, table_elementos, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.interessado}"), tf_interessado, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel3.setText("Tipo do interesse");

        tf_tipo.setEnabled(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, table_elementos, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.tipo}"), tf_tipo, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel4.setText("ID do Local");

        tf_idLocal.setEnabled(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, table_elementos, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.idLocal}"), tf_idLocal, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel9.setText("Preço (R$)");

        tf_preco.setEnabled(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, table_elementos, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.preco}"), tf_preco, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel7.setText("Data limite");

        ftf_datalimite.setEnabled(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, table_elementos, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.datalimite}"), ftf_datalimite, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_id, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_interessado, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_idLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_preco, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ftf_datalimite, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 30, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tf_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tf_interessado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tf_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tf_idLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tf_preco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(ftf_datalimite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(160, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        b_Inserir.setText("Inserir");
        b_Inserir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_InserirActionPerformed(evt);
            }
        });

        b_Modificar.setText("Modificar");
        b_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ModificarActionPerformed(evt);
            }
        });

        b_Excluir.setText("Excluir");
        b_Excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ExcluirActionPerformed(evt);
            }
        });

        b_Atualizar.setText("Atualizar");
        b_Atualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_AtualizarActionPerformed(evt);
            }
        });

        b_Cancelar.setText("Cancelar");
        b_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_CancelarActionPerformed(evt);
            }
        });

        b_Ok.setText("Ok");
        b_Ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_OkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(b_Inserir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_Modificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_Excluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_Atualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addComponent(b_Cancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_Ok))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {b_Atualizar, b_Cancelar, b_Excluir, b_Inserir, b_Modificar, b_Ok});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_Inserir, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(b_Modificar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b_Excluir, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(b_Atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(b_Ok, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {b_Atualizar, b_Cancelar, b_Excluir, b_Inserir, b_Modificar, b_Ok});

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * ESTE MÉTODO SÓ DEVE SER UTILIZADO PARA TESTAR INDIVIDUALMENTE ESTE MÓDULO!
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new IG_Op_Interesses().setVisible(true);
        });
    }
    
    /**
     * Ao clicar no botão Inserir é ativado o modo de Inserção de dados. A interface
     * gráfica libera os componentes do painel "Dados do cadastro", para que um
     * novo item seja cadastrado.
     * 
     * Os botões Cancelar e Ok também são liberados para que seja possível sair
     * deste modo, cancelando ou confirmando a inclusão. Os demais botões são bloqueados
     * enquanto isto.
     * @param evt 
     */
    private void b_InserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_InserirActionPerformed
        //Bloqueia a ativação de outros modos de manpulação de dados.
        b_Inserir.setEnabled(false);
        b_Modificar.setEnabled(false);
        b_Excluir.setEnabled(false);
        b_Atualizar.setEnabled(false);
        //Libera a saída deste modo, permitindo o cancelamento ou a confirmação da inclusão.
        b_Cancelar.setEnabled(true);
        b_Ok.setEnabled(true);
        //Libera todos os componentes necessários para o cadastro de um novo item.
        //tf_id.setEnabled(true);   //O BD GERA ID SEQUENCIAL!
        tf_interessado.setEnabled(true);
        tf_tipo.setEnabled(true);
        tf_idLocal.setEnabled(true);
        tf_preco.setEnabled(true);
        ftf_datalimite.setEnabled(true);
        
        //Limpa os campos de edição dos componentes, para que novos valores sejam informados.
        tf_id.setText("");
        tf_interessado.setText("");
        tf_tipo.setText("");
        tf_idLocal.setText("");
        tf_preco.setText("");
        ftf_datalimite.setText("");
        
        //Informa qual o modo de dados que está sendo utilizado neste momento.
        modoDeDados = DADOS_INSERIR;
    }//GEN-LAST:event_b_InserirActionPerformed

    /**
     * Ao clicar no botão Modificar é ativado o modo de Modificação de dados. A interface
     * gráfica libera os componentes do painel "Dados do cadastro", para que o
     * ítem que estiver selecionado no painel "Todos os itens" possa ser modificado.
     * 
     * Os botões Cancelar e Ok também são liberados para que seja possível sair
     * deste modo, cancelando ou confirmando a modificação. Os demais botões são bloqueados
     * enquanto isto.
     * @param evt 
     */
    private void b_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ModificarActionPerformed
        //Bloqueia a ativação de outros modos de manpulação de dados.
        b_Inserir.setEnabled(false);
        b_Modificar.setEnabled(false);
        b_Excluir.setEnabled(false);
        b_Atualizar.setEnabled(false);
        //Libera a saída deste modo, permitindo o cancelamento ou a confirmação da modificação.
        b_Cancelar.setEnabled(true);
        b_Ok.setEnabled(true);
        //Libera todos os componentes necessários para o cadastro de um novo item.
        //tf_id.setEnabled(true);   //O BD GERA ID SEQUENCIAL!
        tf_interessado.setEnabled(true);
        tf_tipo.setEnabled(true);
        tf_idLocal.setEnabled(true);
        tf_preco.setEnabled(true);
        ftf_datalimite.setEnabled(true);
        
        //Informa qual o modo de dados que está sendo utilizado neste momento.
        modoDeDados = DADOS_MODIFICAR;
    }//GEN-LAST:event_b_ModificarActionPerformed

    /**
     * Ao clicar no botão Excluir é ativado o modo de Exclusão de dados. A interface
     * gráfica libera os componentes do painel "Dados do cadastro", para que o
     * ítem que estiver selecionado no painel "Todos os itens" possa ser excluído.
     * 
     * Os botões Cancelar e Ok também são liberados para que seja possível sair
     * deste modo, cancelando ou confirmando a exclusão. Os demais botões são bloqueados
     * enquanto isto.
     * @param evt 
     */
    private void b_ExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ExcluirActionPerformed
        //Informa qual o modo de dados que está sendo utilizado neste momento.
        modoDeDados = DADOS_EXCLUIR;
        //TODO usar JOPtionPane para confirmar a exclusão
    }//GEN-LAST:event_b_ExcluirActionPerformed

    /**
     * Ao clicar no botão Atualizar todos os dados de itens já cadastrados no painel
     * "Todos os itens" são atualizados.
     * @param evt 
     */
    private void b_AtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_AtualizarActionPerformed
        ResultSet rs = DatabaseOperations.consultarDB("select * from interesses");
        table_elementos.setModel(DbUtils.resultSetToTableModel(rs));
        DatabaseOperations.fecharConexãoDB();
    }//GEN-LAST:event_b_AtualizarActionPerformed

    /**
     * Ao clicar no botão Cancelar o modo de manipulação de dados atual é abortado,
     * retornando para o estado DADOS_NENHUM, reabilitando os botões Inserir, Modificar,
     * Excluir e Atualizar e bloqueando os botões Cancelar e Ok.
     * 
     * Os componentes do painel "Dados do cadastro" ficam novamente bloqueados, para
     * que seja permitido apenas a consulta dos mesmos.
     * @param evt 
     */
    private void b_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_CancelarActionPerformed
        //Bloqueia a ativação de outros modos de manpulação de dados.
        b_Inserir.setEnabled(true);
        b_Modificar.setEnabled(true);
        b_Excluir.setEnabled(true);
        b_Atualizar.setEnabled(true);
        //Libera a saída deste modo, permitindo o cancelamento ou a confirmação da modificação.
        b_Cancelar.setEnabled(false);
        b_Ok.setEnabled(false);
        //Libera todos os componentes necessários para o cadastro de um novo item.
        //tf_id.setEnabled(false);   //O BD GERA ID SEQUENCIAL!
        tf_interessado.setEnabled(false);
        tf_tipo.setEnabled(false);
        tf_idLocal.setEnabled(false);
        tf_preco.setEnabled(false);
        ftf_datalimite.setEnabled(false);
        
        //Informa qual o modo de dados que está sendo utilizado neste momento.
        modoDeDados = DADOS_NENHUM;
    }//GEN-LAST:event_b_CancelarActionPerformed

    /**
     * Ao clicar no botão Ok o modo de manipulação de dados atual é encerrado, confirmando
     * a ação que foi realizada (inserção, modificação ou exclusão) e retornando para o 
     * estado DADOS_NENHUM, reabilitando os botões Inserir, Modificar, Excluir e Atualizar
     * e bloqueando os botões Cancelar e Ok.
     * 
     * Os componentes do painel "Dados do cadastro" ficam novamente bloqueados, para
     * que seja permitido apenas a consulta dos mesmos.
     * @param evt 
     */
    private void b_OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_OkActionPerformed
        switch (modoDeDados){
            case DADOS_INSERIR:
                // TODO criar o comando que insere os dados na respectiva tabela!
                DatabaseOperations.insertInteresse(tf_interessado.getText(), Integer.parseInt(tf_tipo.getText()), 
                        Integer.parseInt(tf_idLocal.getText()), Integer.parseInt(tf_preco.getText()),
                        ftf_datalimite.getText());
                break;
            case DADOS_MODIFICAR:
                // TODO criar o comando que modifica os dados na respectiva tabela!
                break;
            case DADOS_EXCLUIR:
                // TODO criar o comando que exclui os dados na respectiva tabela!
                break;
            default:
                JOptionPane.showMessageDialog(this, "Nenhum modo de manipulação de dados está ativo.", 
                        "ERRO", JOptionPane.ERROR_MESSAGE);
                break;
        }

        //Bloqueia a ativação de outros modos de manpulação de dados.
        b_Inserir.setEnabled(true);
        b_Modificar.setEnabled(true);
        b_Excluir.setEnabled(true);
        b_Atualizar.setEnabled(true);
        //Libera a saída deste modo, permitindo o cancelamento ou a confirmação da modificação.
        b_Cancelar.setEnabled(false);
        b_Ok.setEnabled(false);
        //Libera todos os componentes necessários para o cadastro de um novo item.
        //tf_id.setEnabled(false);   //O BD GERA ID SEQUENCIAL!
        tf_interessado.setEnabled(false);
        tf_tipo.setEnabled(false);
        tf_idLocal.setEnabled(false);
        tf_preco.setEnabled(false);
        ftf_datalimite.setEnabled(false);
        
        //Informa qual o modo de dados que está sendo utilizado neste momento.
        modoDeDados = DADOS_NENHUM;
    }//GEN-LAST:event_b_OkActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.persistence.EntityManager DISTRIBUIDOSRMIPUEntityManager;
    private javax.swing.JButton b_Atualizar;
    private javax.swing.JButton b_Cancelar;
    private javax.swing.JButton b_Excluir;
    private javax.swing.JButton b_Inserir;
    private javax.swing.JButton b_Modificar;
    private javax.swing.JButton b_Ok;
    private javax.swing.JFormattedTextField ftf_datalimite;
    private java.util.List<Database.Interesses> interessesList;
    private javax.persistence.Query interessesQuery;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table_elementos;
    private javax.swing.JTextField tf_id;
    private javax.swing.JTextField tf_idLocal;
    private javax.swing.JTextField tf_interessado;
    private javax.swing.JTextField tf_preco;
    private javax.swing.JTextField tf_tipo;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
