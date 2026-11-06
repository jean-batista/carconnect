package dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import banco.BancoDeDados;
import banco.BancoException;
import dao.interfaces.GestorDao;
import entidades.Gestor;

public class GestorDaoImpl implements GestorDao {

    // Conexao do Banco de Dados
    private Connection connection = null;

    // Recebe a conexao do Banco de Dados via construtor
    public GestorDaoImpl(Connection connection) {
        this.connection = connection; // Define a conexao do banco no atributo da classe
    }

    @Override
    public void cadastrar(Gestor gestor) {
        String sql = "INSERT INTO tabela_gestor (id) VALUES (?)";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, gestor.getId().toString());
            int linhasAlteradas = st.executeUpdate();

            // Ocorre um erro caso nao houver linhas alteradas
            if(linhasAlteradas == 0) throw new BancoException("Não foi possível cadastrar o gestor");
        } catch (Exception e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public Gestor buscarGestor(UUID id) {
        String sql = "SELECT * FROM tabela_gestor WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        Gestor gestor = new Gestor(); // Cria um objeto Gestor vazio
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, id.toString());
            rs = st.executeQuery();
            while(rs.next()) {
                gestor.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Gestor
            }
            return gestor;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    @Override
    public List<Gestor> listarGestores() {
        String sql = "SELECT * FROM tabela_gestor";
        Statement st = null;
        ResultSet rs = null;
        Gestor gestor = new Gestor(); // Cria um objeto Gestor vazio
        List<Gestor> gestores = new ArrayList<>(); // Cria uma lista de gestores vazia
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                gestor.setId(UUID.fromString(rs.getString("id"))); // Define o id no objeto Gestor
                gestores.add(gestor);
            }
            return gestores;
        } catch(SQLException e) {
            throw new BancoException(e.getMessage());
        } finally {
            BancoDeDados.closeResultSet(rs);
            BancoDeDados.closeStatement(st);
        }
    }

    // TODO implementar metodo caso tiver solucao
    @Override
    public void atualizarCadastro(Gestor gestor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarCadastro'");
    }

    @Override
    public void excluirGestor(Gestor gestor) {
        String sql = "DELETE FROM tabela_gestor WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            st.setString(1, gestor.getId().toString());
            int linhasAlteradas = st.executeUpdate();
            if(linhasAlteradas == 0) throw new BancoException("O gestor não pode ser excluído");
        } catch(SQLException e) {
            throw new BancoException("Erro: " + e.getMessage());
        } finally {
            BancoDeDados.closeStatement(st);
        }
    }
    
}
