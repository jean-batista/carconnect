package banco;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*
    =================================================================
    == Classe responsavel por fazer a conexao com o banco de dados ==
    =================================================================
*/
public class BancoDeDados {
    
    // Inicia a conexao como nulo
    private static Connection conexao = null;

    // Metodo para fazer a conexao com o mysql
    public static Connection getConnection() {
        try {
            // Se a conexao for nula, tenta iniciar uma conexao
            if(conexao == null) {
                Properties propriedades = carregarPropriedades(); // Usa o metodo carregarPropriedades para obter as informacoes do banco
                String url = propriedades.getProperty("url"); // Obtem a informacao sobre a url do banco
                conexao = DriverManager.getConnection(url, propriedades); // Inicia uma conexao com o banco
            }
        } catch(SQLException e) {
            // Caso falhar na conexao, uma execao BancoException vai ser lancada
            throw new BancoException(e.getMessage());
        }
        // Retorna a conexao
        return conexao;
    }

    // Metodo privado (helper) para carregar as propriedades vindas do arquivo `database.properties`
    private static Properties carregarPropriedades() {
        try(FileInputStream fis = new FileInputStream("database.properties")) {
            Properties propriedades = new Properties(); // Inicia um objeto de propriedades
            propriedades.load(fis); // Carrega o objeto de propriedades com as propriedades do arquivo `database.properties`
            return propriedades; // Retorna as propriedades
        } catch(IOException e) {
            // Caso falhar ao carregar as propriedades, sera lancada uma excecao BancoException
            throw new BancoException(e.getMessage());
        }
    }

    public static void closeStatement(Statement st) {
        try {
            if(st != null) st.close();
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if(rs != null) rs.close();
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        }
    }

}