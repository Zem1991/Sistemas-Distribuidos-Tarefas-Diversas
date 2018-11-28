package Database.Operations;

import InterfaceGrafica.IG_InteressesSatisfeitos;
import RMI.Servidor;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;

public class DatabaseOperations {
    //Variáveis utilizadas para a conexão com o banco de dados.
    private static boolean isDBopen = false;
    private static Connection c;
    private static Statement stmt;
    
    /**
     * Prepara uma conexão ao banco de dados para que em seguida algum comando SQL seja
     * executado.
     * 
     * Utilizada em conjunto com as funções "executarComandoSQL" e "fecharConexãoDB".
     * 
     * @return true se a conexão foi aberta, false em caso contrário.
     */
    private static boolean abrirConexãoDB(){
        if (isDBopen){
            System.out.println("DATABASE já está aberto (isDBopen=" + isDBopen + ")! Ignorando este método.");
        }else{
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/DISTRIBUIDOSRMI", "postgres", "teste");
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");
                //stmt = c.createStatement();
                stmt = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);    //Evita erros de ResultSet que não é rolável
                isDBopen = true;
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
                isDBopen = false;
            }
        }
        return isDBopen;
    }
    /**
     * Executa no banco de dados a expressão informada como parâmetro. Este método deve ser 
     * utilizado somente para operações de INSERT, UPDATE ou DELETE!
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "fecharConexãoDB".
     * 
     * @param sql é a expressão a ser executada no banco de dados.
     */
    private static boolean executarComandoSQL(String sql){
        try {
            System.out.println("SQL a ser executado no DB: " + sql);
            stmt.executeUpdate(sql);
            System.out.println("Operation done successfully");
            return true;
        } catch (SQLException e) {
            //TODO: fazer disso uma janela informando ao usuário o erro que aconteceu no lado do DB.
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return false;
    }
    /**
     * Encerra uma conexão do banco de dados que esteja em andamento.
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "executarComandoSQL".
     * Este método é publico para o caso de precisar manter as conexões do DB abertas,
     * onde o mesmo será chamado após outras atividades em outras classes.
     * 
     * @return true se a conexão foi fechada, false em caso contrário.
     */
    public static boolean fecharConexãoDB(){
        if (isDBopen){
            try{
                stmt.close();
                c.commit();
                c.close();
                isDBopen = false;
                System.out.println("Closed database successfully");
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }else{
            System.out.println("DATABASE já está fechado (isDBopen=" + isDBopen + ")! Ignorando este método.");
        }
        return true;
    }
    
    /**
     * Executa no banco de dados a expressão informada como parâmetro. Este método deve ser 
     * utilizado somente para operações de SELECT!
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "fecharConexãoDB".
     * Porém pode causar problemas ao fechar as conexões do DB antes de fazer uso
     * do ResultSet gerado!
     * 
     * @param sql é a expressão a ser executada no banco de dados.
     * @return um ResultSet contendo o resultado da expressão executada.
     */
    public static ResultSet consultarDB(String sql){
        try {
            abrirConexãoDB();
            System.out.println("SQL a ser executado no DB: " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Operation done successfully");
            //fecharConexãoDB();
            return rs;
        } catch (SQLException e) {
            //TODO: fazer disso uma janela informando ao usuário o erro que aconteceu no lado do DB.
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return null;
    }
    /**
     * Executa no banco de dados a expressão informada como parâmetro. Este método deve ser 
     * utilizado somente para operações de SELECT!
     * 
     * A diferença deste método para "consultarDB é que ele foi feito para que o retorno
     * da consulta seja um ArrayList. Sendo assim, o repasse de dados para os Clientes
     * torna-se mais acessível, uma vez que não é possível enviar objetos ResultSet
     * pela rede por estes não serem serializáveis.
     * 
     * Utilizada em conjunto com as funções "abrirConexãoDB" e "fecharConexãoDB".
     * Porém pode causar problemas ao fechar as conexões do DB antes de fazer uso
     * do ResultSet gerado!
     * 
     * @param sql é a expressão a ser executada no banco de dados.
     * @return um ArrayList contendo em sua primeira posição um String[] com os nomes das
     * colunas do comando 'sql' executado, e nas demais posições os dados obtidos deste
     * mesmo comando.
     */
    public static ArrayList consultarDBComColunas(String sql){
        ArrayList dados = new ArrayList();
        try {
            abrirConexãoDB();
            System.out.println("SQL a ser executado no DB: " + sql);
            ResultSet rs = stmt.executeQuery(sql);  //String sql = "SELECT * FROM " + tabela + ";";
            System.out.println("Operation done successfully");
            //fecharConexãoDB();
            
            //COLETA O NOME DE CADA COLUNA E OS INSERE COMO O PRIMEIRO ELEMENTO DO ARRAYLIST 'dados'
            ResultSetMetaData rsmd = rs.getMetaData();
            String[] coluna = new String[rsmd.getColumnCount()];
            for(int j = 0; j < rsmd.getColumnCount(); j++){ 
                coluna[j] = rsmd.getColumnLabel(j+1);
            }
            dados.add(coluna);
            
            //COLETA OS DADOS OBTIDOS E OS INSERE COMO OS DEMAIS ELEMENTOS DO ARRAYLIST 'dados'
            while ( rs.next() ) {
                ArrayList linha = new ArrayList();
                for(int i = 0 ; i < rsmd.getColumnCount(); i++){
                    linha.add(rs.getObject(i+1));
                }
                dados.add(linha);
            }
        } catch (SQLException e) {
            //TODO: fazer disso uma janela informando ao usuário o erro que aconteceu no lado do DB.
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return dados;
    }
    /**
     * Converte um objeto ResultSet em um objeto TableModel. O TableModel retornado pode
     * ser utilizado para construir ou atualizar as tabelas com os dados do DB, tanto para
     * as interfaces gráficas do Servidor quanto as do Cliente.
     * 
     * O uso deste método é recomendado após utilizar dos métodos "consultarDB" e "consultarDBComColunas".
     * 
     * @param rs é o ResultSet a ser convertido.
     * @return um objeto TableModel.
     */
    public static TableModel converterTableModelParaResultSet(ResultSet rs){
        return DbUtils.resultSetToTableModel(rs);
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
     * @return o ArrayList com os dados de 'rs'.
     */
    private static ArrayList resultSetToArrayList(ResultSet rs, boolean nomeDasColunas){
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
            rs.first();
            while ( rs.next() ) {
                ArrayList<String> linha = new ArrayList();
                for(int i = 0 ; i < rs.getMetaData().getColumnCount() ; i++){
                    linha.add((String) rs.getObject(i+1).toString());
                }
                dados.add(linha);
            }
            return dados;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /*
    BEGIN INSERT
    */
    /**
     * Insere um novo local na Tabela local do banco de Dados. Esse Local será utilizado tanto para passagens
     * aéreas quanto para hospedagens em hoteis.
     * @param nome do local que está sendo inserido
     * @return Verdadeiro se a inserção foi bem sucedida, Falso em caso contrário
     */
    public static boolean insertLocal(String nome){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO locais " + 
                        "(nome) " + 
                        "VALUES ('" + nome + "');"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Função que insere nova Passagem feita pelo lado do servidor, que cadastra passagens para os clientes comprarem
     * @param tipo 0 para ida, 1 para ida e volta
     * @param id_localorigem identificador do local de origem, tabela locais
     * @param id_localdestino identificador do local de destino, tabela locais
     * @param dataida data de ida
     * @param datavolta data da volta
     * @param preco preço da passagem
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertPassagem(int tipo, int id_localorigem, int id_localdestino, String dataida, String datavolta, int preco){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO passagens " + 
                        "(tipo, id_localorigem, id_localdestino, dataida, datavolta, preco) " + 
                        "VALUES ('" + tipo + "','" + id_localorigem + "','" + id_localdestino + "','" + dataida + "','" + datavolta + "','" + preco + "');"
        );
        fecharConexãoDB();
        System.out.println("Inseriu passagem.");
        if (result==true) {
            buscaInteressePassagem(id_localdestino, preco);
            System.out.println("Verificou interesses em passagens.");
        }
        return result;
    }
    /**
     * Insere um novo Hotel no sistema, na tabela hoteis
     * @param nome do novo hotel
     * @param id_local local onde o hotel está, da tabela locais
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertHotel(String nome, int id_local){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO hoteis " + 
                        "(nome, id_local) " + 
                        "VALUES ('" + nome + "','" + id_local + "');"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Função para inserir um novo quarto em um hotel já cadastrado no sistema - Server Side
     * @param id_hotel - Identificador do hotel
     * @param nome - nome do quarto 
     * @param preco - preco da estadia
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertQuartoHotel(int id_hotel, String nome, int preco){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO quartoshotel " + 
                        "(id_hotel, nome, preco) " + 
                        "VALUES ('" + id_hotel + "','" + nome + "','" + preco + "');"
        );
        fecharConexãoDB();
        System.out.println("Inseriu quarto hotel.");
        if (result==true) {
            buscaInteresseHospedagem(id_hotel, preco);
            System.out.println("Verificou interesses em hospedagens.");
        }
        return result;
    }
    /**
     * Insere novo Pacote Turístico - é o que o cliente compra
     * @param cliente - nome do cliente
     * @param numeroPessoas - quantidade de pessoas para passagem/estadia
     * @param valorTotal - valor total da compra
     * @param status - 0 para compra não efetivada, 1 para compra efetivada
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertPacoteTuristico(String cliente, int numeroPessoas, int valorTotal, int status){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO pacotesturisticos " + 
                        "(cliente, numeropessoas, valortotal, status) " + 
                        "VALUES ('" + cliente + "','" + numeroPessoas + "','" + valorTotal + "','" + status + "');"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Realiza a Venda de uma Passagem
     * @param cliente - Nome do Cliente comprador
     * @param cartao - número do cartão do comprador
     * @param parcelas - numero de parcelas
     * @param numeroDePessoas - numero de pessoas
     * @param valorTotal - valor total da compra
     * @param id_pacoteTuristico - identificador do pacote turistico
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertVendaDePassagem(String cliente, String cartao, int parcelas, int numeroDePessoas, int valorTotal, int id_pacoteTuristico){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO vendasdepassagem " + 
                        "(cliente, cartao, parcelas, numeropessoas, valortotal, id_pacoteturistico) " + 
                        "VALUES ('" + cliente +  "','" + cartao + "', " + parcelas + ","
                        + numeroDePessoas + "," + valorTotal + "," + id_pacoteTuristico + ");"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Insere um item no "carrinho de compras" do cliente - Passagem
     * @param id_venda - Identificador da Venda
     * @param id_passagem - Identificador da Passagem
     * @param usuario - Identificador do usuário
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertItemVendaDePassagem(int id_venda, int id_passagem, String usuario){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO itensvendadepassagem " + 
                        "(id_venda, id_passagem, usuario) " + 
                        "VALUES ('" + id_venda + "','" + id_passagem + "', " + usuario + ");"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Insere Realiza uma Venda de Hospedagem
     * @param cliente - Identificação do cliente comprador
     * @param cartao - numero do cartão do comprador
     * @param parcelas - numero de parcelas
     * @param numeroDePessoas - numero de pessoas
     * @param valorTotal - valor total da compra
     * @param id_pacoteTuristico - identificador do pacote turistico
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertVendaDeHospedagem(String cliente, String cartao, int parcelas, int numeroDePessoas, int valorTotal, int id_pacoteTuristico){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO vendasdehospedagem " + 
                        "(cliente, cartao, parcelas, numeropessoas, valortotal, id_pacoteturistico) " + 
                        "VALUES ('" + cliente + "','" + cartao + "', " + parcelas + ","
                        + numeroDePessoas + "," + valorTotal + "," + id_pacoteTuristico + ");"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Insere um item no "carrinho de compras" do cliente - hospedagem
     * @param id_venda - identificador da venda
     * @param id_hospedagem - identificador da hospedagem
     * @param hospedes - 
     * @param dataentrada - data de entrada
     * @param datasaida - data de saida
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertItemVendaDeHospedagem(int id_venda, int id_hospedagem, String hospedes, String dataentrada, String datasaida){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "INSERT INTO itensvendadehospedagem " + 
                        "(id_venda, id_hospedagem, hospedes, dataentrada, datasaida) " + 
                        "VALUES ('" + id_venda + "','" + id_hospedagem + "', " + hospedes + ","
                        + dataentrada + "," + datasaida + ");"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Cliente insere interesse por receber novidades do servidor
     * @param interessado - nome/email do interessado
     * @param tipo - 0 para passagem e 1 para hospedagem
     * @param id_local - identificador do local de interesse
     * @param preco - preço máximo de interesse
     * @param datalimite - data que determina fim do interesse
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean insertInteresse(String interessado, int tipo, int id_local, int preco, String datalimite){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                //TODO: fazer no próprio banco de dados a trigger que verifica o interesse só em local ou só em hotel.
                //TODO: por preguiça foi feito aqui mesmo, na fução "buscaInteressePassagem(int id_hotel, int id_local)".
                "INSERT INTO interesses " + 
                        "(interessado, tipo, id_local, preco, datalimite) " + 
                        "VALUES ('" + interessado + "','" + tipo + "','" + id_local + "','" + preco + "','" + datalimite + "');"
        );
        fecharConexãoDB();
        return result;
    }
    /*
    END INSERT
    */ 
    
    /* 
    BEGIN UPDATE
    */
    /**
     * Realiza atualização em um local - Server Side
     * @param id que está sendo modificado
     * @param nome que será colocado no lugar do nome antigo
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updateLocal(int id, String nome){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE locais SET "
                        + "nome = '" + nome + "' "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Realiza atualização em passagem
     * @param id - identificador da passagem para ser atualizada
     * @param tipo - novo tipo da passagem, 0 para ida e 1 para ida e volta
     * @param id_localorigem - novo local de origem
     * @param id_localdestino - novo local de destino
     * @param dataida - nova data de ida
     * @param datavolta - nova data de volta
     * @param preco - novo preço
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updatePassagem(int id, int tipo, int id_localorigem, int id_localdestino, String dataida, String datavolta, int preco){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE passagens SET "
                        + "tipo = " + tipo + ", "
                        + "id_localorigem = " + id_localorigem + ", "
                        + "id_localdestino = " + id_localdestino + ", "
                        + "dataida = '" + dataida + "', "
                        + "datavolta = '" + datavolta + "', "
                        + "preco = " + preco + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        if (result==true) {
            buscaInteressePassagem(id_localdestino, preco);
        }
        return result;
    }
    /**
     * Atualiza hotel - Server Side
     * @param id - identificador do hotel que será atualizado
     * @param nome - novo nome do hotel
     * @param id_local - novo local - Caso o hotel mude de cidade :0
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updateHotel(int id, String nome, int id_local){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE hoteis SET "
                        + "nome = " + nome + ", "
                        + "id_local = " + id_local + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Atualiza Quarto de Hotel - Server Side
     * @param id - Identificador do Quarto a ser atualizado
     * @param id_hotel - novo identificador do hotel
     * @param nome - novo nome do quarto
     * @param preco - novo preco do quarto
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updateQuartoHotel(int id, int id_hotel, String nome, int preco){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE quartoshotel SET "
                        + "id_hotel = " + id_hotel + ", "
                        + "nome = " + nome + ", "
                        + "preco = " + preco + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        if (result==true) {
            buscaInteresseHospedagem(id_hotel, preco);
        }
        return result;
    }
    /**
     * Atualiza Pacote turistico - Client Side
     * @param id - identificador do pacote turistico
     * @param cliente - novo cliente
     * @param numeroPessoas - novo numero de pessoas
     * @param valorTotal - novo valor total
     * @param status - novo status
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updatePacoteTuristico(int id, String cliente, int numeroPessoas, int valorTotal, int status){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE pacotesturisticos SET "
                        + "cliente = " + cliente + ", "
                        + "numeroPessoas = " + numeroPessoas + ", "
                        + "valorTotal = " + valorTotal + ", "
                        + "status = " + status + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Atualiza Venda de Passagens
     * @param id - Identificador da Venda que será atualizada
     * @param cliente - novo nome do cliente
     * @param cartao - novo numero de cartão
     * @param parcelas - novo valor de parcelas
     * @param numeroDePessoas - novo numero de pessoas
     * @param valorTotal - novo valor total
     * @param id_pacoteTuristico - identificador do pacote turistico
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updateVendaDePassagem(int id, String cliente, String cartao, int parcelas, int numeroDePessoas, int valorTotal, int id_pacoteTuristico){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE vendasdepassagem SET "
                        + "cliente = " + cliente + ", "
                        + "cartao = " + cartao + ", "
                        + "parcelas = " + parcelas + ", "
                        + "numeroDePessoas = " + numeroDePessoas + ", "
                        + "valorTotal = " + valorTotal + ", "
                        + "id_pacoteTuristico = " + id_pacoteTuristico + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Atualiza um Item de Venda de passagem
     * @param id - Identificador do Item a ser atualizado
     * @param id_venda - identificador da venda
     * @param id_passagem - identificador da passagem
     * @param usuario - 
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updateItemVendaDePassagem(int id, int id_venda, int id_passagem, String usuario){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE itensvendadepassagem SET "
                        + "id_venda = " + id_venda + ", "
                        + "id_passagem = " + id_passagem + ", "
                        + "usuario = " + usuario + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Atualiza uma Venda de Hospedagem
     * @param id - Identificador da Venda de hospedagem a ser atualizada
     * @param cliente - novo cliente
     * @param cartao - novo cartao
     * @param parcelas - novo numero de parcelas
     * @param numeroDePessoas - novo numero de pessoas
     * @param valorTotal - novo valor total
     * @param id_pacoteTuristico - identificador do pacote turistico
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updateVendaDeHospedagem(int id, String cliente, String cartao, int parcelas, int numeroDePessoas, int valorTotal, int id_pacoteTuristico){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE vendasdehospedagem SET "
                        + "cliente = " + cliente + ", "
                        + "cartao = " + cartao + ", "
                        + "parcelas = " + parcelas + ", "
                        + "numeroDePessoas = " + numeroDePessoas + ", "
                        + "valorTotal = " + valorTotal + ", "
                        + "id_pacoteTuristico = " + id_pacoteTuristico + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Atualiza Item de Venda de Hospedagem
     * @param id - Identificador de Item de Venda de Hospedagem a ser atualizado
     * @param id_venda - identificador da venda
     * @param id_hospedagem - identiricador da hospedagem
     * @param hospedes -
     * @param dataentrada - nova data de entrada
     * @param datasaida - nova da de saida
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updateItemVendaDeHospedagem(int id, int id_venda, int id_hospedagem, String hospedes, String dataentrada, String datasaida){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "UPDATE itensvendadehospedagem SET "
                        + "id_venda = " + id_venda + ", "
                        + "id_hospedagem = " + id_hospedagem + ", "
                        + "hospedes = " + hospedes + ", "
                        + "dataentrada = " + dataentrada + ", "
                        + "datasaida = " + datasaida + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /**
     * Atualiza o Interesse - Client Side
     * @param id - Identificador do Interesse a ser atualizado
     * @param interessado - nome/email do interessado (ou referencia)
     * @param tipo - tipo do interesse, 0 para passagem 1 para hospedagem
     * @param id_local - novo local de interesse
     * @param preco - novo preço maximo de interesse
     * @param datalimite - nova data limite do interesse
     * @return True se foi executado corretamente e False caso contrario
     */
    public static boolean updateInteresse(int id, String interessado, int tipo, int id_local, int preco, String datalimite){
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                //TODO: fazer no próprio banco de dados a trigger que verifica o interesse só em local ou só em hotel.
                //TODO: por preguiça foi feito aqui mesmo, na fução "buscaInteressePassagem(int id_hotel, int id_local)".
                "UPDATE interesses SET "
                        + "interessado = " + interessado + ", "
                        + "tipo = " + tipo + ", "
                        + "id_local = " + id_local + ", "
                        + "preco = " + preco + ", "
                        + "datalimite = " + datalimite + " "
                        + "WHERE id = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /* 
    END UPDATE
    */
    
    /* 
    BEGIN DELETE
    */ 
    /**
     * Apaga um Dado de Qualquer tabela do sistema - Server Side
     * @param tabela - nome da tabela que se deseja apagar um dato
     * @param id - identificador do dado a ser apagado
     * @return  True se foi executado corretamente e False caso contrario
     */
    public static boolean apagarDado(String tabela, int id){   //Tabelas possivels: passagemvenda , interesse, reserva
        abrirConexãoDB();
        boolean result = executarComandoSQL(
                "DELETE from " + tabela + " where ID = " + id + ";"
        );
        fecharConexãoDB();
        return result;
    }
    /* 
    END DELETE
    */ 
    
    /**
     * Essa funçao será chamada quando houver inserção ou atualização de passagens, para
     * que eventuais interessados em passagens mais baratas para os destinhos escolhidos
     * sejam notificados.
     * 
     * A função "fecharConexãoDB" não é chamada dentro deste método!
     * @param id_local é o id do local onde há interess por passagens mais baratas
     * @param preco é o preço que está cadastrado no interesse, cujas passagens devem ser mais baratas que ele
     * @return ResultSet (para criar um JFrame contendo uma tabela dos interesses que são satisfeitos)
     */
    public static boolean buscaInteressePassagem(int id_local, int preco){
        String sql = "SELECT * FROM interesses X "
                + "JOIN locais L on L.id = X.id_local "
                + "JOIN passagens P on P.id_localdestino = L.id " 
                + "WHERE X.tipo = 0 AND X.id_local = " +  id_local +  " AND P.preco < X.preco ;"; //" + preco + ";";
        abrirConexãoDB();
        ResultSet rs = consultarDB(sql);
        if (rs != null){
            try {
                rs.first();  //Move o cursor do ResultSet para a ultima linha (se existirem linhas).
                if (rs.getRow() > 0){
                    //PRIMEIRAMENTE INFORMA O PRÓRPIO SERVIDOR DOS INTERESSES SATISFEITOS.
//                    IG_InteressesSatisfeitos ig_InteressesSatisfeitos = new IG_InteressesSatisfeitos();
//                    ig_InteressesSatisfeitos.redefineTableContents(rs);
//                    ig_InteressesSatisfeitos.setVisible(true);

                    //EM SEGUIDA INFORMA TODOS OS CLIENTES DOS INTERESSES SATISFEITOS.
                    
                    TableModel dado = converterTableModelParaResultSet(rs);
                    Servidor.getServente().exibirInteressesSatisfeitos( dado);
                }
            } catch (SQLException | RemoteException ex) {
                Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        fecharConexãoDB();
        return true;
    }
    /**
     * Essa funçao será chamada quando houver inserção ou atualização de quartos de hoteis, para
     * que eventuais interessados em hospedagens mais baratas para os destinhos escolhidos
     * sejam notificados.
     * 
     * A função "fecharConexãoDB" não é chamada dentro deste método!
     * @param id_hotel é o id do hotel aonde houve inserção ou modificação de dados de seus quartos. O local desse hotel
     * será utilizado na determinação dos interesses a serem retornados.
     * @param preco é o preço que está cadastrado no interesse, cujas hospedagens devem ser mais baratas que ele
     * @return ResultSet (para criar um JFrame contendo uma tabela dos interesses que são satisfeitos)
     */
    public static boolean buscaInteresseHospedagem(int id_hotel, int preco){
        String sql = "SELECT * FROM interesses X "
                + "JOIN locais L on L.id = X.id_local "
                + "JOIN hoteis H on H.id_local = L.id and H.id = " + id_hotel + " "
                + "JOIN quartoshotel QH on QH.id_hotel = H.id "
                + "WHERE X.tipo = 1 AND X.id_local = H.id_local AND QH.preco < X.preco ;"; //" + preco + ";";
        abrirConexãoDB();
        ResultSet rs = consultarDB(sql);
        if (rs != null){
            try {
                rs.first();  //Move o cursor do ResultSet para a ultima linha (se existirem linhas).
                if (rs.getRow() > 0){
                    //PRIMEIRAMENTE INFORMA O PRÓRPIO SERVIDOR DOS INTERESSES SATISFEITOS.
                    //IG_InteressesSatisfeitos ig_InteressesSatisfeitos = new IG_InteressesSatisfeitos();
                    //ig_InteressesSatisfeitos.redefineTableContents(rs);

                    //EM SEGUIDA INFORMA TODOS OS CLIENTES DOS INTERESSES SATISFEITOS.
                    TableModel dado = converterTableModelParaResultSet(rs);
                    Servidor.getServente().exibirInteressesSatisfeitos( dado);
                }
            } catch (SQLException | RemoteException ex) {
                Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        fecharConexãoDB();
        return true;
    }
}