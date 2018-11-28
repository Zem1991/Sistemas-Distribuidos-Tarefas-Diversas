package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import multicastpeertransacoes.Main;

public class ControleDB {
    //Variáveis utilizadas para a conexão com o banco de dados.
    private static boolean isDBOriginalOpen = false;
    private static boolean isDBFakeOpen = false;
    private static Connection c;
    private static Connection fakeC;
    private static Statement stmt;
    private static Statement fakeSTMT;
    
    /**
     * Prepara uma conexão ao banco de dados para que em seguida algum comando SQL seja
     * executado.
     * 
     * Utilizada em conjunto com as funções "executarComandoSQL" e "fecharConexãoDB".
     * 
     * @return true se a conexão foi aberta, false em caso contrário.
     */
    public static boolean abrirConexaoDBOficial(){
        if (isDBOriginalOpen){
            System.out.println("DATABASE OFICIAL já está aberto (isDBOriginalOpen=" + isDBOriginalOpen + ")! Ignorando esta função.");
        }else{
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/DistribuidosFinal", "postgres", "teste");
                c.setAutoCommit(false);
                if (Main.DEBUG_MSG_DB) System.out.println("Opened DATABASE OFICIAL successfully");
                //stmt = c.createStatement();
                stmt = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);    //Evita erros de ResultSet que não é rolável
                isDBOriginalOpen = true;
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ControleDB.class.getName()).log(Level.SEVERE, null, ex);
                isDBOriginalOpen = false;
            }
        }
        return isDBOriginalOpen;
    }
    
    /**
     * Prepara uma conexão ao banco de dados para que em seguida algum comando SQL seja
     * executado.
     * 
     * Utilizada em conjunto com as funções "executarComandoSQL" e "fecharConexãoDB".
     * 
     * @return true se a conexão foi aberta, false em caso contrário.
     */
    public static boolean abrirConexaoDBFake(){
        if (isDBFakeOpen){
            System.out.println("DATABASE TRANSITIVO já está aberto (isDBFakeOpen=" + isDBFakeOpen + ")! Ignorando esta função.");
        }else{
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/DistribuidosFinalTransitivo", "postgres", "teste");
                c.setAutoCommit(false);
                if (Main.DEBUG_MSG_DB) System.out.println("Opened DATABASE TRANSITIVO successfully");
                //stmt = c.createStatement();
                stmt = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);    //Evita erros de ResultSet que não é rolável
                isDBFakeOpen = true;
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ControleDB.class.getName()).log(Level.SEVERE, null, ex);
                isDBFakeOpen = false;
            }
        }
        return isDBFakeOpen;
    }
    
    /**
     * Executa no banco de dados a expressão informada como parâmetro. Esta função deve ser 
     * utilizado somente para operações de SELECT!
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "fecharConexãoDB".
     * Porém pode causar problemas ao fechar as conexões do DB antes de fazer uso
     * do ResultSet gerado!
     * 
     * @param sql é a expressão a ser executada no banco de dados.
     * @return um ResultSet contendo o resultado da expressão executada.
     */
    public static ResultSet consultarDBOficial(String sql){
        JFrame ig = new JFrame() {};  //Usado apenas para emitir mensagens de erro.
        try {
            System.out.println("SQL a ser executado no DB: " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (Main.DEBUG_MSG_DB) System.out.println("Operation done successfully");
            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(ig, "SQLException gerado:\n" + e.getClass().getName() + ": " + e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return null;
    }
    /**
     * Executa no banco de dados a expressão informada como parâmetro. Esta função deve ser 
     * utilizado somente para operações de INSERT, UPDATE ou DELETE!
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "fecharConexãoDB".
     * 
     * @param sql é a expressão a ser executada no banco de dados.
     * @return True se funcionou ou False se falhou
     */
    public static boolean executarComandoSQLDbFake(String sql){
        JFrame ig = new JFrame() {};  //Usado apenas para emitir mensagens de erro.
        try {
            System.out.println("SQL a ser executado no DB: " + sql);
            fakeSTMT.executeUpdate(sql);
            if (Main.DEBUG_MSG_DB) System.out.println("Operation done successfully");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(ig, "SQLException gerado:\n" + e.getClass().getName() + ": " + e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return false;
    }
    /**
     * Executa no banco de dados a expressão informada como parâmetro. Esta função deve ser 
     * utilizado somente para operações de SELECT!
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "fecharConexãoDB".
     * Porém pode causar problemas ao fechar as conexões do DB antes de fazer uso
     * do ResultSet gerado!
     * 
     * @param sql é a expressão a ser executada no banco de dados.
     * @return um ResultSet contendo o resultado da expressão executada.
     */
    public static ResultSet consultarDBFake(String sql){
        JFrame ig = new JFrame() {};  //Usado apenas para emitir mensagens de erro.
        try {
            System.out.println("SQL a ser executado no DB: " + sql);
            ResultSet rs = fakeSTMT.executeQuery(sql);
            if (Main.DEBUG_MSG_DB) System.out.println("Operation done successfully");
            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(ig, "SQLException gerado:\n" + e.getClass().getName() + ": " + e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return null;
    }
    /**
     * Executa no banco de dados a expressão informada como parâmetro. Esta função deve ser 
     * utilizado somente para operações de INSERT, UPDATE ou DELETE!
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "fecharConexãoDB".
     * 
     * @param sql é a expressão a ser executada no banco de dados.
     * @return true se funcionou ou false se falhou
     */
    public static boolean executarComandoSQLDbOficial(String sql){
        JFrame ig = new JFrame() {};  //Usado apenas para emitir mensagens de erro.
        try {
            System.out.println("SQL a ser executado no DB: " + sql);
            stmt.executeUpdate(sql);
            if (Main.DEBUG_MSG_DB) System.out.println("Operation done successfully");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(ig, "SQLException gerado:\n" + e.getClass().getName() + ": " + e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return false;
    }
    /**
     * Encerra uma conexão do banco de dados que esteja em andamento.
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "executarComandoSQL".
     * Esta função é publico para o caso de precisar manter as conexões do DB abertas,
     * onde o mesmo será chamado após outras atividades em outras classes.
     * 
     * @return true se a conexão foi fechada, false em caso contrário.
     */
    public static boolean fecharConexaoDBOficial(){
        if (isDBOriginalOpen){
            try{
                stmt.close();
                c.commit();
                c.close();
                isDBOriginalOpen = false;
                if (Main.DEBUG_MSG_DB) System.out.println("Closed DATABASE OFICIAL successfully");
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(ControleDB.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }else{
            System.out.println("DATABASE ORIGINAL já está fechado (isDBOriginalOpen=" + isDBOriginalOpen + ")! Ignorando esta função.");
        }
        return true;
    }
    /**
     * Encerra uma conexão do banco de dados que esteja em andamento.
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "executarComandoSQL".
     * Esta função é publico para o caso de precisar manter as conexões do DB abertas,
     * onde o mesmo será chamado após outras atividades em outras classes.
     * 
     * @return true se a conexão foi fechada, false em caso contrário.
     */
    public static boolean fecharConexaoDBFake(){
        if (isDBFakeOpen){
            try{
                fakeSTMT.close();
                fakeC.commit();
                fakeC.close();
                isDBFakeOpen = false;
                if (Main.DEBUG_MSG_DB) System.out.println("Closed DATABASE TRANSITIVO successfully");
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(ControleDB.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }else{
            System.out.println("DATABASE TRANSITIVO já está fechado (isDBFakeOpen=" + isDBFakeOpen + ")! Ignorando esta função.");
        }
        return true;
    }
    
    /**
     * Transforma um ResultSet em ArrayList. Esta função é utilizada majoritariamente
     * para o repasse de dados do DB para o Client.
     * 
     * Os dados da tabela contida no ResultSet são transformados em String.
     * 
     * O ArrayList gerado contém outros ArrayLists (linhas), onde cada elemento destes 
     * é uma String (coluna).
     * @param rs é o ResultSet a ser convertido
     * @param nomeDasColunas informa se o primeiro elemento do ArrayList gerado deve ser
     * um String[] contendo o nome das colunas.
     * @return o ArrayList com os dados de 'rs'.
     */
    public static ArrayList resultSetToArrayList(ResultSet rs, boolean nomeDasColunas){
        try {
            ArrayList dados = new ArrayList(); 
            if (nomeDasColunas){
                ResultSetMetaData rsmd =  rs.getMetaData();
                //COLETA O NOME DE CADA COLUNA E OS INSERE COMO O PRIMEIRO ELEMENTO DO ARRAYLIST 'dados'
                String[] nomesColunas = new String[rsmd.getColumnCount()];
                for(int j = 0; j < rsmd.getColumnCount(); j++){
                    nomesColunas[j] = rsmd.getColumnLabel(j+1);
                }
                dados.add(nomesColunas);
            }
            rs.beforeFirst();
            while ( rs.next() ) {
                ArrayList<String> linha = new ArrayList();
                for(int i = 0 ; i < rs.getMetaData().getColumnCount() ; i++){
                    linha.add((String) rs.getObject(i+1).toString());
                }
                dados.add(linha);
            }
            return dados;
        } catch (SQLException ex) {
            Logger.getLogger(ControleDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Esta função coleta os dados de um ResultSet e o transforma em um TableModel, que
     * por sua vez é utilizado nas tabelas das diversas janelas de interface gráfica
     * desta aplicação.
     * @param rs é o ResultSet a ser convertido.
     * @return um TableModel.
     */
    public static TableModel resultSetToTableModel(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector();

            // Get the column names
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            // Get all rows.
            Vector rows = new Vector();

            while (rs.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(rs.getObject(i));
                }

                rows.addElement(newRow);
            }

            return new DefaultTableModel(rows, columnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}